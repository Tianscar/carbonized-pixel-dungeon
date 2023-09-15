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

package com.tianscar.carbonizedpixeldungeon.items.weapon.melee.ranged;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.darts.Dart;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

public class Crossbow extends RangedWeapon {
	
	{
		image = ItemSpriteSheet.CROSSBOW;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1f;
		
		tier = 4;

		twoHanded = true;

		missileType = Dart.class;

		loadSound = Assets.Sounds.UNLOCK;
		loadSoundPitch = 1.1f;
		unloadSound = Assets.Sounds.UNLOCK;
		unloadSoundPitch = 1.1f;
	}
	
	@Override
	public int max(int lvl) {
		return  4*(tier+1) +    //20 base, down from 25
				lvl*(tier);     //+4 per level, down from +5
	}

	@Override
	protected int initialCharges() {
		return 3;
	}

	@Override
	public void missileThrowSound() {
		Sample.INSTANCE.play(Assets.Sounds.ATK_CROSSBOW, 1, Random.Float(0.87f, 1.15f));
	}

}
