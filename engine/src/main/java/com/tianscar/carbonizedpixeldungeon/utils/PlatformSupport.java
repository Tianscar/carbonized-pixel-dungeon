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

package com.tianscar.carbonizedpixeldungeon.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;

import java.util.HashMap;
import java.util.regex.Pattern;

public abstract class PlatformSupport {
	
	public abstract void updateDisplaySize();
	
	public abstract void updateSystemUI();

	public abstract boolean connectedToUnmeteredNetwork();

	public void vibrate( int millis ) {
		//regular GDX vibration by default
		Gdx.input.vibrate( millis );
	}

	//TODO should consider spinning this into its own class, rather than platform support getting ever bigger
	protected static HashMap<FreeTypeFontGenerator, HashMap<Integer, BitmapFont>> fonts;

	protected static FreeTypeFontGenerator fallbackFontGenerator;

	//splits on newlines, underscores, and chinese/japaneses characters
	protected static Pattern regularsplitter = Pattern.compile(
			"(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\\\)|(?=\\\\)|" +
					"(?<=[^\\x00-\\xff])|(?=[^\\x00-\\xff])|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InHangul_Syllables})|(?=!\\p{InHangul_Syllables})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})" +
					"(?<=\\p{InHalfwidth_and_Fullwidth_Forms})|(?=\\p{InHalfwidth_and_Fullwidth_Forms})");

	//additionally splits on words, so that each word can be arranged individually
	protected static Pattern regularsplitterMultiline = Pattern.compile(
			"(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|(?<=\\\\)|(?=\\\\)|" +
					"(?<=[^\\x00-\\xff])|(?=[^\\x00-\\xff])|" +
					"(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
					"(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
					"(?<=\\p{InHangul_Syllables})|(?=!\\p{InHangul_Syllables})|" +
					"(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
					"(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})" +
					"(?<=\\p{InHalfwidth_and_Fullwidth_Forms})|(?=\\p{InHalfwidth_and_Fullwidth_Forms})");

	protected int pageSize;
	protected PixmapPacker packer;
	protected boolean systemfont;
	
	public abstract void setupFontGenerators(int pageSize, boolean systemFont );

	protected abstract FreeTypeFontGenerator getGeneratorForString( String input );

	public String[] splitforTextBlock(String text, boolean multiline) {
		if (multiline) {
			return regularsplitterMultiline.split(text);
		} else {
			return regularsplitter.split(text);
		}
	}

	public void resetGenerators(){
		resetGenerators( true );
	}

	public void resetGenerators( boolean setupAfter ){
		if (fonts != null) {
			for (FreeTypeFontGenerator generator : fonts.keySet()) {
				for (BitmapFont f : fonts.get(generator).values()) {
					f.dispose();
				}
				fonts.get(generator).clear();
				generator.dispose();
			}
			fonts.clear();
			if (packer != null) {
				for (PixmapPacker.Page p : packer.getPages()) {
					p.getTexture().dispose();
				}
				packer.dispose();
			}
			fonts = null;
		}
		if (setupAfter) setupFontGenerators(pageSize, systemfont);
	}

	public void reloadGenerators(){
		if (packer != null) {
			for (FreeTypeFontGenerator generator : fonts.keySet()) {
				for (BitmapFont f : fonts.get(generator).values()) {
					f.dispose();
				}
				fonts.get(generator).clear();
			}
			if (packer != null) {
				for (PixmapPacker.Page p : packer.getPages()) {
					p.getTexture().dispose();
				}
				packer.dispose();
			}
			packer = new PixmapPacker(pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false);
		}
	}

	//flipped is needed because Shattered's graphics are y-down, while GDX graphics are y-up.
	//this is very confusing, I know.
	public BitmapFont getFont(int size, String text, boolean flipped, boolean border, boolean fallback) {
		FreeTypeFontGenerator generator = fallback ? fallbackFontGenerator : getGeneratorForString(text);

		if (generator == null){
			return null;
		}

		int key = size;
		if (border) key += Short.MAX_VALUE; //surely we'll never have a size above 32k
		if (flipped) key = -key;
		if (!fonts.get(generator).containsKey(key)) {
			FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameters.size = size;
			parameters.flip = flipped;
			if (border) {
				parameters.borderWidth = parameters.size / 10f;
			}
			if (size >= 20){
				parameters.renderCount = 2;
			} else {
				parameters.renderCount = 3;
			}
			parameters.hinting = FreeTypeFontGenerator.Hinting.None;
			parameters.spaceX = -(int) parameters.borderWidth;
			parameters.incremental = true;
			parameters.characters = "�";
			parameters.packer = packer;

			try {
				BitmapFont font = generator.generateFont(parameters);
				font.getData().missingGlyph = font.getData().getGlyph('�');
				fonts.get(generator).put(key, font);
			} catch ( Exception e ) {
				Game.reportException(e);
				return null;
			}
		}

		return fonts.get(generator).get(key);
	}

	public boolean supportsFullScreen(){
		switch (Gdx.app.getType()){
			case Android:
				//Android 4.4+ supports hiding UI via immersive mode
				return Gdx.app.getVersion() >= 19;
			case iOS:
				//iOS supports hiding UI via drawing into the gesture safe area
				return Gdx.graphics.getSafeInsetBottom() != 0;
			default:
				//TODO implement functionality for other platforms here
				return true;
		}
	}

	public boolean isAndroid() {
		return Gdx.app.getType() == Application.ApplicationType.Android;
	}

	public boolean isiOS() {
		return Gdx.app.getType() == Application.ApplicationType.iOS;
	}

	public boolean isDesktop() {
		return Gdx.app.getType() == Application.ApplicationType.Desktop;
	}

	public boolean hasHardKeyboard() {
		return Gdx.input.isPeripheralAvailable(Input.Peripheral.HardwareKeyboard);
	}

	public boolean isDebug() {
		return Game.version.contains("INDEV");
	}

	public boolean isSnapshot() {
		return Game.version.contains("SNAPSHOT");
	}

	public void openURI( String URI ) {
		Gdx.net.openURI(URI);
	}

	public void log( String tag, String message ){
		Gdx.app.log( tag, message );
	}

	public void debug( String message ) {
		if (isDebug()) Gdx.app.log("DEBUG", message);
	}

	public void setClipboardContents( String str ) {
		Gdx.app.getClipboard().setContents( str );
	}

	public String getClipboardContents() {
		return Gdx.app.getClipboard().getContents();
	}

	public boolean isClipboardEmpty() {
		return !Gdx.app.getClipboard().hasContents();
	}

	protected abstract String getAppName();
	protected abstract void setTitle(String title);

}
