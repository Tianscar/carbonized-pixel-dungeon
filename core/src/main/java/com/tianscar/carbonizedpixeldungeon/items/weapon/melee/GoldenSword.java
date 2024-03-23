package com.tianscar.carbonizedpixeldungeon.items.weapon.melee;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.GoldenStatue;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

public class GoldenSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GOLDEN_SWORD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier=5;

        twoHanded = true;
    }

    @Override
    public int damageRoll(Char owner) {
        int damage = super.damageRoll(owner);
        int gold = 0;
        if (owner instanceof Hero) {
            gold = Dungeon.gold;
        }
        else if (owner instanceof GoldenStatue) {
            gold = ((GoldenStatue) owner).gold();
        }
        if (gold > 0) {
            int max = Math.min(50, Math.max(0, gold / 100));         // +1 per 100 gold, max to +50
            int min = max / 10;                                      // +1 per 1000 gold, max to +5
            damage += Random.NormalIntRange(min, max);
        }
        return damage;
    }

    @Override
    public String statsInfo() {
        if (Dungeon.hero != null) {
            int max = Math.min(50, Math.max(0, Dungeon.gold / 100)); // +1 per 100 gold, max to +50
            int min = max / 10;                                      // +1 per 1000 gold, max to +5
            if (max > 0) return Messages.get(this, "stats_desc", min, max);
        }
        return "";
    }

    //Essentially it's a tier 5 weapon, with tier 3 base max damage, and tier 2 scaling.

    @Override
    public int max(int lvl) {
        return  5*(tier-1) +              //20 base, down from 30
                lvl*(tier-2);	          //+2 per level, down from +6
    }

    @Override
    public int value() {
        return super.value() * 10;
    }

}
