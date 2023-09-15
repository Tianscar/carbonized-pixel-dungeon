package com.tianscar.carbonizedpixeldungeon.items.weapon.melee.ranged;

import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;

public class LightCrossbow extends Crossbow {

    {
        image = ItemSpriteSheet.LIGHT_CROSSBOW;

        tier = 2;
        twoHanded = false;
    }

    @Override
    protected int initialCharges() {
        return 2;
    }

}
