package com.ansdoship.carbonizedpixeldungeon.windows;

import com.ansdoship.carbonizedpixeldungeon.Chrome;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.items.Generator;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.Artifact;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.CloakOfShadows;
import com.ansdoship.carbonizedpixeldungeon.items.bags.Bag;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.PixelScene;
import com.ansdoship.carbonizedpixeldungeon.ui.InventorySlot;
import com.ansdoship.carbonizedpixeldungeon.ui.QuickSlotButton;
import com.ansdoship.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.ansdoship.carbonizedpixeldungeon.ui.Window;
import com.ansdoship.pixeldungeonclasses.noosa.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WndQuickBag extends Window {

	private static Item bag;

	public WndQuickBag(Bag bag){
		super(0, 0, Chrome.get(Chrome.Type.TOAST_TR));

		if( WndBag.INSTANCE != null ){
			WndBag.INSTANCE.hide();
		}
		WndBag.INSTANCE = this;

		WndQuickBag.bag = bag;

		float width = 0, height = 0;
		int maxWidth = PixelScene.landscape() ? 240 : 135;
		int left = 0;
		int top = 10;

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

		int btnWidth = 16;
		int btnHeight = 20;

		//height of the toolbar and status pane, plus a little extra
		int targetHeight = PixelScene.uiCamera.height - 100;
		int rows = (int)Math.ceil(items.size() / (float)((maxWidth+1) / (btnWidth+1)));
		int expectedHeight = rows * btnHeight + (rows-1);
		while (expectedHeight > targetHeight && btnHeight > 16){
			btnHeight--;
			expectedHeight -= rows;
		}

		for (Item i : items){
			InventorySlot slot = new InventorySlot(i){
				@Override
				protected void onClick() {
					if (Dungeon.hero == null || !Dungeon.hero.isAlive() || !Dungeon.hero.belongings.contains(item)){
						Game.scene().addToFront(new WndUseItem(WndQuickBag.this, item));
						return;
					}

					hide();
					item.execute(Dungeon.hero);
					if (item.usesTargeting && bag != null){
						int idx = Dungeon.quickslot.getSlot(WndQuickBag.bag);
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

				@Override
				protected String hoverText() {
					return null; //no tooltips here
				}
			};
			slot.setRect(left, top, btnWidth, btnHeight);
			add(slot);

			if (width < slot.right()) width = slot.right();
			if (height < slot.bottom()) height = slot.bottom();

			left += btnWidth+1;

			if (left + btnWidth > maxWidth){
				left = 0;
				top += btnHeight+1;
			}
		}

		RenderedTextBlock txtTitle;
		txtTitle = PixelScene.renderTextBlock( Messages.titleCase(Messages.get(this, "title")), 8 );
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
		offset(0, (int) (bottom/2 - 30 - height/2));

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

}
