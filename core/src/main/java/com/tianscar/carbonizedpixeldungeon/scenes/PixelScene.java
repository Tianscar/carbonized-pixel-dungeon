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

package com.tianscar.carbonizedpixeldungeon.scenes;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Badges;
import com.tianscar.carbonizedpixeldungeon.PDSettings;
import com.tianscar.carbonizedpixeldungeon.effects.BadgeBanner;
import com.tianscar.carbonizedpixeldungeon.messages.Languages;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.carbonizedpixeldungeon.ui.Tooltip;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.pixeldungeonclasses.gltextures.TextureCache;
import com.tianscar.pixeldungeonclasses.glwrap.Blending;
import com.tianscar.pixeldungeonclasses.input.ControllerHandler;
import com.tianscar.pixeldungeonclasses.input.PointerEvent;
import com.tianscar.pixeldungeonclasses.noosa.*;
import com.tianscar.pixeldungeonclasses.noosa.BitmapText.Font;
import com.tianscar.pixeldungeonclasses.noosa.ui.Component;
import com.tianscar.pixeldungeonclasses.noosa.ui.Cursor;
import com.tianscar.pixeldungeonclasses.utils.Callback;
import com.tianscar.pixeldungeonclasses.utils.GameMath;
import com.tianscar.pixeldungeonclasses.utils.PointF;
import com.tianscar.pixeldungeonclasses.utils.Reflection;

import java.util.ArrayList;

public class PixelScene extends Scene {

	// Minimum virtual display size for portrait orientation
	public static final float MIN_WIDTH_P        = 135;
	public static final float MIN_HEIGHT_P        = 225;

	// Minimum virtual display size for landscape orientation
	public static final float MIN_WIDTH_L        = 240;
	public static final float MIN_HEIGHT_L        = 160;

	public static int defaultZoom = 0;
	public static int maxDefaultZoom = 0;
	public static int maxScreenZoom = 0;
	public static float minZoom;
	public static float maxZoom;

	public static Camera uiCamera;

	//stylized 3x5 bitmapped pixel font. Only latin characters supported.
	public static BitmapText.Font pixelFont;

	protected boolean inGameScene = false;

	@Override
	public void create() {

		super.create();

		GameScene.scene = null;

		//flush the texture cache whenever moving from ingame to menu, helps reduce memory load
		if (!inGameScene && InterlevelScene.lastRegion != -1){
			InterlevelScene.lastRegion = -1;
			TextureCache.clear();
		}

		float minWidth, minHeight;
		if (landscape()) {
			minWidth = MIN_WIDTH_L;
			minHeight = MIN_HEIGHT_L;
		} else {
			minWidth = MIN_WIDTH_P;
			minHeight = MIN_HEIGHT_P;
		}

		maxDefaultZoom = (int)Math.min(Game.width/minWidth, Game.height/minHeight);
		maxScreenZoom = (int)Math.min(Game.dispWidth/minWidth, Game.dispHeight/minHeight);
		defaultZoom = PDSettings.scale();

		if (defaultZoom < Math.ceil( Game.density * 2 ) || defaultZoom > maxDefaultZoom){
			defaultZoom = (int)GameMath.gate(2, (int)Math.ceil( Game.density * 2.5f ), maxDefaultZoom);
		}

		minZoom = 1;
		maxZoom = defaultZoom * 2;

		Camera.reset( new PixelCamera( defaultZoom ) );

		float uiZoom = defaultZoom;
		uiCamera = Camera.createFullscreen( uiZoom );
		Camera.add( uiCamera );

		// 3x5 (6)
		pixelFont = Font.colorMarked(
				TextureCache.get( Assets.Fonts.PIXELFONT), 0x00000000, BitmapText.Font.LATIN_FULL );
		pixelFont.baseLine = 6;
		pixelFont.tracking = -1;

		//set up the texture size which rendered text will use for any new glyphs.
		int renderedTextPageSize;
		if (defaultZoom <= 3){
			renderedTextPageSize = 256;
		} else if (defaultZoom <= 8){
			renderedTextPageSize = 512;
		} else {
			renderedTextPageSize = 1024;
		}
		//asian languages have many more unique characters, so increase texture size to anticipate that
		if (Messages.lang() == Languages.KOREAN ||
				Messages.lang() == Languages.SI_CHINESE ||
				Messages.lang() == Languages.TR_CHINESE ||
				Messages.lang() == Languages.JAPANESE){
			renderedTextPageSize *= 2;
		}
		Game.platform.setupFontGenerators(renderedTextPageSize, PDSettings.systemFont());

		Tooltip.resetLastUsedTime();

		Cursor.setCustomCursor(Cursor.Type.DEFAULT, defaultZoom);

	}

	private static PointF virtualCursorPos;

	@Override
	public void update() {
		super.update();
		//20% deadzone
		if (Math.abs(ControllerHandler.rightStickPosition.x) >= 0.2f
				|| Math.abs(ControllerHandler.rightStickPosition.y) >= 0.2f) {
			if (!ControllerHandler.controllerPointerActive()) {
				ControllerHandler.setControllerPointer(true);
				virtualCursorPos = PointerEvent.currentHoverPos();
			}
			//cursor moves 500 scaled pixels per second at full speed, 100 at minimum speed
			virtualCursorPos.x += defaultZoom * 500 * Game.elapsed * ControllerHandler.rightStickPosition.x;
			virtualCursorPos.y += defaultZoom * 500 * Game.elapsed * ControllerHandler.rightStickPosition.y;
			virtualCursorPos.x = GameMath.gate(0, virtualCursorPos.x, Game.width);
			virtualCursorPos.y = GameMath.gate(0, virtualCursorPos.y, Game.height);
			PointerEvent.addPointerEvent(new PointerEvent((int) virtualCursorPos.x, (int) virtualCursorPos.y, 10_000, PointerEvent.Type.HOVER, PointerEvent.NONE));
		}
	}

