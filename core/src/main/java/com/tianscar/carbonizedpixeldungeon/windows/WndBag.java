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

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.PDAction;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.LostInventory;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Belongings;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.items.EquipableItem;
import com.tianscar.carbonizedpixeldungeon.items.Gold;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.bags.Bag;
import com.tianscar.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.tianscar.carbonizedpixeldungeon.items.bags.PotionBandolier;
import com.tianscar.carbonizedpixeldungeon.items.bags.ScrollHolder;
import com.tianscar.carbonizedpixeldungeon.items.bags.VelvetPouch;
import com.tianscar.carbonizedpixeldungeon.items.wands.Wand;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.ItemSlot;
import com.tianscar.carbonizedpixeldungeon.ui.QuickSlotButton;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.gltextures.TextureCache;
import com.tianscar.carbonizedpixeldungeon.input.GameAction;
import com.tianscar.carbonizedpixeldungeon.input.KeyBindings;
import com.tianscar.carbonizedpixeldungeon.input.KeyEvent;
import com.tianscar.carbonizedpixeldungeon.noosa.BitmapText;
import com.tianscar.carbonizedpixeldungeon.noosa.ColorBlock;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;

public class WndBag extends WndTabbed {
	
	//only one bag window can appear at a time
	public static Window INSTANCE;

	protected static final int COLS_P   = 6;
	protected static final int COLS_L   = 6;
	
	protected static int SLOT_WIDTH_P   = 18;
	protected static int SLOT_WIDTH_L   = 18;

	protected static int SLOT_HEIGHT_P	= 20;
	protected static int SLOT_HEIGHT_L	= 20;

	protected static final int SLOT_MARGIN	= 1;
	
	protected static final int TITLE_HEIGHT	= 14;
	
	private ItemSelector selector;

	private int nCols;
	private int nRows;

	private int slotWidth;
	private int slotHeight;

	protected int count;
	protected int col;
	protected int row;
	
	private static Bag lastBag;

	public WndBag( Bag bag ) {
		this(bag, null);
	}

	public WndBag( Bag bag, ItemSelector selector ) {
		
		super();
		
		if( INSTANCE != null ){
			INSTANCE.hide();
		}
		INSTANCE = this;
		
		this.selector = selector;
		
		lastBag = bag;

		slotWidth = PixelScene.landscape() ? SLOT_WIDTH_L : SLOT_WIDTH_P;
		slotHeight = PixelScene.landscape() ? SLOT_HEIGHT_L : SLOT_HEIGHT_P;

		nCols = PixelScene.landscape() ? COLS_L : COLS_P;
		nRows = (int)Math.ceil(36/(float)nCols); //we expect to lay out 36 slots in all cases

		int windowWidth = slotWidth * nCols + SLOT_MARGIN * (nCols - 1);
		int windowHeight = TITLE_HEIGHT + slotHeight * nRows + SLOT_MARGIN * (nRows - 1);

		placeTitle( bag, windowWidth );
		
		placeItems( bag );

		resize( windowWidth, windowHeight );

		int i = 1;
		for (Bag b : Dungeon.hero.belongings.getBags()) {
			if (b != null) {
				BagTab tab = new BagTab( b, i++ );
				add( tab );
				tab.select( b == bag );
			}
		}

		layoutTabs();
	}
	
	public static WndBag lastBag( ItemSelector selector ) {
		
		if (lastBag != null && Dungeon.hero.belongings.backpack.contains( lastBag )) {
			
			return new WndBag( lastBag, selector );
			
		} else {
			
			return new WndBag( Dungeon.hero.belongings.backpack, selector );
			
		}
	}

	public static WndBag getBag( ItemSelector selector ) {
		if (selector.preferredBag() == Belongings.Backpack.class){
			return new WndBag( Dungeon.hero.belongings.backpack, selector );

		} else if (selector.preferredBag() != null){
			Bag bag = Dungeon.hero.belongings.getItem( selector.preferredBag() );
			if (bag != null) return new WndBag( bag, selector );
		}

		return lastBag( selector );
	}
	
