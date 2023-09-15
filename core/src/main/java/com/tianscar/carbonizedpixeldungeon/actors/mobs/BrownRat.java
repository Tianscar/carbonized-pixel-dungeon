/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * Overgrown Pixel Dungeon
 * Copyright (C) 2018-2019 Anon
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

package com.tianscar.carbonizedpixeldungeon.actors.mobs;

import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Bleeding;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.items.food.MysteryMeat;
import com.tianscar.carbonizedpixeldungeon.sprites.BrownRatSprite;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

public class BrownRat extends Rat {

	{
		spriteClass = BrownRatSprite.class;
		
		HP = HT = 15;
		EXP = 2;

		loot = new MysteryMeat();
		lootChance = 0.083f;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 3 );
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (Random.Int( 2 ) == 0) {
			Buff.affect( enemy, Bleeding.class ).set( damage );
		}

		return damage;
	}
}
