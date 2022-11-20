/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * Overgrown Pixel Dungeon
 * Copyright (C) 2018-2019 Anon
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

public class BrownRatSprite extends MobSprite {

	public BrownRatSprite() {
		super();
		
		texture( Assets.Sprites.RAT );
		
		TextureFilm frames = new TextureFilm( texture, 16, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 80, 80, 80, 81 );

        run = new Animation( 10, true );
        run.frames( frames, 86, 87, 88, 89, 90 );

        attack = new Animation( 15, false );
        attack.frames( frames, 82, 83, 84, 85, 80 );

        die = new Animation( 10, false );
        die.frames( frames, 91, 92, 93, 94 );
		
		play( idle );
	}
}
