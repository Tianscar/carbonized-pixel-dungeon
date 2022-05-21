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

import com.ansdoship.carbonizedpixeldungeon.*;
import com.ansdoship.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.ansdoship.carbonizedpixeldungeon.effects.BannerSprites;
import com.ansdoship.carbonizedpixeldungeon.effects.Fireball;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.ui.Archs;
import com.ansdoship.carbonizedpixeldungeon.ui.Icons;
import com.ansdoship.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.ansdoship.carbonizedpixeldungeon.ui.StyledButton;
import com.ansdoship.pixeldungeonclasses.glwrap.Blending;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.ColorBlock;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Music;
import com.ansdoship.pixeldungeonclasses.utils.FileUtils;

public class WelcomeScene extends PixelScene {

	private static final int LATEST_UPDATE = CarbonizedPixelDungeon.v0_0_1;

	@Override
	public void create() {
		super.create();

		final int previousVersion = PDSettings.version();

		if (CarbonizedPixelDungeon.versionCode == previousVersion && !PDSettings.intro()) {
			CarbonizedPixelDungeon.switchNoFade(TitleScene.class);
			return;
		}

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME},
				new float[]{1},
				false);

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		//darkens the arches
		add(new ColorBlock(w, h, 0x88000000));

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		placeTorch(title.x + 22, title.y + 46);
		placeTorch(title.x + title.width - 22, title.y + 46);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );
		
		StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				if (previousVersion == 0 || PDSettings.intro()){
					PDSettings.version(CarbonizedPixelDungeon.versionCode);
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					CarbonizedPixelDungeon.switchScene(HeroSelectScene.class);
				} else {
					updateVersion(previousVersion);
					CarbonizedPixelDungeon.switchScene(TitleScene.class);
				}
			}
		};

		float buttonY = Math.min(topRegion + (PixelScene.landscape() ? 60 : 120), h - 24);

		if (previousVersion != 0 && !PDSettings.intro()){
			StyledButton changes = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(TitleScene.class, "changes")){
				@Override
				protected void onClick() {
					super.onClick();
					updateVersion(previousVersion);
					CarbonizedPixelDungeon.switchScene(ChangesScene.class);
				}
			};
			okay.setRect(title.x, buttonY, (title.width()/2)-2, 20);
			add(okay);

			changes.setRect(okay.right()+2, buttonY, (title.width()/2)-2, 20);
			changes.icon(Icons.get(Icons.CHANGES));
			add(changes);
		} else {
			okay.text(Messages.get(TitleScene.class, "enter"));
			okay.setRect(title.x, buttonY, title.width(), 20);
			okay.icon(Icons.get(Icons.ENTER));
			add(okay);
		}

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		String message;
		if (previousVersion == 0 || PDSettings.intro()) {
			message = Messages.get(this, "welcome_msg");
		} else if (previousVersion <= CarbonizedPixelDungeon.versionCode) {
			if (previousVersion < LATEST_UPDATE){
				message = Messages.get(this, "update_intro");
				message += "\n\n" + Messages.get(this, "update_msg");
			} else {
				//TODO: change the messages here in accordance with the type of patch.
				message = Messages.get(this, "patch_intro");
				message += "\n";
				//message += "\n" + Messages.get(this, "patch_balance");
				message += "\n" + Messages.get(this, "patch_bugfixes");
				//message += "\n" + Messages.get(this, "patch_translations");

			}
		} else {
			message = Messages.get(this, "what_msg");
		}
		text.text(message, w-20);
		float textSpace = okay.top() - topRegion - 4;
		text.setPos((w - text.width()) / 2f, (topRegion + 2) + (textSpace - text.height())/2);
		add(text);

	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private void updateVersion(int previousVersion){

		//update rankings, to update any data which may be outdated
		if (previousVersion < LATEST_UPDATE){
			try {
				Rankings.INSTANCE.load();
				for (Rankings.Record rec : Rankings.INSTANCE.records.toArray(new Rankings.Record[0])){
					try {
						Rankings.INSTANCE.loadGameData(rec);
						Rankings.INSTANCE.saveGameData(rec);
					} catch (Exception e) {
						//if we encounter a fatal per-record error, then clear that record
						Rankings.INSTANCE.records.remove(rec);
						CarbonizedPixelDungeon.reportException(e);
					}
				}
				Rankings.INSTANCE.save();
			} catch (Exception e) {
				//if we encounter a fatal error, then just clear the rankings
				FileUtils.deleteFile( Rankings.RANKINGS_FILE );
				CarbonizedPixelDungeon.reportException(e);
			}

		}

		PDSettings.version(CarbonizedPixelDungeon.versionCode);
	}
	
}
