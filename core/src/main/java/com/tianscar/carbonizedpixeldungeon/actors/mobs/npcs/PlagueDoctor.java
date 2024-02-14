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
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Rat;
import com.tianscar.carbonizedpixeldungeon.items.armor.Armor;
import com.tianscar.carbonizedpixeldungeon.items.armor.LeatherArmor;
import com.tianscar.carbonizedpixeldungeon.items.quest.RatHide;
import com.tianscar.carbonizedpixeldungeon.items.weapon.Weapon;
import com.tianscar.carbonizedpixeldungeon.journal.Notes;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.Room;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.PlagueDoctorSprite;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.Point;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.windows.WndPlagueDoctor;
import com.tianscar.carbonizedpixeldungeon.windows.WndQuest;

public class PlagueDoctor extends NPC {

	{
		spriteClass = PlagueDoctorSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	@Override
	protected boolean act() {

		if (Quest.given && Quest.completed && !Dungeon.level.heroFOV[pos] && !Dungeon.level.heroFOV[Dungeon.level.entrance]) {
			destroy();
			sprite.die();
		}
		else if (Dungeon.level.heroFOV[pos] && Quest.given && !Quest.completed) {
			Notes.add( Notes.Landmark.PLAGUEDOCTOR );
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
				tell( Messages.get(this, "soliloquize") );
			}
			else {

				RatHide hides = Dungeon.hero.belongings.getItem( RatHide.class );
				if (hides != null && hides.quantity() >= 10) {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show( new WndPlagueDoctor( hides ) );
						}
					});
				} else {
					tell( Messages.get(this, "reminder") );
				}

			}
			
		} else {
			tell( Messages.get(this, "quest") );
			Quest.given = true;
			Quest.completed = false;
			Notes.add( Notes.Landmark.PLAGUEDOCTOR );
		}

		return true;
	}
	
	private void tell( String text ) {
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndQuest( PlagueDoctor.this, text ));
			}
		});
	}

	public static class Quest {
		
		private static boolean spawned;
		private static boolean given;
		private static boolean completed;
		
		public static Armor armor;
		public static Armor.Glyph glyph;
		
		public static void reset() {
			spawned = false;
			given   = false;
			completed = false;

			armor = null;
			glyph = null;
		}
		
		private static final String NODE		= "plaguedoctor";

		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String ARMOR       = "armor";
		private static final String GLYPH       = "glpyh";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( ARMOR, armor );

				if (glyph != null) {
					node.put( GLYPH, glyph );
				}
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				armor = (Armor) node.get( ARMOR );

				if (node.contains( GLYPH )) glyph = (Armor.Glyph) node.get( GLYPH );
			}
		}

		public static void spawn( Level level, Room room ) {

			if (!spawned) {

				PlagueDoctor npc = new PlagueDoctor();
				boolean validPos;
				//Do not spawn plaguedoctor on the entrance, a trap, other mob, or in front of a door.
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

				armor = new LeatherArmor();
				armor.cursed = false;
				if (Random.Int(2) == 0) armor.upgrade();
				//10% to be enchanted. We store it separately so enchant status isn't revealed early
				if (Random.Int(10) == 0) glyph = Armor.Glyph.random();
			}

		}
		
		public static void process( Mob mob ) {
			if (spawned && given && !completed) {
				if (mob instanceof Rat) {

					Dungeon.level.drop( new RatHide(), mob.pos ).sprite.drop();

				}
			}
		}
		
		public static void complete() {
			armor = null;
			completed = true;
			
			Notes.remove( Notes.Landmark.PLAGUEDOCTOR );
		}

	}

}
