package com.ansdoship.carbonizedpixeldungeon.items.armor;

import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;

public class MithrilArmor extends LightArmor {

    {
        image = ItemSpriteSheet.ARMOR_MITHRIL;
        metal = true;
    }

    public MithrilArmor() {
        super( 5 );
    }

}
