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

package com.tianscar.carbonizedpixeldungeon.actors.mobs;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Freezing;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Blindness;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Burning;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Chill;
import com.tianscar.carbonizedpixeldungeon.effects.Lightning;
import com.tianscar.carbonizedpixeldungeon.effects.Splash;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfFrost;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.tianscar.carbonizedpixeldungeon.items.quest.Embers;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.tianscar.carbonizedpixeldungeon.items.wands.CursedWand;
import com.tianscar.carbonizedpixeldungeon.items.weapon.enchantments.Shocking;
import com.tianscar.carbonizedpixeldungeon.mechanics.Ballistica;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ElementalSprite;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

import java.util.ArrayList;

public abstract class Elemental extends Mob {

	{
		HP = HT = 60;
		defenseSkill = 20;

		EXP = 10;
		maxLvl = 20;

		flying = true;
	}

	protected boolean summonedAlly;

	@Override
	public int damageRoll() {
		if (!summonedAlly) {
			return Random.NormalIntRange(20, 25);
		} else {
			int regionScale = Math.max(2, (1 + Dungeon.depth/5));
			return Random.NormalIntRange(5*regionScale, 5 + 5*regionScale);
		}
	}

	@Override
	public int attackSkill( Char target ) {
		if (!summonedAlly) {
			return 25;
		} else {
			int regionScale = Math.max(2, (1 + Dungeon.depth/5));
			return 5 + 5*regionScale;
		}
	}

