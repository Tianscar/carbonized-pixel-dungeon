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

package com.tianscar.carbonizedpixeldungeon.items.weapon.enchantments;

import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfWealth;
import com.tianscar.carbonizedpixeldungeon.items.weapon.Weapon;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite.Glowing;
import com.tianscar.pixeldungeonclasses.noosa.Visual;
import com.tianscar.pixeldungeonclasses.utils.Random;

public class Lucky extends Weapon.Enchantment {

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x00FF00 );
	
	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		int level = Math.max( 0, weapon.buffedLvl() );

		// lvl 0 - 10%
		// lvl 1 ~ 12%
		// lvl 2 ~ 14%
		float procChance = (level+4f)/(level+40f) * procChanceMultiplier(attacker);
		if (defender.HP <= damage && Random.Float() < procChance){
			Buff.affect(defender, LuckProc.class);
		}
		
		return damage;

	}
	
	public static Item genLoot(){
		//80% common, 20% uncommon, 0% rare
		return RingOfWealth.genConsumableDrop(-5);
	}

	public static void showFlare( Visual vis ){
		RingOfWealth.showFlareForBonusDrop(vis);
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}
	
	//used to keep track of whether a luck proc is incoming. see Mob.die()
	public static class LuckProc extends Buff {
		
		@Override
		public boolean act() {
			detach();
			return true;
		}
	}
	
}
