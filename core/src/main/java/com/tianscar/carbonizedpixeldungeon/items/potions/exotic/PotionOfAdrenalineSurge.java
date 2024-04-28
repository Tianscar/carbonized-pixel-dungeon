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

package com.tianscar.carbonizedpixeldungeon.items.potions.exotic;

import com.tianscar.carbonizedpixeldungeon.Badges;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.AdrenalineSurge;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;

public class PotionOfAdrenalineSurge extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_ARENSURGE;

		unique = true;
	}
	
	@Override
	public void apply(Hero hero) {
		identify();

		hero.STR++;
		hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "msg_1") );
		GLog.p( Messages.get(this, "msg_2") );

		Badges.validateStrengthAttained();

		Buff.affect(hero, AdrenalineSurge.class).reset(2, 800f);
	}
	
}
