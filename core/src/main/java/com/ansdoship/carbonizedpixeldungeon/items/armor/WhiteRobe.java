package com.ansdoship.carbonizedpixeldungeon.items.armor;

import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;

public class WhiteRobe extends Robe {

    {
        image = ItemSpriteSheet.ROBE_WHITE;

        bones = false; //Finding them in bones would be semi-frequent and disappointing.
    }

    public WhiteRobe() {
        super(1);
    }

}
