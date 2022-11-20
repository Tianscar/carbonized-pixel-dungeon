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

package com.ansdoship.carbonizedpixeldungeon.items;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Belongings;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.effects.Enchanting;
import com.ansdoship.carbonizedpixeldungeon.effects.particles.PurpleParticle;
import com.ansdoship.carbonizedpixeldungeon.items.armor.Armor;
import com.ansdoship.carbonizedpixeldungeon.items.bags.Bag;
import com.ansdoship.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.ansdoship.carbonizedpixeldungeon.items.wands.spark.SparkWand;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndBag;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;

import java.util.ArrayList;

public class Stylus extends Item {
	
	private static final float TIME_TO_INSCRIBE = 2;

	private static final float TIME_TO_MODIFY = 3;
	
	private static final String AC_INSCRIBE = "INSCRIBE";

	private static final String AC_MODIFY = "MODIFY";
	
	{
		image = ItemSpriteSheet.STYLUS;
		
		stackable = true;

		defaultAction = AC_INSCRIBE;

		bones = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_INSCRIBE );
		if (hero.subClass == HeroSubClass.BATTLEMAGE) actions.add( AC_MODIFY );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_INSCRIBE)) {

			curUser = hero;
			GameScene.selectItem( itemSelector );
			
		}
		else if (action.equals(AC_MODIFY)) {

			curUser = hero;
			GameScene.selectItem( bmageItemSelector );

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
	
	private void inscribe( Armor armor ) {

		if (!armor.isIdentified() ){
			GLog.w( Messages.get(this, "identify"));
			return;
		} else if (armor.cursed || armor.hasCurseGlyph()){
			GLog.w( Messages.get(this, "cursed"));
			return;
		}
		
		detach(curUser.belongings.backpack);

		GLog.w( Messages.get(this, "inscribed"));

		armor.inscribe();
		
		curUser.sprite.operate(curUser.pos);
		curUser.sprite.centerEmitter().start(PurpleParticle.BURST, 0.05f, 10);
		Enchanting.show(curUser, armor);
		Sample.INSTANCE.play(Assets.Sounds.BURNING);
		
		curUser.spend(TIME_TO_INSCRIBE);
		curUser.busy();
	}

	private void modify( Item spark ) {

		if (!(spark instanceof MagesStaff) && !(spark instanceof SparkWand)) return;

		if (!spark.isIdentified() ){
			GLog.w( Messages.get(this, "bmage_identify"));
			return;
		} else if (spark.cursed) {
			GLog.w( Messages.get(this, "bmage_cursed"));
			return;
		}

		detach(curUser.belongings.backpack);

		GLog.p( Messages.get(this, "modified"));

		if (spark instanceof MagesStaff) {
			((SparkWand)((MagesStaff) spark).getWand()).advance();
		}
		else {
			((SparkWand)spark).advance();
		}

		curUser.sprite.operate(curUser.pos);
		curUser.sprite.centerEmitter().start(PurpleParticle.BURST, 0.05f, 10);
		Sample.INSTANCE.play(Assets.Sounds.BURNING);

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
	public int value() {
		return 30 * quantity;
	}

	private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(Stylus.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Armor;
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				Stylus.this.inscribe( (Armor)item );
			}
		}
	};

	private final WndBag.ItemSelector bmageItemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(Stylus.class, "bmage_prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag() {
			return MagicalHolster.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			SparkWand sparkWand = null;
			if (item instanceof MagesStaff) sparkWand = ((MagesStaff) item).getWand() instanceof SparkWand ? (SparkWand) ((MagesStaff) item).getWand() : null;
			else if (item instanceof SparkWand) sparkWand = (SparkWand) item;
			if (sparkWand == null) return false;
			else return !sparkWand.advanced();
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				Stylus.this.modify( item );
			}
		}
	};
}
