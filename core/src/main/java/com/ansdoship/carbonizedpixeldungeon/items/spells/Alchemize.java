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

package com.ansdoship.carbonizedpixeldungeon.items.spells;

import com.ansdoship.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.items.potions.AlchemicalCatalyst;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.AlchemyScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;

public class Alchemize extends Spell implements AlchemyScene.AlchemyProvider {
	
	{
		image = ItemSpriteSheet.ALCHEMIZE;
	}
	
	@Override
	protected void onCast(Hero hero) {
		/*
		if (hero.visibleEnemies() > hero.mindVisionEnemies.size()) {
			GLog.i( Messages.get(this, "enemy_near") );
			return;
		}
		 */
		detach( curUser.belongings.backpack );
		updateQuickslot();
		AlchemyScene.setProvider(this);
		CarbonizedPixelDungeon.switchScene(AlchemyScene.class);
	}
	
	@Override
	public int getEnergy() {
		return 0;
	}
	
	@Override
	public void spendEnergy(int reduction) {
		//do nothing
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((40 + 40) / 8f));
	}
	
	public static class Recipe extends com.ansdoship.carbonizedpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ArcaneCatalyst.class, AlchemicalCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 3;
			
			output = Alchemize.class;
			outQuantity = 8;
		}
		
	}
}
