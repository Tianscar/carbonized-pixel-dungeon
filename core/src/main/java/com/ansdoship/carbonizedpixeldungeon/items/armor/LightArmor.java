package com.ansdoship.carbonizedpixeldungeon.items.armor;

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;

public class LightArmor extends Armor {

    public LightArmor(int tier) {
        super(tier);
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(tier, lvl);
    }

    protected static int STRReq(int tier, int lvl) {
        lvl = Math.max(0, lvl);

        //strength req decreases at +1,+3,+6,+10,etc.
        return (6 + Math.round(tier * 2)) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
    }

    public int DEXReq() {
        return DEXReq(buffedLvl());
    }

    public int DEXReq(int lvl) {
        return DEXReq(tier, lvl);
    }

    protected static int DEXReq(int tier, int lvl) {
        return STRReq(tier, lvl);
    }

    @Override
    public String statsInfo() {
        String info;
        if (levelKnown) {
            info = Messages.get(LightArmor.class, "curr_absorb", DRMin(), DRMax(), STRReq(), DEXReq());

            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Armor.class, "too_heavy");
            }
            if (DEXReq() > Dungeon.hero.DEX()) {
                info += " " + Messages.get(LightArmor.class, "too_bulky");
            }
            if (DEXReq() < Dungeon.hero.DEX()) {
                info += " " + Messages.get(LightArmor.class, "excess_dex");
            }
        } else {
            info = Messages.get(LightArmor.class, "avg_absorb", DRMin(0), DRMax(0), STRReq(0), DEXReq(0));

            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(Armor.class, "probably_too_heavy");
            }
            if (DEXReq(0) > Dungeon.hero.DEX()) {
                info += " " + Messages.get(LightArmor.class, "probably_too_bulky");
            }
        }
        return info;
    }

}
