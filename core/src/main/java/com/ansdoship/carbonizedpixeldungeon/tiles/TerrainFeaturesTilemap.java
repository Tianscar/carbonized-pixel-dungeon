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

package com.ansdoship.carbonizedpixeldungeon.tiles;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.levels.LastShopLevel;
import com.ansdoship.carbonizedpixeldungeon.levels.Terrain;
import com.ansdoship.carbonizedpixeldungeon.levels.traps.Trap;
import com.ansdoship.carbonizedpixeldungeon.plants.Plant;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.tweeners.ScaleTweener;
import com.ansdoship.pixeldungeonclasses.utils.PointF;
import com.ansdoship.pixeldungeonclasses.utils.RectF;
import com.ansdoship.pixeldungeonclasses.utils.SparseArray;

public class TerrainFeaturesTilemap extends DungeonTilemap {

	private static TerrainFeaturesTilemap instance;

	private SparseArray<Plant> plants;
	private SparseArray<Trap> traps;

	public TerrainFeaturesTilemap(SparseArray<Plant> plants, SparseArray<Trap> traps) {
		super(Assets.Environment.TERRAIN_FEATURES);

		this.plants = plants;
		this.traps = traps;

		map( Dungeon.level.map, Dungeon.level.width() );

		instance = this;
	}

	protected int getTileVisual(int pos, int tile, boolean flat){
		if (traps.get(pos) != null){
			Trap trap = traps.get(pos);
			if (!trap.visible)
				return -1;
			else
				return (trap.active ? trap.color : Trap.BLACK) + (trap.shape * 16);
		}

		if (plants.get(pos) != null){
			return plants.get(pos).image + 7*16;
		}

		int stage = (Dungeon.depth-1)/5;
		if (Dungeon.depth == 21 && Dungeon.level instanceof LastShopLevel) stage--;
		if (tile == Terrain.HIGH_GRASS){
			return 9 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.FURROWED_GRASS){
			return 11 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.GRASS) {
			return 13 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.EMBERS) {
			return 9 * (16*5) + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		}

		return -1;
	}

	public static Image tile(int pos, int tile ) {
		RectF uv = instance.tileset.get( instance.getTileVisual( pos, tile, true ) );
		if (uv == null) return null;
		
		Image img = new Image( instance.texture );
		img.frame(uv);
		return img;
	}

	public void growPlant( final int pos ){
		final Image plant = tile( pos, map[pos] );
		if (plant == null) return;
		
		plant.origin.set( 8, 12 );
		plant.scale.set( 0 );
		plant.point( DungeonTilemap.tileToWorld( pos ) );

		parent.add( plant );

		parent.add( new ScaleTweener( plant, new PointF(1, 1), 0.2f ) {
			protected void onComplete() {
				plant.killAndErase();
				killAndErase();
				updateMapCell(pos);
			}
		} );
	}

}