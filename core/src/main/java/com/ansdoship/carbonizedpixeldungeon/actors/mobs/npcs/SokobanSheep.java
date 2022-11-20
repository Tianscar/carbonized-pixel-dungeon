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

package com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.levels.Level;
import com.ansdoship.carbonizedpixeldungeon.sprites.SokobanSheepSprite;

public class SokobanSheep extends Sheep {

	{
		spriteClass = SokobanSheepSprite.class;
	}

	/**
	 *  -W-1 -W  -W+1
	 *  -1    P  +1
	 *  W-1   W  W+1
	 */
	@Override
	public boolean interact(Char c) {
		int curPos = pos;
		int movPos = pos;
		Level level = Dungeon.level;
		int width = level.width();
		int posDif = Dungeon.hero.pos - curPos;

		if (posDif == 1) {
			movPos = curPos-1;
		} else if(posDif == -1) {
			movPos = curPos + 1;
		} else if(posDif == width) {
			movPos = curPos - width;
		} else if(posDif == - width) {
			movPos = curPos + width;
		}

		if (movPos != pos && (level.passable[movPos] || level.avoid[movPos]) && Actor.findChar(movPos) == null) {

			moveSprite(curPos, movPos);
			move(movPos);

			Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
			Dungeon.hero.move(curPos);

			return super.interact(c);
		}

		return super.interact(c);
	}
}