	protected void placeTitle( Bag bag, int width ){
		
		ItemSprite gold = new ItemSprite(ItemSpriteSheet.GOLD, null);
		gold.x = width - gold.width() - 1;
		gold.y = (TITLE_HEIGHT - gold.height())/2f - 1;
		PixelScene.align(gold);
		add(gold);
		
		BitmapText amt = new BitmapText( Integer.toString(Dungeon.gold), PixelScene.pixelFont );
		amt.hardlight(TITLE_COLOR);
		amt.measure();
		amt.x = width - gold.width() - amt.width() - 2;
		amt.y = (TITLE_HEIGHT - amt.baseLine())/2f - 1;
		PixelScene.align(amt);
		add(amt);

		String title = selector != null ? selector.textPrompt() : null;
		RenderedTextBlock txtTitle = PixelScene.renderTextBlock(
				title != null ? Messages.titleCase(title) : Messages.titleCase( bag.name() ), 8 );
		txtTitle.hardlight( TITLE_COLOR );
		txtTitle.maxWidth( (int)amt.x - 2 );
		txtTitle.setPos(
				1,
				(TITLE_HEIGHT - txtTitle.height()) / 2f - 1
		);
		PixelScene.align(txtTitle);
		add( txtTitle );
	}
	
	protected void placeItems( Bag container ) {
		
		// Equipped items
		Belongings stuff = Dungeon.hero.belongings;
		placeItem( stuff.weapon != null ? stuff.weapon : new Placeholder( ItemSpriteSheet.WEAPON_HOLDER ) );
		if (!(stuff.weapon instanceof MeleeWeapon) || !((MeleeWeapon) stuff.weapon).twoHanded) {
			placeItem( stuff.extra != null ? stuff.extra : new Placeholder( ItemSpriteSheet.WEAPON_HOLDER ) );
		}
		placeItem( stuff.armor != null ? stuff.armor : new Placeholder( ItemSpriteSheet.ARMOR_HOLDER ) );
		placeItem( stuff.artifact != null ? stuff.artifact : new Placeholder( ItemSpriteSheet.ARTIFACT_HOLDER ) );
		placeItem( stuff.misc != null ? stuff.misc : new Placeholder( ItemSpriteSheet.SOMETHING ) );
		placeItem( stuff.ring != null ? stuff.ring : new Placeholder( ItemSpriteSheet.RING_HOLDER ) );
		if ( stuff.weapon instanceof MeleeWeapon && ((MeleeWeapon) stuff.weapon).twoHanded ) {
			if (container == Dungeon.hero.belongings.backpack) placeItem(container);
		}

		//the container itself if it's not the root backpack
		if (container != Dungeon.hero.belongings.backpack){
			placeItem(container);
			count--; //don't count this one, as it's not actually inside of itself
		}

		// Items in the bag, except other containers (they have tags at the bottom)
		for (Item item : container.items.toArray(new Item[0])) {
			if (!(item instanceof Bag)) {
				placeItem( item );
			} else {
				count++;
			}
		}
		
		// Free Space
		while ((count - 6) < container.capacity()) {
			placeItem( null );
		}
	}

	protected void placeItem( final Item item ) {

		count++;

		int x = col * (slotWidth + SLOT_MARGIN);
		int y = TITLE_HEIGHT + row * (slotHeight + SLOT_MARGIN);

		add( new ItemButton( item ).setPos( x, y ) );

		if (++col >= nCols) {
			col = 0;
			row++;
		}

	}

