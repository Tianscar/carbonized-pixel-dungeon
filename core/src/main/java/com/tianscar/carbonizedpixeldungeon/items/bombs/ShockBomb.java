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

package com.tianscar.carbonizedpixeldungeon.items.bombs;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Paralysis;
import com.tianscar.carbonizedpixeldungeon.effects.CellEmitter;
import com.tianscar.carbonizedpixeldungeon.effects.Lightning;
import com.tianscar.carbonizedpixeldungeon.effects.particles.SparkParticle;
import com.tianscar.carbonizedpixeldungeon.mechanics.Ballistica;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.tiles.DungeonTilemap;
import com.tianscar.carbonizedpixeldungeon.utils.BArray;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

import java.util.ArrayList;

public class ShockBomb extends Bomb {
	
	{
		image = ItemSpriteSheet.SHOCK_BOMB;
	}
	
	@Override
	public void explode(int cell) {
		super.explode(cell);

		ArrayList<Char> affected = new ArrayList<>();
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 3 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE
				&& Actor.findChar(i) != null) {
				affected.add(Actor.findChar(i));
			}
		}

		for (Char ch : affected.toArray(new Char[0])){
			Ballistica LOS = new Ballistica(cell, ch.pos, Ballistica.PROJECTILE);
			if (LOS.collisionPos != ch.pos){
				affected.remove(ch);
			}
		}

		ArrayList<Lightning.Arc> arcs = new ArrayList<>();
		for (Char ch : affected){
			int power = 16 - 4*Dungeon.level.distance(ch.pos, cell);
			if (power > 0){
				//32% to 8% regular bomb damage
				int damage = Math.round(Random.NormalIntRange(5 + Dungeon.depth, 10 + 2*Dungeon.depth) * (power/50f));
				ch.damage(damage, this);
				if (ch.isAlive()) Buff.prolong(ch, Paralysis.class, power);
				arcs.add(new Lightning.Arc(DungeonTilemap.tileCenterToWorld(cell), ch.sprite.center()));
			}
		}

		CellEmitter.center(cell).burst(SparkParticle.FACTORY, 20);
		Dungeon.hero.sprite.parent.addToFront(new Lightning(arcs, null));
		Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}
}
