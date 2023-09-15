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

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.plants.Swiftthistle;
import com.tianscar.carbonizedpixeldungeon.scenes.InterlevelScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;

public class ScrollOfPassage extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_PASSAGE;
	}
	
	@Override
	public void doRead() {

		identify();
		
		if (Dungeon.bossLevel()) {
			
			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			return;
			
		}

		TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
		if (timeFreeze != null) timeFreeze.disarmPressedTraps();
		Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
		if (timeBubble != null) timeBubble.disarmPressedTraps();

		InterlevelScene.mode = InterlevelScene.Mode.RETURN;
		InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1 - (Dungeon.depth-2)%5));
		InterlevelScene.returnPos = -1;
		Game.switchScene( InterlevelScene.class );
	}
}
