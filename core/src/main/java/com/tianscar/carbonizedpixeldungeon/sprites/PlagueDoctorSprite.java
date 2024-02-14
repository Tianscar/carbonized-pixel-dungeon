/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This Program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without eben the implied warranty of
 * GNU General Public License for more details.
 *
 * You should have have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses>
 */

package com.tianscar.carbonizedpixeldungeon.sprites;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.noosa.TextureFilm;

public class PlagueDoctorSprite extends MobSprite {

	public PlagueDoctorSprite() {
		super();
		
		texture( Assets.Sprites.PLAGUEDR );
		
		TextureFilm frames = new TextureFilm( texture, 18, 16 );
		
		idle = new Animation( 4, true );
		idle.frames( frames, 0, 0 ,1, 1, 0, 0, 2, 2, 0, 1, 0, 2, 0, 1, 0, 1, 0, 1, 3, 4, 5, 6, 7, 8 );
		
		run = new Animation( 20, true );
		run.frames( frames, 0 );
		
		die = new Animation( 20, false );
		die.frames( frames, 0 );
		
		play( idle );
	}

}
