package com.ansdoship.carbonizedpixeldungeon.items.armor;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;

import java.text.DecimalFormat;

public abstract class HeavyArmor extends Armor {

    {
        metal = true;
    }

    public HeavyArmor(int tier) {
        super(tier);
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier+1, lvl);
    }

    @Override
    public int DRMin(int lvl) {
        return DRMin(tier+1, lvl, augment);
    }

    @Override
    public int DRMax(int lvl) {
        return DRMax(tier+1, lvl, augment);
    }

    public float resist(Class effect ) {
        return resist( tier, buffedLvl(), effect );
    }

    protected static float resist( int tier, int lvl, Class effect ) {
        return Robe.resist(tier-2, lvl, effect);
    }

    protected static float resist( int tier, int lvl ) {
        return Robe.resist(tier-2, lvl);
    }

    @Override
    public String statsInfo() {
        String info;
        if (levelKnown) {
            info = Messages.get(HeavyArmor.class, "curr_absorb", DRMin(), DRMax(),
                    new DecimalFormat("#.##").format(100f * (1f - resist(tier, buffedLvl()))),
                    STRReq());

            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(HeavyArmor.class, "too_heavy");
            }
        } else {
            info = Messages.get(HeavyArmor.class, "avg_absorb", DRMin(0), DRMax(0),
                    new DecimalFormat("#.##").format(100f * (1f - resist(tier, 0))),
                    STRReq(0));

            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(HeavyArmor.class, "probably_too_heavy");
            }
        }
        return info;
    }

}
