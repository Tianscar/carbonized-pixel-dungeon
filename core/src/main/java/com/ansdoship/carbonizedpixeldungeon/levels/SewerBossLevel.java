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

package com.ansdoship.carbonizedpixeldungeon.levels;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Bones;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Goo;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.items.Heap;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.levels.builders.Builder;
import com.ansdoship.carbonizedpixeldungeon.levels.builders.FigureEightBuilder;
import com.ansdoship.carbonizedpixeldungeon.levels.painters.Painter;
import com.ansdoship.carbonizedpixeldungeon.levels.painters.SewerPainter;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.Room;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.secret.BigRatRoom;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.sewerboss.GooBossRoom;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.sewerboss.SewerBossEntranceRoom;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.sewerboss.SewerBossExitRoom;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.standard.StandardRoom;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.Group;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Music;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Callback;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.util.ArrayList;

public class SewerBossLevel extends SewerLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}

	@Override
	public void playLevelMusic() {
		if (locked){
			Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
			return;
		}

		boolean gooAlive = false;
		for (Mob m : mobs){
			if (m instanceof Goo) {
				gooAlive = true;
				break;
			}
		}

		if (gooAlive){
			Music.INSTANCE.end();
		} else {
			Music.INSTANCE.playTracks(
					new String[]{Assets.Music.SEWERS_1, Assets.Music.SEWERS_2, Assets.Music.SEWERS_2},
					new float[]{1, 1, 0.5f},
					false);
		}

	}
	
	private int stairs = 0;
	
	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		
		initRooms.add( roomEntrance = new SewerBossEntranceRoom() );
		initRooms.add( roomExit = new SewerBossExitRoom() );
		
		int standards = standardRooms(true);
		for (int i = 0; i < standards; i++) {
			StandardRoom s = StandardRoom.createRoom();
			//force to normal size
			s.setSizeCat(0, 0);
			initRooms.add(s);
		}
		
		GooBossRoom gooRoom = GooBossRoom.randomGooRoom();
		initRooms.add(gooRoom);
		((FigureEightBuilder)builder).setLandmarkRoom(gooRoom);
		initRooms.add(new BigRatRoom());
		return initRooms;
	}
	
	@Override
	protected int standardRooms(boolean forceMax) {
		if (forceMax) return 3;
		//2 to 3, average 2.5
		return 2+Random.chances(new float[]{1, 1});
	}
	
	protected Builder builder(){
		return new FigureEightBuilder()
				.setLoopShape( 2 , Random.Float(0.3f, 0.8f), 0f)
				.setPathLength(1f, new float[]{1})
				.setTunnelLength(new float[]{1, 2}, new float[]{1});
	}
	
	@Override
	protected Painter painter() {
		return new SewerPainter()
				.setWater(0.50f, 5)
				.setGrass(0.20f, 4)
				.setTraps(nTraps(), trapClasses(), trapChances());
	}
	
	protected int nTraps() {
		return 0;
	}

	@Override
	protected void createMobs() {
	}
	
	public Actor addRespawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = pointToCell(roomEntrance.random());
			} while (pos == entrance || solid[pos]);
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		int pos;
		do {
			pos = pointToCell(roomEntrance.random());
		} while (pos == entrance
				|| !passable[pos]
				|| (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[pos])
				|| Actor.findChar(pos) != null);
		return pos;
	}

	
	public void seal() {
		if (entrance != 0) {

			super.seal();
			
			set( entrance, Terrain.WATER );
			GameScene.updateMap( entrance );
			GameScene.ripple( entrance );
			
			stairs = entrance;
			entrance = 0;

			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Music.INSTANCE.play(Assets.Music.SEWERS_BOSS, true);
				}
			});
		}
	}
	
	public void unseal() {
		if (stairs != 0) {

			super.unseal();
			
			entrance = stairs;
			stairs = 0;
			
			set( entrance, Terrain.ENTRANCE );
			GameScene.updateMap( entrance );

			for (Room room : rooms()) {
				if (room instanceof BigRatRoom) {
					Room.Door entrance = ((BigRatRoom) room).entrance();
					discover(entrance.x + entrance.y * width());
					break;
				}
			}

			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Music.INSTANCE.stop();
					playLevelMusic();
				}
			});
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		if (map[exit-1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(exit-1));
		if (map[exit+1] != Terrain.WALL_DECO) visuals.add(new PrisonLevel.Torch(exit+1));
		return visuals;
	}
	
	private static final String STAIRS	= "stairs";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( STAIRS, stairs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		stairs = bundle.getInt( STAIRS );
		roomExit = roomEntrance;
	}
}
