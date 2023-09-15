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

import com.tianscar.carbonizedpixeldungeon.*;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.tianscar.carbonizedpixeldungeon.effects.BannerSprites;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.ui.*;
import com.tianscar.carbonizedpixeldungeon.scenes.SurfaceScene.Sky;
import com.tianscar.carbonizedpixeldungeon.scenes.SurfaceScene.GrassPatch;
import com.tianscar.carbonizedpixeldungeon.scenes.SurfaceScene.Cloud;
import com.tianscar.carbonizedpixeldungeon.scenes.SurfaceScene.Avatar;
import com.tianscar.carbonizedpixeldungeon.windows.WndChallenges;
import com.tianscar.carbonizedpixeldungeon.windows.WndHeroInfo;
import com.tianscar.carbonizedpixeldungeon.windows.WndKeyBindings;
import com.tianscar.carbonizedpixeldungeon.windows.WndMessage;
import com.tianscar.carbonizedpixeldungeon.input.GameAction;
import com.tianscar.carbonizedpixeldungeon.input.PointerEvent;
import com.tianscar.carbonizedpixeldungeon.noosa.*;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Music;
import com.tianscar.carbonizedpixeldungeon.utils.Point;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

import java.util.Calendar;

public class HeroSelectScene extends PixelScene {

	private static final HeroClass[] heroClasses = new HeroClass[] {
			HeroClass.WARRIOR,
			HeroClass.MAGE,
			HeroClass.ROGUE,
			HeroClass.HUNTRESS
	};
	private static int heroClassIndex = 0;
	private static void addHeroClassIndex(int add) {
		heroClassIndex = heroClassIndex + add;
		if (heroClassIndex >= heroClasses.length) heroClassIndex -= heroClasses.length;
		else if (heroClassIndex < 0) heroClassIndex += heroClasses.length;
	}

	private static HeroClass heroClass() {
		return heroClasses[heroClassIndex];
	}

	private static final int TITLE_HEIGHT = 22;

	private static final int FRAME_WIDTH    = 88;
	private static final int FRAME_HEIGHT    = 125;

	private static final int FRAME_MARGIN_TOP    = 9;
	private static final int FRAME_MARGIN_X        = 4;

	private static final int BUTTON_HEIGHT    = 20;

	private static final int SKY_WIDTH    = 80;
	private static final int SKY_HEIGHT    = 112;

	private static final int NSTARS		= 100;
	private static final int NCLOUDS	= 5;

	private Camera viewport;

	private Avatar a;
	private RedButton startBtn;
	private Image frame;

	private IconButton infoButton;
	private IconButton challengeButton;
	private IconButton btnExit;

	@Override
	public void create() {
		
		super.create();

		Dungeon.hero = null;

		Badges.loadGlobal();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.HERO_SELECT},
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

		boolean addTitle = true;

		float vx = align((w - SKY_WIDTH) / 2f);
		float vh = SKY_HEIGHT + BUTTON_HEIGHT + TITLE_HEIGHT + FRAME_MARGIN_TOP * 2;
		if (vh >= h) {
			vh -= (TITLE_HEIGHT + FRAME_MARGIN_TOP * 2);
			addTitle = false;
		}
		float vy = align((h - vh) / 2f);

		float titleBottom;

		if (addTitle) {
			Image title = BannerSprites.get( BannerSprites.Type.SELECT_YOUR_HERO );

			add( title );

			title.x = align( (w - title.width()) / 2 );
			title.y = align( vy + title.height() / 2 - FRAME_MARGIN_TOP );

			titleBottom = align( title.y + title.height() + FRAME_MARGIN_TOP );
		}
		else titleBottom = vy;

		Point s = Camera.main.cameraToScreen( vx, titleBottom );
		viewport = new Camera( s.x, s.y, SKY_WIDTH, SKY_HEIGHT, defaultZoom );
		Camera.add( viewport );
		
		Group window = new Group();
		window.camera = viewport;
		add( window );

		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		boolean dayTime = hour >= 7 && hour <= 18;

