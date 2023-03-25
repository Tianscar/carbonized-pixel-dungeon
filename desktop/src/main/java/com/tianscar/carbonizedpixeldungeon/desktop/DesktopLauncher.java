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

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.PDSettings;
import com.tianscar.carbonizedpixeldungeon.services.news.News;
import com.tianscar.carbonizedpixeldungeon.services.news.NewsImpl;
import com.tianscar.carbonizedpixeldungeon.services.updates.UpdateImpl;
import com.tianscar.carbonizedpixeldungeon.services.updates.Updates;
import com.tianscar.pixeldungeonclasses.noosa.Game;
import com.tianscar.pixeldungeonclasses.utils.FileUtils;
import com.tianscar.pixeldungeonclasses.utils.Point;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DesktopLauncher {

	public static void main (String[] args) {

		if (!DesktopLaunchValidator.verifyValidJVMState(args)){
			return;
		}

		final String title = DesktopMessages.get(DesktopLauncher.class, "app_name");

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable throwable) {
				Game.reportException(throwable);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				throwable.printStackTrace(pw);
				pw.flush();
				String exceptionMsg = sw.toString();

				//shorten/simplify exception message to make it easier to fit into a message box
				exceptionMsg = exceptionMsg.replaceAll("\\(.*:([0-9]*)\\)", "($1)");
				exceptionMsg = exceptionMsg.replace("com.tianscar.carbonizedpixeldungeon.", "");
				exceptionMsg = exceptionMsg.replace("com.badlogic.gdx.", "");
				exceptionMsg = exceptionMsg.replace("\t", "    ");

				if (exceptionMsg.contains("Couldn't create window")){
					TinyFileDialogs.tinyfd_messageBox(DesktopMessages.get(DesktopLauncher.class, "crash_title", title, Game.version),
							DesktopMessages.get(DesktopLauncher.class, "crash_msg_gl", title, Game.version) + "\n" + exceptionMsg,
							"ok", "error", false);
				} else {
					System.out.print(DesktopMessages.get(DesktopLauncher.class, "crash_msg", title, Game.version));
					TinyFileDialogs.tinyfd_messageBox(DesktopMessages.get(DesktopLauncher.class, "crash_title", title, Game.version),
							DesktopMessages.get(DesktopLauncher.class, "crash_msg", title, Game.version) + "\n" + exceptionMsg,
							"ok", "error", false);
				}
				System.exit(1);
			}
		});

		Game.version = DesktopLauncher.class.getPackage().getSpecificationVersion();
		if (Game.version == null) {
			Game.version = System.getProperty("Specification-Version");
		}

		try {
			Game.versionCode = Integer.parseInt(DesktopLauncher.class.getPackage().getImplementationVersion());
		} catch (NumberFormatException e) {
			Game.versionCode = Integer.parseInt(System.getProperty("Implementation-Version"));
		}

		if (UpdateImpl.supportsUpdates()){
			Updates.service = UpdateImpl.getUpdateService();
		}
		if (NewsImpl.supportsNews()){
			News.service = NewsImpl.getNewsService();
		}
		
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle( TEXT(title + " - v" + Game.version + String.format("#%08x", Game.versionCode)) );

		String basePath = "";
		if (SharedLibraryLoader.isWindows) {
			if (System.getProperties().getProperty("os.name").equals("Windows XP")) {
				basePath = "Application Data/AnsdoShip/Carbonized Pixel Dungeon/";
			} else {
				basePath = "AppData/Roaming/AnsdoShip/Carbonized Pixel Dungeon/";
			}
		} else if (SharedLibraryLoader.isMac) {
			basePath = "Library/Application Support/Carbonized Pixel Dungeon/";
		} else if (SharedLibraryLoader.isLinux) {
			String XDGHome = System.getenv().get("XDG_DATA_HOME");
			if (XDGHome == null) XDGHome = ".local/share/";
			basePath = XDGHome + "ansdoship/carbonized-pixel-dungeon/";
		}

		//copy over prefs from old file location from legacy desktop codebase
		FileHandle oldPrefs = new Lwjgl3FileHandle(basePath + "pd-prefs", Files.FileType.External);
		FileHandle newPrefs = new Lwjgl3FileHandle(basePath + PDSettings.DEFAULT_PREFS_FILE, Files.FileType.External);
		if (oldPrefs.exists() && !newPrefs.exists()){
			oldPrefs.copyTo(newPrefs);
		}

		config.setPreferencesConfig( basePath, Files.FileType.External );
		PDSettings.set( new Lwjgl3Preferences( PDSettings.DEFAULT_PREFS_FILE, basePath) );
		FileUtils.setDefaultFileProperties( Files.FileType.External, basePath );

		config.setWindowSizeLimits( 640, 480, -1, -1 );
		Point p = PDSettings.windowResolution();
		config.setWindowedMode( p.x, p.y );

		p = PDSettings.windowPosition();
		if (p.x != Integer.MIN_VALUE && p.y != Integer.MIN_VALUE) config.setWindowPosition( p.x, p.y );

		PDSettings.windowIconified(false);
		config.setMaximized(PDSettings.windowMaximized());
		
		//we set fullscreen/maximized in the listener as doing it through the config seems to be buggy
		DesktopWindowListener listener = new DesktopWindowListener();
		config.setWindowListener( listener );
		
		config.setWindowIcon("icons/icon_16.png", "icons/icon_32.png", "icons/icon_64.png",
				"icons/icon_128.png", "icons/icon_256.png");

		new Lwjgl3Application(new CarbonizedPixelDungeon(new DesktopPlatformSupport(listener)), config);
	}

	private static String TEXT(String text) {
		Charset charset = Charset.defaultCharset();
		if (charset.equals(StandardCharsets.UTF_8)) return text;
		try {
			return charset.newDecoder().decode(charset.newEncoder().encode(CharBuffer.wrap(text))).toString();
		}
		catch (CharacterCodingException e) {
			return text;
		}
	}

}
