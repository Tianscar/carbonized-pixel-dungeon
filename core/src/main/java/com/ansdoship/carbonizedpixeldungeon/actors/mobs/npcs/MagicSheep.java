package com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs;

import com.ansdoship.pixeldungeonclasses.utils.Random;

public class MagicSheep extends Sheep {

    public float lifespan;

    private boolean initialized = false;

    @Override
    protected boolean act() {
        if (initialized) {
            HP = 0;

            destroy();
            sprite.die();

        } else {
            initialized = true;
            spend( lifespan + Random.Float(2) );
        }
        return true;
    }

}
