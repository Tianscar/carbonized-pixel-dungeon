/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.tianscar.carbonizedpixeldungeon.items.artifacts;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Badges;
import com.tianscar.carbonizedpixeldungeon.Challenges;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.Statistics;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Hunger;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Belongings;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.effects.SpellSprite;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.bags.Bag;
import com.tianscar.carbonizedpixeldungeon.items.food.Blandfruit;
import com.tianscar.carbonizedpixeldungeon.items.food.Food;
import com.tianscar.carbonizedpixeldungeon.items.rings.RingOfEnergy;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.windows.WndBag;

import java.util.ArrayList;

public class HornOfPlenty extends Artifact {


	{
		image = ItemSpriteSheet.ARTIFACT_HORN1;

		levelCap = 10;

		charge = 0;
		partialCharge = 0;
		chargeCap = 10 + level();

		defaultAction = AC_EAT;
	}
	
	private int storedFoodEnergy = 0;

	public static final String AC_EAT = "EAT";
	public static final String AC_STORE = "STORE";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) && charge > 0)
			actions.add(AC_EAT);
		if (isEquipped( hero ) && level() < levelCap && !cursed)
			actions.add(AC_STORE);
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)){

			if (!isEquipped(hero)) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
			else if (charge == 0)  GLog.i( Messages.get(this, "no_food") );
			else {
				//consume as much food as it takes to be full, to a minimum of 1
				int satietyPerCharge = (int) (Hunger.STARVING/10f);
				if (Dungeon.isChallenged(Challenges.NO_FOOD)){
					satietyPerCharge /= 3;
				}

				Hunger hunger = Buff.affect(Dungeon.hero, Hunger.class);
				int chargesToUse = Math.max( 1, hunger.hunger() / satietyPerCharge);
				if (chargesToUse > charge) chargesToUse = charge;
				hunger.satisfy(satietyPerCharge * chargesToUse);

				Statistics.foodEaten++;

				charge -= chargesToUse;
				Talent.onArtifactUsed(hero);

				hero.sprite.operate(hero.pos);
				hero.busy();
				SpellSprite.show(hero, SpellSprite.FOOD);
				Sample.INSTANCE.play(Assets.Sounds.EAT);
				GLog.i( Messages.get(this, "eat") );

				if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
						|| Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
						|| Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
						|| Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)){
					hero.spend(Food.TIME_TO_EAT - 2);
				} else {
					hero.spend(Food.TIME_TO_EAT);
				}

				Talent.onFoodEaten(hero, satietyPerCharge * chargesToUse, this);

				Badges.validateFoodEaten();

				int oldImage = image;
				if (charge >= 15)       image = ItemSpriteSheet.ARTIFACT_HORN4;
				else if (charge >= 10)  image = ItemSpriteSheet.ARTIFACT_HORN3;
				else if (charge >= 5)   image = ItemSpriteSheet.ARTIFACT_HORN2;
				else                    image = ItemSpriteSheet.ARTIFACT_HORN1;

				updateQuickslot();
			}

		} else if (action.equals(AC_STORE)){

			GameScene.selectItem(itemSelector);

		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new hornRecharge();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (charge < chargeCap){
			partialCharge += 0.25f*amount;
			if (partialCharge >= 1){
				partialCharge--;
				charge++;
				
				if (charge == chargeCap){
					GLog.p( Messages.get(HornOfPlenty.class, "full") );
					partialCharge = 0;
				}

				int oldImage = image;
				if (charge >= 15)       image = ItemSpriteSheet.ARTIFACT_HORN4;
				else if (charge >= 10)  image = ItemSpriteSheet.ARTIFACT_HORN3;
				else if (charge >= 5)   image = ItemSpriteSheet.ARTIFACT_HORN2;
				else                    image = ItemSpriteSheet.ARTIFACT_HORN1;

				updateQuickslot();
			}
		}
	}
	
	@Override
	public String desc() {
		String desc = super.desc();

		if ( isEquipped( Dungeon.hero ) ){
			if (!cursed) {
				if (level() < levelCap)
					desc += "\n\n" +Messages.get(this, "desc_hint");
			} else {
				desc += "\n\n" +Messages.get(this, "desc_cursed");
			}
		}

		return desc;
	}

	@Override
	public void level(int value) {
		super.level(value);
		chargeCap = 10 + level();
	}

	@Override
	public Item upgrade() {
		super.upgrade();
		chargeCap = 10 + level();
		return this;
	}
	
	public void gainFoodValue( Food food ){
		if (level() >= 10) return;
		
		storedFoodEnergy += food.energy;
		if (storedFoodEnergy >= Hunger.HUNGRY){
			int upgrades = storedFoodEnergy / (int)Hunger.HUNGRY;
			upgrades = Math.min(upgrades, 10 - level());
			upgrade(upgrades);
			storedFoodEnergy -= upgrades * Hunger.HUNGRY;
			if (level() == 10){
				storedFoodEnergy = 0;
				GLog.p( Messages.get(this, "maxlevel") );
			} else {
				GLog.p( Messages.get(this, "levelup") );
			}
		} else {
			GLog.i( Messages.get(this, "feed") );
		}
	}
	
	private static final String STORED = "stored";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( STORED, storedFoodEnergy );
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		storedFoodEnergy = bundle.getInt(STORED);
		
		if (charge >= 15)       image = ItemSpriteSheet.ARTIFACT_HORN4;
		else if (charge >= 10)  image = ItemSpriteSheet.ARTIFACT_HORN3;
		else if (charge >= 5)   image = ItemSpriteSheet.ARTIFACT_HORN2;
	}

	public class hornRecharge extends ArtifactBuff{

		public void gainCharge(float levelPortion) {
			if (cursed) return;
			
			if (charge < chargeCap) {

				//generates 0.25x max hunger value every hero level, +0.125x max value per horn level
				//to a max of 1.5x max hunger value per hero level
				//This means that a standard ration will be recovered in ~5.333 hero levels
				float chargeGain = Hunger.STARVING * levelPortion * (0.25f + (0.125f*level()));
				chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
				partialCharge += chargeGain;

				//charge is in increments of 1/10 max hunger value.
				while (partialCharge >= Hunger.STARVING/10) {
					charge++;
					partialCharge -= Hunger.STARVING/10;

					int oldImage = image;
					if (charge >= 15)       image = ItemSpriteSheet.ARTIFACT_HORN4;
					else if (charge >= 10)  image = ItemSpriteSheet.ARTIFACT_HORN3;
					else if (charge >= 5)   image = ItemSpriteSheet.ARTIFACT_HORN2;
					else                    image = ItemSpriteSheet.ARTIFACT_HORN1;

					updateQuickslot();

					if (charge == chargeCap){
						GLog.p( Messages.get(HornOfPlenty.class, "full") );
						partialCharge = 0;
					}
				}
			} else
				partialCharge = 0;
		}

	}

	protected static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(HornOfPlenty.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Food;
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null && item instanceof Food) {
				if (item instanceof Blandfruit && ((Blandfruit) item).potionAttrib == null){
					GLog.w( Messages.get(HornOfPlenty.class, "reject") );
				} else {
					Hero hero = Dungeon.hero;
					hero.sprite.operate( hero.pos );
					hero.busy();
					hero.spend( Food.TIME_TO_EAT );

					((HornOfPlenty)curItem).gainFoodValue(((Food)item));
					item.detach(hero.belongings.backpack);
				}

			}
		}
	};
}
