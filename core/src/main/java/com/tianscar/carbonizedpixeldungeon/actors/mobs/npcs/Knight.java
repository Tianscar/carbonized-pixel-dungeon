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

package com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Shadow;
import com.tianscar.carbonizedpixeldungeon.items.Gold;
import com.tianscar.carbonizedpixeldungeon.items.quest.GoldenSeal;
import com.tianscar.carbonizedpixeldungeon.journal.Notes;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.Room;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.special.ThiefsTreasureRoom;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.KnightSprite;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.Point;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.windows.WndKnight;
import com.tianscar.carbonizedpixeldungeon.windows.WndQuest;

import java.util.ArrayList;

public class Knight extends NPC {

	{
		spriteClass = KnightSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos] && Quest.gold != null){
			Notes.add( Notes.Landmark.KNIGHT );
		}
		return super.act();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, Dungeon.hero.pos );

		if (c != Dungeon.hero){
			return true;
		}

		if (Quest.given) {
			
			GoldenSeal seal = Dungeon.hero.belongings.getItem(GoldenSeal.class);

			if (seal != null) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndKnight( Knight.this, seal ) );
					}
				});
			} else {
				String msg;
				switch(Knight.Quest.type){
					case 1: default:
						msg = Messages.get(this, "reminder_mimic", Dungeon.hero.name());
						break;
					case 2:
						msg = Messages.get(this, "reminder_thief", Dungeon.hero.name());
						break;
				}
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndQuest(Knight.this, msg));
					}
				});
			}
			
		} else {

			String msg1 = Messages.get(this, "intro", Dungeon.hero.name());
			String msg2 = "";

			switch (Knight.Quest.type){
				case 1:
					msg2 += Messages.get(this, "intro_mimic");
					break;
				case 2:
					msg2 += Messages.get(this, "intro_thief");

					Mob questBoss = new Shadow();
					questBoss.pos = Dungeon.level.randomRespawnCell( questBoss );
					if (questBoss.pos == -1) return true;
					else GameScene.add(questBoss);
					break;
			}

			final String msg1Final = msg1;
			final String msg2Final = msg2;

			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					GameScene.show(new WndQuest(Knight.this, msg1Final){
						@Override
						public void hide() {
							super.hide();
							GameScene.show(new WndQuest(Knight.this, msg2Final));
						}
					});
				}
			});

			Quest.given = true;
			Notes.add( Notes.Landmark.KNIGHT );
		}

		return true;
	}
	
	public static class Quest {

		public static int type;
		// 1 = mimic quest
		// 2 = thief quest
		
		private static boolean spawned;
		
		private static boolean given;
		
		public static Gold gold;
		
		public static void reset() {
			spawned = false;
			type = 0;

			gold = null;
		}
		
		private static final String NODE		= "knight";
		
		private static final String SPAWNED		= "spawned";
		private static final String TYPE		= "type";
		private static final String GIVEN		= "given";
		private static final String GOLD		= "gold";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				
				node.put( GOLD, gold );

			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

				type = node.getInt(TYPE);
				
				given = node.getBoolean( GIVEN );
				
				gold = (Gold) node.get( GOLD );

			} else {
				reset();
			}
		}
		
		private static boolean questRoomSpawned;
		
		public static void spawnKnight( Level level, Room room ) {
			if (questRoomSpawned) {
				
				questRoomSpawned = false;
				
				Knight npc = new Knight();
				boolean validPos;
				//Do not spawn knight on the entrance, a trap, or in front of a door.
				do {
					validPos = true;
					npc.pos = level.pointToCell(room.random());
					if (npc.pos == level.entrance){
						validPos = false;
					}
					for (Point door : room.connected.values()){
						if (level.trueDistance( npc.pos, level.pointToCell( door ) ) <= 1){
							validPos = false;
						}
					}
					if (level.traps.get(npc.pos) != null){
						validPos = false;
					}
				} while (!validPos);
				level.mobs.add( npc );

				spawned = true;

				given = false;
				gold = new Gold(2000);
				
			}
		}
		
		public static ArrayList<Room> spawnRoom( ArrayList<Room> rooms ) {
			questRoomSpawned = false;
			if (!spawned && (type != 0 || (Dungeon.depth > 6 && Random.Int( 10 - Dungeon.depth ) == 0))) {
				
				// decide between 1, or 2 for quest type.
				if (type == 0) {
					type = Random.Int(2)+1;
				}

                if (type == 1) {
                    rooms.add(new ThiefsTreasureRoom());
                }
		
				questRoomSpawned = true;
				
			}
			return rooms;
		}
		
		public static void complete() {
			gold = null;
			
			Notes.remove( Notes.Landmark.KNIGHT );
		}
	}
}
