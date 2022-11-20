package com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.levels.Level;
import com.ansdoship.carbonizedpixeldungeon.sprites.CornerSheepSprite;

public class CornerSheep extends Sheep {

    {
        spriteClass = CornerSheepSprite.class;
    }

    /**
     *  -W-1 -W  -W+1
     *  -1    P  +1
     *  W-1   W  W+1
     */
    @Override
    public boolean interact(Char c) {

        int curPos = pos;
        int movPos = pos;
        Level level = Dungeon.level;
        int width = level.width();
        int posDif = Dungeon.hero.pos - curPos;

        if (posDif == 1) {
            movPos = curPos - 1;
        } else if(posDif == -1) {
            movPos = curPos + 1;
        } else if(posDif == width) {
            movPos = curPos - width;
        } else if(posDif == -width) {
            movPos = curPos + width;
        }

        else if(posDif == -width + 1) {
            movPos = curPos + width - 1;

        } else if(posDif == -width - 1) {
            movPos = curPos + width + 1;

        } else if(posDif == width + 1) {
            movPos = curPos - (width+1);

        } else if(posDif == width - 1) {
            movPos = curPos - (width-1);
        }

        if (movPos != pos && (level.passable[movPos] || level.avoid[movPos]) && Actor.findChar(movPos) == null) {

            moveSprite(curPos, movPos);
            move(movPos);

            Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
            Dungeon.hero.move(curPos);

            return super.interact(c);
        }

        return super.interact(c);
    }
}
