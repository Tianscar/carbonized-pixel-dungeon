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

package com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.CorrosiveGas;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.ToxicGas;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Burning;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Corruption;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Invisibility;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Wielding;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.MirrorSprite;
import com.tianscar.carbonizedpixeldungeon.ui.BuffIndicator;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

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
	private Wielding wielding;
	private int heroID;
	public int armTier;

	@Override
	protected boolean act() {
		
		if ( hero == null ) {
			hero = (Hero) Actor.findById(heroID);
			wielding = new Wielding( hero );
			if ( hero == null ) {
				die(null);
				sprite.killAndErase();
				return true;
			}
		}
		
		if (hero.tier() != armTier) {
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
		wielding = new Wielding( hero );
		heroID = this.hero.id();
		Buff.affect(this, MirrorInvis.class, Short.MAX_VALUE);
	}
	
	@Override
	public int damageRoll() {
		int damage = hero.damageRoll(wielding);
		return (damage+1)/2; //half hero damage, rounded up
	}
	
	@Override
	public int attackSkill( Char target ) {
		return hero.attackSkill(wielding, target );
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
		return hero.attackDelay(wielding); //handles ring of furor
	}
	
	@Override
	protected boolean canAttack(Char enemy) {
		return wielding.canAttack(this, enemy) || super.canAttack(enemy);
	}
	
	@Override
	public int drRoll() {
		if (hero != null && wielding.weaponNotNull()) {
			return Random.NormalIntRange(0, wielding.defenseFactor(this)/2);
		} else {
			return 0;
		}
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
		if (wielding.weaponNotNull()){
			damage = wielding.proc( this, enemy, damage );
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