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

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;

public class ScrollEmpower extends Buff {

	{
		type = buffType.POSITIVE;
	}

	private int left;

	public void reset(){
		left = Dungeon.hero.pointsInTalent(Talent.EMPOWERING_SCROLLS);
		Item.updateQuickslot();
	}

	public void use(){
		left--;
		if (left <= 0){
			detach();
		}
	}

	@Override
	public void detach() {
		super.detach();
		Item.updateQuickslot();
	}

	@Override
	public int icon() {
		return BuffIndicator.UPGRADE;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1, 1, 0);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (3f - left) / 3f);
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(left);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", Dungeon.hero.pointsInTalent(Talent.EMPOWERING_SCROLLS), left);
	}

	private static final String LEFT = "left";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, left);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		left = bundle.getInt(LEFT);
	}
}
