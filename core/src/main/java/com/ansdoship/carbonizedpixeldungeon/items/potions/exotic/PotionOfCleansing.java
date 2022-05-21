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

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Corruption;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Hunger;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.LostInventory;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;

public class PotionOfCleansing extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_CLEANSE;
	}
	
	@Override
	public void apply( Hero hero ) {
		identify();
		
		cleanse( hero );
	}
	
	@Override
	public void shatter(int cell) {
		if (Actor.findChar(cell) == null){
			super.shatter(cell);
		} else {
			if (Dungeon.level.heroFOV[cell]) {
				Sample.INSTANCE.play(Assets.Sounds.SHATTER);
				splash(cell);
				identify();
			}
			
			if (Actor.findChar(cell) != null){
				cleanse(Actor.findChar(cell));
			}
		}
	}
	
	public static void cleanse(Char ch){
		for (Buff b : ch.buffs()){
			if (b.type == Buff.buffType.NEGATIVE
					&& !(b instanceof Corruption)
					&& !(b instanceof LostInventory)){
				b.detach();
			}
			if (b instanceof Hunger){
				((Hunger) b).satisfy(Hunger.STARVING);
			}
		}
	}
}
