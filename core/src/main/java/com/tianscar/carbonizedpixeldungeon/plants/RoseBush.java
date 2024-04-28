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

package com.tianscar.carbonizedpixeldungeon.plants;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.items.Heap;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.DriedRose;

public class RoseBush extends Plant {

	{
		image = 13;
	}

	@Override
	public void activate( Char ch ) {

		Item itemToDrop;
		if (!Dungeon.LimitedDrops.ROSE.dropped()) {
			itemToDrop = new DriedRose();
			itemToDrop.identify();
		}
		else itemToDrop = new DriedRose.Petal();

		Heap heap = Dungeon.level.drop( itemToDrop, pos );
		heap.type = Heap.Type.HEAP;
		heap.sprite.drop();
		if (!Dungeon.LimitedDrops.ROSE.dropped()) {
			Dungeon.LimitedDrops.ROSE.drop();
		}
	}

	//seed is never dropped
	public static class Seed extends Plant.Seed {
		{
			plantClass = RoseBush.class;
		}

	}
}
