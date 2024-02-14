package com.tianscar.carbonizedpixeldungeon.sprites;

import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.tianscar.carbonizedpixeldungeon.noosa.TextureFilm;

public abstract class CustomerSprite extends MobSprite {

    public static Class<? extends CustomerSprite> spriteClass( HeroClass cl ) {
        switch (cl) {
            case WARRIOR: return Warrior.class;
            case MAGE: return Mage.class;
            case ROGUE: return Rogue.class;
            case HUNTRESS: return Huntress.class;
            case ELEMENTALIST: return Elementalist.class;
            default: return Rogue.class;
        }
    }

    protected CustomerSprite( HeroClass cl ) {
        super();

        texture( cl.spritesheet() );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 1, true );
        idle.frames( frames, 21, 21, 21, 22, 21, 21, 22, 22 );

        run = new Animation( 20, true );
        run.frames( frames, 0 );

        die = new Animation( 20, false );
        die.frames( frames, 0 );

        play( idle );
    }

    public static class Warrior extends CustomerSprite {
        public Warrior() {
            super(HeroClass.WARRIOR);
        }
    }

    public static class Mage extends CustomerSprite {
        public Mage() {
            super(HeroClass.MAGE);
        }
    }

    public static class Rogue extends CustomerSprite {
        public Rogue() {
            super(HeroClass.ROGUE);
        }
    }

    public static class Huntress extends CustomerSprite {
        public Huntress() {
            super(HeroClass.HUNTRESS);
        }
    }

    public static class Elementalist extends CustomerSprite {
        public Elementalist() {
            super(HeroClass.ELEMENTALIST);
        }
    }

}
