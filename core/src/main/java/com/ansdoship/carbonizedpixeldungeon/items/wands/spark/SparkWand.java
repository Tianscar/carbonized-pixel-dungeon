package com.ansdoship.carbonizedpixeldungeon.items.wands.spark;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Barrier;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.MagicImmune;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.effects.MagicMissile;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.ansdoship.carbonizedpixeldungeon.items.wands.*;
import com.ansdoship.carbonizedpixeldungeon.mechanics.Ballistica;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.CellSelector;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.ui.QuickSlotButton;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SparkWand extends Wand {

    public static final float SPEED_FACTOR = 3.0f;

    public static final HashMap<Class<?extends Wand>, Class<?extends SparkWand>> wandToSpark = new HashMap<>();
    public static final HashMap<Class<?extends SparkWand>, Class<?extends Wand>> sparkToWand = new HashMap<>();
    static{
        wandToSpark.put(WandOfBlastWave.class, SparkWandOfBlastWave.class);
        sparkToWand.put(SparkWandOfBlastWave.class, WandOfBlastWave.class);

        wandToSpark.put(WandOfCorrosion.class, SparkWandOfCorrosion.class);
        sparkToWand.put(SparkWandOfCorrosion.class, WandOfCorrosion.class);

        wandToSpark.put(WandOfCorruption.class, SparkWandOfCorruption.class);
        sparkToWand.put(SparkWandOfCorruption.class, WandOfCorruption.class);

        wandToSpark.put(WandOfDisintegration.class, SparkWandOfDisintegration.class);
        sparkToWand.put(SparkWandOfDisintegration.class, WandOfDisintegration.class);

        wandToSpark.put(WandOfFireblast.class, SparkWandOfFireblast.class);
        sparkToWand.put(SparkWandOfFireblast.class, WandOfFireblast.class);

        wandToSpark.put(WandOfFrost.class, SparkWandOfFrost.class);
        sparkToWand.put(SparkWandOfFrost.class, WandOfFrost.class);

        wandToSpark.put(WandOfLightning.class, SparkWandOfLightning.class);
        sparkToWand.put(SparkWandOfLightning.class, WandOfLightning.class);

        wandToSpark.put(WandOfLivingEarth.class, SparkWandOfLivingEarth.class);
        sparkToWand.put(SparkWandOfLivingEarth.class, WandOfLivingEarth.class);

        wandToSpark.put(WandOfMagicMissile.class, SparkWandOfMagicMissile.class);
        sparkToWand.put(SparkWandOfMagicMissile.class, WandOfMagicMissile.class);

        wandToSpark.put(WandOfPrismaticLight.class, SparkWandOfPrismaticLight.class);
        sparkToWand.put(SparkWandOfPrismaticLight.class, WandOfPrismaticLight.class);

        wandToSpark.put(WandOfRegrowth.class, SparkWandOfRegrowth.class);
        sparkToWand.put(SparkWandOfRegrowth.class, WandOfRegrowth.class);

        wandToSpark.put(WandOfTransfusion.class, SparkWandOfTransfusion.class);
        sparkToWand.put(SparkWandOfTransfusion.class, WandOfTransfusion.class);

        wandToSpark.put(WandOfWarding.class, SparkWandOfWarding.class);
        sparkToWand.put(SparkWandOfWarding.class, WandOfWarding.class);
    }

    public static final String AC_SPARK	= "SPARK";

    protected int sparkBuffedLvl = 0;

    protected boolean advanced = false;

    public boolean advanced() {
        return advanced;
    }

    public void advance() {
        advanced = true;
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (advanced && (curCharges > 0 || !curChargeKnown)) {
            actions.add( AC_SPARK );
        }

        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals( AC_ZAP )) {

            curUser = hero;
            curItem = this;
            GameScene.selectCell( zapper );

        }
        else if (action.equals( AC_SPARK )) {

            curUser = hero;
            curItem = this;
            GameScene.selectCell( sparker );

        }
    }

    public boolean tryToSpark(Hero owner, int target) {

        if (owner.buff(MagicImmune.class) != null){
            GLog.w( Messages.get(this, "no_magic") );
            return false;
        }

        int chargesPerCast = cursed ? 1 : chargesPerCast() * 2;
        if ( curCharges >= chargesPerCast) {
            return true;
        } else {
            GLog.w(Messages.get(this, "fizzles"));
            return false;
        }
    }

    @Override
    public int buffedLvl() {
        return super.buffedLvl() + sparkBuffedLvl;
    }

    protected static CellSelector.Listener sparker = new CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                //FIXME this safety check shouldn't be necessary
                //it would be better to eliminate the curItem static variable.
                final SparkWand curWand;
                if (curItem instanceof SparkWand) {
                    curWand = (SparkWand) Wand.curItem;
                } else {
                    return;
                }

                final Ballistica shot = new Ballistica( curUser.pos, target, curWand.collisionProperties(target));
                int cell = shot.collisionPos;

                if (target == curUser.pos || cell == curUser.pos) {
                    if (target == curUser.pos && curUser.hasTalent(Talent.SHIELD_BATTERY)) {
                        float shield = curUser.HT * (0.06f*curWand.curCharges);
                        if (curUser.pointsInTalent(Talent.SHIELD_BATTERY) == 2) shield *= 1.5f;
                        Buff.affect(curUser, Barrier.class).setShield(Math.round(shield));
                        curWand.curCharges = 0;
                        curUser.sprite.operate(curUser.pos);
                        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
                        ScrollOfRecharging.charge(curUser);
                        updateQuickslot();
                        curUser.spend(Actor.TICK);
                        return;
                    }
                    GLog.i( Messages.get(Wand.class, "self_target") );
                    return;
                }

                curUser.sprite.zap(cell);

                //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                if (curWand.tryToSpark(curUser, target)) {

                    curUser.busy();

                    if (curWand.cursed || (curUser.INT() < curWand.INTReq() && curUser.heroClass != HeroClass.MAGE) ||
                            (curUser.belongings.armor() != null && curUser.belongings.armor().metal)) {
                        if (!curWand.cursedKnown){
                            GLog.n(Messages.get(Wand.class, "curse_discover", curWand.name()));
                        }
                        CursedWand.cursedZap(curWand,
                                curUser,
                                new Ballistica(curUser.pos, target, Ballistica.MAGIC_BOLT),
                                new Callback() {
                                    @Override
                                    public void call() {
                                        curWand.wandUsed();
                                    }
                                });
                    } else {
                        curWand.sparkBuffedLvl = 3;
                        curWand.fx(shot, new Callback() {
                            public void call() {
                                curWand.onZap(shot);
                                curWand.sparkBuffedLvl = 0;
                                curWand.curCharges -= curWand.chargesPerCast();
                                curWand.wandUsed();
                            }
                        });
                    }
                    curWand.cursedKnown = true;

                }

            }
        }

        @Override
        public String prompt() {
            return Messages.get(Wand.class, "prompt");
        }
    };

    private static final String ADVANCED = "advanced";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( ADVANCED, advanced );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        advanced = bundle.getBoolean( ADVANCED );
    }

    @Override
    public void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.MAGIC_MISSILE,
                SPEED_FACTOR,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    protected int initialCharges() {
        return super.initialCharges() + 2;
    }

    @Override
    public void updateLevel() {
        maxCharges = Math.min( initialCharges() + level(), 12 );
        curCharges = Math.min( curCharges, maxCharges );
    }

}
