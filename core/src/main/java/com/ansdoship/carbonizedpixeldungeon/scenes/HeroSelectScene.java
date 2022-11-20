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
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.ui.*;
import com.ansdoship.carbonizedpixeldungeon.windows.WndChallenges;
import com.ansdoship.carbonizedpixeldungeon.windows.WndHeroInfo;
import com.ansdoship.carbonizedpixeldungeon.windows.WndKeyBindings;
import com.ansdoship.carbonizedpixeldungeon.windows.WndMessage;
import com.ansdoship.pixeldungeonclasses.gltextures.SmartTexture;
import com.ansdoship.pixeldungeonclasses.gltextures.TextureCache;
import com.ansdoship.pixeldungeonclasses.glwrap.Matrix;
import com.ansdoship.pixeldungeonclasses.glwrap.Quad;
import com.ansdoship.pixeldungeonclasses.input.GameAction;
import com.ansdoship.pixeldungeonclasses.input.PointerEvent;
import com.ansdoship.pixeldungeonclasses.noosa.*;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Music;
import com.ansdoship.pixeldungeonclasses.utils.Point;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Calendar;

public class HeroSelectScene extends PixelScene {

	private static HeroClass[] heroClasses = new HeroClass[] {
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

		float vx = align((w - SKY_WIDTH) / 2f);
		float vy = align((h - SKY_HEIGHT - BUTTON_HEIGHT) / 2f);

		Point s = Camera.main.cameraToScreen( vx, vy );
		viewport = new Camera( s.x, s.y, SKY_WIDTH, SKY_HEIGHT, defaultZoom );
		Camera.add( viewport );
		
		Group window = new Group();
		window.camera = viewport;
		add( window );
		
		boolean dayTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= 7;
		
		Sky sky = new Sky( dayTime );
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
		frame.y = vy - FRAME_MARGIN_TOP;
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

		IconButton prevBtn = new IconButton( Icons.get(Icons.LEFTARROW_ALT) ) {
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

		IconButton nextBtn = new IconButton( Icons.get(Icons.RIGHTARROW_ALT) ) {
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
	
	private static class Sky extends Visual {
		
		private static final int[] day		= {0xFF4488FF, 0xFFCCEEFF};
		private static final int[] night	= {0xFF001155, 0xFF335980};
		
		private SmartTexture texture;
		private FloatBuffer verticesBuffer;
		
		public Sky( boolean dayTime ) {
			super( 0, 0, 1, 1 );

			texture = TextureCache.createGradient( dayTime ? day : night );
			
			float[] vertices = new float[16];
			verticesBuffer = Quad.create();
			
			vertices[2]		= 0.25f;
			vertices[6]		= 0.25f;
			vertices[10]	= 0.75f;
			vertices[14]	= 0.75f;
			
			vertices[3]		= 0;
			vertices[7]		= 1;
			vertices[11]	= 1;
			vertices[15]	= 0;
			
			
			vertices[0] 	= 0;
			vertices[1] 	= 0;
			
			vertices[4] 	= 1;
			vertices[5] 	= 0;
			
			vertices[8] 	= 1;
			vertices[9] 	= 1;
			
			vertices[12]	= 0;
			vertices[13]	= 1;

			((Buffer)verticesBuffer).position( 0 );
			verticesBuffer.put( vertices );
		}
		
		@Override
		public void draw() {
			
			super.draw();

			NoosaScript script = NoosaScript.get();
			
			texture.bind();
			
			script.camera( camera() );
			
			script.uModel.valueM4( matrix );
			script.lighting(
				rm, gm, bm, am,
				ra, ga, ba, aa );
			
			script.drawQuad( verticesBuffer );
		}
	}
	
	private static class Cloud extends Image {
		
		private static int lastIndex = -1;
		
		public Cloud( float y, boolean dayTime ) {
			super( Assets.Interfaces.SURFACE );
			
			int index;
			do {
				index = Random.Int( 3 );
			} while (index == lastIndex);
			
			switch (index) {
			case 0:
				frame( 88, 0, 49, 20 );
				break;
			case 1:
				frame( 88, 20, 49, 22 );
				break;
			case 2:
				frame( 88, 42, 50, 18 );
				break;
			}
			
			lastIndex = index;
			
			this.y = y;

			scale.set( 1 - y / SKY_HEIGHT );
			x = Random.Float( SKY_WIDTH + width() ) - width();
			speed.x = scale.x * (dayTime ? +8 : -8);
			
			if (dayTime) {
				tint( 0xCCEEFF, 1 - scale.y );
			} else {
				rm = gm = bm = +3.0f;
				ra = ga = ba = -2.1f;
			}
		}
		
		@Override
		public void update() {
			super.update();
			if (speed.x > 0 && x > SKY_WIDTH) {
				x = -width();
			} else if (speed.x < 0 && x < -width()) {
				x = SKY_WIDTH;
			}
		}
	}

	private static class Avatar extends Image {
		
		private static final int WIDTH	= 24;
		private static final int HEIGHT	= 32;
		
		public Avatar( HeroClass cl ) {
			super( Assets.Sprites.AVATARS );
			frame( new TextureFilm( texture, WIDTH, HEIGHT ).get( cl.ordinal() ) );
		}

		public void heroClass( HeroClass cl ) {
			frame( new TextureFilm( texture, WIDTH, HEIGHT ).get( cl.ordinal() ) );
		}

	}
	
	private static class GrassPatch extends Image {
		
		public static final int WIDTH	= 16;
		public static final int HEIGHT	= 14;
		
		private float tx;
		private float ty;
		
		private double a = Random.Float( 5 );
		private double angle;
		
		private boolean forward;
		
		public GrassPatch( float tx, float ty, boolean forward ) {
			
			super( Assets.Interfaces.SURFACE );
			
			frame( 88 + Random.Int( 4 ) * WIDTH, 60, WIDTH, HEIGHT );
			
			this.tx = tx;
			this.ty = ty;
			
			this.forward = forward;
		}
		
		@Override
		public void update() {
			super.update();
			a += Random.Float( Game.elapsed * 5 );
			angle = (2 + Math.cos( a )) * (forward ? +0.2 : -0.2);
			
			scale.y = (float)Math.cos( angle );
			
			x = tx + (float)Math.tan( angle ) * width;
			y = ty - scale.y * height;
		}
		
		@Override
		protected void updateMatrix() {
			super.updateMatrix();
			Matrix.skewX( matrix, (float)(angle / Matrix.G2RAD) );
		}
	}
}