		Sky sky = new Sky( hour ) {
			@Override
			public void update() {
				super.update();
				if (hour != Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) CarbonizedPixelDungeon.seamlessResetScene();
			}
		};
		sky.scale.set( SKY_WIDTH, SKY_HEIGHT );
		window.add( sky );
		
		if (!dayTime) {
			for (int i=0; i < NSTARS; i++) {
				float size = Random.Float();
				ColorBlock star = new ColorBlock( size, size, 0xFFFFFFFF );
				star.x = Random.Float( SKY_WIDTH ) - size / 2;
				star.y = Random.Float( SKY_HEIGHT ) - size / 2;
				star.am = size * (1 - star.y / SKY_HEIGHT);
				window.add( star );
			}
		}
		
		float range = SKY_HEIGHT * 2 / 3;
		for (int i=0; i < NCLOUDS; i++) {
			Cloud cloud = new Cloud( (NCLOUDS - 1 - i) * (range / NCLOUDS) + Random.Float( range / NCLOUDS ), dayTime );
			window.add( cloud );
		}
		
		int nPatches = (int)(sky.width() / GrassPatch.WIDTH + 1);
		
		for (int i=0; i < nPatches * 4; i++) {
			GrassPatch patch = new GrassPatch( (i - 0.75f) * GrassPatch.WIDTH / 4, SKY_HEIGHT + 1, dayTime );
			patch.brightness( dayTime ? 0.7f : 0.4f );
			window.add( patch );
		}
		
		a = new Avatar( heroClass() );
		// Removing semitransparent contour
		a.am = 2; a.aa = -1;
		a.x = (SKY_WIDTH - a.width) / 2;
		a.y = SKY_HEIGHT - a.height;
		align(a);
		
		window.add( a );

		window.add( new PointerArea( a ) {
			protected void onClick( PointerEvent event ) {
				if (GamesInProgress.selectedClass == null) return;
				HeroClass cl = GamesInProgress.selectedClass;
				if( cl.isUnlocked() ) CarbonizedPixelDungeon.scene().addToFront(new WndHeroInfo(cl));
				else CarbonizedPixelDungeon.scene().addToFront( new WndMessage(cl.unlockMsg()));
			}
		} );
		
		for (int i=0; i < nPatches; i++) {
			GrassPatch patch = new GrassPatch( (i - 0.5f) * GrassPatch.WIDTH, SKY_HEIGHT, dayTime );
			patch.brightness( dayTime ? 1.0f : 0.8f );
			window.add( patch );
		}
		
		frame = new Image( Assets.Interfaces.SURFACE );

		frame.frame( FRAME_WIDTH + GrassPatch.WIDTH*4, 0, FRAME_WIDTH, FRAME_HEIGHT );
		frame.x = vx - FRAME_MARGIN_X;
		frame.y = titleBottom - FRAME_MARGIN_TOP;
		add( frame );

		if (dayTime) {
			a.brightness( 1.2f );
		} else {
			frame.hardlight( 0xDDEEFF );
		}

		startBtn = new RedButton( "" ) {
			@Override
			protected void onClick() {
				super.onClick();

				if (GamesInProgress.selectedClass == null) return;

				Dungeon.hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

				if (PDSettings.intro()) {
					PDSettings.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					Game.switchScene( InterlevelScene.class );
				}
			}
			public void enable( boolean value ) {
				active = value;
				text.alpha( value ? 1.0f : 0.3f );
				icon.alpha( value ? 1.0f : 0.3f );
			}
		};
		startBtn.icon(Icons.get(Icons.ENTER));
		add( startBtn );

