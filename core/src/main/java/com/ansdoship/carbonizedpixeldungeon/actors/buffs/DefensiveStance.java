package com.ansdoship.carbonizedpixeldungeon.actors.buffs;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.effects.SpellSprite;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.ui.ActionIndicator;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIcon;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;

public class DefensiveStance extends Buff implements ActionIndicator.Action {

    private boolean enabled = false;

    public boolean enabled() {
        return enabled;
    }

    @Override
    public boolean attachTo(Char target) {
        if (target == Dungeon.hero && Dungeon.hero.subClass == HeroSubClass.SHIELDGUARD) {
            ActionIndicator.setAction(this);
        }
        return super.attachTo(target);
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    public static final String ENABLED = "enabled";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( ENABLED, enabled );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        enabled = bundle.getBoolean( ENABLED );
        ActionIndicator.setAction(this);
    }

    @Override
    public int icon() {
        return enabled ? BuffIndicator.SHIELD : BuffIndicator.NONE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1,1,0);
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    public String actionName() {
        return Messages.get(this, enabled ? "action_off" : "action_on");
    }

    @Override
    public Image actionIcon() {
        Image im = new BuffIcon(BuffIndicator.DEFENSE, true);
        im.hardlight(0x99992E);
        return im;
    }

    @Override
    public void doAction() {
        enabled = !enabled;
        Hero hero = Dungeon.hero;
        hero.sprite.operate( hero.pos );
        hero.busy();
        SpellSprite.show( hero, enabled ? SpellSprite.DEFENSE : SpellSprite.NO_DEFENSE );
        Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1f, enabled ? 1.2f : 0.8f );
        hero.spend( Actor.TICK );
    }

}
