package com.tianscar.carbonizedpixeldungeon.windows;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Chrome;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.QuickSlot;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.LostInventory;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.gltextures.TextureCache;
import com.tianscar.carbonizedpixeldungeon.items.EquipableItem;
import com.tianscar.carbonizedpixeldungeon.items.Generator;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.Artifact;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.CloakOfShadows;
import com.tianscar.carbonizedpixeldungeon.items.bags.Bag;
import com.tianscar.carbonizedpixeldungeon.items.wands.Wand;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.ColorBlock;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.ui.ItemSlot;
import com.tianscar.carbonizedpixeldungeon.ui.QuickSlotButton;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.carbonizedpixeldungeon.ui.Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WndQuickBag extends Window {

	public WndQuickBag(List<Item> items) {
		super(0, 0, 0, Chrome.get(Chrome.Type.TOAST_TR));

		if( WndBag.INSTANCE != null ){
			WndBag.INSTANCE.hide();
		}
		WndBag.INSTANCE = this;

		float width = 0, height = 0;
		int maxWidth = PixelScene.landscape() ? 240 : 135;
		int left = 0;
		int top = 10;

		if (items == null || items.isEmpty()){
			hide();
			return;
		}

		int btnWidth = 18;
		int btnHeight = 20;

		//height of the toolbar and status pane, plus a little extra
		int targetHeight = PixelScene.uiCamera.height - 100;
		int rows = (int)Math.ceil(items.size() / (float)((maxWidth+1) / (btnWidth+1)));
		int expectedHeight = rows * btnHeight + (rows-1);
		while (expectedHeight > targetHeight && btnHeight > 16){
			btnHeight--;
			expectedHeight -= rows;
		}

		for (Item i : items) {
			QuickItemButton btn = new QuickItemButton(i) {
				@Override
				protected String hoverText() {
					return null;
				}
			};
			btn.setRect(left, top, btnWidth, btnHeight);
			add(btn);

			if (width < btn.right()) width = btn.right();
			if (height < btn.bottom()) height = btn.bottom();

			left += btnWidth+1;

			if (left + btnWidth > maxWidth){
				left = 0;
				top += btnHeight+1;
			}
		}

		RenderedTextBlock txtTitle = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "quickuse")), 8 );
		txtTitle.hardlight( TITLE_COLOR );
		if (txtTitle.width() > width) width = txtTitle.width();

		txtTitle.setPos(
				(width - txtTitle.width())/2f,
				(10 - txtTitle.height()) / 2f - 1);
		PixelScene.align(txtTitle);
		add( txtTitle );

		resize((int)width, (int)height);

		int bottom = GameScene.uiCamera.height;

		//offset to be above the toolbar
		offset((int) (bottom/2 - 30 - height/2));
	}

	public WndQuickBag(QuickSlot quickslot) {
		this(getQuickSlotItems(quickslot));
	}

	private static List<Item> getQuickSlotItems(QuickSlot quickslot) {
		ArrayList<Item> items = new ArrayList<>();
		Item item;
		for (int i = 0; i < QuickSlot.SIZE; i ++) {
			if ((item = quickslot.getItem(i)) != null) {
				items.add(item);
			}
		}
		return items;
	}

	public WndQuickBag(Bag bag) {
		this(getBagItems(bag));
	}

	public WndQuickBag() {
		this((Bag) null);
	}

	private static List<Item> getBagItems( Bag bag ) {
		ArrayList<Item> items = new ArrayList<>();

		for (Item i : bag == null ? Dungeon.hero.belongings : bag){
			if (i.defaultAction == null){
				continue;
			}
			if (i instanceof Bag) {
				continue;
			}
			if (i instanceof Artifact
					&& !i.isEquipped(Dungeon.hero)
					&& (!(i instanceof CloakOfShadows) || !Dungeon.hero.hasTalent(Talent.LIGHT_CLOAK))){
				continue;
			}
			items.add(i);
		}

		Collections.sort(items, quickBagComparator);

		return items;
	}

	public static final Comparator<Item> quickBagComparator = new Comparator<Item>() {
		@Override
		public int compare( Item lhs, Item rhs ) {
			if (lhs.isEquipped(Dungeon.hero) && !rhs.isEquipped(Dungeon.hero)){
				return -1;
			} else if (!lhs.isEquipped(Dungeon.hero) && rhs.isEquipped(Dungeon.hero)){
				return 1;
			} else {
				return Generator.Category.order(lhs) - Generator.Category.order(rhs);
			}
		}
	};

	@Override
	public void hide() {
		super.hide();
		if (WndBag.INSTANCE == this){
			WndBag.INSTANCE = null;
		}
	}

	private class QuickItemButton extends ItemSlot {

		private static final int NORMAL = 0x9953564D;
		private static final int EQUIPPED	= 0x9991938C;
		private Item item;
		private ColorBlock bg;

		public QuickItemButton(Item item) {

			super(item);
			showExtraInfo(false);

			this.item = item;

		}

		@Override
		protected void createChildren() {
			bg = new ColorBlock(1, 1, NORMAL);
			add(bg);

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
		public void item(Item item) {

			super.item(item);
			if (item != null) {

				bg.texture( TextureCache.createSolid( item.isEquipped( Dungeon.hero ) ? EQUIPPED : NORMAL ) );
				if (item.cursed && item.cursedKnown) {
					bg.ra = +0.3f;
					bg.ga = -0.15f;
				} else if (!item.isIdentified()) {
					if ((item instanceof EquipableItem || item instanceof Wand) && item.cursedKnown) {
						bg.ba = 0.3f;
					} else {
						bg.ra = 0.3f;
						bg.ba = 0.3f;
					}
				}

				if (Dungeon.hero.buff(LostInventory.class) != null
						&& !item.keptThoughLostInvent) {
					enable(false);
				}

			} else {
				bg.color(NORMAL);
			}
		}

		@Override
		protected void onPointerDown() {
			bg.brightness(1.5f);
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		protected void onPointerUp() {
			bg.brightness(1.0f);
		}

		@Override
		protected void onClick() {
			if (Dungeon.hero == null || !Dungeon.hero.isAlive() || !Dungeon.hero.belongings.contains(item)){
				Game.scene().addToFront(new WndUseItem(WndQuickBag.this, item));
				return;
			}

			hide();
			item.execute(Dungeon.hero);
			if (item.usesTargeting){
				int idx = Dungeon.quickslot.getSlot(item);
				if (idx != -1){
					QuickSlotButton.useTargeting(idx);
				}
			}
		}

		@Override
		protected boolean onLongClick() {
			Game.scene().addToFront(new WndUseItem(WndQuickBag.this, item));
			return true;
		}

	}

}