		infoButton = new IconButton(Icons.get(Icons.INFO)){
			@Override
			protected void onClick() {
				super.onClick();
				if (GamesInProgress.selectedClass == null) return;
				HeroClass cl = GamesInProgress.selectedClass;
				if( cl.isUnlocked() ) CarbonizedPixelDungeon.scene().addToFront(new WndHeroInfo(cl));
				else CarbonizedPixelDungeon.scene().addToFront( new WndMessage(cl.unlockMsg()));
			}

			@Override
			public GameAction keyAction() {
				return PDAction.HERO_INFO;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "hero_info"));
			}
		};
		infoButton.setSize(21, 21);
		add(infoButton);

		challengeButton = new IconButton(
				Icons.get( PDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
			@Override
			protected void onClick() {
				if (Game.platform.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)) {
					CarbonizedPixelDungeon.scene().addToFront(new WndChallenges(PDSettings.challenges(), true) {
						public void onBackPressed() {
							super.onBackPressed();
							icon(Icons.get(PDSettings.challenges() > 0 ? Icons.CHALLENGE_ON : Icons.CHALLENGE_OFF));
						}
					} );
				}
				else CarbonizedPixelDungeon.scene().addToFront( new WndMessage( Messages.get(HeroSelectScene.class, "challenges_unlock") ));
			}

			@Override
			public void update() {
				if( !visible && GamesInProgress.selectedClass != null){
					visible = true;
				}
				super.update();
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndChallenges.class, "title"));
			}
		};
		challengeButton.setRect(startBtn.left() + 16, Camera.main.height- BUTTON_HEIGHT-16, 21, 21);

		add(challengeButton);
		if (!(Game.platform.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY))){
			Dungeon.challenges = 0;
			PDSettings.challenges(0);
		}

		setSelectedHero();

		IconButton prevBtn = new IconButton( Icons.get(Icons.PREV) ) {
			{
				width = 20;
				height = 20;
			}
			@Override
			protected void onClick() {
				super.onClick();

				addHeroClassIndex(-1);
				setSelectedHero();
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "prev"));
			}

			@Override
			public GameAction keyAction() {
				return GameAction.PREV;
			}
		};
		prevBtn.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		prevBtn.setPos( frame.x - BUTTON_HEIGHT - FRAME_MARGIN_X, frame.y + frame.height / 2 - BUTTON_HEIGHT / 2f);
		PixelScene.align(prevBtn);
		add( prevBtn );

		IconButton nextBtn = new IconButton( Icons.get(Icons.NEXT) ) {
			{
				width = 20;
				height = 20;
			}
			@Override
			protected void onClick() {
				super.onClick();

				addHeroClassIndex(1);
				setSelectedHero();
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "next"));
			}

			@Override
			public GameAction keyAction() {
				return GameAction.NEXT;
			}
		};
		nextBtn.setSize( BUTTON_HEIGHT, BUTTON_HEIGHT );
		nextBtn.setPos( frame.x + frame.width + FRAME_MARGIN_X, frame.y + frame.height / 2 - BUTTON_HEIGHT / 2f);
		PixelScene.align(nextBtn);
		add( nextBtn );

		btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		btnExit.visible = !PDSettings.intro() || Rankings.INSTANCE.totalNumber > 0;
		
		fadeIn();
	}

	@Override
	public void destroy() {
		Camera.remove( viewport );
		super.destroy();
	}

	private void setSelectedHero() {
		HeroClass cl = heroClass();
		GamesInProgress.selectedClass = cl;
		a.heroClass(cl);

		startBtn.text(Messages.titleCase(cl.title()));
		startBtn.textColor(Window.TITLE_COLOR);
		startBtn.setSize(Math.max(startBtn.reqWidth() + 8, SKY_WIDTH - FRAME_MARGIN_X * 2), BUTTON_HEIGHT);
		startBtn.setPos( (Camera.main.width - startBtn.width())/2f, frame.y + frame.height + FRAME_MARGIN_X );
		PixelScene.align(startBtn);
		startBtn.enable( cl.isUnlocked() );

		infoButton.setPos(startBtn.right(), startBtn.top());

		challengeButton.setPos(startBtn.left()-challengeButton.width(), startBtn.top());
	}

	@Override
	public void onBackPressed() {
		CarbonizedPixelDungeon.switchScene(TitleScene.class);
	}

}
