package com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.CustomerSprite;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.windows.WndInfoMob;

public abstract class Customer extends NPC {

    public static Customer of(HeroClass cl ) {
        switch (cl) {
            case WARRIOR: return new Warrior();
            case MAGE: return new Mage();
            case ROGUE: return new Rogue();
            case HUNTRESS: return new Huntress();
            case ELEMENTALIST: return new Elementalist();
            default: return new Rogue();
        }
    }

    protected HeroClass cl;

    {
        properties.add(Property.IMMOVABLE);
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );

        if (c != Dungeon.hero){
            return true;
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndInfoMob(Customer.this));
            }
        });

        return true;
    }

    @Override
    public String name() {
        return cl.title();
    }

    @Override
    public String description() {
        return cl.isUnlocked() ? cl.desc() : cl.unlockMsg();
    }

    public static class Warrior extends Customer {
        {
            cl = HeroClass.WARRIOR;
            spriteClass = CustomerSprite.Warrior.class;
        }
    }

    public static class Mage extends Customer {
        {
            cl = HeroClass.MAGE;
            spriteClass = CustomerSprite.Mage.class;
        }
    }

    public static class Rogue extends Customer {
        {
            cl = HeroClass.ROGUE;
            spriteClass = CustomerSprite.Rogue.class;
        }
    }

    public static class Huntress extends Customer {
        {
            cl = HeroClass.HUNTRESS;
            spriteClass = CustomerSprite.Huntress.class;
        }
    }

    public static class Elementalist extends Customer {
        {
            cl = HeroClass.ELEMENTALIST;
            spriteClass = CustomerSprite.Elementalist.class;
        }
    }

}
