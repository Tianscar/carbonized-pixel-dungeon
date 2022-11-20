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

package com.ansdoship.carbonizedpixeldungeon.items.scrolls;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Badges;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.effects.Identification;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndOptions;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfIdentify extends InventoryScroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_IDENTIFY;

		bones = true;
	}

	@Override
	public void doRead() {
		if (curUser.subClass == HeroSubClass.LOREMASTER) {
			GameScene.show(new WndOptions(new ItemSprite(this),
					Messages.titleCase(Messages.get(this, "name")),
					Messages.get(this, "prompt"),
					Messages.get(this, "one_item"),
					Messages.get(this, "two_rand")) {
				@Override
				protected void onSelect(int index) {
					doRecord(new Callback() {
						@Override
						public void call() {
							switch (index) {
								case 0:
									ScrollOfIdentify.this.superDoRead();
									break;
								case 1:
									curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );

									Sample.INSTANCE.play( Assets.Sounds.READ );

									ScrollOfDivination.identifyItems(ScrollOfIdentify.this, 2);

									readAnimation();

									break;
							}
						}
					});
				}
				@Override
				public void onBackPressed() {
					if (!anonymous) curItem.collect();
					super.onBackPressed();
				}
			});
		}
		else super.doRead();
	}

	private void superDoRead() {
		super.doRead();
	}

	@Override
	protected boolean usableOnItem(Item item) {
		return !item.isIdentified();
	}

	@Override
	protected void onItemSelected( Item item ) {
		
		curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );
		
		item.identify();
		GLog.i( Messages.get(this, "it_is", item) );
		
		Badges.validateItemLevelAquired( item );
	}
	
	@Override
	public int value() {
		return isKnown() ? 30 * quantity : super.value();
	}
}
