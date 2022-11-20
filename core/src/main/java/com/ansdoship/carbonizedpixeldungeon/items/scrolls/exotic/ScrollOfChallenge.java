/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.AdrenalineSurge;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.effects.CellEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.effects.particles.ChallengeParticle;
import com.ansdoship.carbonizedpixeldungeon.mechanics.ShadowCaster;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.carbonizedpixeldungeon.utils.BArray;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.noosa.particles.Emitter;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Callback;
import com.ansdoship.pixeldungeonclasses.utils.PathFinder;
import com.ansdoship.pixeldungeonclasses.utils.Point;

import java.util.ArrayList;

public class ScrollOfChallenge extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_CHALLENGE;
	}
	
	@Override
	public void doRead() {

		doRecord(new Callback() {
			@Override
			public void call() {

				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					mob.beckon( curUser.pos );
				}

				Buff.affect(curUser, ChallengeArena.class).setup(curUser.pos);

				identify();

				curUser.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
				Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );

				if (curUser.subClass == HeroSubClass.LOREMASTER) {
					Buff.affect(curUser, AdrenalineSurge.class).reset(2, 200f);
				}

				readAnimation();

			}
		});

	}


	public static class ChallengeArena extends Buff {

		private ArrayList<Integer> arenaPositions = new ArrayList<>();
		private ArrayList<Emitter> arenaEmitters = new ArrayList<>();

		private static final float DURATION = 100;
		int left = 0;

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.ARMOR;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1f, 0f, 0f);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - left) / DURATION);
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(left);
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", curUser.subClass == HeroSubClass.LOREMASTER ? 66 : 33, left);
		}

		public void setup(int pos){

			int dist;
			if (Dungeon.depth == 5 || Dungeon.depth == 10 || Dungeon.depth == 20){
				dist = 1; //smaller boss arenas
			} else {

				boolean[] visibleCells = new boolean[Dungeon.level.length()];
				Point c = Dungeon.level.cellToPoint(pos);
				ShadowCaster.castShadow(c.x, c.y, visibleCells, Dungeon.level.losBlocking, 8);
				int count=0;
				for (boolean b : visibleCells){
					if (b) count++;
				}

				if (count < 30){
					dist = 1;
				} else if (count >= 100) {
					dist = 3;
				} else {
					dist = 2;
				}
			}

			PathFinder.buildDistanceMap( pos, BArray.or( Dungeon.level.passable, Dungeon.level.avoid, null ), dist );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE && !arenaPositions.contains(i)) {
					arenaPositions.add(i);
				}
			}
			if (target != null) {
				fx(false);
				fx(true);
			}

			left = (int) DURATION;

		}

		@Override
		public boolean act() {

			if (!arenaPositions.contains(target.pos)){
				detach();
			}

			left--;
			BuffIndicator.refreshHero();
			if (left <= 0){
				detach();
			}

			spend(TICK);
			return true;
		}

		@Override
		public void fx(boolean on) {
			if (on){
				for (int i : arenaPositions){
					Emitter e = CellEmitter.get(i);
					e.pour(ChallengeParticle.FACTORY, 0.05f);
					arenaEmitters.add(e);
				}
			} else {
				for (Emitter e : arenaEmitters){
					e.on = false;
				}
				arenaEmitters.clear();
			}
		}

		private static final String ARENA_POSITIONS = "arena_positions";
		private static final String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			int[] values = new int[arenaPositions.size()];
			for (int i = 0; i < values.length; i ++)
				values[i] = arenaPositions.get(i);
			bundle.put(ARENA_POSITIONS, values);

			bundle.put(LEFT, left);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			int[] values = bundle.getIntArray( ARENA_POSITIONS );
			for (int value : values) {
				arenaPositions.add(value);
			}

			left = bundle.getInt(LEFT);
		}
	}
	
}
