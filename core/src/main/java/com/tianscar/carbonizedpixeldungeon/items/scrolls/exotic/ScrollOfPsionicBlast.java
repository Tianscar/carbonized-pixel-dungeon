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

package com.tianscar.carbonizedpixeldungeon.items.scrolls.exotic;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Blindness;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Weakness;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;

public class ScrollOfPsionicBlast extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_PSIBLAST;
	}
	
	@Override
	public void doRead() {
		
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

		identify();
		
	
	}
}
