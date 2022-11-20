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
		int[] points = new int[] { 0, 0, 0, 0, 0, 0, START_POINTS };
		randomAddPoints( hero, points, new boolean[] { false, false, false, false, false, false } );
		PotionOfPotential.addPoints( hero, points, null, null );
		Buff.affect(hero, AdrenalineSurge.class).reset(2, 800f);
	}

	public static void randomAddPoints( Hero hero, int[] points, boolean[] block ) {
		int[] STRpts = new int[] { 0 };
		int[] CONpts = new int[] { 0 };
		int[] DEXpts = new int[] { 0 };
		int[] INTpts = new int[] { 0 };
		int[] WISpts = new int[] { 0 };
		int[] CHApts = new int[] { 0 };
		List<int[]> ALLpts = new ArrayList<>(6);
		if (!block[0]) ALLpts.add(STRpts);
		if (!block[1]) ALLpts.add(CONpts);
		if (!block[2]) ALLpts.add(DEXpts);
		if (!block[3]) ALLpts.add(INTpts);
		if (!block[4]) ALLpts.add(WISpts);
		if (!block[5]) ALLpts.add(CHApts);
		int npoints = points[6];
		if (!block[3] && hero.heroClass == HeroClass.MAGE) {
			INTpts[0] ++;
			ALLpts.remove(INTpts);
			npoints --;
		}
		for (int i = 0; i < npoints; i ++) {
			ALLpts.remove(Random.Int(ALLpts.size()))[0] ++;
		}
		points[0] += STRpts[0];
		points[1] += CONpts[0];
		points[2] += DEXpts[0];
		points[3] += INTpts[0];
		points[4] += WISpts[0];
		points[5] += CHApts[0];
	}
	
}