	public void setSummonedAlly(){
		summonedAlly = true;
		//sewers are prison are equivalent, otherwise scales as normal (2/2/3/4/5)
		int regionScale = Math.max(2, (1 + Dungeon.depth/5));
		defenseSkill = 5*regionScale;
		HT = 15*regionScale;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 5);
	}

	protected boolean ranged = true;
	private int rangedCooldown = Random.NormalIntRange( 3, 5 );

	@Override
	protected boolean act() {
		if (ranged && state == HUNTING){
			rangedCooldown--;
		}

		return super.act();
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		if (ranged && rangedCooldown <= 0) {
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT ).collisionPos == enemy.pos;
		} else {
			return super.canAttack( enemy );
		}
	}

	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos ) || !ranged || rangedCooldown > 0) {

			return super.doAttack( enemy );

		} else {

			if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
				sprite.zap( enemy.pos );
				return false;
			} else {
				zap();
				return true;
			}
		}
	}

	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		meleeProc( enemy, damage );

		return damage;
	}

	private void zap() {
		spend( 1f );

		if (hit( this, enemy, true )) {

			rangedProc( enemy );

		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}

		rangedCooldown = Random.NormalIntRange( 3, 5 );
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public void add( Buff buff ) {
		if (harmfulBuffs.contains( buff.getClass() )) {
			damage( Random.NormalIntRange( HT/2, HT * 3/5 ), buff );
		} else {
			super.add( buff );
		}
	}

	protected abstract void meleeProc( Char enemy, int damage );
	protected abstract void rangedProc( Char enemy );

	protected ArrayList<Class<? extends Buff>> harmfulBuffs = new ArrayList<>();

	private static final String COOLDOWN = "cooldown";
	private static final String SUMMONED_ALLY = "summoned_ally";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( COOLDOWN, rangedCooldown );
		bundle.put( SUMMONED_ALLY, summonedAlly );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if (bundle.contains( COOLDOWN )) {
			rangedCooldown = bundle.getInt( COOLDOWN );
		}
		summonedAlly = bundle.getBoolean( SUMMONED_ALLY );
		if (summonedAlly) {
			setSummonedAlly();
		}
	}

	public static class FireElemental extends Elemental {

		{
			spriteClass = ElementalSprite.Fire.class;

			loot = new PotionOfLiquidFlame();
			lootChance = 1/8f;

			properties.add( Property.FIERY );

			harmfulBuffs.add( com.tianscar.carbonizedpixeldungeon.actors.buffs.Frost.class );
			harmfulBuffs.add( Chill.class );
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 2 ) == 0 && !Dungeon.level.water[enemy.pos]) {
				Buff.affect( enemy, Burning.class ).reignite( enemy );
				if (enemy.sprite.visible) Splash.at( enemy.sprite.center(), sprite.blood(), 5);
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			if (!Dungeon.level.water[enemy.pos]) {
				Buff.affect( enemy, Burning.class ).reignite( enemy, 4f );
			}
			if (enemy.sprite.visible) Splash.at( enemy.sprite.center(), sprite.blood(), 5);
		}
	}

	//used in wandmaker quest
	public static class NewbornFireElemental extends FireElemental {

		{
			spriteClass = ElementalSprite.NewbornFire.class;

			HT = 60;
			HP = HT/2; //30

			defenseSkill = 12;

			EXP = 7;

			properties.add(Property.MINIBOSS);
		}

		@Override
		public void die(Object cause) {
			super.die(cause);
			Dungeon.level.drop( new Embers(), pos ).sprite.drop();
		}

		@Override
		public boolean reset() {
			return true;
		}

	}

	//not a miniboss, no ranged attack, otherwise a newborn elemental
	public static class AllyNewBornElemental extends NewbornFireElemental {

		{
			ranged = false;
			properties.remove(Property.MINIBOSS);
		}

		@Override
		public String description() {
			return super.description() + " " + Messages.get(this, "ally");
		}

	}

	public static class FrostElemental extends Elemental {

		{
			spriteClass = ElementalSprite.Frost.class;

			loot = new PotionOfFrost();
			lootChance = 1/8f;

			properties.add( Property.ICY );

			harmfulBuffs.add( Burning.class );
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			if (Random.Int( 3 ) == 0 || Dungeon.level.water[enemy.pos]) {
				Freezing.freeze( enemy.pos );
				if (enemy.sprite.visible) Splash.at( enemy.sprite.center(), sprite.blood(), 5);
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			Freezing.freeze( enemy.pos );
			if (enemy.sprite.visible) Splash.at( enemy.sprite.center(), sprite.blood(), 5);
		}
	}

	public static class ShockElemental extends Elemental {

		{
			spriteClass = ElementalSprite.Shock.class;

			loot = new ScrollOfRecharging();
			lootChance = 1/4f;

			properties.add( Property.ELECTRIC );
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			ArrayList<Char> affected = new ArrayList<>();
			ArrayList<Lightning.Arc> arcs = new ArrayList<>();
			Shocking.arc( this, enemy, 2, affected, arcs );

			if (!Dungeon.level.water[enemy.pos]) {
				affected.remove( enemy );
			}

			for (Char ch : affected) {
				ch.damage( Math.round( damage * 0.4f ), this );
			}

			boolean visible = sprite.visible || enemy.sprite.visible;
			for (Char ch : affected){
				if (ch.sprite.visible) visible = true;
			}

			if (visible) {
				sprite.parent.addToFront(new Lightning(arcs, null));
				Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
			}
		}

		@Override
		protected void rangedProc( Char enemy ) {
			Buff.affect( enemy, Blindness.class, Blindness.DURATION/2f );
			if (enemy == Dungeon.hero) {
				GameScene.flash(0x80FFFFFF);
			}
		}
	}

	public static class ChaosElemental extends Elemental {

		{
			spriteClass = ElementalSprite.Chaos.class;

			loot = new ScrollOfTransmutation();
			lootChance = 1f;
		}

		@Override
		protected void meleeProc( Char enemy, int damage ) {
			CursedWand.cursedEffect(null, this, enemy);
		}

		@Override
		protected void rangedProc( Char enemy ) {
			CursedWand.cursedEffect(null, this, enemy);
		}
	}

	public static Class<? extends Elemental> random(){
		if (Random.Int( 50 ) == 0){
			return ChaosElemental.class;
		}

		float roll = Random.Float();
		if (roll < 0.4f){
			return FireElemental.class;
		} else if (roll < 0.8f){
			return FrostElemental.class;
		} else {
			return ShockElemental.class;
		}
	}
}
