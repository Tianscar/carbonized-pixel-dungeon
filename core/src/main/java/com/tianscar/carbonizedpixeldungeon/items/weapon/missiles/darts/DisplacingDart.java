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

package com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.darts;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.BArray;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplacingDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.DISPLACING_DART;
	}
	
	int distance = 8;
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {
		
		if (!defender.properties().contains(Char.Property.IMMOVABLE)){
			
			int startDist = Dungeon.level.distance(attacker.pos, defender.pos);
			
			HashMap<Integer, ArrayList<Integer>> positions = new HashMap<>();

			PathFinder.buildDistanceMap(defender.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));

			for (int pos = 0; pos < Dungeon.level.length(); pos++){
				if (Dungeon.level.heroFOV[pos]
						&& Dungeon.level.passable[pos]
						&& PathFinder.distance[pos] != Integer.MAX_VALUE
						&& (!Char.hasProp(defender, Char.Property.LARGE) || Dungeon.level.openSpace[pos])
						&& Actor.findChar(pos) == null){
					
					int dist = Dungeon.level.distance(attacker.pos, pos);
					if (dist > startDist){
						if (positions.get(dist) == null){
							positions.put(dist, new ArrayList<Integer>());
						}
						positions.get(dist).add(pos);
					}
					
				}
			}
			
			float[] probs = new float[distance+1];
			
			for (int i = 0; i <= distance; i++){
				if (positions.get(i) != null){
					probs[i] = i - startDist;
				}
			}
			
			int chosenDist = Random.chances(probs);
			
			if (chosenDist != -1){
				int pos = positions.get(chosenDist).get(Random.index(positions.get(chosenDist)));
				ScrollOfTeleportation.appear( defender, pos );
				Dungeon.level.occupyCell(defender );
			}
		
		}
		
		return super.proc(attacker, defender, damage);
	}
}
