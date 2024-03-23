package com.tianscar.carbonizedpixeldungeon.actors.mobs;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.items.Gold;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.GoldenSword;
import com.tianscar.carbonizedpixeldungeon.sprites.GoldenStatueSprite;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;

public class GoldenStatue extends Statue {

    {
        spriteClass = GoldenStatueSprite.class;
    }

    private Item gold;

    public GoldenStatue() {
        this( null );
    }

    public GoldenStatue( Gold gold ) {

        this.gold = gold == null ? new Gold().random() : gold;

        weapon = new GoldenSword();
        weapon.random();
        weapon.upgrade(2);

        HP = HT = 15 + Dungeon.depth * 5;
        defenseSkill = 4 + Dungeon.depth;
    }

    private static final String GOLD = "gold";

    public int gold() {
        return gold.quantity();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( GOLD, gold );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        gold = (Item) bundle.get( GOLD );
    }

    @Override
    public void die(Object cause) {
        Dungeon.level.drop( gold, pos ).sprite.drop();

        super.die(cause);
    }

}