	@Override
	public boolean onSignal(KeyEvent event) {
		if (event.pressed && KeyBindings.getActionForKey( event ) == PDAction.INVENTORY) {
			hide();
			return true;
		} else {
			return super.onSignal(event);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (selector != null) {
			selector.onSelect( null );
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onClick( Tab tab ) {
		hide();
		Game.scene().addToFront(new WndBag(((BagTab) tab).bag, selector));
	}
	
	@Override
	public void hide() {
		super.hide();
		if (INSTANCE == this){
			INSTANCE = null;
		}
	}
	
	@Override
	protected int tabHeight() {
		return 20;
	}
	
	private Image icon( Bag bag ) {
		if (bag instanceof VelvetPouch) {
			return Icons.get( Icons.SEED_POUCH );
		} else if (bag instanceof ScrollHolder) {
			return Icons.get( Icons.SCROLL_HOLDER );
		} else if (bag instanceof MagicalHolster) {
			return Icons.get( Icons.WAND_HOLSTER );
		} else if (bag instanceof PotionBandolier) {
			return Icons.get( Icons.POTION_BANDOLIER );
		} else {
			return Icons.get( Icons.BACKPACK );
		}
	}

	private class BagTab extends IconTab {

		private Bag bag;
		private int index;

		public BagTab( Bag bag, int index ) {
			super( icon(bag) );

			this.bag = bag;
			this.index = index;
		}

		@Override
		public GameAction keyAction() {
			switch (index){
				case 1: default:
					return PDAction.BAG_1;
				case 2:
					return PDAction.BAG_2;
				case 3:
					return PDAction.BAG_3;
				case 4:
					return PDAction.BAG_4;
				case 5:
					return PDAction.BAG_5;
			}
		}

		@Override
		protected String hoverText() {
			return Messages.titleCase(bag.name());
		}
		
	}
	
	public static class Placeholder extends Item {

		public Placeholder(int image ) {
			this.image = image;
		}

		@Override
		public String name() {
			return null;
		}

		@Override
		public boolean isIdentified() {
			return true;
		}
		
		@Override
		public boolean isEquipped( Hero hero ) {
			return true;
		}
	}
	
	private class ItemButton extends ItemSlot {
		
		private static final int NORMAL		= 0x9953564D;
		private static final int EQUIPPED	= 0x9991938C;
		
		private Item item;
		private ColorBlock bg;
		
		public ItemButton( Item item ) {
			
			super( item );

			this.item = item;
			if (item instanceof Gold || item instanceof Bag) {
				bg.visible = false;
			}
			
			width = slotWidth;
			height = slotHeight;
		}
		
		@Override
		protected void createChildren() {
			bg = new ColorBlock( 1, 1, NORMAL );
			add( bg );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.size(width, height);
			bg.x = x;
			bg.y = y;
			
			super.layout();
		}
		
		@Override
		public void item( Item item ) {
			
			super.item( item );
			if (item != null) {

				bg.texture( TextureCache.createSolid( item.isEquipped( Dungeon.hero ) ? EQUIPPED : NORMAL ) );
				if (item.cursed && item.cursedKnown) {
					bg.ra = +0.3f;
					bg.ga = -0.15f;
				} else if (!item.isIdentified()) {
					if ((item instanceof EquipableItem || item instanceof Wand) && item.cursedKnown){
						bg.ba = 0.3f;
					} else {
						bg.ra = 0.3f;
						bg.ba = 0.3f;
					}
				}
				
				if (item.name() == null) {
					enable( false );
				} else if (selector != null && !selector.itemSelectable(item)) {
					enable(false);
				} else if (Dungeon.hero.buff(LostInventory.class) != null
						&& !item.keptThoughLostInvent){
					enable(false);
				}
			} else {
				bg.color( NORMAL );
			}
		}
		
		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}
		
		protected void onPointerUp() {
			bg.brightness( 1.0f );
		}
		
		@Override
		protected void onClick() {
			if (lastBag != item && !lastBag.contains(item) && !item.isEquipped(Dungeon.hero)){

				hide();

			} else if (selector != null) {
				
				hide();
				selector.onSelect( item );
				
			} else {
				
				Game.scene().addToFront(new WndUseItem( WndBag.this, item ) );
				
			}
		}
		
		@Override
		protected boolean onLongClick() {
			if (selector == null && item.defaultAction != null) {
				hide();
				Dungeon.quickslot.setSlot( 0 , item );
				QuickSlotButton.refresh();
				return true;
			} else if (selector != null) {
				Game.scene().addToFront(new WndInfoItem(item));
				return true;
			} else {
				return false;
			}
		}
	}

	public abstract static class ItemSelector {
		public abstract String textPrompt();
		public Class<?extends Bag> preferredBag(){
			return null; //defaults to last bag opened
		}
		public abstract boolean itemSelectable( Item item );
		public abstract void onSelect( Item item );
	}
}
