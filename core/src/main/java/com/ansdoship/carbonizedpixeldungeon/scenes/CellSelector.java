/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.ansdoship.carbonizedpixeldungeon.scenes;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.PDAction;
import com.ansdoship.carbonizedpixeldungeon.PDSettings;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.items.Heap;
import com.ansdoship.carbonizedpixeldungeon.sprites.CharSprite;
import com.ansdoship.carbonizedpixeldungeon.tiles.DungeonTilemap;
import com.ansdoship.pixeldungeonclasses.input.*;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.ScrollArea;
import com.ansdoship.pixeldungeonclasses.utils.GameMath;
import com.ansdoship.pixeldungeonclasses.utils.PointF;
import com.ansdoship.pixeldungeonclasses.utils.Signal;

public class CellSelector extends ScrollArea {

	public Listener listener = null;

	public boolean enabled;

	private float dragThreshold;

	public CellSelector( DungeonTilemap map ) {
		super( map );
		camera = map.camera();

		dragThreshold = PixelScene.defaultZoom * DungeonTilemap.SIZE / 2;

		mouseZoom = camera.zoom;
		KeyEvent.addKeyListener( keyListener );
	}

	private float mouseZoom;

	@Override
	protected void onScroll( ScrollEvent event ) {
		float diff = event.amount/10f;

		//scale zoom difference so zooming is consistent
		diff /= ((camera.zoom+1)/camera.zoom)-1;
		diff = Math.min(1, diff);
		mouseZoom = GameMath.gate( PixelScene.minZoom, mouseZoom - diff, PixelScene.maxZoom );

		zoom( Math.round(mouseZoom) );
	}

	@Override
	protected void onClick( PointerEvent event ) {
		if (dragging) {

			dragging = false;

		} else {

			PointF p = Camera.main.screenToCamera( (int) event.current.x, (int) event.current.y );

			//Prioritizes a sprite if it and a tile overlap, so long as that sprite isn't more than 4 pixels into another tile.
			//The extra check prevents large sprites from blocking the player from clicking adjacent tiles

			//hero first
			if (Dungeon.hero.sprite != null && Dungeon.hero.sprite.overlapsPoint( p.x, p.y )){
				PointF c = DungeonTilemap.tileCenterToWorld(Dungeon.hero.pos);
				if (Math.abs(p.x - c.x) <= 12 && Math.abs(p.y - c.y) <= 12) {
					select(Dungeon.hero.pos, event.button);
					return;
				}
			}

			//then mobs
			for (Char mob : Dungeon.level.mobs.toArray(new Mob[0])){
				if (mob.sprite != null && mob.sprite.overlapsPoint( p.x, p.y )){
					PointF c = DungeonTilemap.tileCenterToWorld(mob.pos);
					if (Math.abs(p.x - c.x) <= 12 && Math.abs(p.y - c.y) <= 12) {
						select(mob.pos, event.button);
						return;
					}
				}
			}

			//then heaps
			for (Heap heap : Dungeon.level.heaps.valueList()){
				if (heap.sprite != null && heap.sprite.overlapsPoint( p.x, p.y)){
					PointF c = DungeonTilemap.tileCenterToWorld(heap.pos);
					if (Math.abs(p.x - c.x) <= 12 && Math.abs(p.y - c.y) <= 12) {
						select(heap.pos, event.button);
						return;
					}
				}
			}

			select( ((DungeonTilemap)target).screenToTile(
					(int) event.current.x,
					(int) event.current.y,
					true ), event.button );
		}
	}

	private float zoom( float value ) {

		value = GameMath.gate( PixelScene.minZoom, value, PixelScene.maxZoom );
		PDSettings.zoom((int) (value - PixelScene.defaultZoom));
		camera.zoom( value );

		//Resets character sprite positions with the new camera zoom
		//This is important as characters are centered on a 16x16 tile, but may have any sprite size
		//This can lead to none-whole coordinate, which need to be aligned with the zoom
		for (Char c : Actor.chars()){
			if (c.sprite != null && !c.sprite.isMoving){
				c.sprite.point(c.sprite.worldToCamera(c.pos));
			}
		}

		return value;
	}

	public void select( int cell, int button ) {
		if (enabled && Dungeon.hero.ready && !GameScene.interfaceBlockingHero()
				&& listener != null && cell != -1) {

			switch (button){
				default:
					listener.onSelect( cell );
					break;
				case PointerEvent.RIGHT:
					listener.onRightClick( cell );
					break;
			}
			GameScene.ready();

		} else {

			GameScene.cancel();

		}
	}

