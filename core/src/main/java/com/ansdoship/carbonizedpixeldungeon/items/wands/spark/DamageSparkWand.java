package com.ansdoship.carbonizedpixeldungeon.items.wands.spark;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.WandEmpower;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public abstract class DamageSparkWand extends SparkWand {

    public int min(){
        return min(buffedLvl());
    }

    public abstract int min(int lvl);

    public int max(){
        return max(buffedLvl());
    }

    public abstract int max(int lvl);

    public int damageRoll(){
        return damageRoll(buffedLvl());
    }

    public int damageRoll(int lvl){
        int dmg = Random.NormalIntRange(min(lvl), max(lvl));
        WandEmpower emp = Dungeon.hero.buff(WandEmpower.class);
        if (emp != null){
            dmg += emp.dmgBoost;
            emp.left--;
            if (emp.left <= 0) {
                emp.detach();
            }
            Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG, 0.75f, 1.2f);
        }
        return dmg;
    }

    @Override
    public String statsDesc() {
        if (levelKnown)
            return Messages.get(this, "stats_desc", min(), max());
        else
            return Messages.get(this, "stats_desc", min(0), max(0));
    }

}
