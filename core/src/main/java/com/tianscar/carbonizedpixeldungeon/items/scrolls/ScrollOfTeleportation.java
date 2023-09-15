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

package com.tianscar.carbonizedpixeldungeon.items.scrolls;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.levels.RegularLevel;
import com.tianscar.carbonizedpixeldungeon.levels.Terrain;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.Room;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.secret.SecretRoom;
import com.tianscar.carbonizedpixeldungeon.levels.rooms.special.SpecialRoom;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.BArray;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.noosa.tweeners.AlphaTweener;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Point;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

import java.util.ArrayList;

public class ScrollOfTeleportation extends Scroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_TELEPORT;
	}

	@Override
	public void doRead() {

		Sample.INSTANCE.play( Assets.Sounds.READ );
		
		teleportPreferringUnseen( curUser );
		identify();

		if (!Dungeon.bossLevel()) {
			readAnimation();
		}
	}
	
	public static boolean teleportToLocation(Char ch, int pos){
		PathFinder.buildDistanceMap(pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
		if (PathFinder.distance[ch.pos] == Integer.MAX_VALUE
				|| (!Dungeon.level.passable[pos] && !Dungeon.level.avoid[pos])
				|| Actor.findChar(pos) != null){
			if (ch == Dungeon.hero){
				GLog.w( Messages.get(ScrollOfTeleportation.class, "cant_reach") );
			}
			return false;
		}
		
		appear( ch, pos );
		Dungeon.level.occupyCell( ch );
		if (ch == Dungeon.hero) {
			Dungeon.observe();
			GameScene.updateFog();
		}
		return true;
		
	}
	
	public static boolean teleportHero( Hero hero ) {
		return teleportChar( hero );
	}
	
	public static boolean teleportChar( Char ch ) {

		if (Dungeon.bossLevel()){
			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			return false;
		}
		
		int count = 20;
		int pos;
		do {
			pos = Dungeon.level.randomRespawnCell( ch );
			if (count-- <= 0) {
				break;
			}
		} while (pos == -1 || Dungeon.level.secret[pos]);
		
		if (pos == -1) {
			
			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			return false;
			
		} else {
			
			appear( ch, pos );
			Dungeon.level.occupyCell( ch );
			
			if (ch == Dungeon.hero) {
				GLog.i( Messages.get(ScrollOfTeleportation.class, "tele") );
				
				Dungeon.observe();
				GameScene.updateFog();
				Dungeon.hero.interrupt();
			}
			return true;
			
		}
	}
	
	public static void teleportPreferringUnseen( Hero hero ){
		
		if (Dungeon.bossLevel() || !(Dungeon.level instanceof RegularLevel)){
			teleportHero( hero );
			return;
		}
		
		RegularLevel level = (RegularLevel) Dungeon.level;
		ArrayList<Integer> candidates = new ArrayList<>();
		
		for (Room r : level.rooms()){
			if (r instanceof SpecialRoom){
				int terr;
				boolean locked = false;
				for (Point p : r.getPoints()){
					terr = level.map[level.pointToCell(p)];
					if (terr == Terrain.LOCKED_DOOR || terr == Terrain.BARRICADE){
						locked = true;
						break;
					}
				}
				if (locked){
					continue;
				}
			}
			
			int cell;
			for (Point p : r.charPlaceablePoints(level)){
				cell = level.pointToCell(p);
				if (level.passable[cell] && !level.visited[cell] && !level.secret[cell] && Actor.findChar(cell) == null){
					candidates.add(cell);
				}
			}
		}
		
		if (candidates.isEmpty()){
			teleportHero( hero );
		} else {
			int pos = Random.element(candidates);
			boolean secretDoor = false;
			int doorPos = -1;
			if (level.room(pos) instanceof SpecialRoom){
				SpecialRoom room = (SpecialRoom) level.room(pos);
				if (room.entrance() != null){
					doorPos = level.pointToCell(room.entrance());
					for (int i : PathFinder.NEIGHBOURS8){
						if (!room.inside(level.cellToPoint(doorPos + i))
								&& level.passable[doorPos + i]
								&& Actor.findChar(doorPos + i) == null){
							secretDoor = room instanceof SecretRoom;
							pos = doorPos + i;
							break;
						}
					}
				}
			}
			GLog.i( Messages.get(ScrollOfTeleportation.class, "tele") );
			appear( hero, pos );
			Dungeon.level.occupyCell(hero );
			if (secretDoor && level.map[doorPos] == Terrain.SECRET_DOOR){
				Sample.INSTANCE.play( Assets.Sounds.SECRET );
				int oldValue = Dungeon.level.map[doorPos];
				GameScene.discoverTile( doorPos, oldValue );
				Dungeon.level.discover( doorPos );
				ScrollOfMagicMapping.discover( doorPos );
			}
			Dungeon.observe();
			GameScene.updateFog();
		}
		
	}

	public static void appear( Char ch, int pos ) {

		ch.sprite.interruptMotion();

		if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[ch.pos]){
			Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
		}

		ch.move( pos );
		if (ch.pos == pos) ch.sprite.place( pos );

		if (ch.invisible == 0) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}

		if (Dungeon.level.heroFOV[pos] || ch == Dungeon.hero ) {
			ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		}
	}
	
	@Override
	public int value() {
		return isKnown() ? 30 * quantity : super.value();
	}
}
