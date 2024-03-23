/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2042 Evan Debenham
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

package com.tianscar.carbonizedpixeldungeon.sprites;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.noosa.TextureFilm;

public class ArsonistSprite extends MobSprite {

	public ArsonistSprite() {
		super();
		
		texture( Assets.Sprites.THIEF );
		TextureFilm film = new TextureFilm( texture, 12, 13 );
		
		idle = new Animation( 1, true );
		idle.frames( film, 42, 42, 42, 43, 42, 42, 42, 42, 43 );
		
		run = new Animation( 15, true );
		run.frames( film, 42, 42, 44, 45, 45, 46 );
		
		die = new Animation( 10, false );
		die.frames( film, 46, 48, 49, 50, 51 );
		
		attack = new Animation( 12, false );
		attack.frames( film, 52, 53, 54 );
		
		idle();
	}
}
