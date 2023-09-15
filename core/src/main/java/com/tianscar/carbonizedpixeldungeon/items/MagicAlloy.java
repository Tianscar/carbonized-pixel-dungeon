package com.tianscar.carbonizedpixeldungeon.items;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Belongings;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.items.bags.Bag;
import com.tianscar.carbonizedpixeldungeon.items.rings.Ring;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfMight;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.windows.WndBag;
import com.tianscar.pixeldungeonclasses.noosa.audio.Sample;

import java.util.ArrayList;

public class MagicAlloy extends Item {

    {
        image = ItemSpriteSheet.MAGIC_ALLOY;

        stackable = true;

        defaultAction = AC_APPLY;

        bones = true;
    }

    private static final String AC_APPLY = "APPLY";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_APPLY );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_APPLY)) {

            curUser = hero;
            GameScene.selectItem( itemSelector );

        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 30*quantity();
    }

    private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(MagicAlloy.class, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Ring && item.isIdentified();
        }

        @Override
        public void onSelect( Item item ) {
            if (item != null && item instanceof Ring) {
                Ring r = (Ring)item;

                if (r.level() >= 3){
                    GLog.w(Messages.get(MagicAlloy.class, "level_too_high"));
                    return;
                }

                int resinToUse = r.level()+1;

                if (quantity() < resinToUse) {
                    GLog.w(Messages.get(MagicAlloy.class, "not_enough"));

                } else {

                    if (resinToUse < quantity()){
                        quantity(quantity()-resinToUse);
                    } else {
                        detachAll(Dungeon.hero.belongings.backpack);
                    }

                    r.alloyBonus++;
                    if (r instanceof RingOfMight) ((RingOfMight) r).updateTargetHT();
                    Item.updateQuickslot();

                    curUser.sprite.operate(curUser.pos);
                    Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                    curUser.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );

                    curUser.spendAndNext(Actor.TICK);
                    GLog.p(Messages.get(MagicAlloy.class, "apply"));
                }
            }
        }
    };

    public static class Recipe extends com.tianscar.carbonizedpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return ingredients.size() == 1 && ingredients.get(0) instanceof Ring && ingredients.get(0).isIdentified();
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 5;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Item result = sampleOutput(ingredients);

            ingredients.get(0).quantity(0);

            return result;
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            Ring r = (Ring) ingredients.get(0);
            int level = r.level() - r.alloyBonus;
            return new MagicAlloy().quantity(2*(level+1));
        }
    }

}
