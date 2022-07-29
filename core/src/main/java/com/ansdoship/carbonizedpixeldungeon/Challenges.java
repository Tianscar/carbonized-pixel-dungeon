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

package com.ansdoship.carbonizedpixeldungeon;

import com.ansdoship.carbonizedpixeldungeon.items.Dewdrop;
import com.ansdoship.carbonizedpixeldungeon.items.Item;

import java.util.Comparator;
import java.util.Locale;

import static com.ansdoship.carbonizedpixeldungeon.Challenges.Challenge.NO_HERBALISM;

public class Challenges {

	public enum Challenge {
		NO_FOOD(VERY_EASY, 1),
		NO_ARMOR(MEDIUM, 2),
		NO_HEALING(MEDIUM, 4),
		NO_HERBALISM(MEDIUM, 8),
		SWARM_INTELLIGENCE(HARD, 16),
		DARKNESS(VERY_EASY, 32),
		NO_SCROLLS(MEDIUM, 64),
		CHAMPION_ENEMIES(HARD, 128),
		STRONGER_BOSSES(HARD, 256),
		NO_TALENTS(EASY, 512);

		private final int difficulty;
		private final int mask;

		public int mask() {
			return mask;
		}

		public int difficulty() {
			return difficulty;
		}

		public String messageId() {
			return name().toLowerCase(Locale.ENGLISH);
		}

		Challenge(int difficulty, int mask) {
			this.difficulty = difficulty;
			this.mask = mask;
		}

		public static Challenge[] sort() {
			Challenge[] values = values();
			quickSort(values, 0, values.length-1, new Comparator<Challenge>() {
				@Override
				public int compare(Challenge o1, Challenge o2) {
					return o1.difficulty - o2.difficulty;
				}
			});
			return values;
		}

		private static int partition(Challenge[] array, int low, int high, Comparator<Challenge> comparator) {
			Challenge pivot = array[high];
			int pointer = low;
			for (int i = low; i < high; i++) {
				if (comparator.compare(array[i], pivot) <= 0) {
					Challenge temp = array[i];
					array[i] = array[pointer];
					array[pointer] = temp;
					pointer ++;
				}
			}
			Challenge temp = array[pointer];
			array[pointer] = array[high];
			array[high] = temp;
			return pointer;
		}

		private static void quickSort(Challenge[] array, int low, int high, Comparator<Challenge> comparator) {
			if (low < high) {
				int position = partition(array, low, high, comparator);
				quickSort(array, low, position - 1, comparator);
				quickSort(array, position + 1, high, comparator);
			}
		}
	}

	public static final Challenge[] all = Challenge.values();
	public static final Challenge[] allSorted = Challenge.sort();

	public static final int VERY_EASY  = 1;
	public static final int EASY       = 2;
	public static final int MEDIUM     = 4;
	public static final int HARD       = 8;
	public static final int VERY_HARD  = 16;
	public static final int IMPOSSIBLE = 32;

	public static final int MAX_MASK = (int) Math.pow(2, all.length) - 1;

	public static int activeChallenges() {
		int chCount = 0;
		for (Challenge challenge : all){
			if ((Dungeon.challenges & challenge.mask) != 0) chCount++;
		}
		return chCount;
	}

	public static boolean isItemBlocked( Item item ){

		if (Dungeon.isChallenged(NO_HERBALISM) && item instanceof Dewdrop){
			return true;
		}

		return false;

	}

}