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

package com.ansdoship.carbonizedpixeldungeon.actors.hero;

import com.ansdoship.carbonizedpixeldungeon.*;
import com.ansdoship.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.Alchemy;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.*;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Monk;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Snake;
import com.ansdoship.carbonizedpixeldungeon.effects.CheckedCell;
import com.ansdoship.carbonizedpixeldungeon.effects.SpellSprite;
import com.ansdoship.carbonizedpixeldungeon.items.Amulet;
import com.ansdoship.carbonizedpixeldungeon.items.Ankh;
import com.ansdoship.carbonizedpixeldungeon.items.Dewdrop;
import com.ansdoship.carbonizedpixeldungeon.items.EquipableItem;
import com.ansdoship.carbonizedpixeldungeon.items.Heap;
import com.ansdoship.carbonizedpixeldungeon.items.Heap.Type;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.KindOfWeapon;
import com.ansdoship.carbonizedpixeldungeon.items.armor.ClassArmor;
import com.ansdoship.carbonizedpixeldungeon.items.armor.glyphs.AntiMagic;
import com.ansdoship.carbonizedpixeldungeon.items.armor.glyphs.Brimstone;
import com.ansdoship.carbonizedpixeldungeon.items.armor.glyphs.Viscosity;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.*;
import com.ansdoship.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.ansdoship.carbonizedpixeldungeon.items.journal.Guidebook;
import com.ansdoship.carbonizedpixeldungeon.items.keys.CrystalKey;
import com.ansdoship.carbonizedpixeldungeon.items.keys.GoldenKey;
import com.ansdoship.carbonizedpixeldungeon.items.keys.IronKey;
import com.ansdoship.carbonizedpixeldungeon.items.keys.Key;
import com.ansdoship.carbonizedpixeldungeon.items.keys.SkeletonKey;
import com.ansdoship.carbonizedpixeldungeon.items.potions.Potion;
import com.ansdoship.carbonizedpixeldungeon.items.potions.PotionOfExperience;
import com.ansdoship.carbonizedpixeldungeon.items.potions.PotionOfHealing;
import com.ansdoship.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.ansdoship.carbonizedpixeldungeon.items.potions.exotic.PotionOfDivineInspiration;
import com.ansdoship.carbonizedpixeldungeon.items.rings.RingOfAccuracy;
import com.ansdoship.carbonizedpixeldungeon.items.rings.RingOfEvasion;
import com.ansdoship.carbonizedpixeldungeon.items.rings.RingOfForce;
import com.ansdoship.carbonizedpixeldungeon.items.rings.RingOfFuror;
import com.ansdoship.carbonizedpixeldungeon.items.rings.RingOfHaste;
import com.ansdoship.carbonizedpixeldungeon.items.rings.RingOfMight;
import com.ansdoship.carbonizedpixeldungeon.items.rings.RingOfTenacity;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.Scroll;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.ansdoship.carbonizedpixeldungeon.items.wands.Wand;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfLivingEarth;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.SpiritBow;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.Weapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.ansdoship.carbonizedpixeldungeon.journal.Document;
import com.ansdoship.carbonizedpixeldungeon.journal.Notes;
import com.ansdoship.carbonizedpixeldungeon.levels.Level;
import com.ansdoship.carbonizedpixeldungeon.levels.Terrain;
import com.ansdoship.carbonizedpixeldungeon.levels.features.Chasm;
import com.ansdoship.carbonizedpixeldungeon.levels.traps.Trap;
import com.ansdoship.carbonizedpixeldungeon.mechanics.ShadowCaster;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.plants.Earthroot;
import com.ansdoship.carbonizedpixeldungeon.plants.Swiftthistle;
import com.ansdoship.carbonizedpixeldungeon.scenes.AlchemyScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.InterlevelScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.SurfaceScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.CharSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.HeroSprite;
import com.ansdoship.carbonizedpixeldungeon.ui.AttackIndicator;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.carbonizedpixeldungeon.ui.QuickSlotButton;
import com.ansdoship.carbonizedpixeldungeon.ui.StatusPane;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndHero;
import com.ansdoship.carbonizedpixeldungeon.windows.WndMessage;
import com.ansdoship.carbonizedpixeldungeon.windows.WndResurrect;
import com.ansdoship.carbonizedpixeldungeon.windows.WndTradeItem;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Callback;
import com.ansdoship.pixeldungeonclasses.utils.GameMath;
import com.ansdoship.pixeldungeonclasses.utils.PathFinder;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Hero extends Char {

	{
		actPriority = HERO_PRIO;

		alignment = Alignment.ALLY;
	}

	public static final int MAX_LEVEL = 40;

	public static final int MAX_HUNGER = 450;

	public static final int STARTING_STR = 10;
	public static final int STARTING_CON = 10;
	public static final int STARTING_DEX = 10;
	public static final int STARTING_INT = 10;
	public static final int STARTING_WIS = 10;
	public static final int STARTING_CHA = 10;

	private static final float TIME_TO_REST		    = 1f;
	private static final float TIME_TO_SEARCH	    = 2f;
	private static final float HUNGER_FOR_SEARCH	= 6f;

	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;
	public ArmorAbility armorAbility = null;
	public ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();

	private int attackSkill = 10;
	private int defenseSkill = 5;

	private boolean canWep1Attack = false;
	private boolean canWep2Attack = false;

	public boolean ready = false;
	private boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;

	public boolean resting = false;

	public Belongings belongings;

	public int STR;
	public int CON;
	public int DEX;
	public int INT;
	public int WIS;
	public int CHA;

	public float awareness;

	public int lvl = 1;
	public int exp = 0;

	public int HTBoost = 0;

	public int hunger;

	private ArrayList<Mob> visibleEnemies;

	//This list is maintained so that some logic checks can be skipped
	// for enemies we know we aren't seeing normally, resultign in better performance
	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();

	public Hero() {
		super();

		HP = HT = 20;
		hunger = 0;
		STR = STARTING_STR;
		CON = STARTING_CON;
		DEX = STARTING_DEX;
		INT = STARTING_INT;
		WIS = STARTING_WIS;
		CHA = STARTING_CHA;

		belongings = new Belongings( this );

		visibleEnemies = new ArrayList<>();
	}

	public final void updateHT( boolean boostHP ) {
		updateHT(boostHP, false);
	}

	public void updateHT( boolean boostHP, boolean conBoostHP ){
		int curHT = HT;

		HT = 20 + 5*(lvl-1) + HTBoost;
		float conMultiplier = 0.1f * CON();
		HT = Math.round(conMultiplier * HT);
		float ringMultiplier = RingOfMight.HTMultiplier(this);
		HT = Math.round(ringMultiplier * HT);

		if (buff(ElixirOfMight.HTBoost.class) != null){
			HT += buff(ElixirOfMight.HTBoost.class).boost();
		}

		if (boostHP) {
			HP += Math.max(HT - curHT, 0);
		}
		else if (conBoostHP) {
			HP += Math.max(Math.round(conMultiplier * HP) - HP, 0);
		}
		HP = Math.min(HP, HT);
	}

	public int hunger() {
		if (isAlive()) {
			Hunger hungerBuff = Dungeon.hero.buff(Hunger.class);
			hunger = hungerBuff == null ? MAX_HUNGER : MAX_HUNGER - hungerBuff.hunger();
		}
		if (hunger < 0) hunger = 0;
		return hunger;
	}

	public int STR() {
		int strBonus = 0;

		strBonus += RingOfMight.strengthBonus( this );

		AdrenalineSurge buff = buff(AdrenalineSurge.class);
		if (buff != null){
			strBonus += buff.boost();
		}

		if (hasTalent(Talent.STRONGMAN)){
			strBonus += (int)Math.floor(STR * (0.05f + 0.05f*pointsInTalent(Talent.STRONGMAN)));
		}

		return STR + strBonus;
	}

	public int CON() {
		return CON;// TODO
	}

	public int DEX() {
		return DEX;// TODO
	}

	public int INT() {
		return INT;// TODO
	}

	public int WIS() {
		int wisBonus = 0;
		if (hasTalent(Talent.WIDE_SEARCH)) {
			wisBonus += pointsInTalent(Talent.WIDE_SEARCH);
		}
		return WIS + wisBonus;// TODO
	}

	public int CHA() {
		return CHA;// TODO
	}

	private static final String CLASS       = "class";
	private static final String SUBCLASS    = "subClass";
	private static final String ABILITY     = "armorAbility";

	private static final String ATTACK		= "attackSkill";
	private static final String DEFENSE		= "defenseSkill";
	private static final String STRENGTH	= "STR";
	private static final String CONSTITUTION= "CON";
	private static final String DEXTERITY   = "DEX";
	private static final String INTELLIGENCE= "INT";
	private static final String WISDOM      = "WIS";
	private static final String CHARISMA    = "CHA";
	private static final String LEVEL		= "lvl";
	private static final String EXPERIENCE	= "exp";
	private static final String HTBOOST     = "htboost";
	private static final String HUNGER      = "hunger";

	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );

		bundle.put( CLASS, heroClass );
		bundle.put( SUBCLASS, subClass );
		bundle.put( ABILITY, armorAbility );
		Talent.storeTalentsInBundle( bundle, this );

		bundle.put( ATTACK, attackSkill );
		bundle.put( DEFENSE, defenseSkill );

		bundle.put( STRENGTH, STR );
		bundle.put( CONSTITUTION, CON );
		bundle.put( DEXTERITY, DEX );
		bundle.put( INTELLIGENCE, INT );
		bundle.put( WISDOM, WIS );
		bundle.put( CHARISMA, CHA );

		bundle.put( LEVEL, lvl );
		bundle.put( EXPERIENCE, exp );

		bundle.put( HTBOOST, HTBoost );

		bundle.put( HUNGER, hunger );

		belongings.storeInBundle( bundle );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {

		lvl = bundle.getInt( LEVEL );
		exp = bundle.getInt( EXPERIENCE );

		HTBoost = bundle.getInt( HTBOOST );

		super.restoreFromBundle( bundle );

		heroClass = bundle.getEnum( CLASS, HeroClass.class );
		subClass = bundle.getEnum( SUBCLASS, HeroSubClass.class );
		armorAbility = (ArmorAbility)bundle.get( ABILITY );
		Talent.restoreTalentsFromBundle( bundle, this );

		attackSkill = bundle.getInt( ATTACK );
		defenseSkill = bundle.getInt( DEFENSE );

		STR = bundle.getInt( STRENGTH );
		CON = bundle.getInt( CONSTITUTION );
		DEX = bundle.getInt( DEXTERITY );
		INT = bundle.getInt( INTELLIGENCE );
		WIS = bundle.getInt( WISDOM );
		CHA = bundle.getInt( CHARISMA );

		hunger = bundle.getInt( HUNGER );

		belongings.restoreFromBundle( bundle );
	}

	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.level = bundle.getInt( LEVEL );
		info.STR = bundle.getInt( STRENGTH );
		info.CON = bundle.getInt( CONSTITUTION );
		info.DEX = bundle.getInt( DEXTERITY );
		info.INT = bundle.getInt( INTELLIGENCE );
		info.WIS = bundle.getInt( WISDOM );
		info.CHA = bundle.getInt( CHARISMA );
		info.exp = bundle.getInt( EXPERIENCE );
		info.HP = bundle.getInt( Char.TAG_HP );
		info.HT = bundle.getInt( Char.TAG_HT );
		info.hunger = bundle.getInt( HUNGER );
		info.shld = bundle.getInt( Char.TAG_SHLD );
		info.heroClass = bundle.getEnum( CLASS, HeroClass.class );
		info.subClass = bundle.getEnum( SUBCLASS, HeroSubClass.class );
		Belongings.preview( info, bundle );
	}

	public boolean hasTalent( Talent talent ){
		return pointsInTalent(talent) > 0;
	}

	public int pointsInTalent( Talent talent ){
		for (LinkedHashMap<Talent, Integer> tier : talents){
			for (Talent f : tier.keySet()){
				if (f == talent) return tier.get(f);
			}
		}
		return 0;
	}

	public void upgradeTalent( Talent talent ){
		for (LinkedHashMap<Talent, Integer> tier : talents){
			for (Talent f : tier.keySet()){
				if (f == talent) tier.put(talent, tier.get(talent)+1);
			}
		}
		Talent.onTalentUpgraded(this, talent);
	}

	public int talentPointsSpent(int tier){
		int total = 0;
		for (int i : talents.get(tier-1).values()){
			total += i;
		}
		return total;
	}

	public int talentPointsAvailable(int tier){
		int base;
		if (lvl < (Talent.tierLevelThresholds[tier] - 1)
				|| (tier == 3 && subClass == HeroSubClass.NONE)
				|| (tier == 4 && armorAbility == null)) {
			return 0;
		} else if (lvl >= Talent.tierLevelThresholds[tier+1]){
			base = Talent.tierLevelThresholds[tier+1] - Talent.tierLevelThresholds[tier];
			if (Dungeon.isChallenged(Challenges.Challenge.NO_TALENTS)) base += (Talent.totalPoints(tier)/2f)%1 == 0 ? 0 : 1;
		} else {
			base = 1 + lvl - Talent.tierLevelThresholds[tier];
			if (Dungeon.isChallenged(Challenges.Challenge.NO_TALENTS)) base += (Talent.totalPoints(tier)/2f)%1 == 0 ? 0 : 1;
		}
		if (Dungeon.isChallenged(Challenges.Challenge.NO_TALENTS)) base = (int) Math.floor(base / 2f);
		return base - talentPointsSpent(tier) + bonusTalentPoints(tier);
	}

	public boolean shouldShowTalentPointsMessage() {
		if (lvl < Talent.tierLevelThresholds[Talent.MAX_TALENT_TIERS+1]) {
			if (Dungeon.isChallenged(Challenges.Challenge.NO_TALENTS)) {
				for (int i = Talent.MAX_TALENT_TIERS; i > 0; i --) {
					if (lvl >= Talent.tierLevelThresholds[i]) return ((lvl - Talent.tierLevelThresholds[i] + (i == 1 ? 0 : 1))/2f)%1 == 0;
				}
			}
			else return true;
		}
		return false;
	}

	public int bonusTalentPoints(int tier){
		if (lvl < (Talent.tierLevelThresholds[tier]-1)
				|| (tier == 3 && subClass == HeroSubClass.NONE)
				|| (tier == 4 && armorAbility == null)) {
			return 0;
		} else if (buff(PotionOfDivineInspiration.DivineInspirationTracker.class) != null
				&& buff(PotionOfDivineInspiration.DivineInspirationTracker.class).isBoosted(tier)) {
			return 2;
		} else {
			return 0;
		}
	}

	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

	@Override
	public String name(){
		return className();
	}

	@Override
	public void hitSound(float pitch) {
		if (belongings.weapon() instanceof MissileWeapon || belongings.weapon() instanceof SpiritBow) {
			belongings.weapon().hitSound(pitch);
		}
		else if (canWep1Attack && canWep2Attack) {
			belongings.weapon().hitSound(pitch);
			belongings.weapon2().hitSound(pitch);
		}
		else if (canWep1Attack) {
			belongings.weapon().hitSound(pitch);
		}
		else if (canWep2Attack) {
			belongings.weapon2().hitSound(pitch);
		}
		else if (RingOfForce.getBuffedBonus(this, RingOfForce.Force.class) > 0) {
			//pitch deepens by 2.5% (additive) per point of strength, down to 75%
			super.hitSound( pitch * GameMath.gate( 0.75f, 1.25f - 0.025f*STR(), 1f) );
		}
		else {
			super.hitSound(pitch * 1.1f);
		}
	}

	@Override
	public boolean blockSound(float pitch) {
		if ( belongings.weapon() != null && belongings.weapon().defenseFactor(this) >= 4 ){
			Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1, pitch);
			return true;
		}
		if ( belongings.weapon2() != null && belongings.weapon2().defenseFactor(this) >= 4 ){
			Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1, pitch);
			return true;
		}
		if ( subClass == HeroSubClass.SHIELDGUARD ) {
			Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1, pitch);
			return true;
		}
		return super.blockSound(pitch);
	}

	public void live() {
		for (Buff b : buffs()){
			if (!b.revivePersists) b.detach();
		}
		Buff.affect( this, Regeneration.class );
		Buff.affect( this, Hunger.class );
	}

	public int tier() {
		if (belongings.armor() instanceof ClassArmor){
			return 6;
		} else if (belongings.armor() != null){
			return belongings.armor().tier;
		} else {
			return 0;
		}
	}

	public boolean shoot( Char enemy, MissileWeapon wep ) {

		this.enemy = enemy;

		//temporarily set the hero's weapon to the missile weapon being used
		//TODO improve this!
		belongings.thrownWeapon = wep;
		boolean hit = attack( enemy );
		Invisibility.dispel();
		belongings.thrownWeapon = null;

		/*
		if (hit && subClass == HeroSubClass.GLADIATOR){
			Buff.affect( this, Combo.class ).hit( enemy );
		}
		 */

		return hit;
	}

	@Override
	public int attackSkill( Char target ) {
		KindOfWeapon wep = belongings.weapon();
		KindOfWeapon wep2 = belongings.weapon2();

		if (hasTalent(Talent.WEAKNESS_STRIKE)) {
			Talent.WeaknessStrikeTracker tracker = target.buff(Talent.WeaknessStrikeTracker.class);
			boolean mustHit = false;
			if (tracker != null && tracker.count() >= (7-2*pointsInTalent(Talent.WEAKNESS_STRIKE))) {
				mustHit = !(wep instanceof MissileWeapon) && !(wep instanceof SpiritBow);
			}
			if (mustHit) {
				Buff.detach( target, Talent.WeaknessStrikeTracker.class );
				Buff.affect( this, Talent.MeleeMustHit.class );
			}
		}

		if (buff(Talent.MeleeMustHit.class) != null && !(wep instanceof MissileWeapon) && !(wep instanceof SpiritBow)) {
			Buff.detach(this, Talent.MeleeMustHit.class);
			return INFINITE_ACCURACY;
		}


		float accuracy = 1;
		accuracy *= RingOfAccuracy.accuracyMultiplier( this );

		if (wep instanceof MissileWeapon || wep instanceof SpiritBow) {
			if (Dungeon.level.adjacent( pos, target.pos )) {
				accuracy *= (0.5f + 0.2f*pointsInTalent(Talent.POINT_BLANK));
			} else {
				accuracy *= 1.5f;
			}
		}

		if (wep instanceof MissileWeapon) {
			if (((MissileWeapon) wep).shooter != null && ((MissileWeapon) wep).shooter.cursed) {
				return (int)(attackSkill * accuracy * wep.accuracyFactor( this ) * 0.5f);
			}
			else return (int)(attackSkill * accuracy * wep.accuracyFactor( this ));
		}
		else if (wep instanceof SpiritBow) {
			return (int)(attackSkill * accuracy * wep.accuracyFactor( this ));
		}
		else if (canWep1Attack && canWep2Attack) {
			return (int) (((attackSkill * accuracy * wep.accuracyFactor( this ) +
					attackSkill * accuracy * wep2.accuracyFactor( this ))) * 0.5f);
		}
		else if (canWep1Attack) {
			return (int)(attackSkill * accuracy * wep.accuracyFactor( this ));
		}
		else if (canWep2Attack) {
			return (int)(attackSkill * accuracy * wep2.accuracyFactor( this ));
		}
		else {
			return (int)(attackSkill * accuracy);
		}
	}

	@Override
	public int defenseSkill( Char enemy ) {

		if (buff(Combo.ParryTracker.class) != null){
			if (canAttack(enemy)){
				Buff.affect(this, Combo.RiposteTracker.class).enemy = enemy;
			}
			return INFINITE_EVASION;
		}

		float evasion = defenseSkill;
		float dexMultiplier = 0.1f * DEX();
		evasion = Math.round(dexMultiplier * evasion);
		evasion *= RingOfEvasion.evasionMultiplier( this );

		if (paralysed > 0) {
			evasion /= 2;
		}

		if (belongings.armor() != null) {
			evasion = belongings.armor().evasionFactor(this, evasion);
		}

		return Math.round(evasion);
	}

	@Override
	public String defenseVerb() {
		Combo.ParryTracker parry = buff(Combo.ParryTracker.class);
		if (parry == null){
			return super.defenseVerb();
		} else {
			parry.parried = true;
			if (buff(Combo.class).getComboCount() < 9 || pointsInTalent(Talent.ENHANCED_COMBO) < 2){
				parry.detach();
			}
			Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
			return Messages.get(Monk.class, "parried");
		}
	}

	@Override
	public int drRoll() {
		int dr = 0;

		if (belongings.armor() != null) {
			int armDr = Random.NormalIntRange( belongings.armor().DRMin(), belongings.armor().DRMax());
			if (STR() < belongings.armor().STRReq()){
				armDr -= 2*(belongings.armor().STRReq() - STR());
			}
			if (armDr > 0) dr += armDr;
		}
		if (belongings.weapon() != null)  {
			int wepDr = Random.NormalIntRange( 0 , belongings.weapon().defenseFactor( this ) );
			if (STR() < ((Weapon)belongings.weapon()).STRReq()){
				wepDr -= 2*(((Weapon)belongings.weapon()).STRReq() - STR());
			}
			if (wepDr > 0) dr += wepDr;
		}
		if (belongings.weapon2() != null)  {
			int wep2Dr = Random.NormalIntRange( 0 , belongings.weapon2().defenseFactor( this ) );
			if (STR() < ((Weapon)belongings.weapon2()).STRReq()){
				wep2Dr -= 2*(((Weapon)belongings.weapon2()).STRReq() - STR());
			}
			if (wep2Dr > 0) dr += wep2Dr;
		}

		if (buff(HoldFast.class) != null){
			dr += Random.NormalIntRange(0, 2+2*pointsInTalent(Talent.HOLD_FAST));
		}

		if (subClass == HeroSubClass.SHIELDGUARD){
			dr += Random.NormalIntRange(0, buff(DefensiveStance.class) == null ? 2 : 4+4*pointsInTalent(Talent.ENHANCED_SHIELD));
		}

		return dr;
	}

	@Override
	public int damageRoll() {
		KindOfWeapon wep = belongings.weapon();
		KindOfWeapon wep2 = belongings.weapon2();
		int dmg;

		if (wep instanceof MissileWeapon || wep instanceof SpiritBow) {
			dmg = wep.damageRoll( this );
		}
		else if (canWep1Attack && canWep2Attack) {
			dmg = (int) ((wep.damageRoll( this ) + wep2.damageRoll( this )) / 1.5f);
			dmg += RingOfForce.armedDamageBonus(this);
		}
		else if (canWep1Attack) {
			dmg = wep.damageRoll( this );
			dmg += RingOfForce.armedDamageBonus(this);
		}
		else if (canWep2Attack) {
			dmg = wep2.damageRoll( this );
			dmg += RingOfForce.armedDamageBonus(this);
		}
		else {
			dmg = RingOfForce.damageRoll(this);
		}

		if (!(wep instanceof MissileWeapon) && !(wep instanceof SpiritBow)) {
			if (subClass == HeroSubClass.SHIELDGUARD && buff(DefensiveStance.class) != null) {
				dmg += Math.round(drRoll() * 0.1f * (pointsInTalent(Talent.RECKLESS_SLAM)+1));
			}
		}

		if (dmg < 0) dmg = 0;

		return dmg;
	}

	@Override
	public float speed() {

		float speed = super.speed();

		speed *= RingOfHaste.speedMultiplier(this);

		if (belongings.armor() != null) {
			speed = belongings.armor().speedFactor(this, speed);
		}

		Momentum momentum = buff(Momentum.class);
		if (momentum != null){
			((HeroSprite)sprite).sprint( momentum.freerunning() ? 1.5f : 1f );
			speed *= momentum.speedMultiplier();
		} else {
			((HeroSprite)sprite).sprint( 1f );
		}

		NaturesPower.naturesPowerTracker natStrength = buff(NaturesPower.naturesPowerTracker.class);
		if (natStrength != null){
			speed *= (2f + 0.25f*pointsInTalent(Talent.GROWING_POWER));
		}

		return speed;

	}

	public boolean canSurpriseAttack() {
		if (belongings.weapon() instanceof MissileWeapon || belongings.weapon() instanceof SpiritBow) {
			return ((Weapon) belongings.weapon()).canHeroSurpriseAttack(this);
		}
		else if (canWep1Attack && canWep2Attack) {
			if (!(belongings.weapon() instanceof Weapon) && !(belongings.weapon2() instanceof Weapon)) return true;
			return ((Weapon) belongings.weapon()).canHeroSurpriseAttack(this) && ((Weapon) belongings.weapon2()).canHeroSurpriseAttack(this);
		}
		else if (canWep1Attack) {
			if (!(belongings.weapon() instanceof Weapon)) return true;
			return ((Weapon) belongings.weapon()).canHeroSurpriseAttack(this);
		}
		else if (canWep2Attack) {
			if (!(belongings.weapon2() instanceof Weapon)) return true;
			return ((Weapon) belongings.weapon2()).canHeroSurpriseAttack(this);
		}
		return true;
	}

	public boolean canAttack(Char enemy){
		if (enemy == null || pos == enemy.pos || !Actor.chars().contains(enemy)) {
			canWep1Attack = false;
			canWep2Attack = false;
			return false;
		}

		KindOfWeapon wep = Dungeon.hero.belongings.weapon();
		KindOfWeapon wep2 = Dungeon.hero.belongings.weapon2();

		//can always attack adjacent enemies
		if (Dungeon.level.adjacent(pos, enemy.pos)) {
			canWep1Attack = wep instanceof MeleeWeapon;
			canWep2Attack = wep2 instanceof MeleeWeapon;
			return true;
		}

		if (wep instanceof MeleeWeapon && wep2 instanceof MeleeWeapon) {
			canWep1Attack = wep.canReach(this, enemy.pos);
			canWep2Attack = wep2.canReach(this, enemy.pos);
		}
		else if (wep != null) {
			canWep1Attack = wep.canReach(this, enemy.pos);
			canWep2Attack = false;
		}
		else if (wep2 != null) {
			canWep1Attack = false;
			canWep2Attack = wep2.canReach(this, enemy.pos);
		}
		else {
			canWep1Attack = false;
			canWep2Attack = false;
		}

		return canWep1Attack || canWep2Attack;
	}

	public float attackDelay() {
		if (buff(Talent.LethalMomentumTracker.class) != null){
			buff(Talent.LethalMomentumTracker.class).detach();
			return 0;
		}

		KindOfWeapon wep = belongings.weapon();
		KindOfWeapon wep2 = belongings.weapon2();

		if (buff(Talent.ExtraMeleeAttack.class) != null && !(wep instanceof MissileWeapon || wep instanceof SpiritBow)) {
			buff(Talent.ExtraMeleeAttack.class).detach();
			return 0;
		}

		if (wep instanceof MissileWeapon || wep instanceof SpiritBow) {
			return wep.delayFactor( this );
		}
		else if (canWep1Attack && canWep2Attack) {
			return (wep.delayFactor( this ) + wep2.delayFactor( this )) * 0.5f;
		}
		else if (canWep1Attack) {
			return wep.delayFactor( this );
		}
		else if (canWep2Attack) {
			return wep2.delayFactor( this );
		}
		else {
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			return 1f/RingOfFuror.attackSpeedMultiplier(this);
		}
	}

	@Override
	public void spend( float time ) {
		justMoved = false;
		TimekeepersHourglass.timeFreeze freeze = buff(TimekeepersHourglass.timeFreeze.class);
		if (freeze != null) {
			freeze.processTime(time);
			return;
		}

		Swiftthistle.TimeBubble bubble = buff(Swiftthistle.TimeBubble.class);
		if (bubble != null){
			bubble.processTime(time);
			return;
		}

		super.spend(time);
	}

	public void spendAndNext( float time ) {
		busy();
		spend( time );
		next();
	}

	@Override
	public boolean act() {

		//calls to dungeon.observe will also update hero's local FOV.
		fieldOfView = Dungeon.level.heroFOV;

		if (buff(Endure.EndureTracker.class) != null){
			buff(Endure.EndureTracker.class).endEnduring();
		}

		if (!ready) {
			//do a full observe (including fog update) if not resting.
			if (!resting || buff(MindVision.class) != null || buff(Awareness.class) != null) {
				Dungeon.observe();
			} else {
				//otherwise just directly re-calculate FOV
				Dungeon.level.updateFieldOfView(this, fieldOfView);
			}
		}

		checkVisibleMobs();
		BuffIndicator.refreshHero();

		if (paralysed > 0) {

			curAction = null;

			spendAndNext( TICK );
			return false;
		}

		boolean actResult;
		if (curAction == null) {

			if (resting) {
				spend( TIME_TO_REST );
				next();
			} else {
				ready();
			}

			actResult = false;

		} else {

			resting = false;

			ready = false;

			if (curAction instanceof HeroAction.Move) {
				actResult = actMove( (HeroAction.Move)curAction );

			} else if (curAction instanceof HeroAction.Interact) {
				actResult = actInteract( (HeroAction.Interact)curAction );

			} else if (curAction instanceof HeroAction.Buy) {
				actResult = actBuy( (HeroAction.Buy)curAction );

			}else if (curAction instanceof HeroAction.PickUp) {
				actResult = actPickUp( (HeroAction.PickUp)curAction );

			} else if (curAction instanceof HeroAction.OpenChest) {
				actResult = actOpenChest( (HeroAction.OpenChest)curAction );

			} else if (curAction instanceof HeroAction.Unlock) {
				actResult = actUnlock((HeroAction.Unlock) curAction);

			} else if (curAction instanceof HeroAction.Descend) {
				actResult = actDescend( (HeroAction.Descend)curAction );

			} else if (curAction instanceof HeroAction.Ascend) {
				actResult = actAscend( (HeroAction.Ascend)curAction );

			} else if (curAction instanceof HeroAction.Attack) {
				actResult = actAttack( (HeroAction.Attack)curAction );

			} else if (curAction instanceof HeroAction.Alchemy) {
				actResult = actAlchemy( (HeroAction.Alchemy)curAction );

			} else {
				actResult = false;
			}
		}

		if(hasTalent(Talent.BARKSKIN) && Dungeon.level.map[pos] == Terrain.FURROWED_GRASS){
			Buff.affect(this, Barkskin.class).set( (lvl*pointsInTalent(Talent.BARKSKIN))/2, 1 );
		}

		return actResult;
	}

	public void busy() {
		ready = false;
	}

	private void ready() {
		if (sprite.looping()) sprite.idle();
		curAction = null;
		damageInterrupt = true;
		ready = true;

		AttackIndicator.updateState();

		GameScene.ready();
	}

	public void interrupt() {
		if (isAlive() && curAction != null &&
				((curAction instanceof HeroAction.Move && curAction.dst != pos) ||
						(curAction instanceof HeroAction.Ascend || curAction instanceof HeroAction.Descend))) {
			lastAction = curAction;
		}
		curAction = null;
		GameScene.resetKeyHold();
	}

	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		next();
	}

	private boolean actMove( HeroAction.Move action ) {

		if (getCloser( action.dst )) {
			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actInteract( HeroAction.Interact action ) {

		Char ch = action.ch;

		if (ch.canInteract(this)) {

			ready();
			sprite.turnTo( pos, ch.pos );
			return ch.interact(this);

		} else {

			if (fieldOfView[ch.pos] && getCloser( ch.pos )) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	private boolean actBuy( HeroAction.Buy action ) {
		int dst = action.dst;
		if (pos == dst) {

			ready();

			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndTradeItem( heap ) );
					}
				});
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAlchemy( HeroAction.Alchemy action ) {
		int dst = action.dst;
		if (Dungeon.level.distance(dst, pos) <= 1) {

			ready();

			AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
			if (kit != null && kit.isCursed()){
				GLog.w( Messages.get(AlchemistsToolkit.class, "cursed"));
				return false;
			}

			Alchemy alch = (Alchemy) Dungeon.level.blobs.get(Alchemy.class);
			if (alch != null) {
				alch.alchPos = dst;
				AlchemyScene.setProvider( alch );
			}
			CarbonizedPixelDungeon.switchScene(AlchemyScene.class);
			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actPickUp( HeroAction.PickUp action ) {
		int dst = action.dst;
		if (pos == dst) {

			Heap heap = Dungeon.level.heaps.get( pos );
			if (heap != null) {
				Item item = heap.peek();
				if (item.doPickUp( this )) {
					heap.pickUp();

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key) {
						//Do Nothing
					} else {

						//TODO make all unique items important? or just POS / SOU?
						boolean important = item.unique && item.isIdentified() &&
								(item instanceof Scroll || item instanceof Potion);
						if (important) {
							GLog.p( Messages.get(this, "you_now_have", item.name()) );
						} else {
							GLog.i( Messages.get(this, "you_now_have", item.name()) );
						}
					}

					curAction = null;
				} else {

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key) {
						//Do Nothing
					} else {
						GLog.newLine();
						GLog.n(Messages.get(this, "you_cant_have", item.name()));
					}

					heap.sprite.drop();
					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actOpenChest( HeroAction.OpenChest action ) {
		int dst = action.dst;
		if (Dungeon.level.adjacent( pos, dst ) || pos == dst) {

			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {

				if ((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.depth)) < 1)
						|| (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.depth)) < 1)){

					GLog.w( Messages.get(this, "locked_chest") );
					ready();
					return false;

				}

				switch (heap.type) {
					case TOMB:
						Sample.INSTANCE.play( Assets.Sounds.TOMB );
						Camera.main.shake( 1, 0.5f );
						break;
					case SKELETON:
					case REMAINS:
						break;
					default:
						Sample.INSTANCE.play( Assets.Sounds.UNLOCK );
				}

				sprite.operate( dst );

			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actUnlock( HeroAction.Unlock action ) {
		int doorCell = action.dst;
		if (Dungeon.level.adjacent( pos, doorCell )) {

			boolean hasKey = false;
			int door = Dungeon.level.map[doorCell];

			if (door == Terrain.LOCKED_DOOR
					&& Notes.keyCount(new IronKey(Dungeon.depth)) > 0) {

				hasKey = true;

			} else if (door == Terrain.LOCKED_EXIT
					&& Notes.keyCount(new SkeletonKey(Dungeon.depth)) > 0) {

				hasKey = true;

			}

			if (hasKey) {

				sprite.operate( doorCell );

				Sample.INSTANCE.play( Assets.Sounds.UNLOCK );

			} else {
				GLog.w( Messages.get(this, "locked_door") );
				ready();
			}

			return false;

		} else if (getCloser( doorCell )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actDescend( HeroAction.Descend action ) {
		int stairs = action.dst;

		if (rooted) {
			Camera.main.shake(1, 1f);
			ready();
			return false;
			//there can be multiple exit tiles, so descend on any of them
			//TODO this is slightly brittle, it assumes there are no disjointed sets of exit tiles
		} else if ((Dungeon.level.map[pos] == Terrain.EXIT || Dungeon.level.map[pos] == Terrain.UNLOCKED_EXIT)) {

			curAction = null;

			TimekeepersHourglass.timeFreeze timeFreeze = buff(TimekeepersHourglass.timeFreeze.class);
			if (timeFreeze != null) timeFreeze.disarmPressedTraps();
			Swiftthistle.TimeBubble timeBubble = buff(Swiftthistle.TimeBubble.class);
			if (timeBubble != null) timeBubble.disarmPressedTraps();

			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene( InterlevelScene.class );

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAscend( HeroAction.Ascend action ) {
		int stairs = action.dst;


		if (rooted){
			Camera.main.shake( 1, 1f );
			ready();
			return false;
			//there can be multiple entrance tiles, so descend on any of them
			//TODO this is slightly brittle, it assumes there are no disjointed sets of entrance tiles
		} else if (Dungeon.level.map[pos] == Terrain.ENTRANCE) {

			if (Dungeon.depth == 1) {

				if (belongings.getItem( Amulet.class ) == null) {
					Game.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							GameScene.show( new WndMessage( Messages.get(Hero.this, "leave") ) );
						}
					});
					ready();
				} else {
					Badges.silentValidateHappyEnd();
					Dungeon.win( Amulet.class );
					Dungeon.deleteGame( GamesInProgress.curSlot, true );
					Game.switchScene( SurfaceScene.class );
				}

			} else {

				curAction = null;

				TimekeepersHourglass.timeFreeze timeFreeze = buff(TimekeepersHourglass.timeFreeze.class);
				if (timeFreeze != null) timeFreeze.disarmPressedTraps();
				Swiftthistle.TimeBubble timeBubble = buff(Swiftthistle.TimeBubble.class);
				if (timeBubble != null) timeBubble.disarmPressedTraps();

				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene( InterlevelScene.class );
			}

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAttack( HeroAction.Attack action ) {

		enemy = action.target;

		if (enemy.isAlive() && canAttack( enemy ) && !isCharmedBy( enemy )) {

			sprite.attack( enemy.pos );

			return false;

		} else {

			if (fieldOfView[enemy.pos] && getCloser( enemy.pos )) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	public Char enemy(){
		return enemy;
	}

	public void rest( boolean fullRest ) {
		spendAndNext( TIME_TO_REST );
		if (!fullRest) {
			boolean showStatus = true;
			if (hasTalent(Talent.HOLD_FAST)){
				Buff.affect(this, HoldFast.class);
			}
			if (subClass == HeroSubClass.SHIELDGUARD) {
				Buff.affect(this, DefensiveStance.class);
				showStatus = false;
			}
			if (sprite != null && showStatus) {
				sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
			}
		}
		resting = fullRest;
	}

	@Override
	public int attackProc( final Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );

		KindOfWeapon wep = belongings.weapon();
		KindOfWeapon wep2 = belongings.weapon2();

		if (wep instanceof MissileWeapon || wep instanceof SpiritBow) {
			damage = wep.proc( this, enemy, damage );
		}
		else if (canWep1Attack && canWep2Attack) {
			damage = (int) ((wep.proc( this, enemy, damage ) + wep2.proc( this, enemy, damage )) * 0.5f);
		}
		else if (canWep1Attack) {
			damage = wep.proc( this, enemy, damage );
		}
		else if (canWep2Attack) {
			damage = wep2.proc( this, enemy, damage );
		}

		if (buff(Talent.SpiritBladesTracker.class) != null
				&& Random.Int(10) < 3*pointsInTalent(Talent.SPIRIT_BLADES)){
			SpiritBow bow = belongings.getItem(SpiritBow.class);
			if (bow != null) {
				damage = bow.proc( this, enemy, damage );
			}
			buff(Talent.SpiritBladesTracker.class).detach();
		}

		damage = Talent.onAttackProc( this, enemy, damage );

		switch (subClass) {
			case SNIPER:
				if (wep instanceof MissileWeapon && !(wep instanceof SpiritBow.SpiritArrow) && enemy != this) {
					Actor.add(new Actor() {

						{
							actPriority = VFX_PRIO;
						}

						@Override
						protected boolean act() {
							if (enemy.isAlive()) {
								int bonusTurns = hasTalent(Talent.SHARED_UPGRADES) ? wep.buffedLvl() : 0;
								Buff.prolong(Hero.this, SnipersMark.class, SnipersMark.DURATION + bonusTurns).set(enemy.id(), bonusTurns);
							}
							Actor.remove(this);
							return true;
						}
					});
				}
				break;
			default:
		}

		return damage;
	}

	@Override
	public int defenseProc( Char enemy, int damage ) {

		if (belongings.armor() != null) {
			damage = belongings.armor().proc( enemy, this, damage );
		}

		Earthroot.Armor armor = buff( Earthroot.Armor.class );
		if (armor != null) {
			damage = armor.absorb( damage );
		}

		WandOfLivingEarth.RockArmor rockArmor = buff(WandOfLivingEarth.RockArmor.class);
		if (rockArmor != null) {
			damage = rockArmor.absorb(damage);
		}

		return damage;
	}

	@Override
	public void damage( int dmg, Object src ) {
		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
			interrupt();
			resting = false;
		}

		if (this.buff(Drowsy.class) != null){
			Buff.detach(this, Drowsy.class);
			GLog.w( Messages.get(this, "pain_resist") );
		}

		Endure.EndureTracker endure = buff(Endure.EndureTracker.class);
		if (!(src instanceof Char)){
			//reduce damage here if it isn't coming from a character (if it is we already reduced it)
			if (endure != null){
				dmg = endure.adjustDamageTaken(dmg);
			}
			//the same also applies to challenge scroll damage reduction
			if (buff(ScrollOfChallenge.ChallengeArena.class) != null){
				dmg *= 0.67f;
			}
		}

		CapeOfThorns.Thorns thorns = buff( CapeOfThorns.Thorns.class );
		if (thorns != null) {
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char)src : null),  this);
		}

		dmg = (int)Math.ceil(dmg * RingOfTenacity.damageMultiplier( this ));

		//TODO improve this when I have proper damage source logic
		if (belongings.armor() != null && belongings.armor().hasGlyph(AntiMagic.class, this)
				&& AntiMagic.RESISTS.contains(src.getClass())){
			dmg -= AntiMagic.drRoll(belongings.armor().buffedLvl());
		}

		if (buff(Talent.WarriorFoodImmunity.class) != null) {
			dmg = Math.round(dmg*0.00f);
		}

		int preHP = HP + shielding();
		super.damage( dmg, src );
		int postHP = HP + shielding();
		int effectiveDamage = preHP - postHP;

		if (effectiveDamage <= 0) return;

		//flash red when hit for serious damage.
		float percentDMG = effectiveDamage / (float)preHP; //percent of current HP that was taken
		float percentHP = 1 - ((HT - postHP) / (float)HT); //percent health after damage was taken
		// The flash intensity increases primarily based on damage taken and secondarily on missing HP.
		float flashIntensity = 0.25f * (percentDMG * percentDMG) / percentHP;
		//if the intensity is very low don't flash at all
		if (flashIntensity >= 0.05f){
			flashIntensity = Math.min(1/3f, flashIntensity); //cap intensity at 1/3
			GameScene.flash( (int)(0xFF*flashIntensity) << 16 );
			if (isAlive()) {
				if (flashIntensity >= 1/6f) {
					Sample.INSTANCE.play(Assets.Sounds.HEALTH_CRITICAL, 1/3f + flashIntensity * 2f);
				} else {
					Sample.INSTANCE.play(Assets.Sounds.HEALTH_WARN, 1/3f + flashIntensity * 4f);
				}
			}
		}
	}

	public void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		Mob target = null;
		for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (fieldOfView[ m.pos ] && m.alignment == Alignment.ENEMY) {
				visible.add(m);
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}

				if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1){
					if (target == null){
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
					if (m instanceof Snake && Dungeon.level.distance(m.pos, pos) <= 4
							&& !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_EXAMINING)){
						GLog.p(Messages.get(Guidebook.class, "hint"));
						GameScene.flashForDocument(Document.GUIDE_EXAMINING);
						//we set to read here to prevent this message popping up a bunch
						Document.ADVENTURERS_GUIDE.readPage(Document.GUIDE_EXAMINING);
					}
				}
			}
		}

		Char lastTarget = QuickSlotButton.lastTarget;
		if (target != null && (lastTarget == null ||
				!lastTarget.isAlive() ||
				lastTarget.alignment == Alignment.ALLY ||
				!fieldOfView[lastTarget.pos])){
			QuickSlotButton.target(target);
		}

		if (newMob) {
			interrupt();
			if (resting){
				Dungeon.observe();
				resting = false;
			}
		}

		visibleEnemies = visible;
	}

	public int visibleEnemies() {
		return visibleEnemies.size();
	}

	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}

	private boolean walkingToVisibleTrapInFog = false;

	//FIXME this is a fairly crude way to track this, really it would be nice to have a short
	//history of hero actions
	public boolean justMoved = false;

	private boolean getCloser( final int target ) {

		if (target == pos)
			return false;

		if (rooted) {
			Camera.main.shake( 1, 1f );
			return false;
		}

		int step = -1;

		if (Dungeon.level.adjacent( pos, target )) {

			path = null;

			if (Actor.findChar( target ) == null) {
				if (Dungeon.level.pit[target] && !flying && !Dungeon.level.solid[target]) {
					if (!Chasm.jumpConfirmed){
						Chasm.heroJump(this);
						interrupt();
					} else {
						Chasm.heroFall(target);
					}
					return false;
				}
				if (Dungeon.level.passable[target] || Dungeon.level.avoid[target]) {
					step = target;
				}
				if (walkingToVisibleTrapInFog
						&& Dungeon.level.traps.get(target) != null
						&& Dungeon.level.traps.get(target).visible){
					return false;
				}
			}

		} else {

			boolean newPath = false;
			if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
				newPath = true;
			else if (path.getLast() != target)
				newPath = true;
			else {
				if (!Dungeon.level.passable[path.get(0)] || Actor.findChar(path.get(0)) != null) {
					newPath = true;
				}
			}

			if (newPath) {

				int len = Dungeon.level.length();
				boolean[] p = Dungeon.level.passable;
				boolean[] v = Dungeon.level.visited;
				boolean[] m = Dungeon.level.mapped;
				boolean[] passable = new boolean[len];
				for (int i = 0; i < len; i++) {
					passable[i] = p[i] && (v[i] || m[i]);
				}

				PathFinder.Path newpath = Dungeon.findPath(this, target, passable, fieldOfView, true);
				if (newpath != null && path != null && newpath.size() > 2*path.size()){
					path = null;
				} else {
					path = newpath;
				}
			}

			if (path == null) return false;
			step = path.removeFirst();

		}

		if (step != -1) {

			if (subClass == HeroSubClass.FREERUNNER){
				Buff.affect(this, Momentum.class).gainStack();
			}

			float speed = speed();

			sprite.move(pos, step);
			move(step);

			spend( 1 / speed );
			justMoved = true;

			search(false);

			return true;

		} else {

			return false;

		}

	}

	public boolean handle( int cell ) {

		if (cell == -1) {
			return false;
		}

		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}

		Char ch = Actor.findChar( cell );
		Heap heap = Dungeon.level.heaps.get( cell );

		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {

			curAction = new HeroAction.Alchemy( cell );

		} else if (fieldOfView[cell] && ch instanceof Mob) {

			if (ch.alignment != Alignment.ENEMY && ch.buff(Amok.class) == null) {
				curAction = new HeroAction.Interact( ch );
			} else {
				curAction = new HeroAction.Attack( ch );
			}

		} else if (heap != null
				//moving to an item doesn't auto-pickup when enemies are near...
				&& (visibleEnemies.size() == 0 || cell == pos ||
				//...but only for standard heaps, chests and similar open as normal.
				(heap.type != Type.HEAP && heap.type != Type.FOR_SALE))) {

			switch (heap.type) {
				case HEAP:
					curAction = new HeroAction.PickUp( cell );
					break;
				case FOR_SALE:
					curAction = heap.size() == 1 && heap.peek().value() > 0 ?
							new HeroAction.Buy( cell ) :
							new HeroAction.PickUp( cell );
					break;
				default:
					curAction = new HeroAction.OpenChest( cell );
			}

		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {

			curAction = new HeroAction.Unlock( cell );

		} else if ((cell == Dungeon.level.exit || Dungeon.level.map[cell] == Terrain.EXIT || Dungeon.level.map[cell] == Terrain.UNLOCKED_EXIT)
				&& Dungeon.depth < 26) {

			curAction = new HeroAction.Descend( cell );

		} else if (cell == Dungeon.level.entrance || Dungeon.level.map[cell] == Terrain.ENTRANCE) {

			curAction = new HeroAction.Ascend( cell );

		} else  {

			if (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell]
					&& Dungeon.level.traps.get(cell) != null && Dungeon.level.traps.get(cell).visible) {
				walkingToVisibleTrapInFog = true;
			} else {
				walkingToVisibleTrapInFog = false;
			}

			curAction = new HeroAction.Move( cell );
			lastAction = null;

		}

		return true;
	}

	public void earnExp( int exp, Class source ) {

		this.exp += exp;
		float percent = exp/(float)maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if (chains != null) chains.gainExp(percent);

		HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
		if (horn != null) horn.gainCharge(percent);

		AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
		if (kit != null) kit.gainCharge(percent);

		MasterThievesArmband.Thievery armband = buff(MasterThievesArmband.Thievery.class);
		if (armband != null) armband.gainCharge(percent);

		if (source != PotionOfExperience.class) {
			for (Item i : belongings) {
				i.onHeroGainExp(percent, this);
			}
			if (buff(Talent.RejuvenatingStepsFurrow.class) != null){
				buff(Talent.RejuvenatingStepsFurrow.class).countDown(percent*200f);
				if (buff(Talent.RejuvenatingStepsFurrow.class).count() <= 0){
					buff(Talent.RejuvenatingStepsFurrow.class).detach();
				}
			}
		}

		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();
			if (lvl < MAX_LEVEL) {
				lvl++;
				levelUp = true;

				if (buff(ElixirOfMight.HTBoost.class) != null){
					buff(ElixirOfMight.HTBoost.class).onLevelUp();
				}

				updateHT( true );
				attackSkill++;
				defenseSkill++;

			} else {
				Buff.prolong(this, Bless.class, Bless.DURATION);
				this.exp = 0;

				GLog.newLine();
				GLog.p( Messages.get(this, "level_cap"));
				Sample.INSTANCE.play( Assets.Sounds.LEVELUP );
			}

		}

		if (levelUp) {

			if (sprite != null) {
				GLog.newLine();
				GLog.p( Messages.get(this, "new_level") );
				sprite.showStatus( CharSprite.POSITIVE, Messages.get(Hero.class, "level_up") );
				Sample.INSTANCE.play( Assets.Sounds.LEVELUP );
				if (shouldShowTalentPointsMessage()) {
					GLog.newLine();
					GLog.p( Messages.get(this, "new_talent") );
					StatusPane.talentBlink = 10f;
					WndHero.lastIdx = 1;
				}
			}

			Item.updateQuickslot();

			Badges.validateLevelReached();
		}
	}

	public int maxExp() {
		return maxExp( lvl );
	}

	public static int maxExp( int lvl ){
		return 5 + lvl * 5;
	}

	public boolean isStarving() {
		return Buff.affect(this, Hunger.class).isStarving();
	}

	@Override
	public void add( Buff buff ) {

		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		super.add( buff );

		if (sprite != null) {
			String msg = buff.heroMessage();
			if (msg != null){
				GLog.w(msg);
			}

			if (buff instanceof Paralysis || buff instanceof Vertigo) {
				interrupt();
			}

		}

		BuffIndicator.refreshHero();
	}

	@Override
	public void remove( Buff buff ) {
		super.remove( buff );

		BuffIndicator.refreshHero();
	}

	@Override
	public float stealth() {
		float stealth = super.stealth();

		if (belongings.armor() != null){
			stealth = belongings.armor().stealthFactor(this, stealth);
		}

		return stealth;
	}

	@Override
	public void die( Object cause ) {

		curAction = null;

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for (Ankh i : belongings.getAllItems(Ankh.class)){
			if (ankh == null || i.isBlessed()) {
				ankh = i;
			}
		}

		if (ankh != null) {
			interrupt();
			resting = false;

			if (ankh.isBlessed()) {
				this.HP = HT / 4;

				PotionOfHealing.cure(this);
				Buff.prolong(this, AnkhInvulnerability.class, AnkhInvulnerability.DURATION);

				SpellSprite.show(this, SpellSprite.ANKH);
				GameScene.flash(0x80FFFF40);
				Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				GLog.w(Messages.get(this, "revive"));
				Statistics.ankhsUsed++;

				ankh.detach(belongings.backpack);

				for (Char ch : Actor.chars()) {
					if (ch instanceof DriedRose.GhostHero) {
						((DriedRose.GhostHero) ch).sayAnhk();
						return;
					}
				}
			} else {

				//this is hacky, basically we want to declare that a wndResurrect exists before
				//it actually gets created. This is important so that the game knows to not
				//delete the run or submit it to rankings, because a WndResurrect is about to exist
				//this is needed because the actual creation of the window is delayed here
				WndResurrect.instance = new Object();
				Ankh finalAnkh = ankh;
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndResurrect(finalAnkh) );
					}
				});

			}
			return;
		}

		Actor.fixTime();
		super.die( cause );
		reallyDie( cause );
	}

	public static void reallyDie( Object cause ) {

		int length = Dungeon.level.length();
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Dungeon.level.discoverable;

		for (int i=0; i < length; i++) {

			int terr = map[i];

			if (discoverable[i]) {

				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					Dungeon.level.discover( i );
				}
			}
		}

		Bones.leave();

		Dungeon.observe();
		GameScene.updateFog();

		Dungeon.hero.belongings.identify();

		int pos = Dungeon.hero.pos;

		ArrayList<Integer> passable = new ArrayList<>();
		for (Integer ofs : PathFinder.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Dungeon.level.heaps.get( cell ) == null) {
				passable.add( cell );
			}
		}
		Collections.shuffle( passable );

		ArrayList<Item> items = new ArrayList<>(Dungeon.hero.belongings.backpack.items);
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element( items );
			Dungeon.level.drop( item, cell ).sprite.drop( pos );
			items.remove( item );
		}

		for (Char c : Actor.chars()){
			if (c instanceof DriedRose.GhostHero){
				((DriedRose.GhostHero) c).sayHeroKilled();
			}
		}

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.gameOver();
				Sample.INSTANCE.play( Assets.Sounds.DEATH );
			}
		});

		if (cause instanceof Hero.Doom) {
			((Hero.Doom)cause).onDeath();
		}

		Dungeon.deleteGame( GamesInProgress.curSlot, true );
	}

	//effectively cache this buff to prevent having to call buff(...) a bunch.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and thread coordination implications if that method calls buff(...) frequently

	@Override
	public boolean isAlive() {

		if (HP <= 0) {
			return false;
		} else {
			return super.isAlive();
		}
	}

	@Override
	public void move(int step, boolean travelling) {
		boolean wasHighGrass = Dungeon.level.map[step] == Terrain.HIGH_GRASS;

		super.move( step, travelling);

		if (!flying && travelling) {
			if (Dungeon.level.water[pos]) {
				Sample.INSTANCE.play( Assets.Sounds.WATER, 1, Random.Float( 0.8f, 1.25f ) );
			} else if (Dungeon.level.map[pos] == Terrain.EMPTY_SP) {
				Sample.INSTANCE.play( Assets.Sounds.STURDY, 1, Random.Float( 0.96f, 1.05f ) );
			} else if (Dungeon.level.map[pos] == Terrain.GRASS
					|| Dungeon.level.map[pos] == Terrain.EMBERS
					|| Dungeon.level.map[pos] == Terrain.FURROWED_GRASS){
				if (step == pos && wasHighGrass) {
					Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float( 0.96f, 1.05f ) );
				} else {
					Sample.INSTANCE.play( Assets.Sounds.GRASS, 1, Random.Float( 0.96f, 1.05f ) );
				}
			} else {
				Sample.INSTANCE.play( Assets.Sounds.STEP, 1, Random.Float( 0.96f, 1.05f ) );
			}
		}
	}

	@Override
	public void onAttackComplete() {

		AttackIndicator.target(enemy);

		boolean hit = attack( enemy );

		Invisibility.dispel();
		spend( attackDelay() );

		if (hasTalent(Talent.WEAKNESS_STRIKE)) {
			if (hit) {
				Buff.detach( enemy, Talent.WeaknessStrikeTracker.class );
			}
			else if ( canWep1Attack || canWep2Attack || isEmptyHand() ){
				Buff.affect( enemy, Talent.WeaknessStrikeTracker.class ).countUp(1.0f);
			}
		}

		if (hit && subClass == HeroSubClass.GLADIATOR){
			Buff.affect( this, Combo.class ).hit( enemy );
		}

		curAction = null;

		super.onAttackComplete();
	}

	public boolean isEmptyHand() {
		return belongings.weapon == null && belongings.weapon2 == null;
	}

	@Override
	public void onMotionComplete() {
		GameScene.checkKeyHold();
	}

	@Override
	public void onOperateComplete() {

		if (curAction instanceof HeroAction.Unlock) {

			int doorCell = ((HeroAction.Unlock)curAction).dst;
			int door = Dungeon.level.map[doorCell];

			if (Dungeon.level.distance(pos, doorCell) <= 1) {
				boolean hasKey = true;
				if (door == Terrain.LOCKED_DOOR) {
					hasKey = Notes.remove(new IronKey(Dungeon.depth));
					if (hasKey) Level.set(doorCell, Terrain.DOOR);
				} else {
					hasKey = Notes.remove(new SkeletonKey(Dungeon.depth));
					if (hasKey) Level.set(doorCell, Terrain.UNLOCKED_EXIT);
				}

				if (hasKey) {
					GameScene.updateKeyDisplay();
					Level.set(doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT);
					GameScene.updateMap(doorCell);
					spend(Key.TIME_TO_UNLOCK);
				}
			}

		} else if (curAction instanceof HeroAction.OpenChest) {

			Heap heap = Dungeon.level.heaps.get( ((HeroAction.OpenChest)curAction).dst );

			if (Dungeon.level.distance(pos, heap.pos) <= 1){
				boolean hasKey = true;
				if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
					Sample.INSTANCE.play( Assets.Sounds.BONES );
				} else if (heap.type == Type.LOCKED_CHEST){
					hasKey = Notes.remove(new GoldenKey(Dungeon.depth));
				} else if (heap.type == Type.CRYSTAL_CHEST){
					hasKey = Notes.remove(new CrystalKey(Dungeon.depth));
				}

				if (hasKey) {
					GameScene.updateKeyDisplay();
					heap.open(this);
					spend(Key.TIME_TO_UNLOCK);
				}
			}

		}
		curAction = null;

		super.onOperateComplete();
	}

	@Override
	public boolean isImmune(Class effect) {
		if (effect == Burning.class
				&& belongings.armor() != null
				&& belongings.armor().hasGlyph(Brimstone.class, this)){
			return true;
		}
		return super.isImmune(effect);
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return buff(AnkhInvulnerability.class) != null;
	}

	public boolean search( boolean intentional ) {

		if (!isAlive()) return false;

		boolean smthFound = false;

		int distance = 1 + Math.round((WIS()-8) / 8f);
		boolean circular = WIS() < 8 || (WIS() / 8f)%1 >= 0.5f;

		boolean foresight = buff(Foresight.class) != null;

		if (foresight) distance++;

		int cx = pos % Dungeon.level.width();
		int cy = pos / Dungeon.level.width();
		int ax = cx - distance;
		if (ax < 0) {
			ax = 0;
		}
		int bx = cx + distance;
		if (bx >= Dungeon.level.width()) {
			bx = Dungeon.level.width() - 1;
		}
		int ay = cy - distance;
		if (ay < 0) {
			ay = 0;
		}
		int by = cy + distance;
		if (by >= Dungeon.level.height()) {
			by = Dungeon.level.height() - 1;
		}

		TalismanOfForesight.Foresight talisman = buff( TalismanOfForesight.Foresight.class );
		boolean cursed = talisman != null && talisman.isCursed();

		for (int y = ay; y <= by; y++) {
			for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {

				if (circular && Math.abs(x - cx)-1 > ShadowCaster.rounding[distance][distance - Math.abs(y - cy)]){
					continue;
				}

				if (fieldOfView[p] && p != pos) {

					if (intentional) {
						GameScene.effectOverFog(new CheckedCell(p, pos));
					}

					if (Dungeon.level.secret[p]){

						Trap trap = Dungeon.level.traps.get( p );
						float chance;

						//searches aided by foresight always succeed, even if trap isn't searchable
						if (foresight){
							chance = 1f;

							//otherwise if the trap isn't searchable, searching always fails
						} else if (trap != null && !trap.canBeSearched){
							chance = 0f;

							//intentional searches always succeed against regular traps and doors
						} else if (intentional){
							chance = 1f;

							//unintentional searches always fail with a cursed talisman
						} else if (cursed) {
							chance = 0f;

							//unintentional trap detection scales from 40% at floor 0 to 30% at floor 25
						} else if (Dungeon.level.map[p] == Terrain.SECRET_TRAP) {
							chance = 0.4f - (Dungeon.depth / 250f);

							//unintentional door detection scales from 20% at floor 0 to 0% at floor 20
						} else {
							chance = 0.2f - (Dungeon.depth / 100f);
						}

						if (Random.Float() < chance) {

							int oldValue = Dungeon.level.map[p];

							GameScene.discoverTile( p, oldValue );

							Dungeon.level.discover( p );

							ScrollOfMagicMapping.discover( p );

							smthFound = true;

							if (talisman != null){
								if (oldValue == Terrain.SECRET_TRAP){
									talisman.charge(2);
								} else if (oldValue == Terrain.SECRET_DOOR){
									talisman.charge(10);
								}
							}
						}
					}
				}
			}
		}


		if (intentional) {
			sprite.showStatus( CharSprite.DEFAULT, Messages.get(this, "search") );
			sprite.operate( pos );
			if (!Dungeon.level.locked) {
				if (cursed) {
					GLog.n(Messages.get(this, "search_distracted"));
					Buff.affect(this, Hunger.class).affectHunger(TIME_TO_SEARCH - (2 * HUNGER_FOR_SEARCH));
				} else {
					Buff.affect(this, Hunger.class).affectHunger(TIME_TO_SEARCH - HUNGER_FOR_SEARCH);
				}
			}
			spendAndNext(TIME_TO_SEARCH);

		}

		if (smthFound) {
			GLog.w( Messages.get(this, "noticed_smth") );
			Sample.INSTANCE.play( Assets.Sounds.SECRET );
			interrupt();
		}

		return smthFound;
	}

	public void resurrect() {
		HP = HT;
		live();

		MagicalHolster holster = belongings.getItem(MagicalHolster.class);

		Buff.affect(this, LostInventory.class);
		Buff.affect(this, Invisibility.class, 3f);
		//lost inventory is dropped in interlevelscene

		//activate items that persist after lost inventory
		//FIXME this is very messy, maybe it would be better to just have one buff that
		// handled all items that recharge over time?
		for (Item i : belongings){
			if (i instanceof EquipableItem && i.isEquipped(this)){
				((EquipableItem) i).activate(this);
			} else if (i instanceof CloakOfShadows && i.keptThoughLostInvent && hasTalent(Talent.LIGHT_CLOAK)){
				((CloakOfShadows) i).activate(this);
			} else if (i instanceof Wand && i.keptThoughLostInvent){
				if (holster != null && holster.contains(i)){
					((Wand) i).charge(this, MagicalHolster.HOLSTER_SCALE_FACTOR);
				} else {
					((Wand) i).charge(this);
				}
			} else if (i instanceof MagesStaff && i.keptThoughLostInvent){
				((MagesStaff) i).applyWandChargeBuff(this);
			}
		}
	}

	@Override
	public void next() {
		if (isAlive())
			super.next();
	}

	public static interface Doom {
		public void onDeath();
	}
}
