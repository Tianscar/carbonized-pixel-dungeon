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

package com.ansdoship.carbonizedpixeldungeon.items.potions.exotic;

import com.ansdoship.carbonizedpixeldungeon.actors.buffs.AdrenalineSurge;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.ansdoship.carbonizedpixeldungeon.items.potions.PotionOfPotential;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class PotionOfPotentialBurst extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_ARENSURGE;

		unique = true;
	}

	private static final int START_POINTS = 4;
	
	@Override
	public void apply(Hero hero) {
		identify();
		int[] STRpts = new int[] { 0 };
		int[] CONpts = new int[] { 0 };
		int[] DEXpts = new int[] { 0 };
		int[] INTpts = new int[] { 0 };
		int[] WISpts = new int[] { 0 };
		int[] CHApts = new int[] { 0 };
		List<int[]> ALLpts = new ArrayList<>(6);
		ALLpts.add(STRpts);
		ALLpts.add(CONpts);
		ALLpts.add(DEXpts);
		ALLpts.add(INTpts);
		ALLpts.add(WISpts);
		ALLpts.add(CHApts);
		int points = START_POINTS;
		if (hero.heroClass == HeroClass.MAGE) {
			INTpts[0] ++;
			ALLpts.remove(INTpts);
			points --;
		}
		for (int i = 0; i < points; i ++) {
			ALLpts.remove(Random.Int(ALLpts.size()))[0] ++;
		}
		PotionOfPotential.addPoints( hero, new int[] { STRpts[0], CONpts[0], DEXpts[0], INTpts[0], WISpts[0], CHApts[0] }, null, null );
		Buff.affect(hero, AdrenalineSurge.class).reset(2, 800f);
	}
	
}