	private Image cursor = null;

	@Override
	public synchronized void draw() {
		super.draw();

		//cursor is separate from the rest of the scene, always appears above
		if (ControllerHandler.controllerPointerActive()){
			if (cursor == null){
				cursor = new Image(Cursor.Type.CONTROLLER.file);
			}

			cursor.x = (virtualCursorPos.x / defaultZoom) - cursor.width()/2f;
			cursor.y = (virtualCursorPos.y / defaultZoom) - cursor.height()/2f;
			cursor.camera = uiCamera;
			align(cursor);
			cursor.draw();
		}
	}

	//FIXME this system currently only works for a subset of windows
	private static ArrayList<Class<?extends Window>> savedWindows = new ArrayList<>();
	private static Class<?extends PixelScene> savedClass = null;

	public synchronized void saveWindows(){
		if (members == null) return;

		savedWindows.clear();
		savedClass = getClass();
		for (Gizmo g : members.toArray(new Gizmo[0])){
			if (g instanceof Window){
				savedWindows.add((Class<? extends Window>) g.getClass());
			}
		}
	}

	public synchronized void restoreWindows(){
		if (getClass().equals(savedClass)){
			for (Class<?extends Window> w : savedWindows){
				try{
					add(Reflection.newInstanceUnhandled(w));
				} catch (Exception e){
					//window has no public zero-arg constructor, just eat the exception
				}
			}
		}
		savedWindows.clear();
	}

	@Override
	public void destroy() {
		super.destroy();
		PointerEvent.clearListeners();
		if (cursor != null){
			cursor.destroy();
		}
	}

	public static RenderedTextBlock renderTextBlock(int size ){
		return renderTextBlock("", size);
	}

	public static RenderedTextBlock renderTextBlock(String text, int size ){
		RenderedTextBlock result = new RenderedTextBlock( text, size*defaultZoom);
		result.zoom(1/(float)defaultZoom);
		return result;
	}

	/**
	 * These methods align UI elements to device pixels.
	 * e.g. if we have a scale of 3x then valid positions are #.0, #.33, #.67
	 */

	public static float align( float pos ) {
		return Math.round(pos * defaultZoom) / (float)defaultZoom;
	}

	public static float align( Camera camera, float pos ) {
		return Math.round(pos * camera.zoom) / camera.zoom;
	}

	public static void align( Visual v ) {
		v.x = align( v.x );
		v.y = align( v.y );
	}

	public static void align( Component c ){
		c.setPos(align(c.left()), align(c.top()));
	}

	public static boolean noFade = false;
	protected void fadeIn() {
		if (noFade) {
			noFade = false;
		} else if (PDSettings.transAnim() > 0){
			fadeIn( 0xFF000000, false );
		}
	}

	protected void fadeIn( int color, boolean light ) {
		add( new Fader( color, light ) );
	}

	public static void showBadge( Badges.Badge badge ) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				BadgeBanner banner = BadgeBanner.show( badge.image );
				banner.camera = uiCamera;
				float offset = Camera.main.centerOffset.y;
				banner.x = align( banner.camera, (banner.camera.width - banner.width) / 2 );
				banner.y = align( uiCamera, (uiCamera.height - banner.height) / 2 - banner.height/2 - 16 - offset );
				Scene s = Game.scene();
				if (s != null) s.add( banner );
			}
		});
	}

	protected static class Fader extends ColorBlock {

		private static float FADE_TIME = 1f;

		private boolean light;

		private float time;

		private static Fader instance;

		public Fader( int color, boolean light ) {
			super( uiCamera.width, uiCamera.height, color );

			this.light = light;

			camera = uiCamera;

			alpha( 1f );
			time = FADE_TIME;

			if (instance != null){
				instance.killAndErase();
			}
			instance = this;
		}

		@Override
		public void update() {

			super.update();

			if ((time -= Game.elapsed) <= 0) {
				alpha( 0f );
				parent.remove( this );
				destroy();
				if (instance == this) {
					instance = null;
				}
			} else {
				alpha( time / FADE_TIME );
			}
		}

		@Override
		public void draw() {
			if (light) {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			} else {
				super.draw();
			}
		}
	}

	private static class PixelCamera extends Camera {

		public PixelCamera( float zoom ) {
			super(
					(int)(Game.width - Math.ceil( Game.width / zoom ) * zoom) / 2,
					(int)(Game.height - Math.ceil( Game.height / zoom ) * zoom) / 2,
					(int)Math.ceil( Game.width / zoom ),
					(int)Math.ceil( Game.height / zoom ), zoom );
			fullScreen = true;
		}

		@Override
		protected void updateMatrix() {
			float sx = align( this, scroll.x + shakeX );
			float sy = align( this, scroll.y + shakeY );

			matrix[0] = +zoom * invW2;
			matrix[5] = -zoom * invH2;

			matrix[12] = -1 + x * invW2 - sx * matrix[0];
			matrix[13] = +1 - y * invH2 - sy * matrix[5];

		}
	}
}
