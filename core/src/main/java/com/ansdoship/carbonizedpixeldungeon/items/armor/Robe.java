package com.ansdoship.carbonizedpixeldungeon.items.armor;

import com.ansdoship.carbonizedpixeldungeon.Challenges;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.Electricity;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.ToxicGas;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.*;
import com.ansdoship.carbonizedpixeldungeon.items.armor.glyphs.AntiMagic;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;

import java.text.DecimalFormat;
import java.util.HashSet;

public abstract class Robe extends Armor {

    public static final HashSet<Class> RESISTS = new HashSet<>();
    static {
        RESISTS.add( Burning.class );
        RESISTS.add( Chill.class );
        RESISTS.add( Frost.class );
        RESISTS.add( Ooze.class );
        RESISTS.add( Paralysis.class );
        RESISTS.add( Poison.class );
        RESISTS.add( Corrosion.class );

        RESISTS.add( ToxicGas.class );
        RESISTS.add( Electricity.class );

        RESISTS.addAll( AntiMagic.RESISTS );
    }

    public Robe(int tier) {
        super(tier);
    }

    @Override
    public int STRReq(int lvl) {
        return STRReq(0, lvl);
    }

    @Override
    public int DRMin(int lvl) {
        if (tier > 2) return DRMin(tier-1, lvl, augment);
        else return super.DRMin(lvl);
    }

    @Override
    public int DRMax(int lvl) {
        if (tier > 2) return DRMax(tier-1, lvl, augment);
        return super.DRMax(lvl);
    }

    public int INTReq(){
        return INTReq(level());
    }

    public int INTReq(int lvl) {
        return STRReq(tier, lvl);
    }

    public float resist( Class effect ) {
        return resist( tier, buffedLvl(), effect );
    }

    protected static float resist( int tier, int lvl, Class effect ) {

        for (Class c : RESISTS) {
            if (c.isAssignableFrom(effect)) {
                return resist(tier, lvl);
            }
        }

        return 1f;
    }

    protected static float resist( int tier, int lvl ) {
        if (Dungeon.isChallenged(Challenges.Challenge.NO_ARMOR)) {
            return (float) Math.pow(1 - 0.01 - 0.002 * (tier - 1), 1 + lvl) - 0.02f - 0.01f * tier;
        } else return (float) Math.pow(1 - 0.01 * tier, 1 + lvl) - 0.10f - 0.05f * tier;
    }

    @Override
    public String statsInfo() {
        String info;
        if (levelKnown) {
            info = Messages.get(Robe.class, "curr_absorb", DRMin(), DRMax(),
                    new DecimalFormat("#.##").format(100f * (1f - resist(tier, buffedLvl()))),
                    STRReq(), INTReq());

            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Armor.class, "too_heavy");
            }
            if (INTReq() > Dungeon.hero.INT()) {
                info += " " + Messages.get(Robe.class, "int_not_enough");
            }
        } else {
            info = Messages.get(Robe.class, "avg_absorb", DRMin(0), DRMax(0),
                    new DecimalFormat("#.##").format(100f * (1f - resist(tier, 0))),
                    STRReq(0), INTReq(0));

            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(Armor.class, "probably_too_heavy");
            }
            if (INTReq(0) > Dungeon.hero.INT()) {
                info += " " + Messages.get(Robe.class, "probably_int_not_enough");
            }
        }
        return info;
    }
    
}
