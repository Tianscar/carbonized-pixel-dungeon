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

package com.tianscar.carbonizedpixeldungeon.items.keys;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.PDSettings;
import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.windows.WndFeedback;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;

import java.io.IOException;

public class SkeletonKey extends Key {
	
	{
		image = ItemSpriteSheet.SKELETON_KEY;
	}
	
	public SkeletonKey() {
		this( 0 );
	}
	
	public SkeletonKey( int depth ) {
		super();
		this.depth = depth;
	}

	@Override
	public boolean doPickUp(Hero hero) {
		if(!PDSettings.supportNagged()){
			try {
				Dungeon.saveAll();
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						CarbonizedPixelDungeon.scene().add(new WndFeedback(true));
					}
				});
			} catch (IOException e) {
				CarbonizedPixelDungeon.reportException(e);
			}
			
		}
		
		return super.doPickUp(hero);
	}

}
