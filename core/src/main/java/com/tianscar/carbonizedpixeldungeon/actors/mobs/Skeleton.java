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

package com.tianscar.carbonizedpixeldungeon.actors.mobs;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.items.Generator;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.levels.features.Chasm;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.sprites.SkeletonSprite;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

public class Skeleton extends Mob {
	
	{
		spriteClass = SkeletonSprite.class;
		
		HP = HT = 25;
		defenseSkill = 9;
		
		EXP = 5;
		maxLvl = 10;

		loot = Generator.Category.WEAPON;
		lootChance = 0.1667f; //by default, see rollToDropLoot()

		properties.add(Property.UNDEAD);
		properties.add(Property.INORGANIC);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 10 );
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );
		
		if (cause == Chasm.class) return;
		
		boolean heroKilled = false;
		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char ch = findChar( pos + PathFinder.NEIGHBOURS8[i] );
			if (ch != null && ch.isAlive()) {
				int damage = Random.NormalIntRange(6, 12);
				damage = Math.max( 0,  damage - (ch.drRoll() + ch.drRoll()) );
				ch.damage( damage, this );
				if (ch == Dungeon.hero && !ch.isAlive()) {
					heroKilled = true;
				}
			}
		}
		
		if (Dungeon.level.heroFOV[pos]) {
			Sample.INSTANCE.play( Assets.Sounds.BONES );
		}
		
		if (heroKilled) {
			Dungeon.fail( getClass() );
			GLog.n( Messages.get(this, "explo_kill") );
		}
	}

	@Override
	public void rollToDropLoot() {
		//each drop makes future drops 1/2 as likely
		// so loot chance looks like: 1/6, 1/12, 1/24, 1/48, etc.
		lootChance *= Math.pow(1/2f, Dungeon.LimitedDrops.SKELE_WEP.count);
		super.rollToDropLoot();
	}

	@Override
	protected Item createLoot() {
		Dungeon.LimitedDrops.SKELE_WEP.count++;
		return super.createLoot();
	}

	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

}
