package com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.sprites.SwitchSheepSprite;
import com.ansdoship.carbonizedpixeldungeon.utils.BArray;
import com.ansdoship.pixeldungeonclasses.utils.PathFinder;

public class SwitchSheep extends Sheep {

    {
        spriteClass = SwitchSheepSprite.class;
    }

    @Override
    public boolean interact(Char c) {

        int curPos = pos;

        //warp instantly with it in this case
        PathFinder.buildDistanceMap(c.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (PathFinder.distance[pos] == Integer.MAX_VALUE) {
            return super.interact(c);
        }
        sprite.move(curPos, c.pos);
        move(c.pos);
        Dungeon.hero.sprite.move(c.pos, curPos);
        Dungeon.hero.move(curPos);
        return super.interact(c);
    }

}
