package com.tianscar.carbonizedpixeldungeon.items;

import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class CarbonSteel extends Item {

    {
        image = ItemSpriteSheet.CARBON_STEEL;

        stackable = true;

        defaultAction = AC_THROW;
        usesTargeting = true;

        bones = true;
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
    public int value() {
        return Math.max(1, quantity/2);
    }

    public static class Recipe extends com.tianscar.carbonizedpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0) instanceof MeleeWeapon && ingredients.get(0).isIdentified();
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 5;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Item result = sampleOutput(ingredients);

            ingredients.get(0).quantity(0);

            return result;
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            MeleeWeapon m = (MeleeWeapon) ingredients.get(0);
            return new CarbonSteel().quantity(Math.round((5*(m.tier+1))*(float) Math.pow(2, m.level())));
        }

    }

}
