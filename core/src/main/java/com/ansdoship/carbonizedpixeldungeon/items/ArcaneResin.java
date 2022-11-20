package com.ansdoship.carbonizedpixeldungeon.items;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.items.bags.Bag;
import com.ansdoship.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.ansdoship.carbonizedpixeldungeon.items.wands.Wand;
import com.ansdoship.carbonizedpixeldungeon.items.wands.spark.SparkWand;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndBag;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;

import java.util.ArrayList;

public class ArcaneResin extends Item {

	private static final int RESIN_TO_MODIFY = 3;
	private static final float TIME_TO_MODIFY = 2;

	{
		image = ItemSpriteSheet.ARCANE_RESIN;

		stackable = true;

		defaultAction = AC_APPLY;

		bones = true;
	}

	private static final String AC_APPLY = "APPLY";

	private static final String AC_MODIFY = "MODIFY";

	@Override
	public ArrayList<String> actions(Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_APPLY );
		if (hero.subClass == HeroSubClass.BATTLEMAGE) actions.add( AC_MODIFY );
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_APPLY)) {

			curUser = hero;
			GameScene.selectItem( itemSelector );

		}
		else if (action.equals(AC_MODIFY)) {

			curUser = hero;
			GameScene.selectItem( bmageItemSelector );

		}
	}

	private void modify( Item wand ) {

		if (!(wand instanceof MagesStaff) && !(wand instanceof Wand)) return;

		if (!wand.isIdentified() ){
			GLog.w( Messages.get(this, "bmage_identify"));
			return;
		} else if (wand.cursed) {
			GLog.w( Messages.get(this, "bmage_cursed"));
			return;
		} else if (quantity() < RESIN_TO_MODIFY) {
			GLog.w(Messages.get(this, "not_enough"));
		}

		if (RESIN_TO_MODIFY < quantity()) {
			quantity(quantity() - RESIN_TO_MODIFY);
		} else {
			detachAll(curUser.belongings.backpack);
		}

		GLog.p( Messages.get(this, "modified"));

		if (wand instanceof MagesStaff) {
			((MagesStaff) wand).wandToSpark();
		}
		else {
			SparkWand sparkWand = ((Wand) wand).wandToSpark();
			if (Dungeon.quickslot.contains(wand)) Dungeon.quickslot.setSlot(Dungeon.quickslot.getSlot(wand), sparkWand);
			wand.detach(curUser.belongings.backpack);
			sparkWand.collect(curUser.belongings.backpack);
		}

		curUser.sprite.operate(curUser.pos);
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
		curUser.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );

		curUser.spend(TIME_TO_MODIFY);
		curUser.busy();
	}

	@Override
	public String desc() {
		String desc = super.desc();
		if (Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE) desc += "\n\n" + Messages.get(this, "bmage_desc");
		return desc;
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
			//FIXME give this its own prompt string
			return Messages.get(MagesStaff.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return MagicalHolster.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Wand && item.isIdentified();
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof Wand) {
				Wand w = (Wand)item;

				if (w.level() >= 3){
					GLog.w(Messages.get(ArcaneResin.class, "level_too_high"));
					return;
				}

				int resinToUse = w.level()+1;

				if (quantity() < resinToUse){
					GLog.w(Messages.get(ArcaneResin.class, "not_enough"));

				} else {

					if (resinToUse < quantity()){
						quantity(quantity()-resinToUse);
					} else {
						detachAll(Dungeon.hero.belongings.backpack);
					}

					w.resinBonus++;
					w.curCharges++;
					w.updateLevel();
					Item.updateQuickslot();

					curUser.sprite.operate(curUser.pos);
					Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
					curUser.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );

					curUser.spendAndNext(Actor.TICK);
					GLog.p(Messages.get(ArcaneResin.class, "apply"));
				}
			}
		}
	};

	private final WndBag.ItemSelector bmageItemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(ArcaneResin.class, "bmage_prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag() {
			return MagicalHolster.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return (item instanceof MagesStaff && !(((MagesStaff) item).getWand() instanceof SparkWand))
					|| (item instanceof Wand && !(item instanceof SparkWand));
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				ArcaneResin.this.modify( item );
			}
		}
	};

	public static class Recipe extends com.ansdoship.carbonizedpixeldungeon.items.Recipe {

		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			return ingredients.size() == 1 && ingredients.get(0) instanceof Wand && ingredients.get(0).isIdentified();
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
			Wand w = (Wand)ingredients.get(0);
			int level = w.level() - w.resinBonus;
			return new ArcaneResin().quantity(2*(level+1));
		}
	}

}
