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

import com.tianscar.carbonizedpixeldungeon.Chrome;
import com.tianscar.carbonizedpixeldungeon.PDAction;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.pixeldungeonclasses.input.KeyBindings;
import com.tianscar.pixeldungeonclasses.input.KeyEvent;
import com.tianscar.pixeldungeonclasses.input.PointerEvent;
import com.tianscar.pixeldungeonclasses.noosa.Camera;
import com.tianscar.pixeldungeonclasses.noosa.Game;
import com.tianscar.pixeldungeonclasses.noosa.Group;
import com.tianscar.pixeldungeonclasses.noosa.NinePatch;
import com.tianscar.pixeldungeonclasses.noosa.PointerArea;
import com.tianscar.pixeldungeonclasses.utils.Point;
import com.tianscar.pixeldungeonclasses.utils.Signal;

public class Window extends Group implements Signal.Listener<KeyEvent> {

	protected int width;
	protected int height;

	protected int xOffset;
	protected int yOffset;
	
	protected PointerArea blocker;
	protected NinePatch chrome;

	public static final int WHITE = 0xFFFFFF;
	public static final int TITLE_COLOR = 0xFFFF44;
	public static final int SHPX_COLOR = 0x33BB33;
	
	public Window() {
		this( 0, 0, 0, Chrome.get( Chrome.Type.TOAST ) );
	}
	
	public Window( int width, int height ) {
		this( width, height, 0, Chrome.get( Chrome.Type.TOAST ) );
	}

	public Window( int width, int height, NinePatch chrome ) {
		this(width, height, 0, chrome);
	}
			
	public Window( int width, int height, int yOffset, NinePatch chrome ) {
		super();

		this.yOffset = yOffset;
		
		blocker = new PointerArea( 0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height ) {
			@Override
			protected void onClick( PointerEvent event ) {
				if (Window.this.parent != null && !Window.this.chrome.overlapsScreenPoint(
					(int) event.current.x,
					(int) event.current.y )) {
					
					onBackPressed();
				}
			}
		};
		blocker.camera = PixelScene.uiCamera;
		add( blocker );
		
		this.chrome = chrome;

		this.width = width;
		this.height = height;

		chrome.x = -chrome.marginLeft();
		chrome.y = -chrome.marginTop();
		chrome.size(
			width - chrome.x + chrome.marginRight(),
			height - chrome.y + chrome.marginBottom() );
		add( chrome );
		
		camera = new Camera( 0, 0,
			(int)chrome.width,
			(int)chrome.height,
			PixelScene.defaultZoom );
		camera.x = (int)(Game.width - camera.width * camera.zoom) / 2;
		camera.y = (int)(Game.height - camera.height * camera.zoom) / 2;
		camera.y -= yOffset * camera.zoom;
		camera.scroll.set( chrome.x, chrome.y );
		Camera.add( camera );

		KeyEvent.addKeyListener( this );
	}
	
	public void resize( int w, int h ) {
		this.width = w;
		this.height = h;
		
		chrome.size(
			width + chrome.marginHor(),
			height + chrome.marginVer() );
		
		camera.resize( (int)chrome.width, (int)chrome.height );
		camera.x = (int)(Game.width - camera.screenWidth()) / 2;
		camera.x += xOffset * camera.zoom;
		camera.y = (int)(Game.height - camera.screenHeight()) / 2;
		camera.y += yOffset * camera.zoom;
	}

	public Point getOffset(){
		return new Point(xOffset, yOffset);
	}
	public final void offset( Point offset ){
		offset(offset.x, offset.y);
	}

	public final void offset( int yOffset ){
		offset(0, yOffset);
	}

	//windows with scroll panes will likely need to override this and refresh them when offset changes
	public void offset( int xOffset, int yOffset ){
		camera.x -= this.xOffset * camera.zoom;
		this.xOffset = xOffset;
		camera.x += xOffset * camera.zoom;

		camera.y -= this.yOffset * camera.zoom;
		this.yOffset = yOffset;
		camera.y += yOffset * camera.zoom;
	}
	
	public void hide() {
		if (parent != null) {
			parent.erase(this);
		}
		destroy();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		Camera.remove( camera );
		KeyEvent.removeKeyListener( this );
	}

	@Override
	public boolean onSignal( KeyEvent event ) {
		if (event.pressed) {
			if (KeyBindings.getActionForKey( event ) == PDAction.BACK){
				onBackPressed();
			}
		}
		
		//TODO currently always eats the key event as windows always take full focus
		// if they are ever made more flexible, might not want to do this in all cases
		return true;
	}
	
	public void onBackPressed() {
		hide();
	}

}
