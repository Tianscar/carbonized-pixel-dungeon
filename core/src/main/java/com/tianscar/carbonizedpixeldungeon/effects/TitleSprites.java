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

package com.tianscar.carbonizedpixeldungeon.effects;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;

public class TitleSprites {

	public enum Type {
		SWORD,
		SHIELD
	}

	public static Image get( Type type ) {
		Image icon = new Image( Assets.Interfaces.TITLE );
		switch (type) {
			case SHIELD:
				icon.frame( icon.texture.uvRect( 0, 0, 103, 103 ) );
				break;
			case SWORD:
				icon.frame( icon.texture.uvRect( 0, 103, 128, 142 ) );
				break;
		}
		return icon;
	}

}
