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
import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.ui.Archs;
import com.tianscar.carbonizedpixeldungeon.ui.ExitButton;
import com.tianscar.carbonizedpixeldungeon.windows.WndFeedback;
import com.tianscar.carbonizedpixeldungeon.input.KeyEvent;
import com.tianscar.carbonizedpixeldungeon.noosa.Camera;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Music;

public class FeedbackScene extends PixelScene {

	private WndFeedback wndFeedback;

	@Override
	public void create() {
		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.TITLE},
				new float[]{1},
				false);

		wndFeedback = new WndFeedback(false) {
			@Override
			public void onBackPressed() {
				//super.onBackPressed();
			}
		};
		KeyEvent.removeKeyListener(wndFeedback);
		add(wndFeedback);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}
	
	@Override
	public void onBackPressed() {
		if (wndFeedback != null) {
			wndFeedback.hide();
			wndFeedback = null;
		}
		CarbonizedPixelDungeon.switchNoFade(TitleScene.class);
	}

}
