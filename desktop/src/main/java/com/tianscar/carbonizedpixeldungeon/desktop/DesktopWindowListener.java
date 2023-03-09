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

import com.tianscar.carbonizedpixeldungeon.PDSettings;
import com.tianscar.pixeldungeonclasses.utils.Point;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowListener;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowPosCallback;

public class DesktopWindowListener implements Lwjgl3WindowListener {

	private final int[] windowSizeRecords = new int[4];
	private final int[] windowPosRecords = new int[4];

	public DesktopWindowListener() {
		Point p = PDSettings.windowResolution();
		windowSizeRecords[0] = p.x;
		windowSizeRecords[1] = p.y;
		windowSizeRecords[2] = windowSizeRecords[0];
		windowSizeRecords[3] = windowSizeRecords[1];
		p = PDSettings.windowPosition();
		windowPosRecords[0] = p.x;
		windowPosRecords[1] = p.y;
		windowPosRecords[2] = windowPosRecords[0];
		windowPosRecords[3] = windowPosRecords[1];
	}
	
	@Override
	public void created ( Lwjgl3Window lwjgl3Window ) {
		long window = lwjgl3Window.getWindowHandle();
		lwjgl3Window.postRunnable(new Runnable() {
			@Override
			public void run() {
				if (PDSettings.fullscreen()) {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
				}
				GLFW.glfwSetWindowPosCallback(window, new GLFWWindowPosCallback() {
					@Override
					public void invoke(long window, int xpos, int ypos) {
						updateDisplayPos(xpos, ypos);
					}
				});
				GLFW.glfwShowWindow(window);
			}
		});
	}
	
	@Override
	public void maximized ( boolean b ) {
		PDSettings.windowMaximized( b );
		if (b) {
			rollbackWindowSize();
			rollbackWindowPos();
		}
	}
	
	@Override
	public void iconified ( boolean b ) {
		PDSettings.windowIconified( b );
	}
	public void focusLost () { }
	public void focusGained () { }
	public boolean closeRequested () { return true; }
	public void filesDropped ( String[] strings ) { }
	public void refreshRequested () { }

	public void updateDisplaySize(int width, int height) {
		if (!PDSettings.fullscreen() && !PDSettings.windowMaximized()) {
			PDSettings.windowResolution( new Point( width, height ) );
			recordWindowSize(width, height);
		}
	}

	public void updateDisplayPos(int xpos, int ypos) {
		if (!PDSettings.fullscreen() && !PDSettings.windowMaximized()) {
			PDSettings.windowPosition( new Point(xpos, ypos) );
			recordWindowPos(xpos, ypos);
		}
	}

	private void rollbackWindowSize() {
		PDSettings.windowResolution(new Point(windowSizeRecords[0], windowSizeRecords[1]));
		windowSizeRecords[2] = windowSizeRecords[0];
		windowSizeRecords[3] = windowSizeRecords[1];
	}

	private void recordWindowSize(int width, int height) {
		windowSizeRecords[0] = windowSizeRecords[2];
		windowSizeRecords[1] = windowSizeRecords[3];
		windowSizeRecords[2] = width;
		windowSizeRecords[3] = height;
	}

	private void rollbackWindowPos() {
		PDSettings.windowPosition(new Point(windowPosRecords[0], windowPosRecords[1]));
		windowPosRecords[2] = windowPosRecords[0];
		windowPosRecords[3] = windowPosRecords[1];
	}

	private void recordWindowPos(int xpos, int ypos) {
		windowPosRecords[0] = windowPosRecords[2];
		windowPosRecords[1] = windowPosRecords[3];
		windowPosRecords[2] = xpos;
		windowPosRecords[3] = ypos;
	}

}