	private boolean pinching = false;
	private PointerEvent another;
	private float startZoom;
	private float startSpan;

	@Override
	protected void onPointerDown( PointerEvent event ) {

		if (event != curEvent && another == null) {

			if (curEvent.type == PointerEvent.Type.UP) {
				curEvent = event;
				onPointerDown( event );
				return;
			}

			pinching = true;

			another = event;
			startSpan = PointF.distance( curEvent.current, another.current );
			startZoom = camera.zoom;

			dragging = false;
		} else if (event != curEvent) {
			reset();
		}
	}

	@Override
	protected void onPointerUp( PointerEvent event ) {
		if (pinching && (event == curEvent || event == another)) {

			pinching = false;

			zoom(Math.round( camera.zoom ));

			dragging = true;
			if (event == curEvent) {
				curEvent = another;
			}
			another = null;
			lastPos.set( curEvent.current );
		}
	}

	private boolean dragging = false;
	private PointF lastPos = new PointF();

	@Override
	protected void onDrag( PointerEvent event ) {

		if (pinching) {

			float curSpan = PointF.distance( curEvent.current, another.current );
			float zoom = (startZoom * curSpan / startSpan);
			camera.zoom( GameMath.gate(
					PixelScene.minZoom,
					zoom - (zoom % 0.1f),
					PixelScene.maxZoom ) );

		} else {

			if (!dragging && PointF.distance( event.current, event.start ) > dragThreshold) {

				dragging = true;
				lastPos.set( event.current );

			} else if (dragging) {
				camera.shift( PointF.diff( lastPos, event.current ).invScale( camera.zoom ) );
				lastPos.set( event.current );
			}
		}

	}

	//used for movement
	private GameAction heldAction1 = PDAction.NONE;
	private GameAction heldAction2 = PDAction.NONE;
	//not used for movement, but helpful if the player holds 3 keys briefly
	private GameAction heldAction3 = PDAction.NONE;

	private float heldDelay = 0f;
	//note that delay starts ticking down on the frame it is processed
	// so in most cases the actual wait is 50-58ms
	private static final float INITIAL_DELAY = 0.06f;
	private boolean delayingForRelease = false;

	private Signal.Listener<KeyEvent> keyListener = new Signal.Listener<KeyEvent>() {
		@Override
		public boolean onSignal(KeyEvent event) {
			GameAction action = KeyBindings.getActionForKey( event );
			if (!event.pressed){

				if (action == PDAction.ZOOM_IN){
					zoom( camera.zoom+1 );
					mouseZoom = camera.zoom;
					return true;

				} else if (action == PDAction.ZOOM_OUT){
					zoom( camera.zoom-1 );
					mouseZoom = camera.zoom;
					return true;
				}

				if (heldAction1 != PDAction.NONE && heldAction1 == action) {
					heldAction1 = PDAction.NONE;
					if (heldAction2 != PDAction.NONE){
						heldAction1 = heldAction2;
						heldAction2 = PDAction.NONE;
						if (heldAction3 != PDAction.NONE){
							heldAction2 = heldAction3;
							heldAction3 = PDAction.NONE;
						}
					}
				} else if (heldAction2 != PDAction.NONE && heldAction2 == action){
					heldAction2 = PDAction.NONE;
					if (heldAction3 != PDAction.NONE){
						heldAction2 = heldAction3;
						heldAction3 = PDAction.NONE;
					}
				} else if (heldAction3 != PDAction.NONE && heldAction3 == action){
					heldAction3 = PDAction.NONE;
				}

				//move from the action immediately if it was being delayed
				// and another key wasn't recently released
				if (heldDelay > 0f && !delayingForRelease){
					heldDelay = 0f;
					moveFromActions(action, heldAction1, heldAction2);
				}

				if (heldAction1 == GameAction.NONE && heldAction2 == GameAction.NONE) {
					resetKeyHold();
					return true;
				} else {
					delayingForRelease = true;
					//in case more keys are being released
					//note that this delay can tick down while the hero is moving
					heldDelay = INITIAL_DELAY;
				}

			} else if (directionFromAction(action) != 0) {

				Dungeon.hero.resting = false;
				lastCellMoved = -1;
				if (heldAction1 == PDAction.NONE){
					heldAction1 = action;
					heldDelay = INITIAL_DELAY;
					delayingForRelease = false;
				} else if (heldAction2 == PDAction.NONE){
					heldAction2 = action;
				} else {
					heldAction3 = action;
				}

				return true;
			} else if (Dungeon.hero.resting){
				Dungeon.hero.resting = false;
				return true;
			}

			return false;
		}
	};

