package com.tianscar.carbonizedpixeldungeon.sprites;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.noosa.TextureFilm;

public class GoldenStatueSprite extends MobSprite {

    public GoldenStatueSprite() {
        super();

        texture( Assets.Sprites.GOLDEN );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 1, 1 );

        run = new Animation( 15, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 12, false );
        attack.frames( frames, 8, 9, 10 );

        die = new Animation( 5, false );
        die.frames( frames, 11, 12, 13, 14, 15, 15 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xf3f354;
    }

}
