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

package com.tianscar.carbonizedpixeldungeon.items;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.items.armor.Armor;
import com.tianscar.carbonizedpixeldungeon.items.armor.ClothArmor;
import com.tianscar.carbonizedpixeldungeon.items.armor.LeatherArmor;
import com.tianscar.carbonizedpixeldungeon.items.armor.MailArmor;
import com.tianscar.carbonizedpixeldungeon.items.armor.PlateArmor;
import com.tianscar.carbonizedpixeldungeon.items.armor.ScaleArmor;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.Artifact;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.CapeOfThorns;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.CloakOfShadows;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.DriedRose;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.EtherealChains;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.HornOfPlenty;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.LloydsBeacon;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.MasterThievesArmband;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.SandalsOfNature;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.TalismanOfForesight;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.UnstableSpellbook;
import com.tianscar.carbonizedpixeldungeon.items.food.Food;
import com.tianscar.carbonizedpixeldungeon.items.food.MysteryMeat;
import com.tianscar.carbonizedpixeldungeon.items.food.Pasty;
import com.tianscar.carbonizedpixeldungeon.items.potions.Potion;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfExperience;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfFrost;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfHaste;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfHealing;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfInvisibility;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfLevitation;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfMindVision;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfParalyticGas;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfPurity;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfStrength;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfToxicGas;
import com.tianscar.carbonizedpixeldungeon.items.rings.Ring;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfAccuracy;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfElements;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfEnergy;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfEvasion;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfForce;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfFuror;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfHaste;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfMight;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfSharpshooting;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfTenacity;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfWealth;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.Scroll;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRage;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTerror;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.tianscar.carbonizedpixeldungeon.items.stones.Runestone;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfAggression;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfAugmentation;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfBlast;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfBlink;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfClairvoyance;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfDeepSleep;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfDisarming;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfEnchantment;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfFear;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfFlock;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfIntuition;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfShock;
import com.tianscar.carbonizedpixeldungeon.items.wands.Wand;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfBlastWave;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfCorrosion;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfCorruption;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfDisintegration;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfFireblast;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfFrost;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfLightning;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfLivingEarth;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfMagicMissile;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfPrismaticLight;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfRegrowth;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfTransfusion;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfWarding;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.AssassinsBlade;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.BattleAxe;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Dagger;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Dirk;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Flail;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Gauntlet;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Glaive;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Gloves;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.GoldenSword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Greataxe;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Greatshield;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Greatsword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.HandAxe;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Longsword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Mace;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Quarterstaff;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.RoundShield;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.RunicBlade;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Sai;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Scimitar;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Shortsword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Spear;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Sword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.WarHammer;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Whip;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.WornShortsword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.ranged.Crossbow;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.ranged.LightCrossbow;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.Bolas;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.FishingSpear;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.ForceCube;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.Javelin;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.Kunai;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.Shuriken;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.ThrowingClub;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.ThrowingHammer;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.Tomahawk;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.Trident;
import com.tianscar.carbonizedpixeldungeon.plants.Blindweed;
import com.tianscar.carbonizedpixeldungeon.plants.Dreamfoil;
import com.tianscar.carbonizedpixeldungeon.plants.Earthroot;
import com.tianscar.carbonizedpixeldungeon.plants.Fadeleaf;
import com.tianscar.carbonizedpixeldungeon.plants.Firebloom;
import com.tianscar.carbonizedpixeldungeon.plants.Icecap;
import com.tianscar.carbonizedpixeldungeon.plants.Plant;
import com.tianscar.carbonizedpixeldungeon.plants.Rotberry;
import com.tianscar.carbonizedpixeldungeon.plants.Sorrowmoss;
import com.tianscar.carbonizedpixeldungeon.plants.Starflower;
import com.tianscar.carbonizedpixeldungeon.plants.Stormvine;
import com.tianscar.carbonizedpixeldungeon.plants.Sungrass;
import com.tianscar.carbonizedpixeldungeon.plants.Swiftthistle;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.GameMath;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.utils.Reflection;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 4,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		WEP_T4	( 0,    MeleeWeapon.class),
		WEP_T5	( 0,    MeleeWeapon.class),
		
		ARMOR	( 3,    Armor.class ),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		WAND	( 2,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		FOOD	( 0,    Food.class ),
		
		POTION	( 16,   Potion.class ),
		SEED	( 2,    Plant.Seed.class ),
		
		SCROLL	( 16,   Scroll.class ),
		STONE   ( 2,    Runestone.class),
		
		GOLD	( 20,   Gold.class );
		
		public Class<?>[] classes;

		//some item types use a deck-based system, where the probs decrement as items are picked
		// until they are all 0, and then they reset. Those generator classes should define
		// defaultProbs. If defaultProbs is null then a deck system isn't used.
		//Artifacts in particular don't reset, no duplicates!
		public float[] probs;
		public float[] defaultProbs = null;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}

			//items without a category-defined order are sorted based on the spritesheet
			return Short.MAX_VALUE+item.image();
		}

		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };
			
			POTION.classes = new Class<?>[]{
					PotionOfStrength.class, //2 drop every chapter, see Dungeon.posNeeded()
					PotionOfHealing.class,
					PotionOfMindVision.class,
					PotionOfFrost.class,
					PotionOfLiquidFlame.class,
					PotionOfToxicGas.class,
					PotionOfHaste.class,
					PotionOfInvisibility.class,
					PotionOfLevitation.class,
					PotionOfParalyticGas.class,
					PotionOfPurity.class,
					PotionOfExperience.class};
			POTION.defaultProbs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			POTION.probs = POTION.defaultProbs.clone();
			
			SEED.classes = new Class<?>[]{
					Rotberry.Seed.class, //quest item
					Sungrass.Seed.class,
					Fadeleaf.Seed.class,
					Icecap.Seed.class,
					Firebloom.Seed.class,
					Sorrowmoss.Seed.class,
					Swiftthistle.Seed.class,
					Blindweed.Seed.class,
					Stormvine.Seed.class,
					Earthroot.Seed.class,
					Dreamfoil.Seed.class,
					Starflower.Seed.class};
			SEED.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2 };
			SEED.probs = SEED.defaultProbs.clone();
			
			SCROLL.classes = new Class<?>[]{
					ScrollOfUpgrade.class, //3 drop every chapter, see Dungeon.souNeeded()
					ScrollOfIdentify.class,
					ScrollOfRemoveCurse.class,
					ScrollOfMirrorImage.class,
					ScrollOfRecharging.class,
					ScrollOfTeleportation.class,
					ScrollOfLullaby.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfRetribution.class,
					ScrollOfTerror.class,
					ScrollOfTransmutation.class
			};
			SCROLL.defaultProbs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			SCROLL.probs = SCROLL.defaultProbs.clone();
			
			STONE.classes = new Class<?>[]{
					StoneOfEnchantment.class,   //1 is guaranteed to drop on floors 6-19
					StoneOfIntuition.class,     //1 additional stone is also dropped on floors 1-3
					StoneOfDisarming.class,
					StoneOfFlock.class,
					StoneOfShock.class,
					StoneOfBlink.class,
					StoneOfDeepSleep.class,
					StoneOfClairvoyance.class,
					StoneOfAggression.class,
					StoneOfBlast.class,
					StoneOfFear.class,
					StoneOfAugmentation.class  //1 is sold in each shop
			};
			STONE.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0 };
			STONE.probs = STONE.defaultProbs.clone();

			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class };
			WAND.probs = new float[]{ 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					WornShortsword.class,
					Gloves.class,
					Dagger.class,
					MagesStaff.class
			};
			WEP_T1.probs = new float[]{ 1, 1, 1, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					Shortsword.class,
					HandAxe.class,
					Spear.class,
					Quarterstaff.class,
					Dirk.class,
					LightCrossbow.class
			};
			WEP_T2.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T3.classes = new Class<?>[]{
					Sword.class,
					Mace.class,
					Scimitar.class,
					RoundShield.class,
					Sai.class,
					Whip.class
			};
			WEP_T3.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T4.classes = new Class<?>[]{
					Longsword.class,
					BattleAxe.class,
					Flail.class,
					RunicBlade.class,
					AssassinsBlade.class,
					Crossbow.class
			};
			WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T5.classes = new Class<?>[]{
					Greatsword.class,
					WarHammer.class,
					Glaive.class,
					Greataxe.class,
					Greatshield.class,
					Gauntlet.class,
					GoldenSword.class
			};
			WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4, 4, 0 };
			
			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					ThrowingStone.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 6, 5 };
			
			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					ThrowingClub.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 6, 5, 4 };
			
			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Kunai.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 6, 5, 4 };
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class,
					HeavyBoomerang.class
			};
			MIS_T4.probs = new float[]{ 6, 5, 4 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class,
					ForceCube.class
			};
			MIS_T5.probs = new float[]{ 6, 5, 4 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };
			
			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfForce.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class,
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.defaultProbs = new float[]{ 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1};
			ARTIFACT.probs = ARTIFACT.defaultProbs.clone();
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 75, 20,  4,  1},
			{0, 25, 50, 20,  5},
			{0,  0, 40, 50, 10},
			{0,  0, 20, 40, 40},
			{0,  0,  0, 20, 80}
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();

	public static void fullReset() {
		generalReset();
		for (Category cat : Category.values()) {
			reset(cat);
		}
	}

	public static void generalReset(){
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}

	public static void reset(Category cat){
		if (cat.defaultProbs != null) cat.probs = cat.defaultProbs.clone();
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			generalReset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case MISSILE:
				return randomMissile();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				int i = Random.chances(cat.probs);
				if (i == -1) {
					reset(cat);
					i = Random.chances(cat.probs);
				}
				if (cat.defaultProbs != null) cat.probs[i]--;
				return ((Item) Reflection.newInstance(cat.classes[i])).random();
		}
	}

	//overrides any deck systems and always uses default probs
	public static Item randomUsingDefaults( Category cat ){
		if (cat.defaultProbs == null) {
			return random(cat); //currently covers weapons/armor/missiles
		} else {
			return ((Item) Reflection.newInstance(cat.classes[Random.chances(cat.defaultProbs)])).random();
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		return Reflection.newInstance(cl).random();
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Armor a = (Armor)Reflection.newInstance(Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])]);
		a.random();
		return a;
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 5);
	}
	
	public static MeleeWeapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
		MeleeWeapon w = (MeleeWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}
	
	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};
	
	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}
	
	public static MissileWeapon randomMissile(int floorSet) {
		
		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
		MissileWeapon w = (MissileWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		Category cat = Category.ARTIFACT;
		int i = Random.chances( cat.probs );

		//if no artifacts are left, return null
		if (i == -1){
			return null;
		}

		cat.probs[i]--;
		return (Artifact) Reflection.newInstance((Class<? extends Artifact>) cat.classes[i]).random();

	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++){
			if (cat.classes[i].equals(artifact) && cat.probs[i] > 0) {
				cat.probs[i] = 0;
				return true;
			}
		}
		return false;
	}

	private static final String GENERAL_PROBS = "general_probs";
	private static final String CATEGORY_PROBS = "_probs";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);

		for (Category cat : Category.values()){
			if (cat.defaultProbs == null) continue;
			boolean needsStore = false;
			for (int i = 0; i < cat.probs.length; i++){
				if (cat.probs[i] != cat.defaultProbs[i]){
					needsStore = true;
					break;
				}
			}

			if (needsStore){
				bundle.put(cat.name().toLowerCase() + CATEGORY_PROBS, cat.probs);
			}
		}
	}

	public static void restoreFromBundle(Bundle bundle) {
		fullReset();

		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		}

		for (Category cat : Category.values()){
			if (bundle.contains(cat.name().toLowerCase() + CATEGORY_PROBS)){
				float[] probs = bundle.getFloatArray(cat.name().toLowerCase() + CATEGORY_PROBS);
				if (cat.defaultProbs != null && probs.length == cat.defaultProbs.length){
					cat.probs = probs;
				}
			}
		}

		//pre-0.8.1
		if (bundle.contains("spawned_artifacts")) {
			for (Class<? extends Artifact> artifact : bundle.getClassArray("spawned_artifacts")) {
				Category cat = Category.ARTIFACT;
				for (int i = 0; i < cat.classes.length; i++) {
					if (cat.classes[i].equals(artifact)) {
						cat.probs[i] = 0;
					}
				}
			}
		}
		
	}
}
