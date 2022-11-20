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

package com.ansdoship.carbonizedpixeldungeon.items.scrolls;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.AdrenalineSurge;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Amok;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfRage extends Scroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_RAGE;
	}

	@Override
	public void doRead() {

		doRecord(new Callback() {
			@Override
			public void call() {

				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					mob.beckon( curUser.pos );
					if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
						Buff.prolong(mob, Amok.class, 5f);
					}
				}

				GLog.w( Messages.get(ScrollOfRage.this, "roar") );
				identify();

				curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
				Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );

				if (curUser.subClass == HeroSubClass.LOREMASTER) {
					Buff.affect(curUser, AdrenalineSurge.class).reset(2, 200f);
				}

				readAnimation();

			}
		});

	}
	
	@Override
	public int value() {
		return isKnown() ? 40 * quantity : super.value();
	}
}
