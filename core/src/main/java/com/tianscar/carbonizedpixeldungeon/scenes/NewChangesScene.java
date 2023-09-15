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
import com.tianscar.carbonizedpixeldungeon.Chrome;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.ui.*;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.carbonizedpd.v0_1_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeInfo;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeSelection;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.carbonizedpd.v0_2_X_Changes;
import com.tianscar.carbonizedpixeldungeon.noosa.Camera;
import com.tianscar.carbonizedpixeldungeon.noosa.NinePatch;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Music;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.noosa.ui.Component;

import java.util.ArrayList;

public class NewChangesScene extends PixelScene {
	
	public static int changesSelected = 0;

	public static boolean fromChangesScene = false;
	
	@Override
	public void create() {
		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.TITLE},
				new float[]{1},
				false);

		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(ChangesScene.class, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(w - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		NinePatch panel = Chrome.get(Chrome.Type.TOAST);

		int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
		int ph = h - 36;

		panel.size( pw, ph );
		panel.x = (w - pw) / 2f;
		panel.y = title.bottom() + 5;
		align( panel );
		add( panel );
		
		final ArrayList<ChangeInfo> changeInfos = new ArrayList<>();
		
		switch (changesSelected){
			case 0: default:
				v0_2_X_Changes.addAllChanges(changeInfos);
				v0_1_X_Changes.addAllChanges(changeInfos);
				break;
		}

		ScrollPane list = new ScrollPane( new Component() ){

			@Override
			public void onClick(float x, float y) {
				for (ChangeInfo info : changeInfos){
					if (info.onClick( x, y )){
						return;
					}
				}
			}

		};
		add( list );

		Component content = list.content();
		content.clear();

		float posY = 0;
		float nextPosY = 0;
		boolean second = false;
		for (ChangeInfo info : changeInfos) {
			if (info.major) {
				posY = nextPosY;
				second = false;
				info.setRect(0, posY, panel.innerWidth(), 0);
				content.add(info);
				posY = nextPosY = info.bottom();
			} else {
				if (!second){
					second = true;
					info.setRect(0, posY, panel.innerWidth()/2f, 0);
					content.add(info);
					nextPosY = info.bottom();
				} else {
					second = false;
					info.setRect(panel.innerWidth()/2f, posY, panel.innerWidth()/2f, 0);
					content.add(info);
					nextPosY = Math.max(info.bottom(), nextPosY);
					posY = nextPosY;
				}
			}
		}
		if (changesSelected == 0) {
			posY = nextPosY;
			ChangeSelection selection = new ChangeSelection(Messages.get(ChangesScene.class, "earlier"),
					Messages.get(ChangesScene.class, "shatteredpd")) {
				@Override
				public void onClick() {
					ChangesScene.changesSelected = 0;
					CarbonizedPixelDungeon.switchNoFade(ChangesScene.class);
				}
			};
			selection.icon(Icons.get(Icons.SHPX));
			selection.hardlight(Window.TITLE_COLOR);
			selection.setRect(0, posY, panel.innerWidth(), 0);
			content.add(selection);
			posY = selection.bottom();
		}

		content.setSize( panel.innerWidth(), (int)Math.ceil(posY) );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth() + 2,
				panel.innerHeight() + 2);
		if (content.height() <= list.height()) list.scrollTo(0, 0);
		else list.scrollTo(0, fromChangesScene ? posY - list.height() : 0);

		StyledButton btnBeta = new StyledButton(Chrome.Type.TOAST, "0.2-0.1") {
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 0) {
					changesSelected = 0;
					CarbonizedPixelDungeon.seamlessResetScene();
				}
			}
			@Override
			protected void onPointerDown() {
				bg.brightness( 1.2f );
				Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
			}
			@Override
			protected void onPointerUp() {
				bg.resetColor();
			}
		};
		if (changesSelected != 0) btnBeta.textColor( 0xBBBBBB );
		btnBeta.active = changesSelected != 0;
		btnBeta.setRect(list.left()-4f, list.bottom(), panel.width(), changesSelected == 0 ? 19 : 15);
		addToBack(btnBeta);

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}
	
	@Override
	public void onBackPressed() {
		CarbonizedPixelDungeon.switchNoFade(TitleScene.class);
	}

}
