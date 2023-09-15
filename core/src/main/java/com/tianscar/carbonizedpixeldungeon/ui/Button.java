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

package com.tianscar.carbonizedpixeldungeon.ui;

import com.tianscar.carbonizedpixeldungeon.utils.Signal;
import com.tianscar.carbonizedpixeldungeon.input.GameAction;
import com.tianscar.carbonizedpixeldungeon.input.KeyBindings;
import com.tianscar.carbonizedpixeldungeon.input.KeyEvent;
import com.tianscar.carbonizedpixeldungeon.input.ControllerHandler;
import com.tianscar.carbonizedpixeldungeon.input.PointerEvent;
import com.tianscar.carbonizedpixeldungeon.noosa.Camera;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.noosa.PointerArea;
import com.tianscar.carbonizedpixeldungeon.noosa.ui.Component;

import java.util.ArrayList;

public class Button extends Component {

	public static float longClick = 0.5f;

	protected PointerArea hotArea;
	protected Tooltip hoverTip;

	protected boolean pressed;
	protected float pressTime;
	protected boolean processed;

	@Override
	protected void createChildren() {
		hotArea = new PointerArea( 0, 0, 0, 0 ) {
			@Override
			protected void onPointerDown( PointerEvent event ) {
				pressed = true;
				pressTime = 0;
				processed = false;
				Button.this.onPointerDown();
			}
			@Override
			protected void onPointerUp( PointerEvent event ) {
				pressed = false;
				Button.this.onPointerUp();
			}
			@Override
			protected void onClick( PointerEvent event ) {
				if (!processed) {
					killTooltip();
					switch (event.button){
						case PointerEvent.LEFT: default:
							Button.this.onClick();
							break;
						case PointerEvent.RIGHT:
							Button.this.onRightClick();
							break;
						case PointerEvent.MIDDLE:
							Button.this.onMiddleClick();
							break;
					}

				}
			}

			@Override
			protected void onHoverStart(PointerEvent event) {
				Button.this.onHoverStart();
			}

			@Override
			protected void onHoverEnd(PointerEvent event) {
				Button.this.onHoverEnd();
			}
		};
		add( hotArea );

		KeyEvent.addKeyListener( keyListener = new Signal.Listener<KeyEvent>() {
			@Override
			public boolean onSignal ( KeyEvent event ) {
				if ( active && KeyBindings.getActionForKey( event ) == keyAction()){
					if (event.pressed){
						pressed = true;
						pressTime = 0;
						processed = false;
						Button.this.onPointerDown();
					} else {
						Button.this.onPointerUp();
						if (pressed && !processed) onClick();
						pressed = false;
					}
					return true;
				}
				return false;
			}
		});
	}

	private Signal.Listener<KeyEvent> keyListener;

	public GameAction keyAction(){
		return null;
	}

	@Override
	public void update() {
		super.update();

		hotArea.active = visible;

		if (pressed) {
			if ((pressTime += Game.elapsed) >= longClick) {
				pressed = false;
				if (onLongClick()) {

					hotArea.reset();
					processed = true;
					onPointerUp();

					Game.vibrate( 50 );
				}
			}
		}
	}

	protected void onPointerDown() {}
	protected void onPointerUp() {}
	protected void onClick() {} //left click, default key type
	protected void onRightClick() {
		onClick();
	}
	protected void onMiddleClick() {}
	protected boolean onLongClick() {
		return false;
	}

	protected String hoverText() {
		return null;
	}

	protected void onHoverStart() {
		String text = hoverText();
		if (keyAction() != null && text == null) text = "";
		if (text != null){
			ArrayList<Integer> bindings = KeyBindings.getBoundKeysForAction(keyAction());
			if (!bindings.isEmpty()){
				int key = bindings.get(0);
				//prefer controller buttons if we are using a controller
				if (ControllerHandler.controllerPointerActive()){
					for (int code : bindings){
						if (ControllerHandler.icControllerKey(code)){
							key = code;
							break;
						}
					}
				}
				if (text.length() > 0) text += " ";
				text += "_(" + KeyBindings.getKeyName(key) + ")_";
			}
			hoverTip = new Tooltip(Button.this, text, 80, 0);
			Button.this.parent.add(hoverTip);
			hoverTip.camera = camera();
			alignTooltip(hoverTip);
		}
	}

	protected void onHoverEnd() {
		killTooltip();
	}

	//TODO might be nice for more flexibility here
	private void alignTooltip( Tooltip tip ){
		tip.setPos(x, y-tip.height()-1);
		Camera cam = camera();
		//shift left if there's no room on the right
		if (tip.right() > (cam.width+cam.scroll.x)){
			tip.setPos(tip.left() - (tip.right() - (cam.width+cam.scroll.x)), tip.top());
		}
		//move to the bottom if there's no room on top
		if (tip.top() < 0){
			tip.setPos(tip.left(), bottom()+1);
		}
	}

	public void killTooltip(){
		if (hoverTip != null){
			hoverTip.killAndErase();
			hoverTip = null;
		}
	}

	@Override
	protected void layout() {
		hotArea.x = x;
		hotArea.y = y;
		hotArea.width = width;
		hotArea.height = height;
	}

	@Override
	public synchronized void destroy () {
		super.destroy();
		KeyEvent.removeKeyListener( keyListener );
	}

}
