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

package com.tianscar.carbonizedpixeldungeon.actors.buffs;

import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.ui.BuffIndicator;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;

public class Barkskin extends Buff {
	
	{
		type = buffType.POSITIVE;
	}

	private int level = 0;
	private int interval = 1;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			spend( interval );
			if (--level <= 0) {
				detach();
			}
			
		} else {
			
			detach();
			
		}
		
		return true;
	}
	
	public int level() {
		return level;
	}
	
	public void set( int value, int time ) {
		//decide whether to override, preferring high value + low interval
		if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
			level = value;
			interval = time;
			spend(time - cooldown() - 1);
		}
	}
	
	@Override
	public int icon() {
		return BuffIndicator.BARKSKIN;
	}

	@Override
	public float iconFadePercent() {
		if (target instanceof Hero){
			float max = ((Hero) target).lvl*((Hero) target).pointsInTalent(Talent.BARKSKIN)/2;
			max = Math.max(max, 2+((Hero) target).lvl/3);
			return Math.max(0, (max-level)/max);
		}
		return 0;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
	}
	
	private static final String LEVEL	    = "level";
	private static final String INTERVAL    = "interval";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( INTERVAL, interval );
		bundle.put( LEVEL, level );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		interval = bundle.getInt( INTERVAL );
		level = bundle.getInt( LEVEL );
	}
}
