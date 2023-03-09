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

package com.tianscar.carbonizedpixeldungeon.levels.traps;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.Sheep;
import com.tianscar.carbonizedpixeldungeon.effects.CellEmitter;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.utils.BArray;
import com.tianscar.pixeldungeonclasses.noosa.audio.Sample;
import com.tianscar.pixeldungeonclasses.utils.PathFinder;
import com.tianscar.pixeldungeonclasses.utils.Random;

public class FlockTrap extends Trap {

	{
		color = WHITE;
		shape = WAVES;
	}


	@Override
	public void activate() {
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			Trap t;
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				if (Dungeon.level.insideMap(i)
						&& Actor.findChar(i) == null
						&& !(Dungeon.level.pit[i])) {
					Sheep sheep = new Sheep();
					sheep.lifespan = Random.NormalIntRange( 4, 8 );
					sheep.pos = i;
					GameScene.add(sheep);
					CellEmitter.get(i).burst(Speck.factory(Speck.WOOL), 4);
					//before the tile is pressed, directly trigger traps to avoid sfx spam
					if ((t = Dungeon.level.traps.get(i)) != null && t.active){
						if (t.disarmedByActivation) t.disarm();
						t.reveal();
						t.activate();
					}
					Dungeon.level.occupyCell(sheep);
					Sample.INSTANCE.play(Assets.Sounds.PUFF);
					Sample.INSTANCE.play(Assets.Sounds.SHEEP);
				}
			}
		}
	}

}
