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
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.quest.BrokenSatchel;
import com.tianscar.carbonizedpixeldungeon.items.quest.CrabClaw;
import com.tianscar.carbonizedpixeldungeon.items.quest.RatSkull;
import com.tianscar.carbonizedpixeldungeon.items.weapon.Weapon;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.ranged.LightCrossbow;
import com.tianscar.carbonizedpixeldungeon.journal.Notes;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.Room;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.PatrolSprite;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.Point;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.windows.WndPatrol;
import com.tianscar.carbonizedpixeldungeon.windows.WndQuest;

public class Patrol extends NPC {

	{
		spriteClass = PatrolSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	@Override
	protected boolean act() {

		if (Quest.given && Quest.completed && !Dungeon.level.heroFOV[pos] && !Dungeon.level.heroFOV[Dungeon.level.entrance]) {
			destroy();
			sprite.die();
		}
		else if (Dungeon.level.heroFOV[pos] && Quest.given && !Quest.completed) {
			Notes.add( Notes.Landmark.PATROL );
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

			if (Quest.completed) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndQuest( Patrol.this, Messages.get(Patrol.this, "thankyou" )) );
					}
				});
			}
			else {

				boolean ghostQuestCompleted = Ghost.Quest.completed();

				Item questedItem;
				if (ghostQuestCompleted) {
					switch (Ghost.Quest.type) {
						case 1: questedItem = Dungeon.hero.belongings.getItem(RatSkull.class); break;
						case 2: questedItem = Dungeon.hero.belongings.getItem(BrokenSatchel.class); break;
						case 3: questedItem = Dungeon.hero.belongings.getItem(CrabClaw.class); break;
						default: questedItem = null; break;
					}
				}
				else questedItem = null;

				if (ghostQuestCompleted && questedItem != null) {

					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show( new WndPatrol( questedItem ) );
						}
					});

				}
				else {

					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show(new WndQuest(Patrol.this, Messages.get(Patrol.this, "reminder", Dungeon.hero.name())));
						}
					});

				}
			}

		} else {

			Game.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show(new WndQuest(Patrol.this, Messages.get(Patrol.this, "intro", Dungeon.hero.name())) {
								@Override
								public void hide() {
									super.hide();
									GameScene.show(new WndQuest(Patrol.this, Messages.get(Patrol.this, "quest")));
								}
							});
						}
					});
				}
			});

			Quest.given = true;
			Notes.add( Notes.Landmark.PATROL );
		}

		return true;
	}

	public static class Quest {

		private static boolean spawned;
		static boolean given;
		private static boolean completed;

		public static Weapon weapon;
		public static Weapon.Enchantment enchant;

		public static void reset() {
			spawned = false;
			given   = false;
			completed = false;

			weapon = null;
			enchant = null;
		}

		private static final String NODE		= "patrol";

		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String WEAPON      = "weapon";
		private static final String ENCHANT     = "enchant";

		public static void storeInBundle( Bundle bundle ) {

			Bundle node = new Bundle();

			node.put( SPAWNED, spawned );

			if (spawned) {
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( WEAPON, weapon );

				if (enchant != null) {
					node.put( ENCHANT, enchant );
				}
			}

			bundle.put( NODE, node );
		}

		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );

			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				weapon = (Weapon) node.get( WEAPON );

				if (node.contains( ENCHANT )) enchant = (Weapon.Enchantment) node.get( ENCHANT );
			}
		}

		public static void spawn(Level level, Room room ) {

			if (!spawned) {

				Patrol npc = new Patrol();
				boolean validPos;
				//Do not spawn patrol on the entrance, a trap, other mob, or in front of a door.
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
					if (level.findMob(npc.pos) != null) {
						validPos = false;
					}
				} while (!validPos);
				level.mobs.add( npc );

				spawned = true;

				given = false;

				weapon = new LightCrossbow();
				weapon.cursed = false;
				if (Random.Int(2) == 0) weapon.upgrade();
				//10% to be enchanted. We store it separately so enchant status isn't revealed early
				if (Random.Int(10) == 0) enchant = Weapon.Enchantment.random();
			}

		}

		public static void complete() {
			weapon = null;
			completed = true;

			Notes.remove( Notes.Landmark.PATROL );
		}

	}

}
