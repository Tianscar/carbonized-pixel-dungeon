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

package com.ansdoship.carbonizedpixeldungeon.items.spells;

import com.ansdoship.carbonizedpixeldungeon.Challenges;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.effects.Transmuting;
import com.ansdoship.carbonizedpixeldungeon.items.Generator;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.potions.AlchemicalCatalyst;
import com.ansdoship.carbonizedpixeldungeon.items.potions.Potion;
import com.ansdoship.carbonizedpixeldungeon.items.potions.brews.Brew;
import com.ansdoship.carbonizedpixeldungeon.items.potions.elixirs.Elixir;
import com.ansdoship.carbonizedpixeldungeon.items.potions.exotic.ExoticPotion;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.Scroll;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.ansdoship.carbonizedpixeldungeon.items.stones.Runestone;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.plants.Plant;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.utils.Reflection;

public class Recycle extends InventorySpell {

	{
		image = ItemSpriteSheet.RECYCLE;
	}

	@Override
	protected boolean usableOnItem(Item item) {
		return (item instanceof Potion && !(item instanceof Elixir || item instanceof Brew || item instanceof AlchemicalCatalyst)) ||
				item instanceof Scroll ||
				item instanceof Plant.Seed ||
				item instanceof Runestone ||
				item instanceof TippedDart;
	}

	@Override
	protected void onItemSelected(Item item) {
		Item result;
		do {
			if (item instanceof Potion) {
				result = Generator.random(Generator.Category.POTION);
				if (item instanceof ExoticPotion){
					result = Reflection.newInstance(ExoticPotion.regToExo.get(result.getClass()));
				}
			} else if (item instanceof Scroll) {
				result = Generator.random(Generator.Category.SCROLL);
				if (item instanceof ExoticScroll){
					result = Reflection.newInstance(ExoticScroll.regToExo.get(result.getClass()));
				}
			} else if (item instanceof Plant.Seed) {
				result = Generator.random(Generator.Category.SEED);
			} else if (item instanceof Runestone) {
				result = Generator.random(Generator.Category.STONE);
			} else {
				result = TippedDart.randomTipped(1);
			}
		} while (result.getClass() == item.getClass() || Challenges.isItemBlocked(result));

		item.detach(curUser.belongings.backpack);
		GLog.p(Messages.get(this, "recycled", result.name()));
		if (!result.collect()){
			Dungeon.level.drop(result, curUser.pos).sprite.drop();
		}
		Transmuting.show(curUser, item, result);
		curUser.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
	}

	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 40) / 12f));
	}
	
	public static class Recipe extends com.ansdoship.carbonizedpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfTransmutation.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};

			cost = 8;

			output = Recycle.class;
			outQuantity = 12;
		}

	}
}
