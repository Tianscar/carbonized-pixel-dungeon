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

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.plants.Swiftthistle;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.InterlevelScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndOptions;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

import java.util.ArrayList;

public class ScrollOfPassage extends ExoticScroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_PASSAGE;
	}

	@Override
	public void doRead() {

		identify();

		if (Dungeon.level.locked) {

			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			return;

		}

		TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
		if (timeFreeze != null) timeFreeze.disarmPressedTraps();
		Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
		if (timeBubble != null) timeBubble.disarmPressedTraps();
		final int depth = Math.max(1, (Dungeon.depth - 1 - (Dungeon.depth-2)%5));
		if (curUser.subClass == HeroSubClass.LOREMASTER) {
			ArrayList<Integer> depths = new ArrayList<>(5);
			for (int i = depth; i <= Dungeon.depth; i ++) {
				depths.add(i);
			}
			if (depths.size() == 1) returnDepth(depth);
			else {
				String[] depthTexts = new String[depths.size()];
				for (int i = 0; i < depthTexts.length; i ++) {
					depthTexts[i] = Messages.get(this, "floor", depths.get(i));
				}
				GameScene.show( new WndOptions(new ItemSprite(this),
						Messages.titleCase(Messages.get(ScrollOfPassage.class, "name")),
						Messages.get(ScrollOfPassage.class, "prompt"),
						depthTexts) {
					@Override
					protected void onSelect(int index) {
						doRecord(new Callback() {
							@Override
							public void call() {

								if (index != -1) {
									returnDepth(depths.get(index));
								}

							}
						});
					}
					@Override
					public void onBackPressed() {
						if (!anonymous) curItem.collect();
						super.onBackPressed();
					}
				});
			}
		}
		else returnDepth(depth);
	}

	public static void returnDepth(int depth) {
		InterlevelScene.mode = InterlevelScene.Mode.RETURN;
		InterlevelScene.returnDepth = depth;
		InterlevelScene.returnPos = -1;
		Game.switchScene( InterlevelScene.class );
	}

}
