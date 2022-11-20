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

package com.ansdoship.carbonizedpixeldungeon.sprites;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.pixeldungeonclasses.noosa.TextureFilm;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class BlackSheepSprite extends MobSprite {

	public BlackSheepSprite() {
		super();
		
		texture( Assets.Sprites.SHEEP );
		
		TextureFilm frames = new TextureFilm( texture, 16, 15 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 17, 18, 19, 16 );

		run = idle.clone();
		attack = idle.clone();

		die = new Animation( 20, false );
		die.frames( frames, 16 );
		
		play( idle );
		curFrame = Random.Int( curAnim.frames.length );
	}
}
