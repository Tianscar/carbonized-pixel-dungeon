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

package com.tianscar.carbonizedpixeldungeon.items.spells;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.FlavourBuff;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.items.potions.PotionOfLevitation;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;

public class FeatherFall extends Spell {
	
	{
		image = ItemSpriteSheet.FEATHER_FALL;
	}
	
	@Override
	protected void onCast(Hero hero) {
		Buff.append(hero, FeatherBuff.class, 30f);
		hero.sprite.operate(hero.pos);
		Sample.INSTANCE.play(Assets.Sounds.READ );
		hero.sprite.emitter().burst( Speck.factory( Speck.JET ), 20);
		
		GLog.p(Messages.get(this, "light"));
		
		detach( curUser.belongings.backpack );
		updateQuickslot();
		hero.spendAndNext( 1f );
	}
	
	public static class FeatherBuff extends FlavourBuff {
		//does nothing, just waits to be triggered by chasm falling
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((30 + 40) / 2f));
	}
	
	public static class Recipe extends com.tianscar.carbonizedpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfLevitation.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = FeatherFall.class;
			outQuantity = 2;
		}
		
	}
}
