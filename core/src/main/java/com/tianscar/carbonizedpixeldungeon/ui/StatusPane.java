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

package com.tianscar.carbonizedpixeldungeon.ui;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Challenges;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.PDAction;
import com.tianscar.carbonizedpixeldungeon.Statistics;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Hunger;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.input.GameAction;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.journal.Document;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.BitmapText;
import com.tianscar.carbonizedpixeldungeon.noosa.Camera;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.NinePatch;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.noosa.particles.Emitter;
import com.tianscar.carbonizedpixeldungeon.noosa.ui.Component;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.HeroSprite;
import com.tianscar.carbonizedpixeldungeon.utils.ColorMath;
import com.tianscar.carbonizedpixeldungeon.windows.WndChallenges;
import com.tianscar.carbonizedpixeldungeon.windows.WndGame;
import com.tianscar.carbonizedpixeldungeon.windows.WndHero;
import com.tianscar.carbonizedpixeldungeon.windows.WndJournal;
import com.tianscar.carbonizedpixeldungeon.windows.WndKeyBindings;
import com.tianscar.carbonizedpixeldungeon.windows.WndStory;

public class StatusPane extends Component {

	private NinePatch bg;
	private Image avatar;
	public static float talentBlink;
	private float warning;

	private static final float FLASH_RATE = (float)(Math.PI*1.5f); //1.5 blinks per second

	private int lastTier = 0;

	private Image rawShielding;
	private Image shieldedHP;
	private Image hp;
	private Image hg;

	private Image exp;

	private BossHealthBar bossHP;

	private int lastLvl = -1;

	private RenderedTextBlock hpText;
	private RenderedTextBlock hgText;
	private RenderedTextBlock expText;

	private Image depthIcon;
	private BitmapText depthText;
	private Button depthButton;

	private Image challengeIcon;
	private BitmapText challengeText;
	private Button challengeButton;

	private DangerIndicator danger;
	private BuffIndicator buffs;
	private Compass compass;

	private JournalButton btnJournal;
	private MenuButton btnMenu;

	private Toolbar.PickedUpItem pickedUp;
	
	private BitmapText version;

