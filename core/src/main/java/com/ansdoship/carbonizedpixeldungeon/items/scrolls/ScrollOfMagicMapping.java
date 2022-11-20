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
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Awareness;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.effects.CellEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.effects.SpellSprite;
import com.ansdoship.carbonizedpixeldungeon.levels.Terrain;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfMagicMapping extends Scroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_MAGICMAP;
	}

	@Override
	public void doRead() {

		doRecord(new Callback() {
			@Override
			public void call() {

				int length = Dungeon.level.length();
				int[] map = Dungeon.level.map;
				boolean[] mapped = Dungeon.level.mapped;
				boolean[] discoverable = Dungeon.level.discoverable;

				boolean noticed = false;

				for (int i=0; i < length; i++) {

					int terr = map[i];

					if (discoverable[i]) {

						mapped[i] = true;
						if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

							Dungeon.level.discover( i );

							if (Dungeon.level.heroFOV[i]) {
								GameScene.discoverTile( i, terr );
								discover( i );

								noticed = true;
							}
						}
					}
				}

				if (curUser.subClass == HeroSubClass.LOREMASTER) {
					Buff.affect( curUser, Awareness.class, Awareness.DURATION );
					Dungeon.observe();

					Dungeon.hero.interrupt();
				}

				GameScene.updateFog();

				GLog.i( Messages.get(ScrollOfMagicMapping.this, "layout") );
				if (noticed) {
					Sample.INSTANCE.play( Assets.Sounds.SECRET );
				}

				SpellSprite.show( curUser, SpellSprite.MAP );
				Sample.INSTANCE.play( Assets.Sounds.READ );

				identify();

				readAnimation();

			}
		});

	}
	
	@Override
	public int value() {
		return isKnown() ? 40 * quantity : super.value();
	}
	
	public static void discover( int cell ) {
		CellEmitter.get( cell ).start( Speck.factory( Speck.DISCOVER ), 0.1f, 4 );
	}
}
