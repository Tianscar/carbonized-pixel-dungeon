package com.tianscar.carbonizedpixeldungeon.items.weapon.melee.ranged;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.PDSettings;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Momentum;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.bags.Bag;
import com.tianscar.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.tianscar.carbonizedpixeldungeon.messages.Languages;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.CellSelector;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.utils.Reflection;
import com.tianscar.carbonizedpixeldungeon.windows.WndBag;

import java.util.ArrayList;
import java.util.Collection;

public abstract class RangedWeapon extends MeleeWeapon {

    public Class<? extends MissileWeapon> missileType = MissileWeapon.class;

    public static final String AC_SHOOT	  = "SHOOT";
    public static final String AC_LOAD	  = "LOAD";
    public static final String AC_UNLOAD  = "UNLOAD";
    public static final String AC_EMPTY   = "EMPTY";

    {
        defaultAction = AC_SHOOT;
    }

    public float timeToLoad = 1f;

    public float loadDelayFactor(Char owner) {
        return timeToLoad * (1f/speedMultiplier(owner));
    }

    public float timeToUnload = 0.5f;
    public float unloadDelayFactor(Char owner) {
        return timeToUnload * (1f/speedMultiplier(owner));
    }

    public float timeToShoot = 1f;

    public float shootDelayFactor(Char owner) {
        return timeToShoot;
    }

    public boolean canMissileSurpriseAttack( Hero hero ) {
        return canSurpriseAttack( hero );
    }

    public float missileAccuracyFactor( Char owner ) {
        float accFactor = super.accuracyFactor(owner);
        if (owner instanceof Hero && owner.buff(Momentum.class) != null && owner.buff(Momentum.class).freerunning()){
            accFactor *= 1f + 0.2f*((Hero) owner).pointsInTalent(Talent.PROJECTILE_MOMENTUM);
        }
        return accFactor;
    }

    public boolean needEquip = true;

    protected String loadSound = Assets.Sounds.UNLOCK;
    protected float loadSoundPitch = 1.1f;
    protected String unloadSound = Assets.Sounds.UNLOCK;
    protected float unloadSoundPitch = 1.1f;

    public int maxMissiles = initialCharges();

    protected abstract int initialCharges();

    public ArrayList<MissileWeapon> missiles = new ArrayList<>(maxMissiles);
    private static final String MISSILES = "missiles";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( MISSILES, missiles );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        missiles = new ArrayList<>((Collection<? extends MissileWeapon>) ((Collection<?>) bundle.getCollection(MISSILES)));
        for (MissileWeapon missile : missiles) {
            missile.shooter = this;
        }
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (!missiles.isEmpty()) {
            actions.add( AC_SHOOT );
            actions.add( AC_UNLOAD );
            actions.add( AC_EMPTY );
        }
        if (missiles.size() < maxMissiles) {
            actions.add( AC_LOAD );
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {

        super.execute(hero, action);

        if (action.equals(AC_SHOOT)) {

            if (needEquip && !isEquipped(hero)) {
                usesTargeting = false;
                GLog.i( Messages.get(this, "need_to_equip") );
            }
            else if (missiles.isEmpty()) {
                usesTargeting = false;
                GLog.w( Messages.get(this, "no_missile") );
            }
            else {
                usesTargeting = true;
                GameScene.selectCell( shooter );
            }

        }
        else if (action.equals(AC_UNLOAD)) {
            unload();
        }
        else if (action.equals(AC_EMPTY)) {
            empty();
        }
        else if (action.equals(AC_LOAD)) {

            itemSelector.weapon = this;
            GameScene.selectItem( itemSelector );

        }
    }

    protected static RangedWeaponSelector itemSelector = new RangedWeaponSelector();

    protected static class RangedWeaponSelector extends WndBag.ItemSelector {

        protected RangedWeapon weapon;

        @Override
        public String textPrompt() {
            return Messages.get(RangedWeapon.class, "load_prompt");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return MagicalHolster.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return weapon.missileType.isAssignableFrom(item.getClass());
        }

        @Override
        public void onSelect(Item item) {
            if (item != null && weapon.missileType.isAssignableFrom(item.getClass())) {

                weapon.load(weapon.missileType.cast(item));

            }
        }
    }

    protected void empty() {

        int amount = missiles.size();
        if (amount == 1) {
            unload();
            return;
        }

        Hero hero = Dungeon.hero;
        hero.sprite.operate( hero.pos );
        hero.busy();
        Sample.INSTANCE.play(unloadSound, 2, unloadSoundPitch);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < amount; i ++) {
            MissileWeapon missile = popLastMissile();
            missile.shooter = null;

            if (!missile.collect()){
                Dungeon.level.drop(missile, hero.pos);
            }

            builder.append(missile.name()).append(", ");
        }
        String missilesString = builder.substring(0, builder.length() - 2);
        if (PDSettings.language() == Languages.SI_CHINESE || PDSettings.language() == Languages.TR_CHINESE)
            missilesString = missilesString.replace(", ", "，");
        GLog.i(Messages.get(this, "you_now_unloaded", missilesString, name()));
        hero.spendAndNext( unloadDelayFactor( hero ) * amount );

        updateQuickslot();

    }

