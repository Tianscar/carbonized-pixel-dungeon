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

package com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.CorrosiveGas;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.ToxicGas;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Burning;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Corruption;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Invisibility;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.CharSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.MirrorSprite;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class MirrorImage extends NPC {
	
	{
		spriteClass = MirrorSprite.class;
		
		HP = HT = 1;
		defenseSkill = 1;
		
		alignment = Alignment.ALLY;
		state = HUNTING;
		
		//before other mobs
		actPriority = MOB_PRIO + 1;
	}
	
	private Hero hero;
	private int heroID;
	public int armTier;

	private boolean canWep1Attack = false;
	private boolean canWep2Attack = false;
	
	@Override
	protected boolean act() {
		
		if ( hero == null ){
			hero = (Hero)Actor.findById(heroID);
			if ( hero == null ){
				die(null);
				sprite.killAndErase();
				return true;
			}
		}
		
		if (hero.tier() != armTier){
			armTier = hero.tier();
			((MirrorSprite)sprite).updateArmor( armTier );
		}
		
		return super.act();
	}
	
	private static final String HEROID	= "hero_id";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( HEROID, heroID );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		heroID = bundle.getInt( HEROID );
	}
	
	public void duplicate( Hero hero ) {
		this.hero = hero;
		heroID = this.hero.id();
		Buff.affect(this, MirrorInvis.class, Short.MAX_VALUE);
	}
	
	@Override
	public int damageRoll() {
		int damage;
		if (canWep1Attack && canWep2Attack) {
			damage = (int) ((hero.belongings.weapon().damageRoll(this) +
								hero.belongings.weapon2().damageRoll(this)) / 1.5f);
		}
		else if (canWep1Attack) {
			damage = hero.belongings.weapon().damageRoll(this);
		}
		else if (canWep2Attack) {
			damage = hero.belongings.weapon2().damageRoll(this);
		}
		else {
			damage = hero.damageRoll(); //handles ring of force
		}
		return (damage+1)/2; //half hero damage, rounded up
	}
	
	@Override
	public int attackSkill( Char target ) {
		return hero.attackSkill(target);
	}
	
	@Override
	public int defenseSkill(Char enemy) {
		if (hero != null) {
			int baseEvasion = 4 + hero.lvl;
			int heroEvasion = hero.defenseSkill(enemy);
			
			//if the hero has more/less evasion, 50% of it is applied
			return super.defenseSkill(enemy) * (baseEvasion + heroEvasion) / 2;
		} else {
			return 0;
		}
	}
	
	@Override
	public float attackDelay() {
		return hero.attackDelay(); //handles ring of furor
	}
	
	@Override
	protected boolean canAttack(Char enemy) {
		if (hero.belongings.weapon() != null && hero.belongings.weapon().canReach(this, enemy.pos)) canWep1Attack = true;
		else canWep1Attack = false;
		if (hero.belongings.weapon2() != null && hero.belongings.weapon2().canReach(this, enemy.pos)) canWep2Attack = true;
		else canWep2Attack = false;
		return super.canAttack(enemy) || canWep1Attack || canWep2Attack;
	}
	
	@Override
	public int drRoll() {
		int dr = 0;
		if (hero != null) {
			if (hero.belongings.weapon() != null) dr += Random.NormalIntRange(0, hero.belongings.weapon().defenseFactor(this)/2);
			if (hero.belongings.weapon2() != null) dr += Random.NormalIntRange(0, hero.belongings.weapon2().defenseFactor(this)/2);
		}
		return dr;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		
		MirrorInvis buff = buff(MirrorInvis.class);
		if (buff != null){
			buff.detach();
		}
		
		if (enemy instanceof Mob) {
			((Mob)enemy).aggro( this );
		}
		if (canWep1Attack && canWep2Attack) {
			damage = (int) ((hero.belongings.weapon().proc( this, enemy, damage ) +
								hero.belongings.weapon2().proc( this, enemy, damage )) * 0.5f);
		}
		else if (canWep1Attack) {
			damage = hero.belongings.weapon().proc( this, enemy, damage );
		}
		else if (canWep2Attack) {
			damage = hero.belongings.weapon2().proc( this, enemy, damage );
		}
		if (hero.belongings.weapon() != null || hero.belongings.weapon2() != null) {
			if (!enemy.isAlive() && enemy == Dungeon.hero){
				Dungeon.fail(getClass());
				GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
			}
			return damage;
		} else {
			return damage;
		}
	}
	
	@Override
	public CharSprite sprite() {
		CharSprite s = super.sprite();
		
		hero = (Hero)Actor.findById(heroID);
		if (hero != null) {
			armTier = hero.tier();
		}
		((MirrorSprite)s).updateArmor( armTier );
		return s;
	}
	
	{
		immunities.add( ToxicGas.class );
		immunities.add( CorrosiveGas.class );
		immunities.add( Burning.class );
		immunities.add( Corruption.class );
	}
	
	public static class MirrorInvis extends Invisibility {
		
		{
			announced = false;
		}
		
		@Override
		public int icon() {
			return BuffIndicator.NONE;
		}
	}
}