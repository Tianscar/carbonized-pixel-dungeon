/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.ansdoship.carbonizedpixeldungeon.actors.buffs;

import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;

public class Drowsy extends Buff {

	public static final float DURATION = 5f;

	private float duration;

	{
		type = buffType.NEUTRAL;
		announced = true;
		duration = DURATION;
	}

	public void duration(float duration) {
		this.duration = duration;
	}

	@Override
	public int icon() {
		return BuffIndicator.DROWSY;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (duration - visualcooldown()) / duration);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString((int)visualcooldown());
	}

	public boolean attachTo(Char target ) {
		if (!target.isImmune(Sleep.class) && super.attachTo(target)) {
			if (cooldown() == 0) {
				spend(duration);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean act(){
		Buff.affect(target, MagicalSleep.class);

		detach();
		return true;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(visualcooldown()));
	}
}
