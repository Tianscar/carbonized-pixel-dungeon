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

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.ShatteredPixelDungeon;
import com.ansdoship.carbonizedpixeldungeon.ui.Archs;
import com.ansdoship.carbonizedpixeldungeon.ui.ExitButton;
import com.ansdoship.carbonizedpixeldungeon.windows.WndSettings;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Music;

public class SettingsScene extends PixelScene {

	private WndSettings wndSettings;

	@Override
	public void create() {
		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME},
				new float[]{1},
				false);

		wndSettings = new WndSettings() {
			@Override
			public void onBackPressed() {
				//super.onBackPressed();
			}
		};
		add(wndSettings);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		if (wndSettings != null) {
			wndSettings.hide();
			wndSettings = null;
		}
		ShatteredPixelDungeon.switchNoFade(TitleScene.class);
	}

}
