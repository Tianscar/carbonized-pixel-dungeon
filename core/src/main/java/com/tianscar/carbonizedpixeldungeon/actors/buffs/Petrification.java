package com.tianscar.carbonizedpixeldungeon.actors.buffs;

import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.ui.BuffIndicator;

public class Petrification extends FlavourBuff {

    public static final float DURATION	= 10f;

    {
        type = buffType.NEGATIVE;
        announced = true;
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            target.paralysed++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        super.detach();
        if (target.paralysed > 0)
            target.paralysed--;
    }

    @Override
    public int icon() {
        return BuffIndicator.PARALYSIS;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - visualcooldown()) / DURATION);
    }

    @Override
    public void fx(boolean on) {
        if (on)                         target.sprite.add(CharSprite.State.PARALYSED);
        else if (target.paralysed <= 1) target.sprite.remove(CharSprite.State.PARALYSED);
    }

    @Override
    public String heroMessage() {
        return Messages.get(this, "heromsg");
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }

}
