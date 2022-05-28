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
import com.ansdoship.carbonizedpixeldungeon.messages.Languages;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.services.news.News;
import com.ansdoship.carbonizedpixeldungeon.services.updates.AvailableUpdateData;
import com.ansdoship.carbonizedpixeldungeon.services.updates.Updates;
import com.ansdoship.carbonizedpixeldungeon.sprites.CharSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.ui.Archs;
import com.ansdoship.carbonizedpixeldungeon.ui.Icons;
import com.ansdoship.carbonizedpixeldungeon.ui.StyledButton;
import com.ansdoship.carbonizedpixeldungeon.ui.Window;
import com.ansdoship.carbonizedpixeldungeon.windows.WndOptions;
import com.ansdoship.carbonizedpixeldungeon.windows.WndSettings;
import com.ansdoship.pixeldungeonclasses.glwrap.Blending;
import com.ansdoship.pixeldungeonclasses.noosa.BitmapText;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Music;
import com.ansdoship.pixeldungeonclasses.utils.ColorMath;

import java.util.Date;

public class TitleScene extends PixelScene {
	
	@Override
	public void create() {
		
		super.create();

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
		
		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		placeTorch(title.x + 28, title.y + 46);
		placeTorch(title.x + title.width - 28, title.y + 46);

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

		final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;
		
		StyledButton btnPlay = new StyledButton(GREY_TR, Messages.get(this, "enter")){
			@Override
			protected void onClick() {
				if (GamesInProgress.checkAll().size() == 0){
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					CarbonizedPixelDungeon.switchScene(HeroSelectScene.class);
				} else {
					CarbonizedPixelDungeon.switchNoFade( StartScene.class );
				}
			}
			
			@Override
			protected boolean onLongClick() {
				//making it easier to start runs quickly while debugging
				if (Game.platform.isDebug()) {
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					CarbonizedPixelDungeon.switchScene(HeroSelectScene.class);
					return true;
				}
				return super.onLongClick();
			}
		};
		btnPlay.icon(Icons.get(Icons.ENTER));
		add(btnPlay);

		StyledButton btnFeedback = new FeedbackButton(GREY_TR, Messages.get(this, "feedback"));
		add(btnFeedback);

		StyledButton btnRankings = new StyledButton(GREY_TR,Messages.get(this, "rankings")){
			@Override
			protected void onClick() {
				CarbonizedPixelDungeon.switchNoFade( RankingsScene.class );
			}
		};
		btnRankings.icon(Icons.get(Icons.RANKINGS));
		add(btnRankings);

		StyledButton btnBadges = new StyledButton(GREY_TR, Messages.get(this, "badges")){
			@Override
			protected void onClick() {
				CarbonizedPixelDungeon.switchNoFade( BadgesScene.class );
			}
		};
		btnBadges.icon(Icons.get(Icons.BADGES));
		add(btnBadges);

		StyledButton btnNews = new NewsButton(GREY_TR, Messages.get(this, "news"));
		btnNews.icon(Icons.get(Icons.NEWS));
		add(btnNews);

		StyledButton btnChanges = new ChangesButton(GREY_TR, Messages.get(this, "changes"));
		btnChanges.icon(Icons.get(Icons.CHANGES));
		add(btnChanges);

		StyledButton btnSettings = new SettingsButton(GREY_TR, Messages.get(this, "settings"));
		add(btnSettings);

		StyledButton btnAbout = new StyledButton(GREY_TR, Messages.get(this, "about")){
			@Override
			protected void onClick() {
				CarbonizedPixelDungeon.switchScene( AboutScene.class );
			}
		};
		btnAbout.icon( new ItemSprite(ItemSpriteSheet.CARBON_STEEL) );
		add(btnAbout);
		
		final int BTN_HEIGHT = 20;
		int GAP = (int)(h - topRegion - (landscape() ? 3 : 4)*BTN_HEIGHT)/3;
		GAP /= landscape() ? 3 : 5;
		GAP = Math.max(GAP, 2);

		if (landscape()) {
			btnPlay.setRect(title.x-50, topRegion+GAP, ((title.width()+100)/2)-1, BTN_HEIGHT);
			align(btnPlay);
			btnFeedback.setRect(btnPlay.right()+2, btnPlay.top(), btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (btnPlay.width()*.67f)-1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.left(), btnRankings.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
			btnNews.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnNews.left(), btnNews.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
			btnSettings.setRect(btnNews.right()+2, btnNews.top(), btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnSettings.left(), btnSettings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
		} else {
			btnPlay.setRect(title.x, topRegion+GAP, title.width(), BTN_HEIGHT);
			align(btnPlay);
			btnFeedback.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left(), btnFeedback.bottom()+ GAP, (btnPlay.width()/2)-1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnNews.setRect(btnRankings.left(), btnRankings.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnNews.right()+2, btnNews.top(), btnNews.width(), BTN_HEIGHT);
			btnSettings.setRect(btnNews.left(), btnNews.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnSettings.right()+2, btnSettings.top(), btnSettings.width(), BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );

		fadeIn();
	}
	
	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private static class NewsButton extends StyledButton {

		public NewsButton(Chrome.Type type, String label ){
			super(type, label);
			if (PDSettings.news()) News.checkForNews();
		}

		int unreadCount = -1;

		@Override
		public void update() {
			super.update();

			if (unreadCount == -1 && News.articlesAvailable()){
				long lastRead = PDSettings.newsLastRead();
				if (lastRead == 0){
					if (News.articles().get(0) != null) {
						PDSettings.newsLastRead(News.articles().get(0).date.getTime());
					}
				} else {
					unreadCount = News.unreadArticles(new Date(PDSettings.newsLastRead()));
					if (unreadCount > 0) {
						unreadCount = Math.min(unreadCount, 9);
						text(text() + "(" + unreadCount + ")");
					}
				}
			}

			if (unreadCount > 0){
				textColor(ColorMath.interpolate( 0xFFFFFF, Window.SHPX_COLOR, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			super.onClick();
			CarbonizedPixelDungeon.switchNoFade( NewsScene.class );
		}
	}

	private static class ChangesButton extends StyledButton {

		public ChangesButton( Chrome.Type type, String label ){
			super(type, label);
			if (PDSettings.updates()) Updates.checkForUpdate();
		}

		boolean updateShown = false;

		@Override
		public void update() {
			super.update();

			if (!updateShown && (Updates.updateAvailable() || Updates.isInstallable())){
				updateShown = true;
				if (Updates.isInstallable())    text(Messages.get(TitleScene.class, "install"));
				else                            text(Messages.get(TitleScene.class, "update"));
			}

			if (updateShown){
				textColor(ColorMath.interpolate( 0xFFFFFF, Window.SHPX_COLOR, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Updates.isInstallable()){
				Updates.launchInstall();

			} else if (Updates.updateAvailable()){
				AvailableUpdateData update = Updates.updateData();

				CarbonizedPixelDungeon.scene().addToFront(new WndOptions(
						Icons.get(Icons.CHANGES),
						update.versionName == null ? Messages.get(this,"title") : Messages.get(this,"versioned_title", update.versionName),
						update.desc == null ? Messages.get(this,"desc") : update.desc,
						Messages.get(this,"update"),
						Messages.get(this,"changes")
				) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							Updates.launchUpdate(Updates.updateData());
						} else if (index == 1){
							NewChangesScene.changesSelected = 0;
							NewChangesScene.fromChangesScene = false;
							CarbonizedPixelDungeon.switchNoFade( NewChangesScene.class );
						}
					}
				});

			} else {
				NewChangesScene.changesSelected = 0;
				NewChangesScene.fromChangesScene = false;
				CarbonizedPixelDungeon.switchNoFade( NewChangesScene.class );
			}
		}

	}

	private static class SettingsButton extends StyledButton {

		public SettingsButton( Chrome.Type type, String label ){
			super(type, label);
			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				icon(Icons.get(Icons.LANGS));
				icon.hardlight(1.5f, 0, 0);
			} else {
				icon(Icons.get(Icons.PREFS));
			}
		}

		@Override
		public void update() {
			super.update();

			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				textColor(ColorMath.interpolate( 0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				WndSettings.last_index = 4;
			}
			CarbonizedPixelDungeon.switchNoFade(SettingsScene.class);
		}
	}

	private static class FeedbackButton extends StyledButton{

		public FeedbackButton( Chrome.Type type, String label ){
			super(type, label);
			icon(Icons.get(Icons.WARNING));
			textColor(Window.TITLE_COLOR);
		}

		@Override
		protected void onClick() {
			CarbonizedPixelDungeon.switchNoFade(FeedbackScene.class);
		}
	}
}
