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

package com.tianscar.carbonizedpixeldungeon.items.quest;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Elemental;
import com.tianscar.carbonizedpixeldungeon.effects.CellEmitter;
import com.tianscar.carbonizedpixeldungeon.effects.particles.ElmoParticle;
import com.tianscar.carbonizedpixeldungeon.items.Heap;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

import java.util.ArrayList;


public class CeremonialCandle extends Item {

	//generated with the wandmaker quest
	public static int ritualPos;

	{
		image = ItemSpriteSheet.CANDLE;

		defaultAction = AC_THROW;

		unique = true;
		stackable = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public void doDrop(Hero hero) {
		super.doDrop(hero);
		checkCandles();
	}

	@Override
	protected void onThrow(int cell) {
		super.onThrow(cell);
		checkCandles();
	}

	private static void checkCandles(){
		Heap heapTop = Dungeon.level.heaps.get(ritualPos - Dungeon.level.width());
		Heap heapRight = Dungeon.level.heaps.get(ritualPos + 1);
		Heap heapBottom = Dungeon.level.heaps.get(ritualPos + Dungeon.level.width());
		Heap heapLeft = Dungeon.level.heaps.get(ritualPos - 1);

		if (heapTop != null &&
				heapRight != null &&
				heapBottom != null &&
				heapLeft != null){

			if (heapTop.peek() instanceof CeremonialCandle &&
					heapRight.peek() instanceof CeremonialCandle &&
					heapBottom.peek() instanceof CeremonialCandle &&
					heapLeft.peek() instanceof CeremonialCandle){

				heapTop.pickUp();
				heapRight.pickUp();
				heapBottom.pickUp();
				heapLeft.pickUp();
				
				Elemental.NewbornFireElemental elemental = new Elemental.NewbornFireElemental();
				Char ch = Actor.findChar( ritualPos );
				if (ch != null) {
					ArrayList<Integer> candidates = new ArrayList<>();
					for (int n : PathFinder.NEIGHBOURS8) {
						int cell = ritualPos + n;
						if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Actor.findChar( cell ) == null) {
							candidates.add( cell );
						}
					}
					if (candidates.size() > 0) {
						elemental.pos = Random.element( candidates );
					} else {
						elemental.pos = ritualPos;
					}
				} else {
					elemental.pos = ritualPos;
				}
				elemental.state = elemental.HUNTING;
				GameScene.add(elemental, 1);

				for (int i : PathFinder.NEIGHBOURS9){
					CellEmitter.get(ritualPos+i).burst(ElmoParticle.FACTORY, 10);
				}
				Sample.INSTANCE.play(Assets.Sounds.BURNING);
			}
		}

	}
}
