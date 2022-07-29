package com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.ranged;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Badges;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.PDSettings;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Momentum;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.armor.Armor;
import com.ansdoship.carbonizedpixeldungeon.items.bags.Bag;
import com.ansdoship.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.Weapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.curses.Wayward;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.ansdoship.carbonizedpixeldungeon.messages.Languages;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.CellSelector;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndBag;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Reflection;

import java.util.ArrayList;
import java.util.Collection;

public abstract class RangedWeapon extends MeleeWeapon {

    public Class<? extends MissileWeapon> missileClass = MissileWeapon.class;

    public static final String AC_SHOOT	  = "SHOOT";
    public static final String AC_LOAD	  = "LOAD";
    public static final String AC_UNLOAD  = "UNLOAD";
    public static final String AC_EMPTY   = "EMPTY";

    {
        defaultAction = AC_SHOOT;
        bones = false;
    }

    public float time2Load = 1f;

    public float loadDelayFactor(Char owner) {
        return time2Load * (1f/speedMultiplier(owner));
    }

    public float time2Unload = 0.5f;
    public float unloadDelayFactor(Char owner) {
        return time2Unload * (1f/speedMultiplier(owner));
    }

    public float timeToShoot = 1f;

    public float shootDelayFactor(Char owner) {
        return timeToShoot;
    }

    public int DEXReq() {
        return DEXReq(level());
    }

    public int DEXReq(int lvl) {
        return DEXReq(tier, lvl);
    }

    protected static int DEXReq(int tier, int lvl){
        lvl = Math.max(0, lvl);

        //dexterity req decreases at +1,+3,+6,+10,etc.
        return (8 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2 - 4;
    }

    public boolean canMissileSurpriseAttack(Hero hero) {
        return hero.STR() >= STRReq() && hero.DEX() >= DEXReq();
    }

    public float missileAccuracyFactor( Char owner ) {

        float ACC = this.ACC;

        int encumbrance = 0;

        if( owner instanceof Hero ){
            encumbrance = DEXReq() - ((Hero)owner).DEX();
        }

        if (hasEnchant(Wayward.class, owner))
            encumbrance = Math.max(2, encumbrance+2);

        ACC = encumbrance > 0 ? (float)(ACC / Math.pow( 1.5, encumbrance )) : ACC;
        if (owner instanceof Hero && ((Hero) owner).STR() < STRReq()) ACC = Math.min(ACC, super.accuracyFactor(owner));
        return ACC;
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
        missiles = new ArrayList<>((Collection<MissileWeapon>) ((Collection<?>) bundle.getCollection(MISSILES)));
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
            return weapon.missileClass.isAssignableFrom(item.getClass());
        }

        @Override
        public void onSelect(Item item) {
            if (item != null && weapon.missileClass.isAssignableFrom(item.getClass())) {

                weapon.load(weapon.missileClass.cast(item));

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

        String missilesString = "";
        for (int i = 0; i < amount; i ++) {
            MissileWeapon missile = popLastMissile();
            missile.shooter = null;

            if (!missile.collect()){
                Dungeon.level.drop(missile, hero.pos);
            }

            missilesString += missile.name() + ", ";
        }
        missilesString = missilesString.substring(0, missilesString.length() - 2);
        if (PDSettings.language() == Languages.CHINESE || PDSettings.language() == Languages.TR_CHINESE)
            missilesString = missilesString.replaceAll(", ", "、");
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
        if (!missile.collect()){
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
        addMissile(newMissile);

        GLog.i(Messages.get(this, "you_now_loaded", missile.name(), name()));

        missile.detach(hero.belongings.backpack);

        updateQuickslot();

    }

    protected boolean addMissile(MissileWeapon missile) {
        if (missiles.size() < maxMissiles) {
            missiles.add(missile);
            return true;
        }
        else return false;
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

    public abstract int missileAddMin();

    public abstract int missileAddMax();

    @Override
    public String status() {
        return missiles.size() + "/" + maxMissiles;
    }

    @Override
    public String info() {
        String info = super.info();

        info += "\n\n" + Messages.get( this, levelKnown ? "stats_known" : "stats_unknown", DEXReq());
        if (DEXReq() > Dungeon.hero.DEX()) {
            info += " " + Messages.get(MissileWeapon.class, levelKnown ? "too_bulky" : "probably_too_bulky");
        }

        if (cursed && cursedKnown) {
            info += "\n\n" + Messages.get( this, "cursed" );
        }
        if (!missiles.isEmpty()) {

            String missilesString = "";
            for (MissileWeapon missile : missiles) {
                missilesString += "_" + missile.name() + "_, ";
            }
            missilesString = missilesString.substring(0, missilesString.length() - 2);
            if (PDSettings.language() == Languages.CHINESE || PDSettings.language() == Languages.TR_CHINESE)
                missilesString = missilesString.replaceAll(", ", "、");
            info += "\n\n" + Messages.get( this, "missiles", missilesString );

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

    public void missileProc(Char attacker, Char defender, int damage) {
        if (!levelKnown && attacker == Dungeon.hero) {
            float uses = Math.min( availableUsesToID, Talent.itemIDSpeedFactor(Dungeon.hero, this) );
            availableUsesToID -= uses;
            usesLeftToID -= uses;
            if (usesLeftToID <= 0) {
                identify();
                GLog.p( Messages.get(Weapon.class, "identify") );
                Badges.validateItemLevelAquired( this );
            }
        }
    }

}
