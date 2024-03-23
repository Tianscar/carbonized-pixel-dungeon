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

package com.tianscar.carbonizedpixeldungeon.levels.rooms.special;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mimic;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.StrangeMimic;
import com.tianscar.carbonizedpixeldungeon.items.Gold;
import com.tianscar.carbonizedpixeldungeon.items.keys.IronKey;
import com.tianscar.carbonizedpixeldungeon.items.quest.GoldenSeal;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.Terrain;
import com.tianscar.carbonizedpixeldungeon.levels.painters.Painter;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

public class ThiefsTreasureRoom extends SpecialRoom {

	@Override
	public int maxWidth() {
		return 7;
	}

	@Override
	public int maxHeight() {
		return 7;
	}

	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		level.mobs.add(Mimic.spawnAt(level.pointToCell(center()), new GoldenSeal(), StrangeMimic.class));

		for (int i=0; i < 6; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY);
			level.drop( new Gold( Random.IntRange( 5, 12 ) ), pos );
		}
		
		entrance().set( Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey( Dungeon.depth ) );
	}

}
