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
import com.tianscar.carbonizedpixeldungeon.Chrome;
import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.ui.*;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeInfo;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeSelection;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_1_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_2_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_3_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_4_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_5_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_6_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_7_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_8_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v0_9_X_Changes;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.shatteredpd.v1_0_X_Changes;
import com.tianscar.pixeldungeonclasses.noosa.Camera;
import com.tianscar.pixeldungeonclasses.noosa.NinePatch;
import com.tianscar.pixeldungeonclasses.noosa.audio.Music;
import com.tianscar.pixeldungeonclasses.noosa.ui.Component;

import java.util.ArrayList;

public class ChangesScene extends PixelScene {
	
	public static int changesSelected = 0;
	
	@Override
	public void create() {
		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.TITLE},
				new float[]{1},
				false);

		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9 );
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
				v1_0_X_Changes.addAllChanges(changeInfos);
				v0_9_X_Changes.addAllChanges(changeInfos);
				break;
			case 1:
				v0_8_X_Changes.addAllChanges(changeInfos);
				break;
			case 2:
				v0_7_X_Changes.addAllChanges(changeInfos);
				break;
			case 3:
				v0_6_X_Changes.addAllChanges(changeInfos);
				break;
			case 4:
				v0_5_X_Changes.addAllChanges(changeInfos);
				v0_4_X_Changes.addAllChanges(changeInfos);
				v0_3_X_Changes.addAllChanges(changeInfos);
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
		if (changesSelected == 0) {
			ChangeSelection selection = new ChangeSelection(Messages.get(this, "later"), Messages.get(this, "carbonizedpd")) {
				@Override
				public void onClick() {
					NewChangesScene.changesSelected = 0;
					NewChangesScene.fromChangesScene = true;
					CarbonizedPixelDungeon.switchNoFade(NewChangesScene.class);
				}
			};
			selection.icon(Icons.get(Icons.CAPD));
			selection.hardlight(Window.TITLE_COLOR);
			selection.setRect(0, posY, panel.innerWidth(), 0);
			content.add(selection);
			posY = nextPosY = selection.bottom();
		}
		for (ChangeInfo info : changeInfos){
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

		content.setSize( panel.innerWidth(), (int)Math.ceil(posY) );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth() + 2,
				panel.innerHeight() + 2);
		list.scrollTo(0, 0);

		StyledButton btn0_9 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "1.0-0.9"){
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 0) {
					changesSelected = 0;
					CarbonizedPixelDungeon.seamlessResetScene();
				}
			}
		};
		if (changesSelected != 0) btn0_9.textColor( 0xBBBBBB );
		btn0_9.active = changesSelected != 0;
		btn0_9.setRect(list.left()-4f, list.bottom(), 34, changesSelected == 0 ? 19 : 15);
		addToBack(btn0_9);

		StyledButton btn0_8 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "0.8"){
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 1) {
					changesSelected = 1;
					CarbonizedPixelDungeon.seamlessResetScene();
				}
			}
		};
		if (changesSelected != 1) btn0_8.textColor( 0xBBBBBB );
		btn0_8.active = changesSelected != 1;
		btn0_8.setRect(btn0_9.right()+1, list.bottom(), 23, changesSelected == 1 ? 19 : 15);
		addToBack(btn0_8);
		
		StyledButton btn0_7 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "0.7"){
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 2) {
					changesSelected = 2;
					CarbonizedPixelDungeon.seamlessResetScene();
				}
			}
		};
		if (changesSelected != 2) btn0_7.textColor( 0xBBBBBB );
		btn0_7.active = changesSelected != 2;
		btn0_7.setRect(btn0_8.right() + 1, btn0_8.top(), 23, changesSelected == 2 ? 19 : 15);
		addToBack(btn0_7);
		
		StyledButton btn0_6 = new StyledButton(Chrome.Type.GREY_BUTTON_TR, "0.6"){
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 3) {
					changesSelected = 3;
					CarbonizedPixelDungeon.seamlessResetScene();
				}
			}
		};
		if (changesSelected != 3) btn0_6.textColor( 0xBBBBBB );
		btn0_6.active = changesSelected != 3;
		btn0_6.setRect(btn0_7.right() + 1, btn0_8.top(), 23, changesSelected == 3 ? 19 : 15);
		addToBack(btn0_6);
		
		StyledButton btnOld = new StyledButton(Chrome.Type.GREY_BUTTON_TR,"0.5-0.1"){
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 4) {
					changesSelected = 4;
					CarbonizedPixelDungeon.seamlessResetScene();
				}
			}
		};
		if (changesSelected != 4) btnOld.textColor( 0xBBBBBB );
		btnOld.active = changesSelected != 4;
		btnOld.setRect(btn0_6.right() + 1, btn0_8.top(), 34, changesSelected == 4 ? 19 : 15);
		addToBack(btnOld);

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
