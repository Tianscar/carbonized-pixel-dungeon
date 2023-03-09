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

package com.tianscar.carbonizedpixeldungeon.levels;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.items.Torch;
import com.tianscar.carbonizedpixeldungeon.levels.painters.HallsPainter;
import com.tianscar.carbonizedpixeldungeon.levels.painters.Painter;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.Room;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.special.DemonSpawnerRoom;
import com.tianscar.carbonizedpixeldungeon.levels.traps.BlazingTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.CorrosionTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.CursingTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.DisarmingTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.DisintegrationTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.DistortionTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.FlashingTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.FrostTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.GatewayTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.GeyserTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.GrimTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.GuardianTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.PitfallTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.RockfallTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.StormTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.SummoningTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.WarpingTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.WeakeningTrap;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.tiles.DungeonTilemap;
import com.tianscar.pixeldungeonclasses.glwrap.Blending;
import com.tianscar.pixeldungeonclasses.noosa.Game;
import com.tianscar.pixeldungeonclasses.noosa.Group;
import com.tianscar.pixeldungeonclasses.noosa.audio.Music;
import com.tianscar.pixeldungeonclasses.noosa.particles.PixelParticle;
import com.tianscar.pixeldungeonclasses.utils.PointF;
import com.tianscar.pixeldungeonclasses.utils.Random;

import java.util.ArrayList;

public class HallsLevel extends RegularLevel {

	{
		
		viewDistance = Math.min( 26 - Dungeon.depth, viewDistance );
		
		color1 = 0x801500;
		color2 = 0xa68521;
	}

	@Override
	public void playLevelMusic() {
		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.HALLS_1, Assets.Music.HALLS_2, Assets.Music.HALLS_2},
				new float[]{1, 1, 0.5f},
				false);
	}

	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> rooms = super.initRooms();

		rooms.add(new DemonSpawnerRoom());

		return rooms;
	}

	@Override
	protected int standardRooms(boolean forceMax) {
		if (forceMax) return 9;
		//8 to 9, average 8.33
		return 8+Random.chances(new float[]{2, 1});
	}
	
	@Override
	protected int specialRooms(boolean forceMax) {
		if (forceMax) return 3;
		//2 to 3, average 2.5
		return 2 + Random.chances(new float[]{1, 1});
	}
	
	@Override
	protected Painter painter() {
		return new HallsPainter()
				.setWater(feeling == Feeling.WATER ? 0.70f : 0.15f, 6)
				.setGrass(feeling == Feeling.GRASS ? 0.65f : 0.10f, 3)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}
	
	@Override
	public void create() {
		addItemToSpawn( new Torch() );
		addItemToSpawn( new Torch() );
		super.create();
	}
	
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_HALLS;
	}
	
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}
	
	@Override
	protected Class<?>[] trapClasses() {
		return new Class[]{
				FrostTrap.class, StormTrap.class, CorrosionTrap.class, BlazingTrap.class, DisintegrationTrap.class,
				RockfallTrap.class, FlashingTrap.class, GuardianTrap.class, WeakeningTrap.class,
				DisarmingTrap.class, SummoningTrap.class, WarpingTrap.class, CursingTrap.class, GrimTrap.class, PitfallTrap.class, DistortionTrap.class, GatewayTrap.class, GeyserTrap.class };
	}

	@Override
	protected float[] trapChances() {
		return new float[]{
				4, 4, 4, 4, 4,
				2, 2, 2, 2,
				1, 1, 1, 1, 1, 1, 1, 1, 1 };
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		addHallsVisuals( this, visuals );
		return visuals;
	}
	
	public static void addHallsVisuals( Level level, Group group ) {
		for (int i=0; i < level.length(); i++) {
			if (level.map[i] == Terrain.WATER) {
				group.add( new Stream( i ) );
			}
		}
	}
	
	private static class Stream extends Group {
		
		private int pos;
		
		private float delay;
		
		public Stream( int pos ) {
			super();
			
			this.pos = pos;
			
			delay = Random.Float( 2 );
		}
		
		@Override
		public void update() {

			if (!Dungeon.level.water[pos]){
				killAndErase();
				return;
			}
			
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				
				super.update();
				
				if ((delay -= Game.elapsed) <= 0) {
					
					delay = Random.Float( 2 );
					
					PointF p = DungeonTilemap.tileToWorld( pos );
					((FireParticle)recycle( FireParticle.class )).reset(
						p.x + Random.Float( DungeonTilemap.SIZE ),
						p.y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
		
		@Override
		public void draw() {
			Blending.setLightMode();
			super.draw();
			Blending.setNormalMode();
		}
	}
	
	public static class FireParticle extends PixelParticle.Shrinking {
		
		public FireParticle() {
			super();
			
			color( 0xEE7722 );
			lifespan = 1f;
			
			acc.set( 0, +80 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan;
			
			speed.set( 0, -40 );
			size = 4;
		}
		
		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? (1 - p) * 5 : 1;
		}
	}
}
