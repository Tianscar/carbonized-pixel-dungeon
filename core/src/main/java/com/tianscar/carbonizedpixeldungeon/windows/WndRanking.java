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

package com.tianscar.carbonizedpixeldungeon.windows;

import com.tianscar.carbonizedpixeldungeon.*;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Hunger;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Belongings;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.sprites.HeroSprite;
import com.tianscar.carbonizedpixeldungeon.ui.*;
import com.tianscar.pixeldungeonclasses.noosa.*;
import com.tianscar.pixeldungeonclasses.noosa.audio.Sample;
import com.tianscar.pixeldungeonclasses.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Locale;

public class WndRanking extends WndTabbed {
	
	private static final int WIDTH			= 115;
	private static final int HEIGHT			= 152;
	
	private static Thread thread;
	private String error = null;
	
	private Image busy;
	
	public WndRanking(final Rankings.Record rec ) {
		
		super();
		resize( WIDTH, HEIGHT );
		
		if (thread != null){
			hide();
			return;
		}
		
		thread = new Thread() {
			@Override
			public void run() {
				try {
					Badges.loadGlobal();
					Rankings.INSTANCE.loadGameData( rec );
				} catch ( Exception e ) {
					error = Messages.get(WndRanking.class, "error");
				}
			}
		};

		busy = Icons.BUSY.get();
		busy.origin.set( busy.width / 2, busy.height / 2 );
		busy.angularSpeed = 720;
		busy.x = (WIDTH - busy.width) / 2;
		busy.y = (HEIGHT - busy.height) / 2;
		add( busy );
		
		thread.start();
	}
	
