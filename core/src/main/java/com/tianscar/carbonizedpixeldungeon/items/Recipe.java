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

package com.tianscar.carbonizedpixeldungeon.items;

import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.tianscar.carbonizedpixeldungeon.items.bombs.Bomb;
import com.tianscar.carbonizedpixeldungeon.items.food.Blandfruit;
import com.tianscar.carbonizedpixeldungeon.items.food.MeatPie;
import com.tianscar.carbonizedpixeldungeon.items.food.StewedMeat;
import com.tianscar.carbonizedpixeldungeon.items.potions.AlchemicalCatalyst;
import com.tianscar.carbonizedpixeldungeon.items.potions.Potion;
import com.tianscar.carbonizedpixeldungeon.items.potions.brews.BlizzardBrew;
import com.tianscar.carbonizedpixeldungeon.items.potions.brews.CausticBrew;
import com.tianscar.carbonizedpixeldungeon.items.potions.brews.InfernalBrew;
import com.tianscar.carbonizedpixeldungeon.items.potions.brews.ShockingBrew;
import com.tianscar.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.tianscar.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.tianscar.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.tianscar.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.tianscar.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.tianscar.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.tianscar.carbonizedpixeldungeon.items.potions.elixirs.ElixirOfToxicEssence;
import com.tianscar.carbonizedpixeldungeon.items.potions.exotic.ExoticPotion;
import com.tianscar.carbonizedpixeldungeon.items.rings.Ring;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.Scroll;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.tianscar.carbonizedpixeldungeon.items.spells.Alchemize;
import com.tianscar.carbonizedpixeldungeon.items.spells.AquaBlast;
import com.tianscar.carbonizedpixeldungeon.items.spells.ArcaneCatalyst;
import com.tianscar.carbonizedpixeldungeon.items.spells.BeaconOfReturning;
import com.tianscar.carbonizedpixeldungeon.items.spells.CurseInfusion;
import com.tianscar.carbonizedpixeldungeon.items.spells.FeatherFall;
import com.tianscar.carbonizedpixeldungeon.items.spells.MagicalInfusion;
import com.tianscar.carbonizedpixeldungeon.items.spells.MagicalPorter;
import com.tianscar.carbonizedpixeldungeon.items.spells.PhaseShift;
import com.tianscar.carbonizedpixeldungeon.items.spells.ReclaimTrap;
import com.tianscar.carbonizedpixeldungeon.items.spells.Recycle;
import com.tianscar.carbonizedpixeldungeon.items.spells.WildEnergy;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.tianscar.pixeldungeonclasses.utils.Reflection;

import java.util.ArrayList;

public abstract class Recipe {
	
	public abstract boolean testIngredients(ArrayList<Item> ingredients);
	
	public abstract int cost(ArrayList<Item> ingredients);
	
	public abstract Item brew(ArrayList<Item> ingredients);
	
	public abstract Item sampleOutput(ArrayList<Item> ingredients);
	
	//subclass for the common situation of a recipe with static inputs and outputs
	public static abstract class SimpleRecipe extends Recipe {
		
		//*** These elements must be filled in by subclasses
		protected Class<?extends Item>[] inputs; //each class should be unique
		protected int[] inQuantity;
		
		protected int cost;
		
		protected Class<?extends Item> output;
		protected int outQuantity;
		//***
		
		//gets a simple list of items based on inputs
		public ArrayList<Item> getIngredients() {
			ArrayList<Item> result = new ArrayList<>();
			for (int i = 0; i < inputs.length; i++) {
				Item ingredient = Reflection.newInstance(inputs[i]);
				ingredient.quantity(inQuantity[i]);
				result.add(ingredient);
			}
			return result;
		}
		
		@Override
		public final boolean testIngredients(ArrayList<Item> ingredients) {
			
			int[] needed = inQuantity.clone();
			
			for (Item ingredient : ingredients){
				if (!ingredient.isIdentified()) return false;
				for (int i = 0; i < inputs.length; i++){
					if (ingredient.getClass() == inputs[i]){
						needed[i] -= ingredient.quantity();
						break;
					}
				}
			}
			
			for (int i : needed){
				if (i > 0){
					return false;
				}
			}
			
			return true;
		}
		
