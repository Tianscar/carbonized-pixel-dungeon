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

package com.tianscar.carbonizedpixeldungeon.levels.rooms.secret;

import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.RatKing;
import com.tianscar.carbonizedpixeldungeon.items.Gold;
import com.tianscar.carbonizedpixeldungeon.items.Heap;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.food.Cheese;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.Terrain;
import com.tianscar.carbonizedpixeldungeon.levels.painters.Painter;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.Room;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.sewerboss.SewerBossEntranceRoom;
import com.tianscar.carbonizedpixeldungeon.utils.GrowableIntArray;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

public class RatKingRoom extends SecretRoom {
	
	@Override
	public boolean canConnect(Room r) {
		//never connects at the entrance
		return !(r instanceof SewerBossEntranceRoom) && super.canConnect(r);
	}
	
	//reduced max size to limit chest numbers.
	// normally would gen with 8-28, this limits it to 8-16
	@Override
	public int maxHeight() { return 7; }
	public int maxWidth() { return 7; }
	
	public void paint( Level level ) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );
		
		Door entrance = entrance();
		entrance.set( Door.Type.HIDDEN );
		int door = entrance.x + entrance.y * level.width();

		GrowableIntArray keys = new GrowableIntArray();

		for (int i=left + 1; i < right; i++) {
			testPlaceChest( level, (top + 1) * level.width() + i, door, keys );
			testPlaceChest( level, (bottom - 1) * level.width() + i, door, keys );
		}
		
		for (int i=top + 2; i < bottom - 1; i++) {
			testPlaceChest( level, i * level.width() + left + 1, door, keys );
			testPlaceChest( level, i * level.width() + right - 1, door, keys );
		}

		Item[] values = new Item[keys.size];
		values[0] = new Cheese();
		for (int i = 1; i < values.length; i ++) {
			values[i] = new Gold( Random.IntRange( 10, 25 ) );
		}

		Random.shuffle( values );

		for (int i = 0; i < keys.size; i ++) {
			level.drop( values[i], keys.get(i) ).type = Heap.Type.CHEST;
		}

		RatKing king = new RatKing();
		king.pos = level.pointToCell(random( 2 ));
		level.mobs.add( king );
	}
	
	private static void testPlaceChest(Level level, int pos, int door, GrowableIntArray keys ) {
		
		if (pos == door - 1 ||
			pos == door + 1 ||
			pos == door - level.width() ||
			pos == door + level.width()) {
			return;
		}

		keys.add( pos );

	}

}
