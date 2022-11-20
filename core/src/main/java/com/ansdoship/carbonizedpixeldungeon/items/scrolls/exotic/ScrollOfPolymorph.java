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
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs.MagicSheep;
import com.ansdoship.carbonizedpixeldungeon.effects.CellEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.Flare;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.ui.TargetHealthIndicator;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class ScrollOfPolymorph extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_POLYMORPH;
	}

	@Override
	protected String extra() {
		return null;
	}

	@Override
	public void doRead() {

		doRecord(new Callback() {
			@Override
			public void call() {

				new Flare( 5, 32 ).color( 0xFFFFFF, true ).show( curUser.sprite, 2f );
				Sample.INSTANCE.play( Assets.Sounds.READ );

				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
						if (!mob.properties().contains(Char.Property.BOSS)
								&& !mob.properties().contains(Char.Property.MINIBOSS)){
							MagicSheep sheep = new MagicSheep();
							sheep.lifespan = 10;
							sheep.pos = mob.pos;

							//awards half exp for each sheep-ified mob
							//50% chance to round up, 50% to round down
							if (mob.EXP % 2 == 1) mob.EXP += Random.Int(2);
							mob.EXP /= 2;

							mob.destroy();
							mob.sprite.killAndErase();
							Dungeon.level.mobs.remove(mob);
							TargetHealthIndicator.instance.target(null);
							GameScene.add(sheep);
							CellEmitter.get(sheep.pos).burst(Speck.factory(Speck.WOOL), 4);
							Sample.INSTANCE.play(Assets.Sounds.PUFF);
							Sample.INSTANCE.play(Assets.Sounds.SHEEP);
						}
					}
				}
				identify();

				readAnimation();

			}
		});

	}
	
}
