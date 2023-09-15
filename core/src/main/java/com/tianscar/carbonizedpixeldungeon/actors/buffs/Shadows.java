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

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.ui.BuffIndicator;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;

public class Shadows extends Invisibility {
	
	protected float left;
	
	private static final String LEFT	= "left";

	{
		announced = false;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEFT, left );
		
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		left = bundle.getFloat( LEFT );
	}
	
	@Override
	public boolean attachTo( Char target ) {
		if (Dungeon.level != null) {
			for (Mob m : Dungeon.level.mobs) {
				if (Dungeon.level.adjacent(m.pos, target.pos) && m.alignment != target.alignment) {
					return false;
				}
			}
		}
		if (super.attachTo( target )) {
			if (Dungeon.level != null) {
				Sample.INSTANCE.play( Assets.Sounds.MELD );
				Dungeon.observe();
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			
			spend( TICK );
			
			if (--left <= 0) {
				detach();
				return true;
			}

			for (Mob m : Dungeon.level.mobs){
				if (Dungeon.level.adjacent(m.pos, target.pos) && m.alignment != target.alignment){
					detach();
					return true;
				}
			}
			
		} else {
			
			detach();
			
		}
		
		return true;
	}
	
	public void prolong() {
		left = 2;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.SHADOWS;
	}

	@Override
	public float iconFadePercent() {
		return 0;
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
