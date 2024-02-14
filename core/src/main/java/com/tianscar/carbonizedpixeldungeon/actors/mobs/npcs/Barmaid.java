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

package com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.BarmaidSprite;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.windows.WndQuest;

public class Barmaid extends NPC {

	public static final int MAX_GREETINGS = 3;

	{
		spriteClass = BarmaidSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, Dungeon.hero.pos );

		if (c != Dungeon.hero){
			return true;
		}

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndQuest(Barmaid.this,
						Messages.get(Barmaid.this, "greetings_" + Random.Int(1, MAX_GREETINGS + 1), Dungeon.hero.name())));
			}
		});

		return true;
	}

}
