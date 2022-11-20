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

package com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.ArtifactRecharge;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.effects.SpellSprite;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfMysticalEnergy extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_MYSTENRG;
	}
	
	@Override
	public void doRead() {

		doRecord(new Callback() {
			@Override
			public void call() {

				//append buff
				float duration = 30;
				if (curUser.subClass == HeroSubClass.LOREMASTER) duration = Math.round(duration * 1.5f);
				Buff.affect(curUser, ArtifactRecharge.class).set( duration ).ignoreHornOfPlenty = false;

				Sample.INSTANCE.play( Assets.Sounds.READ );
				Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );

				SpellSprite.show( curUser, SpellSprite.CHARGE );
				identify();
				ScrollOfRecharging.charge(curUser);

				readAnimation();

			}
		});

	}
	
}
