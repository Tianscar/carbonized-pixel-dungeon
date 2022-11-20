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

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Degrade;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Belongings;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.effects.Flare;
import com.ansdoship.carbonizedpixeldungeon.effects.particles.ShadowParticle;
import com.ansdoship.carbonizedpixeldungeon.items.EquipableItem;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.armor.Armor;
import com.ansdoship.carbonizedpixeldungeon.items.wands.Wand;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.Weapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfRemoveCurse extends InventoryScroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_REMCURSE;
		preferredBag = Belongings.Backpack.class;
	}

	@Override
	public void doRead() {
		if (curUser.subClass == HeroSubClass.LOREMASTER) {

			doRecord(new Callback() {
				@Override
				public void call() {

					new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;

					boolean procced = uncurse( curUser, curUser.belongings.getAllItems().toArray(new Item[0]) );

					Degrade.detach( curUser, Degrade.class );

					if (procced) {
						GLog.p( Messages.get(ScrollOfRemoveCurse.this, "cleansed_lore") );
					} else {
						GLog.i( Messages.get(ScrollOfRemoveCurse.this, "not_cleansed_lore") );
					}

				}
			});

		}
		else super.doRead();
	}

	@Override
	protected boolean usableOnItem(Item item) {
		return uncursable(item);
	}

	public static boolean uncursable( Item item ){
		if (item.isEquipped(Dungeon.hero) && Dungeon.hero.buff(Degrade.class) != null) {
			return true;
		} if ((item instanceof EquipableItem || item instanceof Wand) && ((!item.isIdentified() && !item.cursedKnown) || item.cursed)) {
			return true;
		} else if (item instanceof Weapon){
			return ((Weapon)item).hasCurseEnchant();
		} else if (item instanceof Armor){
			return ((Armor)item).hasCurseGlyph();
		} else {
			return false;
		}
	}

	@Override
	protected void onItemSelected(Item item) {
		new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;

		boolean procced = uncurse( curUser, item );

		Degrade.detach( curUser, Degrade.class );

		if (procced) {
			GLog.p( Messages.get(this, "cleansed") );
		} else {
			GLog.i( Messages.get(this, "not_cleansed") );
		}
	}

	public static boolean uncurse( Hero hero, Item... items ) {
		
		boolean procced = false;
		for (Item item : items) {
			if (item != null && uncursable(item)) {
				item.cursedKnown = true;
				if (item.cursed) {
					procced = true;
					item.cursed = false;
				}
				if (item instanceof Weapon) {
					Weapon w = (Weapon) item;
					if (w.hasCurseEnchant()) {
						w.enchant(null);
						procced = true;
					}
					if (w instanceof MagesStaff) {
						((MagesStaff) w).updateWand(curUser, false);
					}
				}
				else if (item instanceof Armor) {
					Armor a = (Armor) item;
					if (a.hasCurseGlyph()) {
						a.inscribe(null);
						procced = true;
					}
				}
				else if (item instanceof Wand) {
					((Wand) item).updateLevel();
				}
			}
		}
		
		if (procced && hero != null) {
			hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
			hero.updateHT( false ); //for ring of might
			updateQuickslot();
		}
		
		return procced;
	}
	
	@Override
	public int value() {
		return isKnown() ? 30 * quantity : super.value();
	}
}
