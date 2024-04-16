package com.tianscar.carbonizedpixeldungeon;

import com.tianscar.carbonizedpixeldungeon.actors.mobs.Elemental;

public class ElementalStatistics {

    private static final int FIRE    = 1;
    private static final int NEWBORN = 1 << 1;
    private static final int FROST   = 1 << 2;
    private static final int SHOCK   = 1 << 3;
    private static final int CHAOS   = 1 << 4;

    private static int mask(Class<? extends Elemental> clazz) {
        if (clazz.equals(Elemental.FireElemental.class)) {
            return FIRE;
        }
        else if (clazz.equals(Elemental.NewbornFireElemental.class)) {
            return NEWBORN;
        }
        else if (clazz.equals(Elemental.FrostElemental.class)) {
            return FROST;
        }
        else if (clazz.equals(Elemental.ShockElemental.class)) {
            return SHOCK;
        }
        else if (clazz.equals(Elemental.ChaosElemental.class)) {
            return CHAOS;
        }
        else throw new IllegalArgumentException("Unknown elemental");
    }

    public static void registerSlain(Class<? extends Elemental> clazz) {
        Statistics.elementalKindsSlain |= mask(clazz);
    }

    public static boolean isSlain(Class<? extends Elemental> clazz) {
        int mask = mask(clazz);
        return (Statistics.elementalKindsSlain & mask) != 0;
    }

    public static int elementalKindsSlain() {
        int kinds = 0;
        if ((Statistics.elementalKindsSlain & FIRE) != 0) kinds ++;
        if ((Statistics.elementalKindsSlain & NEWBORN) != 0) kinds ++;
        if ((Statistics.elementalKindsSlain & FROST) != 0) kinds ++;
        if ((Statistics.elementalKindsSlain & SHOCK) != 0) kinds ++;
        if ((Statistics.elementalKindsSlain & CHAOS) != 0) kinds ++;
        return kinds;
    }

}