	private GameAction leftStickAction = PDAction.NONE;

	@Override
	public void update() {
		super.update();

		if (GameScene.interfaceBlockingHero()){
			return;
		}

		GameAction newLeftStick = actionFromStick(ControllerHandler.leftStickPosition.x,
				ControllerHandler.leftStickPosition.y);

		if (newLeftStick != leftStickAction){
			if (leftStickAction == PDAction.NONE){
				heldDelay = INITIAL_DELAY;
				Dungeon.hero.resting = false;
			} else if (newLeftStick == PDAction.NONE && heldDelay > 0f){
				heldDelay = 0f;
				moveFromActions(leftStickAction);
			}
			leftStickAction = newLeftStick;
		}

		if (heldDelay > 0){
			heldDelay -= Game.elapsed;
		}

		if ((heldAction1 != PDAction.NONE || leftStickAction != PDAction.NONE) && Dungeon.hero.ready){
			processKeyHold();
		} else if (Dungeon.hero.ready) {
			lastCellMoved = -1;
		}
	}

	//prevents repeated inputs when the hero isn't moving
	private int lastCellMoved = 0;

	private boolean moveFromActions(GameAction... actions){
		if (Dungeon.hero == null || !Dungeon.hero.ready){
			return false;
		}

		int cell = Dungeon.hero.pos;
		for (GameAction action : actions) {
			cell += directionFromAction(action);
		}

		if (cell != Dungeon.hero.pos && cell != lastCellMoved){
			lastCellMoved = cell;
			if (Dungeon.hero.handle( cell )) {
				Dungeon.hero.next();
			}
			return true;

		} else {
			return false;
		}

	}

	private int directionFromAction(GameAction action){
		if (action == PDAction.N)  return -Dungeon.level.width();
		if (action == PDAction.NE) return +1-Dungeon.level.width();
		if (action == PDAction.E)  return +1;
		if (action == PDAction.SE) return +1+Dungeon.level.width();
		if (action == PDAction.S)  return +Dungeon.level.width();
		if (action == PDAction.SW) return -1+Dungeon.level.width();
		if (action == PDAction.W)  return -1;
		if (action == PDAction.NW) return -1-Dungeon.level.width();
		else                        return 0;
	}

	//~80% deadzone
	private GameAction actionFromStick(float x, float y){
		if (x > 0.5f){
			if (y < -0.5f){
				return PDAction.NE;
			} else if (y > 0.5f){
				return PDAction.SE;
			} else if (x > 0.8f){
				return PDAction.E;
			}
		} else if (x < -0.5f){
			if (y < -0.5f){
				return PDAction.NW;
			} else if (y > 0.5f){
				return PDAction.SW;
			} else if (x < -0.8f){
				return PDAction.W;
			}
		} else if (y > 0.8f){
			return PDAction.S;
		} else if (y < -0.8f){
			return PDAction.N;
		}
		return PDAction.NONE;
	}

	public void processKeyHold() {
		//prioritize moving by controller stick over moving via keys
		if (directionFromAction(leftStickAction) != 0 && heldDelay < 0) {
			enabled = Dungeon.hero.ready = true;
			Dungeon.observe();
			if (moveFromActions(leftStickAction)) {
				Dungeon.hero.ready = false;
			}
		} else if (directionFromAction(heldAction1) + directionFromAction(heldAction2) != 0
				&& heldDelay <= 0){
			enabled = Dungeon.hero.ready = true;
			Dungeon.observe();
			if (moveFromActions(heldAction1, heldAction2)) {
				Dungeon.hero.ready = false;
			}
		}
	}

	public void resetKeyHold(){
		heldAction1 = heldAction2 = heldAction3 = PDAction.NONE;
	}

	public void cancel() {

		if (listener != null) {
			listener.onSelect( null );
		}

		GameScene.ready();
	}

	@Override
	public void reset() {
		super.reset();
		another = null;
		if (pinching){
			pinching = false;

			zoom( Math.round( camera.zoom ) );
		}
	}

	public void enable(boolean value){
		if (enabled != value){
			enabled = value;
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		KeyEvent.removeKeyListener( keyListener );
	}

	public static abstract class Listener {
		public abstract void onSelect( Integer cell );

		public void onRightClick( Integer cell ){} //do nothing by default

		public abstract String prompt();
	}
}
