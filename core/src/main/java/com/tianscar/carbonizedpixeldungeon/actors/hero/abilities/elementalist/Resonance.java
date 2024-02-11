package com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.elementalist;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Barrier;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.FlavourBuff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Invisibility;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Spellweave;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.items.armor.ClassArmor;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.ui.HeroIcon;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

// TODO Re-design the effects
public class Resonance extends ArmorAbility {

    {
        baseChargeUse = 50;
    }

    @Override
    public float chargeUse( Hero hero ) {
        float chargeUse = super.chargeUse(hero);
        if (hero.buff(Resonance.DoubleResonanceTracker.class) != null) {
            //reduced charge use by 20%/36%/50%/60%
            chargeUse *= Math.pow(0.795, hero.pointsInTalent(Talent.DOUBLE_RESONANCE));
        }
        return chargeUse;
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {

        hero.busy();
        hero.sprite.zap(hero.pos, new Callback() {
            @Override
            public void call() {

                GameScene.flash( 0x80FFFFFF );
                Sample.INSTANCE.play( Assets.Sounds.BLAST );

                int charsHit = 0;
                boolean killed = false;
                for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                    if (Dungeon.level.heroFOV[mob.pos]) {
                        charsHit ++;

                        mob.damage(Random.NormalIntRange(15, 25), this);

                        if (!mob.isAlive() && !killed) killed = true;
                    }
                }

                if (killed && hero.hasTalent(Talent.ECHOING_RESONANCE) &&
                        Random.Int(4) < hero.pointsInTalent(Talent.ECHOING_RESONANCE)) {
                    for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
                        if (Dungeon.level.heroFOV[mob.pos]) {
                            charsHit ++;

                            mob.damage(Random.NormalIntRange(15, 25), this);

                        }
                    }
                }

                charsHit = Math.min(5, charsHit);
                if (charsHit > 0) {
                    if (hero.subClass == HeroSubClass.SPELLWEAVER) Buff.affect(hero, Spellweave.class).stack(charsHit);
                    if (hero.hasTalent(Talent.RESONANCE_BARRIER)){
                        Buff.affect(hero, Barrier.class).setShield(charsHit*2*hero.pointsInTalent(Talent.RESONANCE_BARRIER));
                    }
                }

                Invisibility.dispel();
                hero.spendAndNext(Actor.TICK);

                if (hero.buff(DoubleResonanceTracker.class) != null){
                    hero.buff(DoubleResonanceTracker.class).detach();
                } else {
                    if (hero.hasTalent(Talent.DOUBLE_RESONANCE)) {
                        Buff.affect(hero, DoubleResonanceTracker.class, 5);
                    }
                }

            }
        });

    }

    public static class DoubleResonanceTracker extends FlavourBuff {}

    @Override
    public int icon() {
        return HeroIcon.RESONANCE;
    }

    @Override
    public Talent[] talents() {
        return new Talent[] { Talent.ECHOING_RESONANCE, Talent.RESONANCE_BARRIER, Talent.DOUBLE_RESONANCE, Talent.HEROIC_ENERGY };
    }

}
