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

package com.ansdoship.carbonizedpixeldungeon.items.weapon.missiles;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.MagicImmune;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Momentum;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.PinCushion;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.bags.Bag;
import com.ansdoship.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.ansdoship.carbonizedpixeldungeon.items.rings.RingOfSharpshooting;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.SpiritBow;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.Weapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.curses.Wayward;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.enchantments.Projecting;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.ranged.RangedWeapon;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.util.ArrayList;

abstract public class MissileWeapon extends Weapon {

	{
		stackable = true;
		levelKnown = true;

		bones = true;

		defaultAction = AC_THROW;
		usesTargeting = true;
	}

	protected boolean sticky = true;

	public static final float MAX_DURABILITY = 100;
	public float durability = MAX_DURABILITY;
	protected float baseUses = 10;

	public boolean holster;

	//used to reduce durability from the source weapon stack, rather than the one being thrown.
	protected MissileWeapon parent;

	public RangedWeapon shooter;
	
	public int tier;

	public boolean shooterHasEnchant( Char owner ){
		return shooter != null && shooter.enchantment != null && owner.buff(MagicImmune.class) == null;
	}

	@Override
	public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
		if (shooter != null && shooter.hasEnchant(type, owner)){
			return true;
		} else {
			return super.hasEnchant(type, owner);
		}
	}

	@Override
	public void throwSound() {
		if (shooter != null) {
			shooter.missileThrowSound();
		} else {
			super.throwSound();
		}
	}

	@Override
	public int min() {
		return Math.max(0, min( buffedLvl() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) ));
	}

	@Override
	public int min(int lvl) {
		return  2 * tier +                      //base
				(tier == 1 ? lvl : 2*lvl);      //level scaling
	}

	@Override
	public int max() {
		return Math.max(0, max( buffedLvl() + RingOfSharpshooting.levelDamageBonus(Dungeon.hero) ));
	}

	@Override
	public int max(int lvl) {
		return  5 * tier +                      //base
				(tier == 1 ? 2*lvl : tier*lvl); //level scaling
	}

	public int STRReq(int lvl){
		return STRReq(tier, lvl) - 4; //4 less str than normal for their tier
	}

	public int DEXReq() {
		return DEXReq(level());
	}

	public int DEXReq(int lvl) {
		return DEXReq(tier, lvl);
	}

	protected static int DEXReq(int tier, int lvl){
		lvl = Math.max(0, lvl);

		//dexterity req decreases at +1,+3,+6,+10,etc.
		return (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}

	@Override
	public boolean canHeroSurpriseAttack(Hero hero) {
		return shooter == null ? (hero.STR() >= STRReq() && hero.DEX() >= DEXReq()) : shooter.canMissileSurpriseAttack(hero);
	}

	@Override
	//FIXME some logic here assumes the items are in the player's inventory. Might need to adjust
	public Item upgrade() {
		if (!bundleRestoring) {
			durability = MAX_DURABILITY;
			if (quantity > 1) {
				MissileWeapon upgraded = (MissileWeapon) split(1);
				upgraded.parent = null;

				upgraded = (MissileWeapon) upgraded.upgrade();

				//try to put the upgraded into inventory, if it didn't already merge
				if (upgraded.quantity() == 1 && !upgraded.collect()) {
					Dungeon.level.drop(upgraded, Dungeon.hero.pos);
				}
				updateQuickslot();
				return upgraded;
			} else {
				super.upgrade();

				Item similar = Dungeon.hero.belongings.getSimilar(this);
				if (similar != null){
					detach(Dungeon.hero.belongings.backpack);
					Item result = similar.merge(this);
					updateQuickslot();
					return result;
				}
				updateQuickslot();
				return this;
			}

		} else {
			return super.upgrade();
		}
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.remove( AC_EQUIP );
		return actions;
	}

	@Override
	public boolean collect(Bag container) {
		if (container instanceof MagicalHolster) holster = true;
		return super.collect(container);
	}

	@Override
	public int throwPos(Hero user, int dst) {

		boolean projecting = hasEnchant(Projecting.class, user);
		if (!projecting && Random.Int(3) < user.pointsInTalent(Talent.SHARED_ENCHANTMENT)){
			if (shooterHasEnchant(Dungeon.hero)){
				//do nothing
			} else {
				SpiritBow bow = Dungeon.hero.belongings.getItem(SpiritBow.class);
				if (bow != null && bow.hasEnchant(Projecting.class, user)) {
					projecting = true;
				}
			}
		}

		if (projecting && !Dungeon.level.solid[dst] && Dungeon.level.distance(user.pos, dst) <= 4){
			return dst;
		} else {
			return super.throwPos(user, dst);
		}
	}

	@Override
	public float accuracyFactor( Char owner ) {

		float ACC = this.ACC;

		if (shooter == null) {
			int encumbrance = 0;

			if( owner instanceof Hero ){
				encumbrance = DEXReq() - ((Hero)owner).DEX();
			}

			if (hasEnchant(Wayward.class, owner))
				encumbrance = Math.max(2, encumbrance+2);

			ACC = encumbrance > 0 ? (float)(ACC / Math.pow( 1.5, encumbrance )) : ACC;
			if (owner instanceof Hero && ((Hero) owner).STR() < STRReq()) ACC = Math.min(ACC, super.accuracyFactor(owner));
		}
		else ACC = shooter.missileAccuracyFactor(owner);

		if (owner instanceof Hero && owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()){
			ACC *= 1f + 0.2f*((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM);
		}
		return ACC;
	}

	protected float baseDelay( Char owner ){
		float delay = augment.delayFactor(this.DLY);
		if (owner instanceof Hero) {
			int encumbrance = DEXReq() - ((Hero)owner).DEX();
			if (encumbrance > 0){
				delay *= Math.pow( 1.2, encumbrance );
			}
			if (((Hero) owner).STR() < STRReq()) delay = Math.max(super.baseDelay(owner), delay);
		}

		return delay;
	}

	@Override
	public void doThrow(Hero hero) {
		parent = null; //reset parent before throwing, just incase
		super.doThrow(hero);
	}

	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser) {
			parent = null;
			super.onThrow( cell );
		} else {
			if (!curUser.shoot( enemy, this )) {
				rangedMiss( cell );
			} else {

				rangedHit( enemy, cell );

			}
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (attacker == Dungeon.hero && Random.Int(3) < Dungeon.hero.pointsInTalent(Talent.SHARED_ENCHANTMENT)){
			if (shooterHasEnchant(Dungeon.hero)){
				//do nothing
			} else {
				SpiritBow bow = Dungeon.hero.belongings.getItem(SpiritBow.class);
				if (bow != null && bow.enchantment != null && Dungeon.hero.buff(MagicImmune.class) == null) {
					damage = bow.enchantment.proc(this, attacker, defender, damage);
				}
			}
		}
		if (shooter != null) {
			shooter.missileProc(attacker, defender, damage);
		}

		return super.proc(attacker, defender, damage);
	}

	@Override
	public Item random() {
		if (!stackable) return this;

		//2: 66.67% (2/3)
		//3: 26.67% (4/15)
		//4: 6.67%  (1/15)
		quantity = 2;
		if (Random.Int(3) == 0) {
			quantity++;
			if (Random.Int(5) == 0) {
				quantity++;
			}
		}
		return this;
	}

	@Override
	public float castDelay(Char user, int dst) {
		return shooter == null ? delayFactor( user ) : shooter.shootDelayFactor( user );
	}

	protected void rangedHit( Char enemy, int cell ){
		decrementDurability();
		if (durability > 0){
			//attempt to stick the missile weapon to the enemy, just drop it if we can't.
			if (sticky && enemy != null && enemy.isAlive() && enemy.alignment != Char.Alignment.ALLY){
				PinCushion p = Buff.affect(enemy, PinCushion.class);
				if (p.target == enemy){
					p.stick(this);
					return;
				}
			}
			Dungeon.level.drop( this, cell ).sprite.drop();
		}
	}

	protected void rangedMiss( int cell ) {
		parent = null;
		super.onThrow(cell);
	}

	public float durabilityLeft(){
		return durability;
	}

	public void repair( float amount ){
		durability += amount;
		durability = Math.min(durability, MAX_DURABILITY);
	}

	public float durabilityPerUse(){
		float usages = baseUses * (float)(Math.pow(3, level()));

		//+50%/75% durability
		if (Dungeon.hero.hasTalent(Talent.DURABLE_PROJECTILES)){
			usages *= 1.25f + (0.25f*Dungeon.hero.pointsInTalent(Talent.DURABLE_PROJECTILES));
		}
		if (holster) {
			usages *= MagicalHolster.HOLSTER_DURABILITY_FACTOR;
		}

		usages *= RingOfSharpshooting.durabilityMultiplier( Dungeon.hero );

		//at 100 uses, items just last forever.
		if (usages >= 100f) return 0;

		usages = Math.round(usages);

		//add a tiny amount to account for rounding error for calculations like 1/3
		return (MAX_DURABILITY/usages) + 0.001f;
	}

	protected void decrementDurability(){
		//if this weapon was thrown from a source stack, degrade that stack.
		//unless a weapon is about to break, then break the one being thrown
		if (parent != null){
			if (parent.durability <= parent.durabilityPerUse()){
				durability = 0;
				parent.durability = MAX_DURABILITY;
			} else {
				parent.durability -= parent.durabilityPerUse();
				if (parent.durability > 0 && parent.durability <= parent.durabilityPerUse()){
					if (level() <= 0)GLog.w(Messages.get(this, "about_to_break"));
					else             GLog.n(Messages.get(this, "about_to_break"));
				}
			}
			parent = null;
		} else {
			durability -= durabilityPerUse();
			if (durability > 0 && durability <= durabilityPerUse()){
				if (level() <= 0)GLog.w(Messages.get(this, "about_to_break"));
				else             GLog.n(Messages.get(this, "about_to_break"));
			}
		}
	}

	@Override
	public int damageRoll(Char owner) {
		int damage = augment.damageFactor(super.damageRoll( owner ));

		if (owner instanceof Hero) {
			if (shooter == null) {
				int exStr = ((Hero)owner).DEX() - DEXReq();
				if (exStr > 0) {
					damage += Random.IntRange( 0, exStr );
				}
			}
			if (owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()) {
				damage = Math.round(damage * (1f + 0.15f * ((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM)));
			}
		}

		return damage;
	}

	@Override
	public void reset() {
		super.reset();
		durability = MAX_DURABILITY;
	}

	@Override
	public Item merge(Item other) {
		super.merge(other);
		if (isSimilar(other)) {
			durability += ((MissileWeapon)other).durability;
			durability -= MAX_DURABILITY;
			while (durability <= 0){
				quantity -= 1;
				durability += MAX_DURABILITY;
			}
		}
		return this;
	}

	@Override
	public Item split(int amount) {
		bundleRestoring = true;
		Item split = super.split(amount);
		bundleRestoring = false;

		//unless the thrown weapon will break, split off a max durability item and
		//have it reduce the durability of the main stack. Cleaner to the player this way
		if (split != null){
			MissileWeapon m = (MissileWeapon)split;
			m.durability = MAX_DURABILITY;
			m.parent = this;
		}

		return split;
	}

	@Override
	public boolean doPickUp(Hero hero, int pos) {
		parent = null;
		return super.doPickUp(hero, pos);
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {

		int level = 0;
		if (shooter != null && !shooter.isIdentified()) {
			level = shooter.level();
			//temporarily sets the level of the shooter to 0 for IDing purposes
			shooter.level(0);
		}

		String info = desc();

		if (levelKnown) {
			info += "\n\n" + Messages.get( MissileWeapon.class, "stats_known",
					tier,
					Math.round(augment.damageFactor(min())),
					Math.round(augment.damageFactor(max())),
					STRReq(), DEXReq());
			if (STRReq() > Dungeon.hero.STR()) {
				info += " " + Messages.get(Weapon.class, "too_heavy");
			}
			if (DEXReq() > Dungeon.hero.DEX()) {
				info += " " + Messages.get(MissileWeapon.class, "too_bulky");
			} else if (Dungeon.hero.DEX() > DEXReq()){
				info += " " + Messages.get(MissileWeapon.class, "excess_dex", Dungeon.hero.DEX() - DEXReq());
			}
		} else {
			info += "\n\n" + Messages.get( MissileWeapon.class, "stats_unknown",
					tier,
					Math.round(min(0)),
					Math.round(max(0)),
					STRReq(0), DEXReq(0));
			if (STRReq(0) > Dungeon.hero.STR()) {
				info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
			}
			if (DEXReq() > Dungeon.hero.DEX()) {
				info += " " + Messages.get(MissileWeapon.class, "probably_too_bulky");
			}
		}

		if (enchantment != null && (cursedKnown || !enchantment.curse())){
			info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
			info += " " + Messages.get(enchantment, "desc");
		}

		if (cursed && isEquipped( Dungeon.hero )) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
		} else if (cursedKnown && cursed) {
			info += "\n\n" + Messages.get(Weapon.class, "cursed");
		} else if (!isIdentified() && cursedKnown){
			info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
		}

		info += "\n\n" + Messages.get(MissileWeapon.class, "distance");

		info += "\n\n" + Messages.get(this, "durability");

		if (durabilityPerUse() > 0){
			info += " " + Messages.get(this, "uses_left",
					(int)Math.ceil(durability/durabilityPerUse()),
					(int)Math.ceil(MAX_DURABILITY/durabilityPerUse()));
		} else {
			info += " " + Messages.get(this, "unlimited_uses");
		}

		if (shooter != null && !shooter.isIdentified()) {
			shooter.level(level);
		}
		
		return info;
	}

	@Override
	public int value() {
		return 6 * tier * quantity * (level() + 1);
	}

	private static final String DURABILITY = "durability";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DURABILITY, durability);
	}

	private static boolean bundleRestoring = false;

	@Override
	public void restoreFromBundle(Bundle bundle) {
		bundleRestoring = true;
		super.restoreFromBundle(bundle);
		bundleRestoring = false;
		durability = bundle.getFloat(DURABILITY);
	}

	public static class PlaceHolder extends MissileWeapon {

		{
			image = ItemSpriteSheet.MISSILE_HOLDER;
		}

		@Override
		public boolean isSimilar(Item item) {
			return item instanceof MissileWeapon;
		}

		@Override
		public String info() {
			return "";
		}
	}
}
