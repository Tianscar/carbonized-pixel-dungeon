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

package com.ansdoship.carbonizedpixeldungeon;

import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class GamesInProgress {
	
	public static final int MAX_SLOTS = 4;
	
	//null means we have loaded info and it is empty, no entry means unknown.
	private static HashMap<Integer, Info> slotStates = new HashMap<>();
	public static int curSlot;
	
	public static HeroClass selectedClass;
	
	private static final String GAME_FOLDER = "game%d";
	private static final String GAME_FILE	= "game.dat";
	private static final String DEPTH_FILE	= "depth%d.dat";
	
	public static boolean gameExists( int slot ){
		return FileUtils.dirExists(Messages.format(GAME_FOLDER, slot));
	}
	
	public static String gameFolder( int slot ){
		return Messages.format(GAME_FOLDER, slot);
	}
	
	public static String gameFile( int slot ){
		return gameFolder(slot) + "/" + GAME_FILE;
	}
	
	public static String depthFile( int slot, int depth ) {
		return gameFolder(slot) + "/" + Messages.format(DEPTH_FILE, depth);
	}
	
	public static int firstEmpty(){
		for (int i = 1; i <= MAX_SLOTS; i++){
			if (check(i) == null) return i;
		}
		return -1;
	}
	
	public static ArrayList<Info> checkAll(){
		ArrayList<Info> result = new ArrayList<>();
		for (int i = 0; i <= MAX_SLOTS; i++){
			Info curr = check(i);
			if (curr != null) result.add(curr);
		}
		Collections.sort(result, scoreComparator);
		return result;
	}
	
	public static Info check( int slot ) {
		
		if (slotStates.containsKey( slot )) {
			
			return slotStates.get( slot );
			
		} else if (!gameExists( slot )) {
			
			slotStates.put(slot, null);
			return null;
			
		} else {
			
			Info info;
			try {
				
				Bundle bundle = FileUtils.bundleFromFile(gameFile(slot));
				info = new Info();
				info.slot = slot;
				Dungeon.preview(info, bundle);

				//saves from before v0.0.8 are not supported
				if (info.version < CarbonizedPixelDungeon.v0_0_8) {
					info = null;
				}

			} catch (IOException e) {
				info = null;
			} catch (Exception e){
				CarbonizedPixelDungeon.reportException( e );
				info = null;
			}
			
			slotStates.put( slot, info );
			return info;
			
		}
	}

	public static void set(int slot, int depth, int challenges,
	                       Hero hero) {
		Info info = new Info();
		info.slot = slot;
		
		info.depth = depth;
		info.challenges = challenges;
		
		info.level = hero.lvl;
		info.STR = hero.STR;
		info.CON = hero.CON;
		info.DEX = hero.DEX;
		info.INT = hero.INT;
		info.WIS = hero.WIS;
		info.CHA = hero.CHA;
		info.STRBonus = hero.STR() - hero.STR;
		info.exp = hero.exp;
		info.HP = hero.HP;
		info.HT = hero.HT;
		info.hunger = hero.hunger();
		info.shld = hero.shielding();
		info.heroClass = hero.heroClass;
		info.subClass = hero.subClass;
		info.armorTier = hero.armorTier();
		
		info.goldCollected = Statistics.goldCollected;
		info.maxDepth = Statistics.deepestFloor;

		slotStates.put( slot, info );
	}
	
	public static void setUnknown( int slot ) {
		slotStates.remove( slot );
	}
	
	public static void delete( int slot ) {
		slotStates.put( slot, null );
	}
	
	public static class Info {
		public int slot;
		
		public int depth;
		public int version;
		public int challenges;
		
		public int level;
		public int STR;
		public int STRBonus;
		public int CON;
		public int DEX;
		public int INT;
		public int WIS;
		public int CHA;
		public int exp;
		public int HP;
		public int HT;
		public int hunger;
		public int shld;
		public HeroClass heroClass;
		public HeroSubClass subClass;
		public int armorTier;
		
		public int goldCollected;
		public int maxDepth;
	}
	
	public static final Comparator<GamesInProgress.Info> scoreComparator = new Comparator<GamesInProgress.Info>() {
		@Override
		public int compare(GamesInProgress.Info lhs, GamesInProgress.Info rhs ) {
			int lScore = (lhs.level * lhs.maxDepth * 100) + lhs.goldCollected;
			int rScore = (rhs.level * rhs.maxDepth * 100) + rhs.goldCollected;
			return (int)Math.signum( rScore - lScore );
		}
	};
}
