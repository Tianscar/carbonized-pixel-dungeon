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
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.PrismaticGuard;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfPrismaticImage extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_PRISIMG;
	}
	
	@Override
	public void doRead() {

		doRecord(new Callback() {
			@Override
			public void call() {

				boolean found = false;
				for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
					if (m instanceof PrismaticImage){
						found = true;
						m.HP = m.HT;
						m.sprite.emitter().burst(Speck.factory(Speck.HEALING), 4);
					}
				}

				if (!found) {
					Buff.affect(curUser, PrismaticGuard.class).set( PrismaticGuard.maxHP( curUser ) );
				}

				identify();

				Sample.INSTANCE.play( Assets.Sounds.READ );

				readAnimation();

			}
		});

	}
}
