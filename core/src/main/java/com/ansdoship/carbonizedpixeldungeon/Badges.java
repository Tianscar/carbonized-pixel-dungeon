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

import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.Artifact;
import com.ansdoship.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.ansdoship.carbonizedpixeldungeon.items.bags.PotionBandolier;
import com.ansdoship.carbonizedpixeldungeon.items.bags.ScrollHolder;
import com.ansdoship.carbonizedpixeldungeon.items.bags.VelvetPouch;
import com.ansdoship.carbonizedpixeldungeon.journal.Catalog;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.PixelScene;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Badges {

	public enum Badge {
		MASTERY_WARRIOR,
		MASTERY_MAGE,
		MASTERY_ROGUE,
		MASTERY_HUNTRESS,
		FOUND_RATMOGRIFY,

		//bronze
		UNLOCK_MAGE                 ( 1 ),
		UNLOCK_ROGUE                ( 2 ),
		UNLOCK_HUNTRESS             ( 3 ),
		MONSTERS_SLAIN_1            ( 4 ),
		MONSTERS_SLAIN_2            ( 5 ),
		GOLD_COLLECTED_1            ( 6 ),
		GOLD_COLLECTED_2            ( 7 ),
		ITEM_LEVEL_1                ( 8 ),
		LEVEL_REACHED_1             ( 9 ),
		STRENGTH_ATTAINED_1         ( 10 ),
		CONSTITUTION_ATTAINED_1     ( 11 ),
		DEXTERITY_ATTAINED_1        ( 12 ),
		INTELLIGENCE_ATTAINED_1     ( 13 ),
		WISDOM_ATTAINED_1           ( 14 ),
		CHARISMA_ATTAINED_1         ( 15 ),
		FOOD_EATEN_1                ( 16 ),
		POTIONS_COOKED_1            ( 17 ),
		BOSS_SLAIN_1                ( 18 ),
		DEATH_FROM_FIRE             ( 19 ),
		DEATH_FROM_POISON           ( 20 ),
		DEATH_FROM_GAS              ( 21 ),
		DEATH_FROM_HUNGER           ( 22 ),
		DEATH_FROM_FALLING          ( 23 ),
		GAMES_PLAYED_1              ( 24, true ),

		//silver
		NO_MONSTERS_SLAIN           ( 64 ),
		GRIM_WEAPON                 ( 65 ),
		MONSTERS_SLAIN_3            ( 66 ),
		MONSTERS_SLAIN_4            ( 67 ),
		GOLD_COLLECTED_3            ( 68 ),
		GOLD_COLLECTED_4            ( 69 ),
		ITEM_LEVEL_2                ( 70 ),
		ITEM_LEVEL_3                ( 71 ),
		LEVEL_REACHED_2             ( 72 ),
		LEVEL_REACHED_3             ( 73 ),
		STRENGTH_ATTAINED_2         ( 74 ),
		STRENGTH_ATTAINED_3         ( 75 ),
		CONSTITUTION_ATTAINED_2     ( 76 ),
		CONSTITUTION_ATTAINED_3     ( 77 ),
		DEXTERITY_ATTAINED_2        ( 78 ),
		DEXTERITY_ATTAINED_3        ( 79 ),
		INTELLIGENCE_ATTAINED_2     ( 80 ),
		INTELLIGENCE_ATTAINED_3     ( 81 ),
		WISDOM_ATTAINED_2           ( 82 ),
		WISDOM_ATTAINED_3           ( 83 ),
		CHARISMA_ATTAINED_2         ( 84 ),
		CHARISMA_ATTAINED_3         ( 85 ),
		FOOD_EATEN_2                ( 86 ),
		FOOD_EATEN_3                ( 87 ),
		POTIONS_COOKED_2            ( 88 ),
		POTIONS_COOKED_3            ( 89 ),
		BOSS_SLAIN_2                ( 90 ),
		BOSS_SLAIN_3                ( 91 ),
		ALL_POTIONS_IDENTIFIED      ( 92 ),
		ALL_SCROLLS_IDENTIFIED      ( 93 ),
		DEATH_FROM_GLYPH            ( 94 ),
		BOSS_SLAIN_1_WARRIOR,
		BOSS_SLAIN_1_MAGE,
		BOSS_SLAIN_1_ROGUE,
		BOSS_SLAIN_1_HUNTRESS,
		BOSS_SLAIN_1_ALL_CLASSES    ( 95, true ),
		GAMES_PLAYED_2              ( 96, true ),

		//gold
		PIRANHAS                    ( 128 ),
		BAG_BOUGHT_VELVET_POUCH,
		BAG_BOUGHT_SCROLL_HOLDER,
		BAG_BOUGHT_POTION_BANDOLIER,
		BAG_BOUGHT_MAGICAL_HOLSTER,
		ALL_BAGS_BOUGHT             ( 129 ),
		MASTERY_COMBO               ( 130 ),
		MONSTERS_SLAIN_5            ( 131 ),
		GOLD_COLLECTED_5            ( 132 ),
		ITEM_LEVEL_4                ( 133 ),
		ITEM_LEVEL_5                ( 134 ),
		LEVEL_REACHED_4             ( 135 ),
		LEVEL_REACHED_5             ( 136 ),
		STRENGTH_ATTAINED_4         ( 137 ),
		STRENGTH_ATTAINED_5         ( 138 ),
		CONSTITUTION_ATTAINED_4     ( 139 ),
		CONSTITUTION_ATTAINED_5     ( 140 ),
		DEXTERITY_ATTAINED_4        ( 141 ),
		DEXTERITY_ATTAINED_5        ( 142 ),
		INTELLIGENCE_ATTAINED_4     ( 143 ),
		INTELLIGENCE_ATTAINED_5     ( 144 ),
		WISDOM_ATTAINED_4           ( 145 ),
		WISDOM_ATTAINED_5           ( 146 ),
		CHARISMA_ATTAINED_4         ( 147 ),
		CHARISMA_ATTAINED_5         ( 148 ),
		FOOD_EATEN_4                ( 149 ),
		FOOD_EATEN_5                ( 150 ),
		POTIONS_COOKED_4            ( 151 ),
		POTIONS_COOKED_5            ( 152 ),
		BOSS_SLAIN_4                ( 153 ),
		ALL_WEAPONS_IDENTIFIED      ( 154 ),
		ALL_ARMOR_IDENTIFIED        ( 155 ),
		ALL_WANDS_IDENTIFIED        ( 156 ),
		ALL_RINGS_IDENTIFIED        ( 157 ),
		ALL_ARTIFACTS_IDENTIFIED    ( 158 ),
		VICTORY                     ( 159 ),
		YASD                        ( 160, true ),
		BOSS_SLAIN_3_GLADIATOR,
		BOSS_SLAIN_3_SHIELDGUARD,
		BOSS_SLAIN_3_WARLOCK,
		BOSS_SLAIN_3_BATTLEMAGE,
		BOSS_SLAIN_3_FREERUNNER,
		BOSS_SLAIN_3_ASSASSIN,
		BOSS_SLAIN_3_SNIPER,
		BOSS_SLAIN_3_WARDEN,
		BOSS_SLAIN_3_ALL_SUBCLASSES ( 161, true ),
		GAMES_PLAYED_3              ( 162, true ),

		//diamond
		HAPPY_END                   ( 192 ),
		ALL_ITEMS_IDENTIFIED        ( 193, true ),
		VICTORY_WARRIOR,
		VICTORY_MAGE,
		VICTORY_ROGUE,
		VICTORY_HUNTRESS,
		VICTORY_ALL_CLASSES         ( 194, true ),
		GAMES_PLAYED_4              ( 195, true ),
		CHAMPION_1                  ( 196 ),
		LEVEL_REACHED_6             ( 197 ),

		//platinum
		GAMES_PLAYED_5              ( 224, true ),
		CHAMPION_2                  ( 225 ),
		CHAMPION_3                  ( 226 );

		public boolean meta;

		public int image;

		Badge( int image ) {
			this( image, false );
		}

		Badge( int image, boolean meta ) {
			this.image = image;
			this.meta = meta;
		}

		public String title(){
			return Messages.get(this, name()+".title");
		}

		public String desc(){
			return Messages.get(this, name()+".desc");
		}

		Badge() {
			this( -1 );
		}
	}

	private static HashSet<Badge> global;
	private static HashSet<Badge> local = new HashSet<>();

	private static boolean saveNeeded = false;

	public static void reset() {
		local.clear();
		loadGlobal();
	}

	public static final String BADGES_FILE	= "badges.dat";
	private static final String BADGES		= "badges";

	private static final HashSet<String> removedBadges = new HashSet<>();
	static{
		//no recently removed badges
	}

	private static final HashMap<String, String> renamedBadges = new HashMap<>();
	static{
		//v1.1.0 (some names were from before 1.1.0, but conversion was added then)
		renamedBadges.put("BAG_BOUGHT_SEED_POUCH",      "BAG_BOUGHT_VELVET_POUCH");
		renamedBadges.put("BAG_BOUGHT_WAND_HOLSTER",    "BAG_BOUGHT_MAGICAL_HOLSTER");
	}

	public static HashSet<Badge> restore( Bundle bundle ) {
		HashSet<Badge> badges = new HashSet<>();
		if (bundle == null) return badges;

		String[] names = bundle.getStringArray( BADGES );
		for (int i=0; i < names.length; i++) {
			try {
				if (renamedBadges.containsKey(names[i])){
					names[i] = renamedBadges.get(names[i]);
				}
				if (!removedBadges.contains(names[i])){
					badges.add( Badge.valueOf( names[i] ) );
				}
			} catch (Exception e) {
				CarbonizedPixelDungeon.reportException(e);
			}
		}

		addReplacedBadges(badges);

		return badges;
	}

	public static void store( Bundle bundle, HashSet<Badge> badges ) {
		addReplacedBadges(badges);

		int count = 0;
		String names[] = new String[badges.size()];

		for (Badge badge:badges) {
			names[count++] = badge.toString();
		}
		bundle.put( BADGES, names );
	}

	public static void loadLocal( Bundle bundle ) {
		local = restore( bundle );
	}

	public static void saveLocal( Bundle bundle ) {
		store( bundle, local );
	}

	public static void loadGlobal() {
		if (global == null) {
			try {
				Bundle bundle = FileUtils.bundleFromFile( BADGES_FILE );
				global = restore( bundle );

			} catch (IOException e) {
				global = new HashSet<>();
			}
		}
	}

	public static void saveGlobal() {
		if (saveNeeded) {

			Bundle bundle = new Bundle();
			store( bundle, global );

			try {
				FileUtils.bundleToFile(BADGES_FILE, bundle);
				saveNeeded = false;
			} catch (IOException e) {
				CarbonizedPixelDungeon.reportException(e);
			}
		}
	}

	public static int unlocked(boolean global){
		if (global) return Badges.global.size();
		else        return Badges.local.size();
	}

	public static void validateMonstersSlain() {
		Badge badge = null;

		if (!local.contains( Badge.MONSTERS_SLAIN_1 ) && Statistics.enemiesSlain >= 10) {
			badge = Badge.MONSTERS_SLAIN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_2 ) && Statistics.enemiesSlain >= 50) {
			badge = Badge.MONSTERS_SLAIN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_3 ) && Statistics.enemiesSlain >= 100) {
			badge = Badge.MONSTERS_SLAIN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_4 ) && Statistics.enemiesSlain >= 200) {
			badge = Badge.MONSTERS_SLAIN_4;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_5 ) && Statistics.enemiesSlain >= 300) {
			badge = Badge.MONSTERS_SLAIN_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateGoldCollected() {
		Badge badge = null;

		if (!local.contains( Badge.GOLD_COLLECTED_1 ) && Statistics.goldCollected >= 250) {
			badge = Badge.GOLD_COLLECTED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_2 ) && Statistics.goldCollected >= 1000) {
			badge = Badge.GOLD_COLLECTED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_3 ) && Statistics.goldCollected >= 2500) {
			badge = Badge.GOLD_COLLECTED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_4 ) && Statistics.goldCollected >= 5000) {
			badge = Badge.GOLD_COLLECTED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_5 ) && Statistics.goldCollected >= 10_000) {
			badge = Badge.GOLD_COLLECTED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateLevelReached() {
		Badge badge = null;

		if (!local.contains( Badge.LEVEL_REACHED_1 ) && Dungeon.hero.lvl >= 6) {
			badge = Badge.LEVEL_REACHED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_2 ) && Dungeon.hero.lvl >= 12) {
			badge = Badge.LEVEL_REACHED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_3 ) && Dungeon.hero.lvl >= 18) {
			badge = Badge.LEVEL_REACHED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_4 ) && Dungeon.hero.lvl >= 24) {
			badge = Badge.LEVEL_REACHED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_5 ) && Dungeon.hero.lvl >= 30) {
			badge = Badge.LEVEL_REACHED_5;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_6 ) && Dungeon.hero.lvl >= 40) {
			badge = Badge.LEVEL_REACHED_6;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validatePropertiesAttained() {
		validateStrengthAttained();
		validateConstitutionAttained();
		validateDexterityAttained();
		validateIntelligenceAttained();
		validateWisdomAttained();
		validateCharismaAttained();
	}

	public static void validateStrengthAttained() {
		Badge badge = null;

		if (!local.contains( Badge.STRENGTH_ATTAINED_1 ) && Dungeon.hero.STR >= 13) {
			badge = Badge.STRENGTH_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_2 ) && Dungeon.hero.STR >= 15) {
			badge = Badge.STRENGTH_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_3 ) && Dungeon.hero.STR >= 17) {
			badge = Badge.STRENGTH_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_4 ) && Dungeon.hero.STR >= 19) {
			badge = Badge.STRENGTH_ATTAINED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_5 ) && Dungeon.hero.STR >= 21) {
			badge = Badge.STRENGTH_ATTAINED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateConstitutionAttained() {
		Badge badge = null;

		if (!local.contains( Badge.CONSTITUTION_ATTAINED_1 ) && Dungeon.hero.CON >= 13) {
			badge = Badge.CONSTITUTION_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.CONSTITUTION_ATTAINED_2 ) && Dungeon.hero.CON >= 15) {
			badge = Badge.CONSTITUTION_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.CONSTITUTION_ATTAINED_3 ) && Dungeon.hero.CON >= 17) {
			badge = Badge.CONSTITUTION_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.CONSTITUTION_ATTAINED_4 ) && Dungeon.hero.CON >= 19) {
			badge = Badge.CONSTITUTION_ATTAINED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.CONSTITUTION_ATTAINED_5 ) && Dungeon.hero.CON >= 21) {
			badge = Badge.CONSTITUTION_ATTAINED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateDexterityAttained() {
		Badge badge = null;

		if (!local.contains( Badge.DEXTERITY_ATTAINED_1 ) && Dungeon.hero.DEX >= 13) {
			badge = Badge.DEXTERITY_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.DEXTERITY_ATTAINED_2 ) && Dungeon.hero.DEX >= 15) {
			badge = Badge.DEXTERITY_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.DEXTERITY_ATTAINED_3 ) && Dungeon.hero.DEX >= 17) {
			badge = Badge.DEXTERITY_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.DEXTERITY_ATTAINED_4 ) && Dungeon.hero.DEX >= 19) {
			badge = Badge.DEXTERITY_ATTAINED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.DEXTERITY_ATTAINED_5 ) && Dungeon.hero.DEX >= 21) {
			badge = Badge.DEXTERITY_ATTAINED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateIntelligenceAttained() {
		Badge badge = null;

		if (!local.contains( Badge.INTELLIGENCE_ATTAINED_1 ) && Dungeon.hero.INT >= 13) {
			badge = Badge.INTELLIGENCE_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.INTELLIGENCE_ATTAINED_2 ) && Dungeon.hero.INT >= 15) {
			badge = Badge.INTELLIGENCE_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.INTELLIGENCE_ATTAINED_3 ) && Dungeon.hero.INT >= 17) {
			badge = Badge.INTELLIGENCE_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.INTELLIGENCE_ATTAINED_4 ) && Dungeon.hero.INT >= 19) {
			badge = Badge.INTELLIGENCE_ATTAINED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.INTELLIGENCE_ATTAINED_5 ) && Dungeon.hero.INT >= 21) {
			badge = Badge.INTELLIGENCE_ATTAINED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateWisdomAttained() {
		Badge badge = null;

		if (!local.contains( Badge.WISDOM_ATTAINED_1 ) && Dungeon.hero.WIS >= 13) {
			badge = Badge.WISDOM_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.WISDOM_ATTAINED_2 ) && Dungeon.hero.WIS >= 15) {
			badge = Badge.WISDOM_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.WISDOM_ATTAINED_3 ) && Dungeon.hero.WIS >= 17) {
			badge = Badge.WISDOM_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.WISDOM_ATTAINED_4 ) && Dungeon.hero.WIS >= 19) {
			badge = Badge.WISDOM_ATTAINED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.WISDOM_ATTAINED_5 ) && Dungeon.hero.WIS >= 21) {
			badge = Badge.WISDOM_ATTAINED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateCharismaAttained() {
		Badge badge = null;

		if (!local.contains( Badge.CHARISMA_ATTAINED_1 ) && Dungeon.hero.CHA >= 13) {
			badge = Badge.CHARISMA_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.CHARISMA_ATTAINED_2 ) && Dungeon.hero.CHA >= 15) {
			badge = Badge.CHARISMA_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.CHARISMA_ATTAINED_3 ) && Dungeon.hero.CHA >= 17) {
			badge = Badge.CHARISMA_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.CHARISMA_ATTAINED_4 ) && Dungeon.hero.CHA >= 19) {
			badge = Badge.CHARISMA_ATTAINED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.CHARISMA_ATTAINED_5 ) && Dungeon.hero.CHA >= 21) {
			badge = Badge.CHARISMA_ATTAINED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateFoodEaten() {
		Badge badge = null;

		if (!local.contains( Badge.FOOD_EATEN_1 ) && Statistics.foodEaten >= 10) {
			badge = Badge.FOOD_EATEN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_2 ) && Statistics.foodEaten >= 20) {
			badge = Badge.FOOD_EATEN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_3 ) && Statistics.foodEaten >= 30) {
			badge = Badge.FOOD_EATEN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_4 ) && Statistics.foodEaten >= 40) {
			badge = Badge.FOOD_EATEN_4;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_5 ) && Statistics.foodEaten >= 50) {
			badge = Badge.FOOD_EATEN_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validatePotionsCooked() {
		Badge badge = null;

		if (!local.contains( Badge.POTIONS_COOKED_1 ) && Statistics.potionsCooked >= 5) {
			badge = Badge.POTIONS_COOKED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_2 ) && Statistics.potionsCooked >= 10) {
			badge = Badge.POTIONS_COOKED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_3 ) && Statistics.potionsCooked >= 15) {
			badge = Badge.POTIONS_COOKED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_4 ) && Statistics.potionsCooked >= 20) {
			badge = Badge.POTIONS_COOKED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_5 ) && Statistics.potionsCooked >= 25) {
			badge = Badge.POTIONS_COOKED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validatePiranhasKilled() {
		Badge badge = null;

		if (!local.contains( Badge.PIRANHAS ) && Statistics.piranhasKilled >= 6) {
			badge = Badge.PIRANHAS;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateItemLevelAquired( Item item ) {

		// This method should be called:
		// 1) When an item is obtained (Item.collect)
		// 2) When an item is upgraded (ScrollOfUpgrade, ScrollOfWeaponUpgrade, ShortSword, WandOfMagicMissile)
		// 3) When an item is identified

		// Note that artifacts should never trigger this badge as they are alternatively upgraded
		if (!item.levelKnown || item instanceof Artifact) {
			return;
		}

		Badge badge = null;
		if (!local.contains( Badge.ITEM_LEVEL_1 ) && item.level() >= 3) {
			badge = Badge.ITEM_LEVEL_1;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_2 ) && item.level() >= 6) {
			badge = Badge.ITEM_LEVEL_2;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_3 ) && item.level() >= 9) {
			badge = Badge.ITEM_LEVEL_3;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_4 ) && item.level() >= 12) {
			badge = Badge.ITEM_LEVEL_4;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_5 ) && item.level() >= 15) {
			badge = Badge.ITEM_LEVEL_5;
			local.add( badge );
		}

		displayBadge( badge );
	}

	public static void validateAllBagsBought( Item bag ) {

		Badge badge = null;
		if (bag instanceof VelvetPouch) {
			badge = Badge.BAG_BOUGHT_VELVET_POUCH;
		} else if (bag instanceof ScrollHolder) {
			badge = Badge.BAG_BOUGHT_SCROLL_HOLDER;
		} else if (bag instanceof PotionBandolier) {
			badge = Badge.BAG_BOUGHT_POTION_BANDOLIER;
		} else if (bag instanceof MagicalHolster) {
			badge = Badge.BAG_BOUGHT_MAGICAL_HOLSTER;
		}

		if (badge != null) {

			local.add( badge );

			if (!local.contains( Badge.ALL_BAGS_BOUGHT ) &&
					local.contains( Badge.BAG_BOUGHT_VELVET_POUCH ) &&
					local.contains( Badge.BAG_BOUGHT_SCROLL_HOLDER ) &&
					local.contains( Badge.BAG_BOUGHT_POTION_BANDOLIER ) &&
					local.contains( Badge.BAG_BOUGHT_MAGICAL_HOLSTER )) {

				badge = Badge.ALL_BAGS_BOUGHT;
				local.add( badge );
				displayBadge( badge );
			}
		}
	}

	public static void validateItemsIdentified() {

		for (Catalog cat : Catalog.values()){
			if (cat.allSeen()){
				Badge b = Catalog.catalogBadges.get(cat);
				if (!global.contains(b)){
					displayBadge(b);
				}
			}
		}

		if (isUnlocked( Badge.ALL_WEAPONS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_ARMOR_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_WANDS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_RINGS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_ARTIFACTS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_POTIONS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_SCROLLS_IDENTIFIED )) {

			Badge badge = Badge.ALL_ITEMS_IDENTIFIED;
			if (!isUnlocked( badge )) {
				displayBadge( badge );
			}
		}
	}

	public static void validateDeathFromFire() {
		Badge badge = Badge.DEATH_FROM_FIRE;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromPoison() {
		Badge badge = Badge.DEATH_FROM_POISON;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromGas() {
		Badge badge = Badge.DEATH_FROM_GAS;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromHunger() {
		Badge badge = Badge.DEATH_FROM_HUNGER;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromGlyph() {
		Badge badge = Badge.DEATH_FROM_GLYPH;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	public static void validateDeathFromFalling() {
		Badge badge = Badge.DEATH_FROM_FALLING;
		local.add( badge );
		displayBadge( badge );

		validateYASD();
	}

	private static void validateYASD() {
		if (isUnlocked( Badge.DEATH_FROM_FIRE ) &&
				isUnlocked( Badge.DEATH_FROM_POISON ) &&
				isUnlocked( Badge.DEATH_FROM_GAS ) &&
				isUnlocked( Badge.DEATH_FROM_HUNGER) &&
				isUnlocked( Badge.DEATH_FROM_GLYPH) &&
				isUnlocked( Badge.DEATH_FROM_FALLING)) {

			Badge badge = Badge.YASD;
			if (!isUnlocked( badge )) {
				displayBadge( badge );
			}
		}
	}

	public static void validateBossSlain() {
		Badge badge = null;
		switch (Dungeon.depth) {
			case 5:
				badge = Badge.BOSS_SLAIN_1;
				break;
			case 10:
				badge = Badge.BOSS_SLAIN_2;
				break;
			case 15:
				badge = Badge.BOSS_SLAIN_3;
				break;
			case 20:
				badge = Badge.BOSS_SLAIN_4;
				break;
		}

		if (badge != null) {
			local.add( badge );
			displayBadge( badge );

			if (badge == Badge.BOSS_SLAIN_1) {
				switch (Dungeon.hero.heroClass) {
					case WARRIOR:
						badge = Badge.BOSS_SLAIN_1_WARRIOR;
						break;
					case MAGE:
						badge = Badge.BOSS_SLAIN_1_MAGE;
						break;
					case ROGUE:
						badge = Badge.BOSS_SLAIN_1_ROGUE;
						break;
					case HUNTRESS:
						badge = Badge.BOSS_SLAIN_1_HUNTRESS;
						break;
				}
				local.add( badge );
				addGlobal(badge);

				if (isUnlocked( Badge.BOSS_SLAIN_1_WARRIOR ) &&
						isUnlocked( Badge.BOSS_SLAIN_1_MAGE ) &&
						isUnlocked( Badge.BOSS_SLAIN_1_ROGUE ) &&
						isUnlocked( Badge.BOSS_SLAIN_1_HUNTRESS)) {

					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!isUnlocked( badge )) {
						displayBadge( badge );
					}
				}
			} else
			if (badge == Badge.BOSS_SLAIN_3) {
				switch (Dungeon.hero.subClass) {
					case GLADIATOR:
						badge = Badge.BOSS_SLAIN_3_GLADIATOR;
						break;
					case SHIELDGUARD:
						badge = Badge.BOSS_SLAIN_3_SHIELDGUARD;
						break;
					case WARLOCK:
						badge = Badge.BOSS_SLAIN_3_WARLOCK;
						break;
					case BATTLEMAGE:
						badge = Badge.BOSS_SLAIN_3_BATTLEMAGE;
						break;
					case FREERUNNER:
						badge = Badge.BOSS_SLAIN_3_FREERUNNER;
						break;
					case ASSASSIN:
						badge = Badge.BOSS_SLAIN_3_ASSASSIN;
						break;
					case SNIPER:
						badge = Badge.BOSS_SLAIN_3_SNIPER;
						break;
					case WARDEN:
						badge = Badge.BOSS_SLAIN_3_WARDEN;
						break;
					default:
						return;
				}
				local.add( badge );
				addGlobal(badge);

				if (isUnlocked( Badge.BOSS_SLAIN_3_GLADIATOR ) &&
						isUnlocked( Badge.BOSS_SLAIN_3_SHIELDGUARD ) &&
						isUnlocked( Badge.BOSS_SLAIN_3_WARLOCK ) &&
						isUnlocked( Badge.BOSS_SLAIN_3_BATTLEMAGE ) &&
						isUnlocked( Badge.BOSS_SLAIN_3_FREERUNNER ) &&
						isUnlocked( Badge.BOSS_SLAIN_3_ASSASSIN ) &&
						isUnlocked( Badge.BOSS_SLAIN_3_SNIPER ) &&
						isUnlocked( Badge.BOSS_SLAIN_3_WARDEN )) {

					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!isUnlocked( badge )) {
						displayBadge( badge );
					}
				}
			}
		}
	}

	public static void validateMastery() {

		Badge badge = null;
		switch (Dungeon.hero.heroClass) {
			case WARRIOR:
				badge = Badge.MASTERY_WARRIOR;
				break;
			case MAGE:
				badge = Badge.MASTERY_MAGE;
				break;
			case ROGUE:
				badge = Badge.MASTERY_ROGUE;
				break;
			case HUNTRESS:
				badge = Badge.MASTERY_HUNTRESS;
				break;
		}

		addGlobal(badge);
	}

	public static void validateRatmogrify(){
		addGlobal(Badge.FOUND_RATMOGRIFY);
	}

	public static void validateMageUnlock(){
		if (Statistics.upgradesUsed >= 1 && !isUnlocked(Badge.UNLOCK_MAGE)){
			displayBadge( Badge.UNLOCK_MAGE );
		}
	}

	public static void validateRogueUnlock(){
		if (Statistics.sneakAttacks >= 10 && !isUnlocked(Badge.UNLOCK_ROGUE)){
			displayBadge( Badge.UNLOCK_ROGUE );
		}
	}

	public static void validateHuntressUnlock(){
		if (Statistics.thrownAssists >= 10 && !isUnlocked(Badge.UNLOCK_HUNTRESS)){
			displayBadge( Badge.UNLOCK_HUNTRESS );
		}
	}

	public static void validateMasteryCombo( int n ) {
		if (!local.contains( Badge.MASTERY_COMBO ) && n == 10) {
			Badge badge = Badge.MASTERY_COMBO;
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateVictory() {

		Badge badge = Badge.VICTORY;
		displayBadge( badge );

		switch (Dungeon.hero.heroClass) {
			case WARRIOR:
				badge = Badge.VICTORY_WARRIOR;
				break;
			case MAGE:
				badge = Badge.VICTORY_MAGE;
				break;
			case ROGUE:
				badge = Badge.VICTORY_ROGUE;
				break;
			case HUNTRESS:
				badge = Badge.VICTORY_HUNTRESS;
				break;
		}
		local.add( badge );
		addGlobal(badge);

		if (isUnlocked( Badge.VICTORY_WARRIOR ) &&
				isUnlocked( Badge.VICTORY_MAGE ) &&
				isUnlocked( Badge.VICTORY_ROGUE ) &&
				isUnlocked( Badge.VICTORY_HUNTRESS )) {

			badge = Badge.VICTORY_ALL_CLASSES;
			displayBadge( badge );
		}
	}

	public static void validateNoKilling() {
		if (!local.contains( Badge.NO_MONSTERS_SLAIN ) && Statistics.completedWithNoKilling) {
			Badge badge = Badge.NO_MONSTERS_SLAIN;
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateGrimWeapon() {
		if (!local.contains( Badge.GRIM_WEAPON )) {
			Badge badge = Badge.GRIM_WEAPON;
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateGamesPlayed() {
		Badge badge = null;
		if (Rankings.INSTANCE.totalNumber >= 10 || Rankings.INSTANCE.wonNumber >= 1) {
			badge = Badge.GAMES_PLAYED_1;
		}
		if (Rankings.INSTANCE.totalNumber >= 50 || Rankings.INSTANCE.wonNumber >= 5) {
			badge = Badge.GAMES_PLAYED_2;
		}
		if (Rankings.INSTANCE.totalNumber >= 100 || Rankings.INSTANCE.wonNumber >= 10) {
			badge = Badge.GAMES_PLAYED_3;
		}
		if (Rankings.INSTANCE.totalNumber >= 250 || Rankings.INSTANCE.wonNumber >= 25) {
			badge = Badge.GAMES_PLAYED_4;
		}
		if (Rankings.INSTANCE.totalNumber >= 1000 || Rankings.INSTANCE.wonNumber >= 100) {
			badge = Badge.GAMES_PLAYED_5;
		}

		displayBadge( badge );
	}

	//necessary in order to display the happy end badge in the surface scene
	public static void silentValidateHappyEnd() {
		if (!local.contains( Badge.HAPPY_END )){
			local.add( Badge.HAPPY_END );
		}
	}

	public static void validateHappyEnd() {
		displayBadge( Badge.HAPPY_END );
	}

	public static void validateChampion( int challenges ) {
		if (challenges == 0) return;
		Badge badge = null;
		if (challenges >= 1) {
			badge = Badge.CHAMPION_1;
		}
		if (challenges >= 3){
			addGlobal(badge);
			badge = Badge.CHAMPION_2;
		}
		if (challenges >= 6){
			addGlobal(badge);
			badge = Badge.CHAMPION_3;
		}
		local.add(badge);
		displayBadge( badge );
	}

	private static void displayBadge( Badge badge ) {

		if (badge == null) {
			return;
		}

		if (global.contains( badge )) {

			if (!badge.meta) {
				GLog.h( Messages.get(Badges.class, "endorsed", badge.title()) );
				GLog.newLine();
			}

		} else {

			global.add( badge );
			saveNeeded = true;

			GLog.h( Messages.get(Badges.class, "new", badge.title() + " (" + badge.desc() + ")") );
			GLog.newLine();
			PixelScene.showBadge( badge );
		}
	}

	public static boolean isUnlocked( Badge badge ) {
		return global.contains( badge );
	}

	public static HashSet<Badge> allUnlocked(){
		loadGlobal();
		return new HashSet<>(global);
	}

	public static void disown( Badge badge ) {
		loadGlobal();
		global.remove( badge );
		saveNeeded = true;
	}

	public static void addGlobal( Badge badge ){
		if (!global.contains(badge)){
			global.add( badge );
			saveNeeded = true;
		}
	}

	public static List<Badge> filterReplacedBadges( boolean global ) {

		ArrayList<Badge> badges = new ArrayList<>(global ? Badges.global : Badges.local);

		Iterator<Badge> iterator = badges.iterator();
		while (iterator.hasNext()) {
			Badge badge = iterator.next();
			if ((!global && badge.meta) || badge.image == -1) {
				iterator.remove();
			}
		}

		Collections.sort(badges);

		return filterReplacedBadges(badges);

	}

	private static final Badge[][] tierBadgeReplacements = new Badge[][]{
			{Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4, Badge.MONSTERS_SLAIN_5},
			{Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4, Badge.GOLD_COLLECTED_5},
			{Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4, Badge.ITEM_LEVEL_5},
			{Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4, Badge.LEVEL_REACHED_5, Badge.LEVEL_REACHED_6},
			{Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4, Badge.STRENGTH_ATTAINED_5},
			{Badge.CONSTITUTION_ATTAINED_1, Badge.CONSTITUTION_ATTAINED_2, Badge.CONSTITUTION_ATTAINED_3, Badge.CONSTITUTION_ATTAINED_4, Badge.CONSTITUTION_ATTAINED_5},
			{Badge.DEXTERITY_ATTAINED_2, Badge.DEXTERITY_ATTAINED_2, Badge.DEXTERITY_ATTAINED_3, Badge.DEXTERITY_ATTAINED_4, Badge.DEXTERITY_ATTAINED_5},
			{Badge.INTELLIGENCE_ATTAINED_1, Badge.INTELLIGENCE_ATTAINED_2, Badge.INTELLIGENCE_ATTAINED_3, Badge.INTELLIGENCE_ATTAINED_4, Badge.INTELLIGENCE_ATTAINED_5},
			{Badge.WISDOM_ATTAINED_1, Badge.WISDOM_ATTAINED_2, Badge.WISDOM_ATTAINED_3, Badge.WISDOM_ATTAINED_4, Badge.WISDOM_ATTAINED_5},
			{Badge.CHARISMA_ATTAINED_1, Badge.CHARISMA_ATTAINED_2, Badge.CHARISMA_ATTAINED_3, Badge.CHARISMA_ATTAINED_4, Badge.CHARISMA_ATTAINED_5},
			{Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4, Badge.FOOD_EATEN_5},
			{Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4, Badge.POTIONS_COOKED_5},
			{Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4},
			{Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4, Badge.GAMES_PLAYED_5},
			{Badge.CHAMPION_1, Badge.CHAMPION_2, Badge.CHAMPION_3}
	};

	private static final Badge[][] metaBadgeReplacements = new Badge[][]{
			{Badge.DEATH_FROM_FIRE, Badge.YASD},
			{Badge.DEATH_FROM_GAS, Badge.YASD},
			{Badge.DEATH_FROM_HUNGER, Badge.YASD},
			{Badge.DEATH_FROM_POISON, Badge.YASD},
			{Badge.DEATH_FROM_GLYPH, Badge.YASD},
			{Badge.DEATH_FROM_FALLING, Badge.YASD },

			{Badge.ALL_WEAPONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARMOR_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_WANDS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARTIFACTS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED}
	};

	public static List<Badge> filterReplacedBadges( List<Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveBest( badges, tierReplace );
		}

		for (Badge[] metaReplace : metaBadgeReplacements){
			leaveBest( badges, metaReplace );
		}

		return badges;
	}

	private static void leaveBest( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static List<Badge> filterHigherIncrementalBadges(List<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveWorst( badges, tierReplace );
		}

		Collections.sort( badges );

		return badges;
	}

	private static void leaveWorst( Collection<Badge> list, Badge...badges ) {
		for (int i=0; i < badges.length; i++) {
			if (list.contains( badges[i])) {
				for (int j=i+1; j < badges.length; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static Collection<Badge> addReplacedBadges(Collection<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			addLower( badges, tierReplace );
		}

		for (Badge[] metaReplace : metaBadgeReplacements){
			addLower( badges, metaReplace );
		}

		return badges;
	}

	private static void addLower( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.add( badges[j] );
				}
				break;
			}
		}
	}
}
