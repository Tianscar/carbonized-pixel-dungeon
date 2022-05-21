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

package com.ansdoship.carbonizedpixeldungeon.levels.traps;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Paralysis;
import com.ansdoship.carbonizedpixeldungeon.effects.CellEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.levels.RegularLevel;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.Room;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.utils.BArray;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.PathFinder;
import com.ansdoship.pixeldungeonclasses.utils.Point;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.util.ArrayList;

public class RockfallTrap extends Trap {

	{
		color = GREY;
		shape = DIAMOND;
		
		canBeHidden = false;
		avoidsHallways = true;
	}
	
	@Override
	public void activate() {
		
		ArrayList<Integer> rockCells = new ArrayList<>();
		
		//determines if the trap is actually in the world, or if it is being spawned for its effect
		boolean onGround = Dungeon.level.traps.get(pos) == this;
		Room r = null;
		if (Dungeon.level instanceof RegularLevel){
			r = ((RegularLevel) Dungeon.level).room(pos);
		}
		
		if (onGround && r != null){
			int cell;
			for (Point p : r.getPoints()){
				cell = Dungeon.level.pointToCell(p);
				if (!Dungeon.level.solid[cell]){
					rockCells.add(cell);
				}
			}
			
		//if we don't have a room, then just do 5x5
		} else {
			PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					rockCells.add(i);
				}
			}
		}
		
		boolean seen = false;
		for (int cell : rockCells){

			if (Dungeon.level.heroFOV[ cell ]){
				CellEmitter.get( cell - Dungeon.level.width() ).start(Speck.factory(Speck.ROCK), 0.07f, 10);
				seen = true;
			}

			Char ch = Actor.findChar( cell );

			if (ch != null && ch.isAlive()){
				int damage = Random.NormalIntRange(5+Dungeon.depth, 10+Dungeon.depth*2);
				damage -= ch.drRoll();
				ch.damage( Math.max(damage, 0) , this);

				Buff.prolong( ch, Paralysis.class, Paralysis.DURATION );

				if (!ch.isAlive() && ch == Dungeon.hero){
					Dungeon.fail( getClass() );
					GLog.n( Messages.get(this, "ondeath") );
				}
			}
		}
		
		if (seen){
			Camera.main.shake(3, 0.7f);
			Sample.INSTANCE.play(Assets.Sounds.ROCKS);
		}

	}
}