    protected void unload() {

        Hero hero = Dungeon.hero;
        hero.sprite.operate( hero.pos );
        hero.busy();
        Sample.INSTANCE.play(unloadSound, 2, unloadSoundPitch);

        MissileWeapon missile = popLastMissile();
        missile.shooter = null;

        GLog.i(Messages.get(this, "you_now_unloaded", missile.name(), name()));
        hero.spendAndNext( unloadDelayFactor( hero ) );
        if (!missile.collect()) {
            Dungeon.level.drop(missile, hero.pos);
        }

        updateQuickslot();

    }

    protected void load(MissileWeapon missile) {

        Hero hero = Dungeon.hero;
        hero.sprite.operate( hero.pos );
        hero.busy();
        hero.spend(loadDelayFactor(hero));
        Sample.INSTANCE.play(loadSound, 2, loadSoundPitch);

        MissileWeapon newMissile = Reflection.newInstance(missile.getClass());
        newMissile.shooter = this;
        missiles.add(newMissile);

        GLog.i(Messages.get(this, "you_now_loaded", missile.name(), name()));

        missile.detach(hero.belongings.backpack);

        updateQuickslot();

    }

    protected MissileWeapon popFirstMissile() {
        return missiles.remove(0);
    }

    protected MissileWeapon popLastMissile() {
        return missiles.remove(missiles.size() - 1);
    }

    protected CellSelector.Listener shooter = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                popFirstMissile().cast(curUser, target);
            }
        }
        @Override
        public String prompt() {
            return Messages.get(RangedWeapon.class, "prompt");
        }
    };

    public abstract void missileThrowSound();

    public int missileMin(MissileWeapon missile, int missileLvl) {
        return  2 * tier +                                      //base
                (tier == 1 ? buffedLvl() : 2*buffedLvl()) +     //level scaling
                (missile.tier == 1 ? missileLvl : 2*missileLvl);
    }

    public int missileMax(MissileWeapon missile, int missileLvl) {
        return  5*(tier+1) +               //base
                buffedLvl()*(tier+1) +     //level scaling
                (missile.tier == 1 ? 2*missileLvl : tier*missileLvl);
    }

    @Override
    public String status() {
        return missiles.size() + "/" + maxMissiles;
    }

    @Override
    public String info() {
        String info = super.info();

        if (cursed && cursedKnown) {
            info += "\n\n" + Messages.get( this, "cursed" );
        }
        if (!missiles.isEmpty()) {

            StringBuilder builder = new StringBuilder();
            for (MissileWeapon missile : missiles) {
                builder.append("_").append(missile.name()).append("_, ");
            }
            String missilesText;
            missilesText = builder.substring(0, builder.length() - 2);
            info += "\n\n" + Messages.get( this, "missiles", Messages.replaceComma(missilesText) );

            MissileWeapon missile = missiles.get(0);
            int level = 0;
            if (!isIdentified()) {
                level = level();
                //temporarily sets the level of the shooter to 0 for IDing purposes
                level(0);
            }
            info += "\n\n" + Messages.get( this, "missile_stats",
                    missile.name(),
                    Math.round(missile.augment.damageFactor(missile.min())),
                    Math.round(missile.augment.damageFactor(missile.max()))) + missile.desc();
            if (!isIdentified()) {
                level(level);
            }
            info += "\n\n" + Messages.get(MissileWeapon.class, "durability");

            if (missile.durabilityPerUse() > 0){
                info += " " + Messages.get(missile, "uses_left",
                        (int)Math.ceil(missile.durability/missile.durabilityPerUse()),
                        (int)Math.ceil(MissileWeapon.MAX_DURABILITY/missile.durabilityPerUse()));
            } else {
                info += " " + Messages.get(missile, "unlimited_uses");
            }
        }
        return info;
    }

    public int missileProc( Char attacker, Char defender, int damage ) {
        return proc( attacker, defender, damage );
    }

}
