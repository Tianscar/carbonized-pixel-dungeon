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

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Foliage;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Bee;
import com.tianscar.carbonizedpixeldungeon.items.Honeypot;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.Terrain;
import com.tianscar.carbonizedpixeldungeon.levels.painters.Painter;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Tilemap;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.tiles.FlamableCustomTilemap;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.Point;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

public class BerryGardenRoom extends SpecialRoom {

	public boolean h = false;

	@Override
	public int maxWidth() {
		return h ? super.maxWidth() : 7;
	}

	@Override
	public int maxHeight() {
		return h ? 7 : super.maxHeight();
	}

	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.HIGH_GRASS );
		Painter.fill( level, this, 2, Terrain.GRASS );

		Door entrance = entrance();
		entrance.set( Door.Type.REGULAR );

		Point pos;
		if (entrance.x == left) {
			pos = new Point( right-1, Random.Int(2) == 0 ? top+1 : bottom-1 );
		} else if (entrance.x == right) {
			pos = new Point( left+1, Random.Int(2) == 0 ? top+1 : bottom-1 );
		} else if (entrance.y == top) {
			pos = new Point( Random.Int(2) == 0 ? left+1 : right-1, bottom-1 );
		} else if (entrance.y == bottom) {
			pos = new Point(Random.Int(2) == 0 ? left + 1 : right - 1, top + 1);
		} else pos = null;

		if (pos != null) {
			Painter.set(level, pos, Terrain.FLAMABLE_SIGN);
			Hive vis = new Hive();
			vis.pos( pos.x, pos.y );
			level.customTiles.add(vis);
		}

		Point c = center();

		if (width() >= 8 || height() >= 8) {
			Painter.set( level, c.offset(h ? -1 : 0, h ? 0 : -1), Terrain.FLAMABLE_SIGN );
			Painter.set( level, c.offset(h ? 3 : 0, h ? 0 : 3), Terrain.FLAMABLE_SIGN );

			int bushes = Random.Int(5);
			boolean first = Random.Int(2) == 0;

			Bush vis = new Bush();
			vis.pos(c.x, c.y);
			if (first && bushes == 0) vis.age = 1;
			level.customTiles.add(vis);
			vis = new Bush();
			vis.pos(c.x - (h ? 3 : 0), c.y - (h ? 0 : 3));
			if (!first && bushes == 0) vis.age = 1;
			level.customTiles.add(vis);
		}
		else {
			Painter.set( level, c, Terrain.FLAMABLE_SIGN );

			Bush vis = new Bush();
			vis.pos( c.x, c.y );
			level.customTiles.add(vis);
		}

		if (Random.Int(5) == 0) {
			Honeypot.ShatteredPot pot = new Honeypot.ShatteredPot();
			placeItem(pot, level);
		}

		Foliage light = (Foliage) level.blobs.get( Foliage.class );
		if (light == null) {
			light = new Foliage();
		}
		for (int i=top + 1; i < bottom; i++) {
			for (int j=left + 1; j < right; j++) {
				light.seed( level, j + level.width() * i, 1 );
			}
		}
		level.blobs.put( Foliage.class, light );
	}

	private void placeItem(Item item, Level level){
		int itemPos;
		do {
			itemPos = level.pointToCell(random());
		} while (level.map[itemPos] == Terrain.GRASS && level.heaps.get(itemPos) != null);
		if (level.map[itemPos] == Terrain.HIGH_GRASS || level.map[itemPos] == Terrain.FURROWED_GRASS) {
			level.map[itemPos] = Terrain.GRASS;
			level.losBlocking[itemPos] = false;
		}

		level.drop(item, itemPos);
	}

	public static class Bush extends FlamableCustomTilemap {

		public int age = 2;

		private static final String AGE = "age";

		{
			texture = Assets.Environment.SEWER_GARDEN;
		}

		public void frame(int frame) {
			vis.map(new int[] { frame }, 1);
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(new int[] {age}, 1);
			return v;
		}

		@Override
		public String name(int tileX, int tileY) {
			return Messages.get(this, "name");
		}

		@Override
		public String desc(int tileX, int tileY) {
			if (age == 2) return Messages.get(this, "ripe");
			else if (age == 1) return Messages.get(this, "bloom");
			else return Messages.get(this, "desc");
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			bundle.put(AGE, age);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			age = bundle.getInt(AGE);
		}

		@Override
		public void remove() {
			if (vis != null) {
				vis.killAndErase();
			}
			Dungeon.level.customTiles.remove(this);
		}

	}

	public static class Hive extends FlamableCustomTilemap {

		{
			texture = Assets.Environment.SEWER_GARDEN;
		}


		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			v.map(new int[] { 3 }, 1);
			return v;
		}

		@Override
		public String name(int tileX, int tileY) {
			return Messages.get(this, "name");
		}

		@Override
		public String desc(int tileX, int tileY) {
			return Messages.get(this, "desc");
		}

		@Override
		public void remove() {
			int pos = Dungeon.level.pointToCell(tileX, tileY);

			if (vis != null) {
				vis.killAndErase();
			}
			Dungeon.level.customTiles.remove(this);

			Honeypot.ShatteredPot pot = new Honeypot.ShatteredPot();
			Dungeon.level.drop(pot, pos);

			Bee bee = new Bee();
			bee.spawn( Dungeon.depth );
			bee.HP = bee.HT;
			bee.pos = pos;

			GameScene.add( bee, 2f );
			Dungeon.level.occupyCell(bee);

			bee.setPotInfo(pos, null);

		}

	}

}
