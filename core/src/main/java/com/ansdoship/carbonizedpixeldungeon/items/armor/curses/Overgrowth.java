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

package com.ansdoship.carbonizedpixeldungeon.items.armor.curses;

import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.effects.CellEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.particles.LeafParticle;
import com.ansdoship.carbonizedpixeldungeon.items.Generator;
import com.ansdoship.carbonizedpixeldungeon.items.armor.Armor;
import com.ansdoship.carbonizedpixeldungeon.plants.Plant;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class Overgrowth extends Armor.Glyph {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		
		if ( Random.Int( 20 ) == 0) {

			Plant p = ((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED)).couch(defender.pos, null);
			
			//momentarily revoke warden benefits, otherwise this curse would be incredibly powerful
			if (defender instanceof Hero && ((Hero) defender).subClass == HeroSubClass.WARDEN){
				((Hero) defender).subClass = HeroSubClass.NONE;
				p.activate( defender );
				((Hero) defender).subClass = HeroSubClass.WARDEN;
			} else {
				p.activate( defender );
			}
			
			
			CellEmitter.get( defender.pos ).burst( LeafParticle.LEVEL_SPECIFIC, 10 );
			
		}
		
		return damage;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}
	
	@Override
	public boolean curse() {
		return true;
	}
}
