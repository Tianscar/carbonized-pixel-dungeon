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
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Blindness;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Weakness;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfPsionicBlast extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_PSIBLAST;
	}
	
	@Override
	public void doRead() {

		doRecord(new Callback() {
			@Override
			public void call() {

				GameScene.flash( 0x80FFFFFF );

				Sample.INSTANCE.play( Assets.Sounds.BLAST );

				int targets = 0;
				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (Dungeon.level.heroFOV[mob.pos]) {
						targets ++;
						mob.damage(Math.round(mob.HT/2f + mob.HP/2f), this);
						if (mob.isAlive()) {
							Buff.prolong(mob, Blindness.class, Blindness.DURATION);
						}
					}
				}

				if (curUser.subClass == HeroSubClass.LOREMASTER) {
					readAnimation();
				}
				else {
					curUser.damage(Math.max(0, Math.round(curUser.HT*(0.5f * (float)Math.pow(0.9, targets)))), this);
					if (curUser.isAlive()) {
						Buff.prolong(curUser, Blindness.class, Blindness.DURATION);
						Buff.prolong(curUser, Weakness.class, Weakness.DURATION*5f);
						Dungeon.observe();
						readAnimation();
					} else {
						Dungeon.fail( getClass() );
						GLog.n( Messages.get(this, "ondeath") );
					}
				}

				identify();

			}
		});
	
	}
}