	@Override
	protected void createChildren() {

		bg = new NinePatch( Assets.Interfaces.STATUS, 0, 0, 128, 43, 85, 0, 45, 0 );
		add( bg );

		add( new Button(){
			@Override
			protected void onClick () {
				Camera.main.panTo( Dungeon.hero.sprite.center(), 5f );
				GameScene.show( new WndHero() );
			}

			@Override
			public GameAction keyAction() {
				return PDAction.HERO_INFO;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "hero_info"));
			}
		}.setRect( 0, 1, 30, 30 ));

		depthIcon = Icons.get(Dungeon.level.feeling);
		add(depthIcon);

		depthText = new BitmapText( Integer.toString( Dungeon.depth ), PixelScene.pixelFont);
		depthText.hardlight( 0xCACFC2 );
		depthText.measure();
		add( depthText );

		depthButton = new Button(){
			@Override
			protected String hoverText() {
				switch (Dungeon.level.feeling) {
					case CHASM:     return Messages.get(GameScene.class, "chasm");
					case WATER:     return Messages.get(GameScene.class, "water");
					case GRASS:     return Messages.get(GameScene.class, "grass");
					case DARK:      return Messages.get(GameScene.class, "dark");
					case LARGE:     return Messages.get(GameScene.class, "large");
					case TRAPS:     return Messages.get(GameScene.class, "traps");
					case SECRETS:   return Messages.get(GameScene.class, "secrets");
					case NONE:      return Messages.get(GameScene.class, "none");
				}
				return null;
			}

			@Override
			protected void onClick() {
				super.onClick();
				int tmp_index = WndJournal.last_index;
				WndJournal wndJournal = new WndJournal();
				wndJournal.select(2);
				WndJournal.last_index = tmp_index;
				GameScene.show( wndJournal );
			}
		};
		add(depthButton);

		if (Challenges.activeChallenges() > 0){
			challengeIcon = Icons.get(Icons.CHAL_COUNT);
			add(challengeIcon);

			challengeText = new BitmapText( Integer.toString( Challenges.activeChallenges() ), PixelScene.pixelFont);
			challengeText.hardlight( 0xCACFC2 );
			challengeText.measure();
			add( challengeText );

			challengeButton = new Button(){
				@Override
				protected void onClick() {
					GameScene.show(new WndChallenges(Dungeon.challenges, false));
				}

				@Override
				protected String hoverText() {
					return Messages.get(WndChallenges.class, "title");
				}
			};
			add(challengeButton);
		}

		btnJournal = new JournalButton();
		add( btnJournal );

		btnMenu = new MenuButton();
		add( btnMenu );

		avatar = HeroSprite.avatar( Dungeon.hero.heroClass, lastTier );
		add( avatar );

		talentBlink = 0;

		compass = new Compass( Statistics.amuletObtained ? Dungeon.level.entrance : Dungeon.level.exit );
		add( compass );

		rawShielding = new Image( Assets.Interfaces.STAT_BAR, 0, 4, 32, 4 );
		rawShielding.alpha(0.5f);
		add(rawShielding);

		shieldedHP = new Image( Assets.Interfaces.STAT_BAR, 0, 4, 32, 4 );
		add(shieldedHP);

		hp = new Image( Assets.Interfaces.STAT_BAR, 0, 0, 32, 4 );
		add( hp );

		hg = new Image( Assets.Interfaces.STAT_BAR, 0, 8, 32, 4 );
		add( hg );

		exp = new Image( Assets.Interfaces.XP_BAR );
		add( exp );

		bossHP = new BossHealthBar();
		add( bossHP );

		danger = new DangerIndicator();
		add( danger );

		hpText = PixelScene.renderTextBlock( 6 );
		add( hpText );

		hgText = PixelScene.renderTextBlock( 6 );
		add( hgText );

		expText = PixelScene.renderTextBlock( 6 );
		add( expText );

		buffs = new BuffIndicator( Dungeon.hero );
		add( buffs );

		add( pickedUp = new Toolbar.PickedUpItem());
		
		version = new BitmapText( "v" + Game.version, PixelScene.pixelFont);
		version.alpha( 0.5f );
		add(version);
	}

	@Override
	protected void layout() {

		height = 43;

		bg.size( width, bg.height );

		avatar.x = bg.x + 15 - avatar.width / 2f;
		avatar.y = bg.y + 16 - avatar.height / 2f;
		PixelScene.align(avatar);

		compass.x = avatar.x + avatar.width / 2f - compass.origin.x;
		compass.y = avatar.y + avatar.height / 2f - compass.origin.y;
		PixelScene.align(compass);

		hp.x = shieldedHP.x = rawShielding.x = 2;
		hp.y = shieldedHP.y = rawShielding.y = 31;

		hg.x = hp.x;
		hg.y = hp.y + hp.height + 2;

		bossHP.setPos( 6 + (width - bossHP.width())/2, 20);

		danger.setPos( width - danger.width(), 20 );

		hpText.setPos( 31, 12 );
		PixelScene.align( hpText );

		hgText.setPos( 31, 19 );
		PixelScene.align( hgText );

		expText.setPos( 31, 5 );
		PixelScene.align( expText );

		buffs.setPos( 31, 28 );

		btnMenu.setPos( width - btnMenu.width(), 1 );

		btnJournal.setPos( width - btnJournal.width() - btnMenu.width() + 2, 1 );

		depthIcon.x = btnJournal.left() - 7 + (7 - depthIcon.width())/2f - 0.1f;
		depthIcon.y = y + 2;
		PixelScene.align(depthIcon);

		depthText.scale.set(PixelScene.align(0.67f));
		depthText.x = depthIcon.x + (depthIcon.width() - depthText.width())/2f;
		depthText.y = depthIcon.y + depthIcon.height();
		PixelScene.align(depthText);

		depthButton.setRect(depthIcon.x, depthIcon.y, depthIcon.width(), depthIcon.height() + depthText.height());

		if (challengeIcon != null) {
			challengeIcon.x = btnJournal.left() - 14 + (7 - challengeIcon.width())/2f - 0.1f;
			challengeIcon.y = y + 2;
			PixelScene.align(challengeIcon);

			challengeText.scale.set(PixelScene.align(0.67f));
			challengeText.x = challengeIcon.x + (challengeIcon.width() - challengeText.width())/2f;
			challengeText.y = challengeIcon.y + challengeIcon.height();
			PixelScene.align(challengeText);

			challengeButton.setRect(challengeIcon.x, challengeIcon.y, challengeIcon.width(), challengeIcon.height() + challengeText.height());
		}
		
		version.scale.set(PixelScene.align(0.5f));
		version.measure();
		version.x = width - version.width();
		version.y = btnMenu.bottom() + (4 - version.baseLine());
		PixelScene.align(version);
	}
	
	private static final int[] warningColors = new int[] { 0x660000, 0xCC0000, 0x660000 };

	@Override
	public void update() {
		super.update();
		
		int health = Dungeon.hero.HP;
		int shield = Dungeon.hero.shielding();
		int max = Dungeon.hero.HT;

		if (!Dungeon.hero.isAlive()) {
			avatar.tint(0x000000, 0.5f);
		} else if ((health/(float)max) < 0.3f) {
			warning += Game.elapsed * 5f *(0.4f - (health/(float)max));
			warning %= 1f;
			avatar.tint(ColorMath.interpolate(warning, warningColors), 0.5f );
		} else if (talentBlink > 0.33f){ //stops early so it doesn't end in the middle of a blink
			talentBlink -= Game.elapsed;
			avatar.tint(1, 1, 0, (float)Math.abs(Math.cos(talentBlink*FLASH_RATE))/2f);
		} else {
			avatar.resetColor();
		}

		hp.scale.x = Math.max( 0, (health-shield)/(float)max);
		shieldedHP.scale.x = health/(float)max;
		rawShielding.scale.x = shield/(float)max;

		float hunger = Dungeon.hero.hunger();
		hg.scale.x = Math.max( 0, hunger / Hunger.STARVING );

		exp.scale.x = (width / exp.width) * Dungeon.hero.exp / Dungeon.hero.maxExp();

		if (Dungeon.hero.lvl != lastLvl) {

			if (lastLvl != -1) {
				showStarParticles();
			}

			lastLvl = Dungeon.hero.lvl;
		}

		hpText.text(shield <= 0 ? health + "/" + max : health + "+" + shield + "/" + max);
		if ((health/(float)max) < 0.3f) hpText.hardlight(CharSprite.NEGATIVE);
		else if ((health/(float)max) < 0.5f) hpText.hardlight(CharSprite.WARNING);
		else hpText.resetColor();

		hgText.text((int) hunger + "/" + (int) Hunger.STARVING);
		if (hunger == 0) hgText.hardlight(CharSprite.NEGATIVE);
		else if (hunger <= Hunger.STARVING - Hunger.HUNGRY) hgText.hardlight(CharSprite.WARNING);
		else hgText.resetColor();

		expText.text(Dungeon.hero.exp + "/" + Dungeon.hero.maxExp() + " _[" + lastLvl + "]_");

		int tier = Dungeon.hero.tier();
		if (tier != lastTier) {
			lastTier = tier;
			avatar.copy( HeroSprite.avatar( Dungeon.hero.heroClass, tier ) );
		}
	}

	public void pickup( Item item, int cell) {
		pickedUp.reset( item,
			cell,
			btnJournal.journalIcon.x + btnJournal.journalIcon.width()/2f,
			btnJournal.journalIcon.y + btnJournal.journalIcon.height()/2f);
	}

	public void flashForPage( String page ){
		btnJournal.flashingPage = page;
	}
	
	public void updateKeys(){
		btnJournal.updateKeyDisplay();
	}

	private static class JournalButton extends Button {

		private Image bg;
		private Image journalIcon;
		private KeyDisplay keyIcon;
		
		private String flashingPage = null;

		public JournalButton() {
			super();

			width = bg.width + 4;
			height = bg.height + 4;
		}
		
		@Override
		public GameAction keyAction() {
			return PDAction.JOURNAL;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();

			bg = new Image( Assets.Interfaces.MENU, 2, 2, 13, 11 );
			add( bg );
			
			journalIcon = new Image( Assets.Interfaces.MENU, 31, 0, 11, 7);
			add( journalIcon );
			
			keyIcon = new KeyDisplay();
			add(keyIcon);
			updateKeyDisplay();
		}

		@Override
		protected void layout() {
			super.layout();

			bg.x = x + 2;
			bg.y = y + 2;
			
			journalIcon.x = bg.x + (bg.width() - journalIcon.width())/2f;
			journalIcon.y = bg.y + (bg.height() - journalIcon.height())/2f;
			PixelScene.align(journalIcon);
			
			keyIcon.x = bg.x + 1;
			keyIcon.y = bg.y + 1;
			keyIcon.width = bg.width - 2;
			keyIcon.height = bg.height - 2;
			PixelScene.align(keyIcon);
		}

		private float time;
		
		@Override
		public void update() {
			super.update();
			
			if (flashingPage != null){
				journalIcon.am = (float)Math.abs(Math.cos( FLASH_RATE * (time += Game.elapsed) ));
				keyIcon.am = journalIcon.am;
				if (time >= Math.PI/FLASH_RATE) {
					time = 0;
				}
			}
		}

		public void updateKeyDisplay() {
			keyIcon.updateKeys();
			keyIcon.visible = keyIcon.keyCount() > 0;
			journalIcon.visible = !keyIcon.visible;
			if (keyIcon.keyCount() > 0) {
				bg.brightness(.8f - (Math.min(6, keyIcon.keyCount()) / 20f));
			} else {
				bg.resetColor();
			}
		}

		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		@Override
		protected void onPointerUp() {
			if (keyIcon.keyCount() > 0) {
				bg.brightness(.8f - (Math.min(6, keyIcon.keyCount()) / 20f));
			} else {
				bg.resetColor();
			}
		}

		@Override
		protected void onClick() {
			time = 0;
			keyIcon.am = journalIcon.am = 1;
			if (flashingPage != null){
				if (Document.ADVENTURERS_GUIDE.pageNames().contains(flashingPage)){
					GameScene.show( new WndStory( WndJournal.GuideTab.iconForPage(flashingPage),
							Document.ADVENTURERS_GUIDE.pageTitle(flashingPage),
							Document.ADVENTURERS_GUIDE.pageBody(flashingPage) ));
					Document.ADVENTURERS_GUIDE.readPage(flashingPage);
				} else {
					GameScene.show( new WndJournal() );
				}
				flashingPage = null;
			} else {
				GameScene.show( new WndJournal() );
			}
		}

		@Override
		protected String hoverText() {
			return Messages.titleCase(Messages.get(WndKeyBindings.class, "journal"));
		}

	}

	private static class MenuButton extends Button {

		private Image image;

		public MenuButton() {
			super();

			width = image.width + 4;
			height = image.height + 4;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			image = new Image( Assets.Interfaces.MENU, 17, 2, 12, 11 );
			add( image );
		}

		@Override
		protected void layout() {
			super.layout();

			image.x = x + 2;
			image.y = y + 2;
		}

		@Override
		protected void onPointerDown() {
			image.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		@Override
		protected void onPointerUp() {
			image.resetColor();
		}

		@Override
		protected void onClick() {
			GameScene.show( new WndGame() );
		}

		@Override
		public GameAction keyAction() {
			return GameAction.BACK;
		}

		@Override
		protected String hoverText() {
			return Messages.titleCase(Messages.get(WndKeyBindings.class, "menu"));
		}

	}

	public void showStarParticles(){
		Emitter emitter = (Emitter)recycle( Emitter.class );
		emitter.revive();
		emitter.pos( avatar.center() );
		emitter.burst( Speck.factory( Speck.STAR ), 12 );
	}

}
