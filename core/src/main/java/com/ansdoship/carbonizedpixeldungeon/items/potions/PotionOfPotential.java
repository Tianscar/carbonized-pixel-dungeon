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

package com.ansdoship.carbonizedpixeldungeon.items.potions;

import com.ansdoship.carbonizedpixeldungeon.Badges;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.PDSettings;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.ansdoship.carbonizedpixeldungeon.messages.Languages;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.CharSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndGameInProgress;
import com.ansdoship.carbonizedpixeldungeon.windows.WndOptions;
import com.ansdoship.pixeldungeonclasses.noosa.Image;

public class PotionOfPotential extends Potion {

	{
		icon = ItemSpriteSheet.Icons.POTION_POWER;

		unique = true;
	}

	private static final int START_POINTS = 3;

	@Override
	protected void drink(Hero hero) {
		drink(hero, new ItemSprite(this), START_POINTS, null, null, new Runnable() {
			@Override
			public void run() {
				PotionOfPotential.super.drink(hero);
			}
		});
	}

	@Override
	public void apply( Hero hero ) {
		identify();
	}

	public static void drink( Hero hero, Image icon, int startPoints, String[] statsExt, String[] logExt, Runnable afterDrink ) {
		int[] points = new int[] { 0, 0, 0, 0, 0, 0, startPoints }; // STR, CON, DEX, INT, WIS, CHA, points
		switch (hero.heroClass) {
			case WARRIOR:
				points[0] ++;
				points[1] ++;
				points[6] -= 2;
				break;
			case MAGE:
				points[3] ++;
				points[6] -= 1;
				break;
			case ROGUE:
				points[2] ++;
				points[4] ++;
				points[6] -= 2;
				break;
			case HUNTRESS:
				points[0] ++;
				points[4] ++;
				points[6] -= 2;
				break;
		}
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (points[6] < 1) {
					addPoints( hero, points, statsExt, logExt );
					if (afterDrink != null) afterDrink.run();
				}
				else showWndOptions(copyImage(icon), points, this);
			}
		};
		showWndOptions(icon, points, runnable);
	}

	private static Image copyImage(Image src) {
		Image copy = new Image();
		copy.copy(src);
		return copy;
	}

	public static void addPoints( Hero hero, int[] points, String[] statExt, String[] logExt ) {
		String[] stat = new String[6 + (statExt == null ? 0 : statExt.length)];
		String[] log = new String[6 + (logExt == null ? 0 : logExt.length)];
		if (statExt != null && statExt.length > 0) {
			System.arraycopy(statExt, 0, stat, 6, statExt.length);
		}
		if (logExt != null && logExt.length > 0) {
			System.arraycopy(logExt, 0, log, 6, logExt.length);
		}
		if (points[0] > 0) {
			stat[0] = Messages.get(PotionOfPotential.class, "msg_str", points[0]);
			log[0] = Messages.get(PotionOfPotential.class, "msg_str_2");
		}
		if (points[1] > 0) {
			stat[1] = Messages.get(PotionOfPotential.class, "msg_con", points[1]);
			log[1] = Messages.get(PotionOfPotential.class, "msg_con_2");
		}
		if (points[2] > 0) {
			stat[2] = Messages.get(PotionOfPotential.class, "msg_dex", points[2]);
			log[2] = Messages.get(PotionOfPotential.class, "msg_dex_2");
		}
		if (points[3] > 0) {
			stat[3] = Messages.get(PotionOfPotential.class, "msg_int", points[3]);
			log[3] = Messages.get(PotionOfPotential.class, "msg_int_2");
		}
		if (points[4] > 0) {
			stat[4] = Messages.get(PotionOfPotential.class, "msg_wis", points[4]);
			log[4] = Messages.get(PotionOfPotential.class, "msg_wis_2");
		}
		if (points[5] > 0) {
			stat[5] = Messages.get(PotionOfPotential.class, "msg_cha", points[5]);
			log[5] = Messages.get(PotionOfPotential.class, "msg_cha_2");
		}
		hero.STR += points[0];
		hero.CON += points[1];
		hero.updateHT(false, true);
		hero.DEX += points[2];
		hero.INT += points[3];
		hero.WIS += points[4];
		hero.CHA += points[5];
		hero.sprite.showStatus( CharSprite.POSITIVE,  arr2str(", ", stat) );
		GLog.p( arr2str(" ", log) );
		Badges.validatePropertiesAttained();
	}

	private static String arr2str(String join, String... arr) {
		StringBuilder builder = new StringBuilder();
		for (String s : arr) {
			if (s == null) continue;
			builder.append(s).append(join);
		}
		String result = builder.substring(0, builder.length() - join.length());
		if (PDSettings.language() == Languages.CHINESE || PDSettings.language() == Languages.TR_CHINESE)
			result = result.replaceAll("。 ", "。").replaceAll(", ", "，");
		return result;
	}

	private static void showWndOptions(Image icon, int[] points, Runnable afterSelect) {
		GameScene.show(new WndOptions(icon,
				Messages.get(PotionOfPotential.class, "name"),
				Messages.get(PotionOfPotential.class, "select", points[6]),
				Messages.get(WndGameInProgress.class, "str"),
				Messages.get(WndGameInProgress.class, "con"),
				Messages.get(WndGameInProgress.class, "dex"),
				Messages.get(WndGameInProgress.class, "int"),
				Messages.get(WndGameInProgress.class, "wis"),
				Messages.get(WndGameInProgress.class, "cha")) {
			@Override
			protected void onSelect(int index) {
				points[index]++;
				points[6]--;
				if (afterSelect != null) afterSelect.run();
			}
			@Override
			protected boolean enabled(int index) {
				if (Dungeon.hero.heroClass == HeroClass.MAGE && index == 0) return false;
				if (index == 5) return false;
				return points[index] < 1;
			}
		});
	}

	@Override
	public int value() {
		return isKnown() ? 50 * quantity : super.value();
	}

}
