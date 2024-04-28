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

package com.tianscar.carbonizedpixeldungeon.actors.hero;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Badges;
import com.tianscar.carbonizedpixeldungeon.Challenges;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.QuickSlot;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.elementalist.AetherBlink;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.elementalist.Resonance;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.tianscar.carbonizedpixeldungeon.items.Amulet;
import com.tianscar.carbonizedpixeldungeon.items.BrokenSeal;
import com.tianscar.carbonizedpixeldungeon.items.CarbonSteel;
import com.tianscar.carbonizedpixeldungeon.items.Honeypot;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.KingsCrown;
import com.tianscar.carbonizedpixeldungeon.items.MerchantsBeacon;
import com.tianscar.carbonizedpixeldungeon.items.Stylus;
import com.tianscar.carbonizedpixeldungeon.items.TengusMask;
import com.tianscar.carbonizedpixeldungeon.items.Waterskin;
import com.tianscar.carbonizedpixeldungeon.items.armor.ClothArmor;
import com.tianscar.carbonizedpixeldungeon.items.armor.PlateArmor;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.CloakOfShadows;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.EtherealChains;
import com.tianscar.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.tianscar.carbonizedpixeldungeon.items.bags.PotionBandolier;
import com.tianscar.carbonizedpixeldungeon.items.bags.ScrollHolder;
import com.tianscar.carbonizedpixeldungeon.items.bags.VelvetPouch;
import com.tianscar.carbonizedpixeldungeon.items.food.Food;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfExperience;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfHealing;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfInvisibility;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfMindVision;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfStrength;
import com.tianscar.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.tianscar.carbonizedpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfForce;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfMight;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRage;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfAffection;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfEnchantment;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfPetrification;
import com.tianscar.carbonizedpixeldungeon.items.spells.CurseInfusion;
import com.tianscar.carbonizedpixeldungeon.items.spells.ElementalHeart;
import com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfEnchantment;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfBlastWave;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfDisintegration;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfFireblast;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfLightning;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfMagicMissile;
import com.tianscar.carbonizedpixeldungeon.items.weapon.SpiritBow;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Buckler;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Dagger;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Gloves;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.GoldenSword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Greatsword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Knuckles;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.WornShortsword;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.FishingSpear;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;

public enum HeroClass {

	WARRIOR( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( HeroSubClass.SNIPER, HeroSubClass.WARDEN ),
	ARCHER( HeroSubClass.NONE, HeroSubClass.NONE ),
	LANCER( HeroSubClass.NONE, HeroSubClass.NONE ),
	BARD( HeroSubClass.NONE, HeroSubClass.NONE ),
	BARBARIAN( HeroSubClass.NONE, HeroSubClass.NONE ),
	ELEMENTALIST( HeroSubClass.BINDER, HeroSubClass.SPELLWEAVER ),
	ALCHEMIST( HeroSubClass.NONE, HeroSubClass.NONE ),
	BLACKSMITH( HeroSubClass.NONE, HeroSubClass.NONE ),
	WARLOCK( HeroSubClass.NONE, HeroSubClass.NONE );

	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor) i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		Waterskin waterskin = new Waterskin();
		waterskin.collect();

