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

package com.tianscar.carbonizedpixeldungeon.items.spells;

import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.Scroll;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRage;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTerror;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.tianscar.carbonizedpixeldungeon.items.stones.Runestone;
import com.tianscar.carbonizedpixeldungeon.plants.Plant;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

public class ArcaneCatalyst extends Spell {
	
	{
		image = ItemSpriteSheet.SCROLL_CATALYST;
	}
	
	private static HashMap<Class<? extends Scroll>, Float> scrollChances = new HashMap<>();
	static{
		scrollChances.put( ScrollOfIdentify.class,      3f );
		scrollChances.put( ScrollOfRemoveCurse.class,   2f );
		scrollChances.put( ScrollOfMagicMapping.class,  2f );
		scrollChances.put( ScrollOfMirrorImage.class,   2f );
		scrollChances.put( ScrollOfRecharging.class,    2f );
		scrollChances.put( ScrollOfLullaby.class,       2f );
		scrollChances.put( ScrollOfRetribution.class,   2f );
		scrollChances.put( ScrollOfRage.class,          2f );
		scrollChances.put( ScrollOfTeleportation.class, 2f );
		scrollChances.put( ScrollOfTerror.class,        2f );
		scrollChances.put( ScrollOfTransmutation.class, 1f );
	}
	
	@Override
	protected void onCast(Hero hero) {
		
		detach( curUser.belongings.backpack );
		updateQuickslot();
		
		Scroll s = Reflection.newInstance(Random.chances(scrollChances));
		s.anonymize();
		curItem = s;
		s.doRead();
	}
	
	@Override
	public int value() {
		return 40 * quantity;
	}
	
	public static class Recipe extends com.tianscar.carbonizedpixeldungeon.items.Recipe {
		
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			boolean scroll = false;
			boolean secondary = false;
			
			for (Item i : ingredients){
				if (i instanceof Plant.Seed || i instanceof Runestone){
					secondary = true;
					//if it is a regular or exotic potion
				} else if (ExoticScroll.regToExo.containsKey(i.getClass())
						|| ExoticScroll.regToExo.containsValue(i.getClass())) {
					scroll = true;
				}
			}
			
			return scroll && secondary;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			for (Item i : ingredients){
				if (i instanceof Plant.Seed){
					return 2;
				} else if (i instanceof Runestone){
					return 1;
				}
			}
			return 1;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			
			for (Item i : ingredients){
				i.quantity(i.quantity()-1);
			}
			
			return sampleOutput(null);
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new ArcaneCatalyst();
		}
	}
}
