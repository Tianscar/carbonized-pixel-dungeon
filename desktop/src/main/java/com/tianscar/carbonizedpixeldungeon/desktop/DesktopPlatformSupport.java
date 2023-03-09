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

import java.io.IOException;
import java.io.InputStream;
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
	//droid sans fallback, for asian fonts
	private static FreeTypeFontGenerator asianFontGenerator;
	
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
			basicFontGenerator = asianFontGenerator = fallbackFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		} else {
			basicFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pixel_font.ttf"));
			asianFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fusion_pixel.ttf"));
			fallbackFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/droid_sans.ttf"));
		}
		
		fonts.put(basicFontGenerator, new HashMap<>());
		fonts.put(asianFontGenerator, new HashMap<>());
		fonts.put(fallbackFontGenerator, new HashMap<>());
		
		packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
	}
	
	private static final Matcher asianMatcher = Pattern.compile("\\p{InHangul_Syllables}|" +
			"\\p{InCJK_Unified_Ideographs}|\\p{InCJK_Symbols_and_Punctuation}|\\p{InHalfwidth_and_Fullwidth_Forms}|" +
			"\\p{InHiragana}|\\p{InKatakana}").matcher("");

	@Override
	protected FreeTypeFontGenerator getGeneratorForString( String input ){
		if (asianMatcher.reset(input).find()){
			return asianFontGenerator;
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

	/*
	@Override
	public void openURI(String URI) {
		if (SharedLibraryLoader.isLinux) {
			try {
				Process process = new ProcessBuilder("xdg-open", new URI(URI).toString()).start();
				skipAllBytes(process.getInputStream());
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else if (SharedLibraryLoader.isMac) {
			try {
				Process process = new ProcessBuilder("open", new URI(URI).toString()).start();
				skipAllBytes(process.getInputStream());
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else if (SharedLibraryLoader.isWindows) {

		}
		else super.openURI(URI);
	}
	 */

	private static void skipAllBytes(InputStream in) throws IOException {
		skipNBytes(in, Integer.MAX_VALUE);
	}

	private static void skipNBytes(InputStream in, int n) throws IOException {
		while (n > 0) {
			long ns = in.skip(n);
			if (ns > 0 && ns <= n) {
				// adjust number to skip
				n -= ns;
			}
			else if (ns == 0) { // no bytes skipped
				// read one byte to check for EOS
				if (in.read() == -1) {
					break;
				}
				// one byte read so decrement number to skip
				n--;
			}
			else { // skipped negative or too many bytes
				throw new IOException("Unable to skip exactly");
			}
		}
	}

}
