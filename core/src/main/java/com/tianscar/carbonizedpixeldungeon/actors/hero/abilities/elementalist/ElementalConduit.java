package com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.elementalist;

import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.tianscar.carbonizedpixeldungeon.items.armor.ClassArmor;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.ui.HeroIcon;

// TODO complete this
public class ElementalConduit extends ArmorAbility {

    {
        baseChargeUse = 25f;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {

    }

    @Override
    public int icon() {
        return HeroIcon.ELEMENTAL_CONDUIT;
    }

    @Override
    public Talent[] talents() {
        return new Talent[] { Talent.CONDUIT_RELAY, Talent.STABILIZED_CONDUIT, Talent.ELEMENTAL_AMPLIFIER, Talent.HEROIC_ENERGY };
    }

}
