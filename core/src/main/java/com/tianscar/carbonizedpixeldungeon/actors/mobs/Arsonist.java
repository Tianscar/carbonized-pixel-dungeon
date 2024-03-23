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

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Blob;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Fire;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Blindness;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Burning;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Cripple;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ArsonistSprite;

public class Arsonist extends Thief {
	
	public Item item;
	
	{
		spriteClass = ArsonistSprite.class;

		//guaranteed first drop, then 1/3, 1/9, etc.
		lootChance = 1f;
	}
	
	@Override
	protected boolean steal( Hero hero ) {
		if (super.steal( hero )) {
			
			Buff.prolong( hero, Blindness.class, Blindness.DURATION/2f );
			if (Dungeon.level.flamable[hero.pos])
				GameScene.add(Blob.seed(hero.pos, 4, Fire.class));
			Buff.affect( hero, Burning.class ).reignite( hero );
			Buff.prolong( hero, Cripple.class, Cripple.DURATION/2f );
			Dungeon.observe();
			
			return true;
		} else {
			return false;
		}
	}
	
}