	@Override
	public void update() {
		super.update();
		
		if (thread != null && !thread.isAlive() && busy != null) {
			if (error == null) {
				remove( busy );
				busy = null;
				if (Dungeon.hero != null) {
					createControls();
				} else {
					hide();
				}
			} else {
				hide();
				Game.scene().add( new WndError( error ) );
			}
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		thread = null;
	}
	
	private void createControls() {

		Icons[] icons =
				{Icons.RANKINGS, Icons.BACKPACK_LRG, Icons.BADGES};
		String[] tooltips =
			{Messages.titleCase(Messages.get(this, "stats")),
					Messages.titleCase(Messages.get(this, "items")),
							Messages.titleCase(Messages.get(this, "badges"))};
		Group[] pages =
			{new StatsTab(), new ItemsTab(), new BadgesTab()};
		
		for (int i=0; i < pages.length; i++) {
			
			add( pages[i] );
			
			Tab tab = new RankingTab( tooltips[i], icons[i], pages[i] );
			add( tab );
		}

		((ItemsTab)pages[1]).updateList();

		layoutTabs();
		
		select( 0 );
	}

	private class RankingTab extends IconTab {
		
		private Group page;

		private String tooltip;
		
		public RankingTab( String tooltip, Icons icon, Group page ) {
			super( Icons.get(icon) );
			this.page = page;
			this.tooltip = tooltip;
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}

		@Override
		protected String hoverText() {
			return tooltip;
		}
	}
	
	private class StatsTab extends Group {

		private int GAP	= 4;
		
		public StatsTab() {
			super();
			
			String heroClass = Dungeon.hero.className();
			
			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar( Dungeon.hero.heroClass, Dungeon.hero.tier() ) );
			title.label( Messages.get(this, "title", Dungeon.hero.lvl, heroClass ).toUpperCase( Locale.ENGLISH ) );
			title.color(Window.TITLE_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add( title );

			IconButton btnTalents = new IconButton( Icons.get(Icons.TALENT) ){
				@Override
				protected void onClick() {
					//removes talents from upper tiers
					int tiers = 1;
					if (Dungeon.hero.lvl >= 6) tiers++;
					if (Dungeon.hero.lvl >= 12 && Dungeon.hero.subClass != HeroSubClass.NONE) tiers++;
					if (Dungeon.hero.lvl >= 20 && Dungeon.hero.armorAbility != null) tiers++;
					while (Dungeon.hero.talents.size() > tiers){
						Dungeon.hero.talents.remove(Dungeon.hero.talents.size()-1);
					}
					Game.scene().addToFront( new Window(){
						{
							TalentsPane p = new TalentsPane(false);
							add(p);
							p.setPos(0, 0);
							p.setSize(120, p.content().height());
							resize((int)p.width(), (int)p.height());
							p.setPos(0, 0);
						}
					});
				}

				@Override
				protected String hoverText() {
					return Messages.get(StatsTab.this, "talents");
				}
			};
			btnTalents.setSize(16 , 16);

			title.setSize(title.width() - btnTalents.width(), title.height());

			btnTalents.setPos(WIDTH - 18, 0);
			PixelScene.align(btnTalents);
			add(btnTalents);

			float pos = btnTalents.bottom();

			if (Dungeon.challenges > 0) {
				IconButton btnChallenges = new IconButton( Icons.get(Icons.CHALLENGE_ON) ) {
					@Override
					protected void onClick() {
						Game.scene().add( new WndChallenges( Dungeon.challenges, false ) );
					}

					@Override
					protected String hoverText() {
						return Messages.get(StatsTab.this, "challenges");
					}
				};

				btnChallenges.icon();
				btnChallenges.setSize( 16, 16 );

				title.setSize(title.width() - btnChallenges.width(), title.height());

				btnChallenges.setPos(WIDTH - 18, 0);
				btnTalents.setPos(btnChallenges.left() - 18, btnChallenges.top());
				PixelScene.align(btnChallenges);
				add( btnChallenges );
			}

			pos += GAP;

			int strBonus = Dungeon.hero.STR() - Dungeon.hero.STR;
			if (strBonus > 0)       pos = statSlot(this, Messages.get(this, "str"), Dungeon.hero.STR + " + " + strBonus, pos);
			else if (strBonus < 0)  pos = statSlot(this, Messages.get(this, "str"), Dungeon.hero.STR + " - " + -strBonus, pos );
			else                    pos = statSlot(this, Messages.get(this, "str"), Integer.toString(Dungeon.hero.STR), pos);
			pos = statSlot(this, Messages.get(this, "hg"), (int) Dungeon.hero.hunger() + "/" + (int) Hunger.STARVING, pos);
			if (Dungeon.hero.shielding() > 0)   pos = statSlot( this, Messages.get(this, "health"), Dungeon.hero.HP + "+" +
					Dungeon.hero.shielding() + "/" + Dungeon.hero.HT, pos );
			else                        pos = statSlot( this, Messages.get(this, "health"), (Dungeon.hero.HP) + "/" + Dungeon.hero.HT, pos );
			
			pos += GAP;
			
			pos = statSlot( this, Messages.get(this, "duration"), Integer.toString( (int)Statistics.duration ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, Messages.get(this, "depth"), Integer.toString( Statistics.deepestFloor ), pos );
			pos = statSlot( this, Messages.get(this, "enemies"), Integer.toString( Statistics.enemiesSlain ), pos );
			pos = statSlot( this, Messages.get(this, "gold"), Integer.toString( Statistics.goldCollected ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, Messages.get(this, "food"), Integer.toString( Statistics.foodEaten ), pos );
			pos = statSlot( this, Messages.get(this, "alchemy"), Integer.toString( Statistics.potionsCooked ), pos );
			pos = statSlot( this, Messages.get(this, "ankhs"), Integer.toString( Statistics.ankhsUsed ), pos );
		}
		
		private float statSlot( Group parent, String label, String value, float pos ) {
			
			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
			txt.setPos(0, pos);
			parent.add( txt );
			
			txt = PixelScene.renderTextBlock( value, 7 );
			txt.setPos(WIDTH * 0.7f, pos);
			PixelScene.align(txt);
			parent.add( txt );
			
			return pos + GAP + txt.height();
		}
	}
	
	private class ItemsTab extends Group {

		private float pos;

		private ScrollPane list;
		private ArrayList<ItemButton> itemButtons = new ArrayList<>();
		private ArrayList<QuickSlotButton> quickSlotButtons = new ArrayList<>();
		
		public ItemsTab() {
			super();

			list = new ScrollPane(new Component());
			add(list);
		}

		private void updateList() {
			list.content().setSize(WIDTH, HEIGHT+21);
			list.setRect(0, 0, WIDTH, HEIGHT);

			Belongings stuff = Dungeon.hero.belongings;
			if (stuff.weapon != null) {
				addItem( stuff.weapon );
			}
			if (stuff.extra != null) {
				addItem( stuff.extra );
			}
			if (stuff.armor != null) {
				addItem( stuff.armor );
			}
			if (stuff.artifact != null) {
				addItem( stuff.artifact );
			}
			if (stuff.misc != null) {
				addItem( stuff.misc );
			}
			if (stuff.ring != null) {
				addItem( stuff.ring );
			}

			ColorBlock sep = new ColorBlock(1, 1, 0xFF000000);
			sep.size(WIDTH, 1);
			sep.y = 127;
			list.content().add(sep);

			pos = 0;
			float slotWidth = (WIDTH - 5) / 6f;
			for (int i = 0; i < 6; i++){
				if (Dungeon.quickslot.getItem(i) != null){
					QuickSlotButton slot = new QuickSlotButton(Dungeon.quickslot.getItem(i));

					slot.setRect( pos, 130, slotWidth, QuickSlotButton.HEIGHT );
					PixelScene.align(slot);

					list.content().add(slot);
					quickSlotButtons.add(slot);
					pos += slotWidth + 1;

				}
			}
			pos = 0;
			for (int i = 6; i < QuickSlot.SIZE; i ++) {
				if (Dungeon.quickslot.getItem(i) != null){
					QuickSlotButton slot = new QuickSlotButton(Dungeon.quickslot.getItem(i));

					slot.setRect( pos, 151, slotWidth, QuickSlotButton.HEIGHT );
					PixelScene.align(slot);

					list.content().add(slot);
					quickSlotButtons.add(slot);
					pos += slotWidth + 1;

				}
			}
		}

		private void addItem( Item item ) {
			ItemButton slot = new ItemButton( item );
			slot.setRect( 0, pos, width, ItemButton.HEIGHT );
			list.content().add( slot );
			itemButtons.add(slot);

			pos += slot.height() + 1;
		}
	}

	private class BadgesTab extends Group {

		public BadgesTab() {
			super();

			camera = WndRanking.this.camera;

			Component badges;
			if (Badges.unlocked(false) <= 7){
				badges = new BadgesList(false);
			} else {
				badges = new BadgesGrid(false);
			}
			add(badges);
			badges.setSize( WIDTH, HEIGHT );
		}
	}

	private class ItemButton extends Button {

		public static final int WIDTH   = 18;
		public static final int HEIGHT	= 20;
		
		private Item item;
		
		private ItemSlot slot;
		private ColorBlock bg;
		private RenderedTextBlock name;
		
		public ItemButton( Item item ) {
			
			super();

			this.item = item;
			
			slot.item( item );
			if (item.cursed && item.cursedKnown) {
				bg.ra = +0.2f;
				bg.ga = -0.1f;
			} else if (!item.isIdentified()) {
				bg.ra = 0.1f;
				bg.ba = 0.1f;
			}

			hotArea.blockLevel = PointerArea.NEVER_BLOCK;
		}
		
		@Override
		protected void createChildren() {
			
			bg = new ColorBlock( WIDTH, HEIGHT, 0x9953564D );
			add( bg );
			
			slot = new ItemSlot() {
				{
					hotArea.blockLevel = PointerArea.NEVER_BLOCK;
				}
			};
			add( slot );
			
			name = PixelScene.renderTextBlock( 7 );
			add( name );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			slot.setRect( x, y, WIDTH, HEIGHT );
			PixelScene.align(slot);
			
			name.maxWidth((int)(width - slot.width() - 2));
			name.text(Messages.titleCase(item.name()));
			name.setPos(
					slot.right()+2,
					y + (height - name.height()) / 2
			);
			PixelScene.align(name);
			
			super.layout();
		}
		
		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
		}
		
		protected void onPointerUp() {
			bg.brightness( 1.0f );
		}
		
		@Override
		protected void onClick() {
			Game.scene().add( new WndInfoItem( item ) );
		}
	}

	private class QuickSlotButton extends ItemSlot{

		public static final int WIDTH   = 18;
		public static final int HEIGHT	= 20;

		private Item item;
		private ColorBlock bg;

		QuickSlotButton(Item item){
			super(item);
			//showExtraInfo(false);
			this.item = item;
			hotArea.blockLevel = PointerArea.NEVER_BLOCK;
		}

		@Override
		protected void createChildren() {
			bg = new ColorBlock( WIDTH, HEIGHT, 0x9953564D );
			add( bg );

			super.createChildren();
		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;

			super.layout();
		}

		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
		}

		protected void onPointerUp() {
			bg.brightness( 1.0f );
		}

		@Override
		protected void onClick() {
			Game.scene().add(new WndInfoItem(item));
		}
	}
}
