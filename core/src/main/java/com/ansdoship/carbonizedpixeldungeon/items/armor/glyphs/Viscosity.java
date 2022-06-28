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

package com.ansdoship.carbonizedpixeldungeon.items.armor.glyphs;

import com.ansdoship.carbonizedpixeldungeon.Badges;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.items.armor.Armor;
import com.ansdoship.carbonizedpixeldungeon.items.armor.Armor.Glyph;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.CharSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite.Glowing;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;

public class Viscosity extends Glyph {

	private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing( 0x8844CC );

	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage ) {

		//FIXME this glyph should really just proc after DR is accounted for.
		//should build in functionality for that, but this works for now
		int realDamage = damage - defender.drRoll();

		//account for icon stomach (just skip the glyph)
		if (defender.buff(Talent.WarriorFoodImmunity.class) != null){
			return damage;
		}

		//account for huntress armor piercing
		if (attacker instanceof Hero
				&& ((Hero) attacker).belongings.weapon() instanceof MissileWeapon
				&& ((Hero) attacker).subClass == HeroSubClass.SNIPER
				&& !Dungeon.level.adjacent(attacker.pos, defender.pos)){
			realDamage = damage;
		}

		if (realDamage <= 0) {
			return 0;
		}

		int level = Math.max( 0, armor.buffedLvl() );

		float percent = (level+1)/(float)(level+6);
		int amount = (int)Math.ceil(realDamage * percent);

		DeferedDamage deferred = Buff.affect( defender, DeferedDamage.class );
		deferred.prolong( amount );

		defender.sprite.showStatus( CharSprite.WARNING, Messages.get(this, "deferred", amount) );

		return damage - amount;

	}

	@Override
	public Glowing glowing() {
		return PURPLE;
	}

	public static class DeferedDamage extends Buff {

		{
			type = buffType.NEGATIVE;
		}

		protected int damage = 0;

		private static final String DAMAGE	= "damage";

		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( DAMAGE, damage );

		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			damage = bundle.getInt( DAMAGE );
		}

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				postpone( TICK );
				return true;
			} else {
				return false;
			}
		}

		public void prolong( int damage ) {
			this.damage += damage;
		}

		@Override
		public int icon() {
			return BuffIndicator.DEFERRED;
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(damage);
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public boolean act() {
			if (target.isAlive()) {

				int damageThisTick = Math.max(1, (int)(damage*0.1f));
				target.damage( damageThisTick, this );
				if (target == Dungeon.hero && !target.isAlive()) {

					Badges.validateDeathFromGlyph();

					Dungeon.fail( getClass() );
					GLog.n( Messages.get(this, "ondeath") );
				}
				spend( TICK );

				damage -= damageThisTick;
				if (damage <= 0) {
					detach();
				}

			} else {

				detach();

			}

			return true;
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", damage);
		}
	}
}
