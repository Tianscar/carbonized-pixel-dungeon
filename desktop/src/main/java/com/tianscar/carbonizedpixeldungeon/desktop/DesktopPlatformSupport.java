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

package com.tianscar.carbonizedpixeldungeon.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.tianscar.carbonizedpixeldungeon.PDSettings;
import com.tianscar.pixeldungeonclasses.noosa.Game;
import com.tianscar.pixeldungeonclasses.utils.PlatformSupport;
import com.tianscar.pixeldungeonclasses.utils.Point;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DesktopPlatformSupport extends PlatformSupport {

	private final DesktopWindowListener windowListener;

	public DesktopPlatformSupport(DesktopWindowListener windowListener) {
		this.windowListener = windowListener;
	}
	
	@Override
	public void updateDisplaySize() {
		windowListener.updateDisplaySize(Game.width, Game.height);
	}
	
	@Override
	public void updateSystemUI() {
		Gdx.app.postRunnable( new Runnable() {
			@Override
			public void run () {
				if (PDSettings.fullscreen()){
					Gdx.graphics.setFullscreenMode( Gdx.graphics.getDisplayMode() );
				} else {
					Point p = PDSettings.windowResolution();
					Gdx.graphics.setWindowedMode( p.x, p.y );
				}
			}
		} );
	}
	
	@Override
	public boolean connectedToUnmeteredNetwork() {
		return true; //no easy way to check this in desktop, just assume user doesn't care
	}
	/* FONT SUPPORT */
	
	//custom pixel fonts, for use with Latin and Cyrillic languages
	private static FreeTypeFontGenerator basicFontGenerator;
	//droid sans fallback, for use with Korean
	private static FreeTypeFontGenerator KRFontGenerator;
	//droid sans fallback, for use with Simplified Chinese
	private static FreeTypeFontGenerator SCFontGenerator;
	//droid sans fallback, for use with Traditional Chinese
	private static FreeTypeFontGenerator TCFontGenerator;
	//droid sans fallback, for use with Japanese
	private static FreeTypeFontGenerator JPFontGenerator;
	
	@Override
	public void setupFontGenerators(int pageSize, boolean systemfont) {
		//don't bother doing anything if nothing has changed
		if (fonts != null && this.pageSize == pageSize && this.systemfont == systemfont){
			return;
		}
		this.pageSize = pageSize;
		this.systemfont = systemfont;

		resetGenerators(false);
		fonts = new HashMap<>();

		if (systemfont) {
			basicFontGenerator = KRFontGenerator = SCFontGenerator = TCFontGenerator = JPFontGenerator
					= fallbackFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans_fallback.ttf"));
		} else {
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font_latin1.ttf"));
			KRFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fusion_pixel_kr.ttf"));
			SCFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fusion_pixel_sc.ttf"));
			TCFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fusion_pixel_tc.ttf"));
			JPFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fusion_pixel_jp.ttf"));
			fallbackFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans_fallback.ttf"));
		}

		fonts.put(basicFontGenerator, new HashMap<>());
		fonts.put(KRFontGenerator, new HashMap<>());
		fonts.put(SCFontGenerator, new HashMap<>());
		fonts.put(TCFontGenerator, new HashMap<>());
		fonts.put(JPFontGenerator, new HashMap<>());
		fonts.put(fallbackFontGenerator, new HashMap<>());
		
		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}

	private static final Matcher KRMatcher = Pattern.compile("\\p{InHangul_Syllables}").matcher("");
	private static final Matcher CNMatcher = Pattern.compile("\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}").matcher("");
	private static final Matcher JPMatcher = Pattern.compile("\\p{InHiragana}|\\p{InKatakana}").matcher("");

	@Override
	protected FreeTypeFontGenerator getGeneratorForString( String input ){
		if (KRMatcher.reset(input).find()){
			return KRFontGenerator;
		} else if (CNMatcher.reset(input).find()){
			switch (PDSettings.language()) {
				case SI_CHINESE:
				default:
					return SCFontGenerator;
				case TR_CHINESE:
					return TCFontGenerator;
			}
		} else if (JPMatcher.reset(input).find()){
			return JPFontGenerator;
		} else {
			return basicFontGenerator;
		}
	}

	@Override
	public boolean isAndroid() {
		return false;
	}

	@Override
	public boolean isiOS() {
		return false;
	}

	@Override
	public boolean isDesktop() {
		return true;
	}

	@Override
	public String getAppName() {
		return DesktopMessages.get(DesktopLauncher.class, "app_name");
	}

	@Override
	public void setTitle(String title) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				long window = windowListener.getWindow();
				if (window != 0L) GLFW.glfwSetWindowTitle(window, title);
			}
		});
	}

}