		new ScrollOfIdentify().identify();

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case ELEMENTALIST:
				initElementalist( hero );
				break;
		}

		//TESTPLAY ONLY
		if (Game.platform.isDebug()) {
			new TengusMask().collect();
			new KingsCrown().collect();

			new ScrollHolder().collect();
			new PotionBandolier().collect();
			new MagicalHolster().collect();

			new ScrollOfUpgrade().identify().quantity(20).collect();
			new ScrollOfRemoveCurse().identify().quantity(20).collect();
			new ScrollOfMysticalEnergy().identify().quantity(20).collect();
			new ScrollOfAntiMagic().identify().quantity(20).collect();
			new ScrollOfMagicMapping().identify().quantity(20).collect();
			new ScrollOfPetrification().identify().quantity(20).collect();

			new CurseInfusion().quantity(20).collect();
			new StoneOfEnchantment().quantity(20).collect();
			new Stylus().quantity(20).collect();

			new PotionOfStrength().identify().quantity(30).collect();
			new PotionOfExperience().identify().quantity(30).collect();
			new PotionOfHealing().identify().quantity(30).collect();
			new PotionOfInvisibility().identify().quantity(30).collect();
			new Food().identify().quantity(30).collect();

			new PlateArmor().identify().upgrade(8).collect();
			new Greatsword().identify().upgrade(8).collect();
			new WandOfBlastWave().identify().upgrade(8).collect();
			new WandOfFireblast().identify().upgrade(8).collect();
			new WandOfLightning().identify().upgrade(8).collect();
			new WandOfDisintegration().identify().upgrade(8).collect();
			new FishingSpear().quantity(30).collect();
			new RingOfMight().identify().upgrade(8).collect();
			new RingOfForce().identify().upgrade(8).collect();
			new EtherealChains().identify().collect();
			new ChaliceOfBlood().identify().collect();

			new Honeypot().collect();
			new Honeypot().collect();
			new Honeypot().collect();
			new Honeypot().collect();
			new ElixirOfHoneyedHealing().quantity(4).collect();

			new Amulet().collect();
			new ScrollOfAffection().identify().quantity(5).collect();

			new CarbonSteel().quantity(30).collect();

			new GoldenSword().identify().upgrade(8).collect();

			new MerchantsBeacon().quantity(10).collect();
			new PotionOfDivineInspiration().quantity(10).collect();
			new ScrollOfEnchantment().identify().quantity(10).collect();
		}

		for (int s = 0; s < QuickSlot.SIZE; s++) {
			if (Dungeon.quickslot.getItem(s) == null) {
				Dungeon.quickslot.setSlot(s, waterskin);
				break;
			}
		}

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
			case ELEMENTALIST:
				return Badges.Badge.MASTERY_ELEMENTALIST;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		(hero.belongings.extra = new Buckler()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}

	private static void initElementalist( Hero hero ) {

		(hero.belongings.weapon = new Knuckles()).identify();
		ElementalHeart heart = new ElementalHeart();
		heart.identify().collect();

		Dungeon.quickslot.setSlot(0, heart);

		new PotionOfExperience().identify();
		new ScrollOfTeleportation().identify();
	}

	public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public HeroSubClass[] subClasses() {
		return subClasses;
	}

	public ArmorAbility[] armorAbilities() {
		switch (this) {
			case WARRIOR: default:
				return new ArmorAbility[] { new HeroicLeap(), new Shockwave(), new Endure() };
			case MAGE:
				return new ArmorAbility[] { new ElementalBlast(), new WildMagic(), new WarpBeacon() };
			case ROGUE:
				return new ArmorAbility[] { new SmokeBomb(), new DeathMark(), new ShadowClone() };
			case HUNTRESS:
				return new ArmorAbility[] { new SpectralBlades(), new NaturesPower(), new SpiritHawk() };
			case ELEMENTALIST:
				return new ArmorAbility[] { new Resonance(), new AetherBlink()/*, new ElementalConduit() */};
		}
	}

	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HUNTRESS;
			case ELEMENTALIST:
				return Assets.Sprites.ELEMENTALIST;
		}
	}
	
	public String[] perks() {
		switch (this) {
			case WARRIOR: default:
				return new String[]{
						Messages.get(HeroClass.class, "warrior_perk1"),
						Messages.get(HeroClass.class, "warrior_perk2"),
						Messages.get(HeroClass.class, "warrior_perk3"),
						Messages.get(HeroClass.class, "warrior_perk4"),
						Messages.get(HeroClass.class, "warrior_perk5"),
				};
			case MAGE:
				return new String[]{
						Messages.get(HeroClass.class, "mage_perk1"),
						Messages.get(HeroClass.class, "mage_perk2"),
						Messages.get(HeroClass.class, "mage_perk3"),
						Messages.get(HeroClass.class, "mage_perk4"),
						Messages.get(HeroClass.class, "mage_perk5"),
				};
			case ROGUE:
				return new String[]{
						Messages.get(HeroClass.class, "rogue_perk1"),
						Messages.get(HeroClass.class, "rogue_perk2"),
						Messages.get(HeroClass.class, "rogue_perk3"),
						Messages.get(HeroClass.class, "rogue_perk4"),
						Messages.get(HeroClass.class, "rogue_perk5"),
				};
			case HUNTRESS:
				return new String[]{
						Messages.get(HeroClass.class, "huntress_perk1"),
						Messages.get(HeroClass.class, "huntress_perk2"),
						Messages.get(HeroClass.class, "huntress_perk3"),
						Messages.get(HeroClass.class, "huntress_perk4"),
						Messages.get(HeroClass.class, "huntress_perk5"),
				};
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (Game.platform.isDebug()) return true;
		
		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
			case ELEMENTALIST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ELEMENTALIST);
		}
	}
	
	public String unlockMsg() {
		switch (this){
			case WARRIOR: default:
				return "";
			case MAGE:
				return Messages.get(HeroClass.class, "mage_unlock");
			case ROGUE:
				return Messages.get(HeroClass.class, "rogue_unlock");
			case HUNTRESS:
				return Messages.get(HeroClass.class, "huntress_unlock");
			case ELEMENTALIST:
				return Messages.get(HeroClass.class, "elementalist_unlock");
		}
	}

}
