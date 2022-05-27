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

package com.ansdoship.carbonizedpixeldungeon.actors.mobs;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.items.Generator;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.Weapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.Weapon.Enchantment;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.enchantments.Grim;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.ansdoship.carbonizedpixeldungeon.journal.Notes;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.StatueSprite;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class Statue extends Mob {
	
	{
		spriteClass = StatueSprite.class;

		EXP = 0;
		state = PASSIVE;
		
		properties.add(Property.INORGANIC);
	}
	
	protected Weapon weapon;
	protected Weapon weapon2;

	private boolean canWep1Attack = false;
	private boolean canWep2Attack = false;
	
	public Statue() {
		super();
		
		do {
			weapon = (MeleeWeapon) Generator.random(Generator.Category.WEAPON);
		} while (weapon.cursed);

		do {
			weapon2 = (MeleeWeapon) Generator.random(Generator.Category.WEAPON);
		} while (weapon2.cursed);

		if (weapon.twoHanded && weapon2.twoHanded) {
			weapon2 = null;
		}
		else if (weapon.twoHanded) {
			weapon2 = null;
		}
		else if (weapon2.twoHanded) {
			weapon2 = null;
		}
		else {
			if (Random.Int(2) < 1) weapon2 = null;
		}
		
		weapon.enchant( Enchantment.random() );
		if (weapon2 != null) weapon2.enchant( Enchantment.random() );
		
		HP = HT = 15 + Dungeon.depth * 5;
		defenseSkill = 4 + Dungeon.depth;
	}
	
	private static final String WEAPON	= "weapon";
	private static final String WEAPON_2= "weapon_2";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( WEAPON, weapon );
		bundle.put( WEAPON_2, weapon2 );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		weapon = (Weapon)bundle.get( WEAPON );
		weapon2 = (Weapon)bundle.get( WEAPON_2 );
	}
	
	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos]) {
			Notes.add( Notes.Landmark.STATUE );
		}
		return super.act();
	}
	
	@Override
	public int damageRoll() {
		if (canWep1Attack && canWep2Attack) {
			return (int) ((weapon.damageRoll(this) + weapon2.damageRoll(this)) / 1.5f);
		}
		else if (canWep1Attack) {
			return weapon.damageRoll(this);
		}
		else if (canWep2Attack) {
			return weapon2.damageRoll(this);
		}
		return weapon.damageRoll(this);
	}
	
	@Override
	public int attackSkill( Char target ) {
		int attackSkill = 9 + Dungeon.depth;
		if (canWep1Attack && canWep2Attack) {
			return (int) ((attackSkill * weapon.accuracyFactor(this) +
					attackSkill * weapon2.accuracyFactor(this)) * 0.5f);
		}
		else if (canWep1Attack) {
			return (int) (attackSkill * weapon.accuracyFactor(this));
		}
		else if (canWep2Attack) {
			return (int) (attackSkill * weapon2.accuracyFactor(this));
		}
		return (int) (attackSkill * weapon.accuracyFactor(this));
	}
	
	@Override
	public float attackDelay() {
		float attackDelay = super.attackDelay();
		if (canWep1Attack && canWep2Attack) {
			return (attackDelay * weapon.delayFactor(this) + attackDelay * weapon2.delayFactor(this)) * 0.5f;
		}
		else if (canWep1Attack) {
			return attackDelay * weapon.delayFactor( this );
		}
		else if (canWep2Attack) {
			return attackDelay * weapon2.delayFactor( this );
		}
		return attackDelay * weapon.delayFactor( this );
	}

	@Override
	protected boolean canAttack(Char enemy) {
		if (weapon.canReach(this, enemy.pos)) canWep1Attack = true;
		else canWep1Attack = false;
		if (weapon2 != null && weapon2.canReach(this, enemy.pos)) canWep2Attack = true;
		else canWep2Attack = false;
		return super.canAttack(enemy) || canWep1Attack || canWep2Attack;
	}

	@Override
	public int drRoll() {
		int dr = Random.NormalIntRange(0, Dungeon.depth + weapon.defenseFactor(this));
		if (weapon2 != null) dr += Random.NormalIntRange(0, weapon2.defenseFactor(this));
		return dr;
	}
	
	@Override
	public void add(Buff buff) {
		super.add(buff);
		if (state == PASSIVE && buff.type == Buff.buffType.NEGATIVE){
			state = HUNTING;
		}
	}

	@Override
	public void damage( int dmg, Object src ) {

		if (state == PASSIVE) {
			state = HUNTING;
		}
		
		super.damage( dmg, src );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		if (canWep1Attack && canWep2Attack) {
			damage = (int) ((weapon.proc( this, enemy, damage ) + weapon2.proc( this, enemy, damage )) * 0.5f);
		}
		else if (canWep1Attack) {
			damage = weapon.proc( this, enemy, damage );
		}
		else if (canWep2Attack) {
			damage = weapon2.proc( this, enemy, damage );
		}
		if (!enemy.isAlive() && enemy == Dungeon.hero){
			Dungeon.fail(getClass());
			GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
		}
		return damage;
	}
	
	@Override
	public void beckon( int cell ) {
		// Do nothing
	}
	
	@Override
	public void die( Object cause ) {
		weapon.identify();
		Dungeon.level.drop( weapon, pos ).sprite.drop();
		if (weapon2 != null) {
			weapon2.identify();
			Dungeon.level.drop( weapon2, pos ).sprite.drop();
		}
		super.die( cause );
	}
	
	@Override
	public void destroy() {
		Notes.remove( Notes.Landmark.STATUE );
		super.destroy();
	}

	@Override
	public float spawningWeight() {
		return 0f;
	}

	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		return weapon2 == null ? Messages.get(this, "desc", weapon.name()) :
				Messages.get(this, "desc2", weapon.name(), weapon2.name());
	}
	
	{
		resistances.add(Grim.class);
	}

	public static Statue random(){
		if (Random.Int(10) == 0){
			return new ArmoredStatue();
		} else {
			return new Statue();
		}
	}
	
}
