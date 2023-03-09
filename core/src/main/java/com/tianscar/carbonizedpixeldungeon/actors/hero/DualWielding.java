package com.tianscar.carbonizedpixeldungeon.actors.hero;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.items.KindOfWeapon;
import com.tianscar.carbonizedpixeldungeon.items.weapon.SpiritBow;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.MissileWeapon;

// TODO Improve this
public class DualWielding {

    public boolean canWeaponAttack = false;
    public boolean canExtraAttack = false;

    public KindOfWeapon weapon() {
        return Dungeon.hero.belongings.weapon();
    }

    public KindOfWeapon extra() {
        return Dungeon.hero.belongings.extra();
    }

    public void weaponHitSound(float pitch) {
        KindOfWeapon wep = weapon();
        KindOfWeapon ext = extra();
        if (canWeaponAttack && canExtraAttack) {
            wep.hitSound(0.5f, pitch);
            ext.hitSound(0.5f, pitch);
        }
        else if (wep != null) {
            wep.hitSound(pitch);
        }
        else if (ext != null) {
            ext.hitSound(pitch);
        }
    }

    public boolean weaponNotNull() {
        return weapon() != null || extra() != null;
    }

    public boolean weaponCanAttack(Char owner, Char enemy) {
        if (enemy == null || owner.pos == enemy.pos || !Actor.chars().contains(enemy)) {
            canWeaponAttack = false;
            canExtraAttack = false;
            return false;
        }

        KindOfWeapon wep = weapon();
        KindOfWeapon ext = extra();

        //can always attack adjacent enemies
        if (Dungeon.level.adjacent(owner.pos, enemy.pos)) {
            canWeaponAttack = wep instanceof MeleeWeapon;
            canExtraAttack = ext != null;
            return true;
        }

        if (wep instanceof MeleeWeapon && ext != null) {
            canWeaponAttack = wep.canReach(owner, enemy.pos);
            canExtraAttack = ext.canReach(owner, enemy.pos);
        } else if (wep != null) {
            canWeaponAttack = wep.canReach(owner, enemy.pos);
            canExtraAttack = false;
        } else if (ext != null) {
            canWeaponAttack = false;
            canExtraAttack = ext.canReach(owner, enemy.pos);
        } else {
            canWeaponAttack = false;
            canExtraAttack = false;
        }

        return canWeaponAttack || canExtraAttack;
    }

    public int weaponProc(Char attacker, Char defender, int damage) {
        KindOfWeapon wep = weapon();
        if (wep instanceof MissileWeapon || wep instanceof SpiritBow) return wep.proc(attacker, defender, damage);
        KindOfWeapon ext = extra();
        if (canWeaponAttack && canExtraAttack)
            return (wep.proc(attacker, defender, damage) + ext.proc(attacker, defender, damage) + 1) / 2;
        else if (canWeaponAttack) return wep.proc(attacker, defender, damage);
        else if (canExtraAttack) return ext.proc(attacker, defender, damage);
        else return 0;
    }

    public int weaponDamageRoll(Char owner) {
        KindOfWeapon wep = weapon();
        if (wep instanceof MissileWeapon || wep instanceof SpiritBow) return wep.damageRoll(owner);
        KindOfWeapon ext = extra();
        if (canWeaponAttack && canExtraAttack) return (wep.damageRoll(owner) + ext.damageRoll(owner) + 1) / 2;
        else if (canWeaponAttack) return wep.damageRoll(owner);
        else if (canExtraAttack) return ext.damageRoll(owner);
        else return 0;
    }

    public int weaponDefenseFactor(Char owner) {
        int defenceFactor = 0;
        if (weapon() != null) defenceFactor += weapon().defenseFactor(owner);
        if (extra() != null) defenceFactor += extra().defenseFactor(owner);
        return defenceFactor;
    }

    public float weaponAccuracyFactor(Char owner) {
        KindOfWeapon wep = weapon();
        if (wep instanceof MissileWeapon || wep instanceof SpiritBow) return wep.accuracyFactor(owner);
        KindOfWeapon ext = extra();
        if (canWeaponAttack && canExtraAttack)
            return Math.max(wep.accuracyFactor(owner), ext.accuracyFactor(owner)) * 1.1f;
        else if (canWeaponAttack) return wep.accuracyFactor(owner);
        else if (canExtraAttack) return ext.accuracyFactor(owner);
        else return 0;
    }

    public float weaponDelayFactor(Char owner) {
        KindOfWeapon wep = weapon();
        if (wep instanceof MissileWeapon || wep instanceof SpiritBow) return wep.delayFactor(owner);
        KindOfWeapon ext = extra();
        if (canWeaponAttack && canExtraAttack) return (wep.delayFactor(owner) + ext.delayFactor(owner)) / 2f;
        else if (canWeaponAttack) return wep.delayFactor(owner);
        else if (canExtraAttack) return ext.delayFactor(owner);
        else return 0;
    }

}