		public final int cost(ArrayList<Item> ingredients){
			return cost;
		}
		
		@Override
		public final Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			int[] needed = inQuantity.clone();
			
			for (Item ingredient : ingredients){
				for (int i = 0; i < inputs.length; i++) {
					if (ingredient.getClass() == inputs[i] && needed[i] > 0) {
						if (needed[i] <= ingredient.quantity()) {
							ingredient.quantity(ingredient.quantity() - needed[i]);
							needed[i] = 0;
						} else {
							needed[i] -= ingredient.quantity();
							ingredient.quantity(0);
						}
					}
				}
			}
			
			//sample output and real output are identical in this case.
			return sampleOutput(null);
		}
		
		//ingredients are ignored, as output doesn't vary
		public final Item sampleOutput(ArrayList<Item> ingredients){
			try {
				Item result = Reflection.newInstance(output);
				result.quantity(outQuantity);
				return result;
			} catch (Exception e) {
				CarbonizedPixelDungeon.reportException( e );
				return null;
			}
		}
	}
	
	
	//*******
	// Static members
	//*******

	private static Recipe[] variableRecipes = new Recipe[]{
			new LiquidMetal.Recipe()
	};
	
	private static Recipe[] oneIngredientRecipes = new Recipe[]{
			new AlchemistsToolkit.upgradeKit(),
			new Scroll.ScrollToStone(),
			new ArcaneResin.Recipe(),
			new MagicAlloy.Recipe(),
			new CarbonSteel.Recipe(),
			new StewedMeat.oneMeat(),
			new LiquidMetal.oneSteel()
	};
	
	private static Recipe[] twoIngredientRecipes = new Recipe[]{
			new Blandfruit.CookFruit(),
			new Bomb.EnhanceBomb(),
			new AlchemicalCatalyst.Recipe(),
			new ArcaneCatalyst.Recipe(),
			new ElixirOfArcaneArmor.Recipe(),
			new ElixirOfAquaticRejuvenation.Recipe(),
			new ElixirOfDragonsBlood.Recipe(),
			new ElixirOfIcyTouch.Recipe(),
			new ElixirOfMight.Recipe(),
			new ElixirOfHoneyedHealing.Recipe(),
			new ElixirOfToxicEssence.Recipe(),
			new BlizzardBrew.Recipe(),
			new InfernalBrew.Recipe(),
			new ShockingBrew.Recipe(),
			new CausticBrew.Recipe(),
			new Alchemize.Recipe(),
			new AquaBlast.Recipe(),
			new BeaconOfReturning.Recipe(),
			new CurseInfusion.Recipe(),
			new FeatherFall.Recipe(),
			new MagicalInfusion.Recipe(),
			new MagicalPorter.Recipe(),
			new PhaseShift.Recipe(),
			new ReclaimTrap.Recipe(),
			new Recycle.Recipe(),
			new WildEnergy.Recipe(),
			new StewedMeat.twoMeat(),
			new LiquidMetal.twoSteel()
	};
	
	private static Recipe[] threeIngredientRecipes = new Recipe[]{
			new Potion.SeedToPotion(),
			new ExoticPotion.PotionToExotic(),
			new ExoticScroll.ScrollToExotic(),
			new StewedMeat.threeMeat(),
			new LiquidMetal.threeSteel(),
			new MeatPie.Recipe()
	};
	
	public static Recipe findRecipe(ArrayList<Item> ingredients){

		for (Recipe recipe : variableRecipes){
			if (recipe.testIngredients(ingredients)){
				return recipe;
			}
		}

		if (ingredients.size() == 1){
			for (Recipe recipe : oneIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
			
		} else if (ingredients.size() == 2){
			for (Recipe recipe : twoIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
			
		} else if (ingredients.size() == 3){
			for (Recipe recipe : threeIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
		}
		
		return null;
	}
	
	public static boolean usableInRecipe(Item item) {
		return !item.cursed
				&& (!(item instanceof EquipableItem)
					|| (item instanceof AlchemistsToolkit && item.isIdentified())
					|| item instanceof MissileWeapon
					|| item instanceof MeleeWeapon && !(item instanceof MagesStaff)
					|| item instanceof Ring);
	}
}


