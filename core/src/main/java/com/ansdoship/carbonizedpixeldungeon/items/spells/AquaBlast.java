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

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Paralysis;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.effects.Splash;
import com.ansdoship.carbonizedpixeldungeon.items.potions.exotic.PotionOfStormClouds;
import com.ansdoship.carbonizedpixeldungeon.levels.traps.GeyserTrap;
import com.ansdoship.carbonizedpixeldungeon.mechanics.Ballistica;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.pixeldungeonclasses.utils.PathFinder;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class AquaBlast extends TargetedSpell {

	{
		image = ItemSpriteSheet.AQUA_BLAST;
		usesTargeting = true;
	}

	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {
		int cell = bolt.collisionPos;

		GeyserTrap geyser = new GeyserTrap();
		geyser.pos = cell;
		if (bolt.path.size() > bolt.dist+1) {
			geyser.centerKnockBackDirection = bolt.path.get(bolt.dist + 1);
		}
		geyser.activate();
	}

	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((60 + 40) / 8f));
	}

	public static class Recipe extends com.ansdoship.carbonizedpixeldungeon.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{PotionOfStormClouds.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};

			cost = 2;

			output = AquaBlast.class;
			outQuantity = 8;
		}

	}
}
