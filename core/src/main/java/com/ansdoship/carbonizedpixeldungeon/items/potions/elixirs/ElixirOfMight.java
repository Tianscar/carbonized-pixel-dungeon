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

package com.ansdoship.carbonizedpixeldungeon.items.potions.elixirs;

import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.items.potions.AlchemicalCatalyst;
import com.ansdoship.carbonizedpixeldungeon.items.potions.PotionOfPotential;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;

public class ElixirOfMight extends Elixir {

	{
		image = ItemSpriteSheet.ELIXIR_MIGHT;

		unique = true;
	}

	private static final int START_POINTS = 3;
	private static final int HTBOOST = 10;

	@Override
	protected void drink(Hero hero) {
		PotionOfPotential.drink(hero, new ItemSprite(this), START_POINTS, new String[] { Messages.get(this, "msg_hp", HTBOOST) },
				new String[]{ Messages.get(this, "msg_hp_2") }, new Runnable() {
			@Override
			public void run() {
				/*
				Buff.affect(hero, HTBoost.class).reset();
				HTBoost boost = Buff.affect(hero, HTBoost.class);
				boost.reset();
				 */
				hero.HTBoost += HTBOOST;
				hero.updateHT( true );
				ElixirOfMight.super.drink(hero);
			}
		});
	}

	@Override
	public void apply( Hero hero ) {
		identify();
	}

	public String desc() {
		return Messages.get(this, "desc", HTBOOST);
	}

	@Override
	public int value() {
		//prices of ingredients
		return quantity * (50 + 40);
	}

	public static class Recipe extends com.ansdoship.carbonizedpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{PotionOfPotential.class, AlchemicalCatalyst.class};
			inQuantity = new int[]{1, 1};

			cost = 6;

			output = ElixirOfMight.class;
			outQuantity = 1;
		}

	}

	public static class HTBoost extends Buff {

		{
			type = buffType.POSITIVE;
		}

		private int left;

		public void reset(){
			left = 5;
		}

		public int boost(){
			return Math.round(left*boost(target.HT)/5f);
		}

		public static int boost(int HT){
			return Math.round(4 + HT/20f);
		}

		public void onLevelUp(){
			left --;
			if (left <= 0){
				detach();
			}
		}

		@Override
		public int icon() {
			return BuffIndicator.HEALING;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1f, 0.5f, 0f);
		}

		@Override
		public float iconFadePercent() {
			return (5f - left) / 5f;
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(left);
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", boost(), left);
		}

		private static final String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( LEFT, left );
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			left = bundle.getInt(LEFT);
		}
	}

}
