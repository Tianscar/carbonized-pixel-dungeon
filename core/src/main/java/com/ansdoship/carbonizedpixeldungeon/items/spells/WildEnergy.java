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

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.ArtifactRecharge;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Recharging;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.Artifact;
import com.ansdoship.carbonizedpixeldungeon.items.quest.MetalShard;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfMysticalEnergy;
import com.ansdoship.carbonizedpixeldungeon.items.wands.CursedWand;
import com.ansdoship.carbonizedpixeldungeon.mechanics.Ballistica;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class WildEnergy extends TargetedSpell {

	{
		image = ItemSpriteSheet.WILD_ENERGY;
		usesTargeting = true;
	}

	//we rely on cursedWand to do fx instead
	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		CursedWand.cursedZap(this, curUser, bolt, callback);
	}

	@Override
	protected void affectTarget(Ballistica bolt, final Hero hero) {
		Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
		Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
		ScrollOfRecharging.charge(hero);

		hero.belongings.charge(1f);
		for (Buff b : hero.buffs()){
			if (b instanceof Artifact.ArtifactBuff){
				if (!((Artifact.ArtifactBuff) b).isCursed()) ((Artifact.ArtifactBuff) b).charge(hero, 4);
			}
		}

		Buff.affect(hero, Recharging.class, 8f);
		Buff.affect(hero, ArtifactRecharge.class).prolong( 8 ).ignoreHornOfPlenty = false;
	}

	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 100) / 5f));
	}
	
	public static class Recipe extends com.ansdoship.carbonizedpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfMysticalEnergy.class, MetalShard.class};
			inQuantity = new int[]{1, 1};

			cost = 4;

			output = WildEnergy.class;
			outQuantity = 5;
		}

	}
}
