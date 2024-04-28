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

package com.tianscar.carbonizedpixeldungeon.actors.mobs;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Corruption;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Haste;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Terror;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.Knight;
import com.tianscar.carbonizedpixeldungeon.effects.CellEmitter;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.items.EquipableItem;
import com.tianscar.carbonizedpixeldungeon.items.Honeypot;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.armor.Armor;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.tianscar.carbonizedpixeldungeon.items.wands.Wand;
import com.tianscar.carbonizedpixeldungeon.items.weapon.Weapon;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.MimicSprite;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

import java.util.ArrayList;

public class StrangeMimic extends Mimic {

	{
		spriteClass = MimicSprite.Dark.class;

		FLEEING = new Fleeing();

		properties.add(Property.MINIBOSS);
	}

	//does not deal bonus damage, steals instead. See attackProc
	@Override
	public int damageRoll() {
		if (alignment == Alignment.NEUTRAL) {
			alignment = Alignment.ENEMY;
			int dmg = super.damageRoll();
			alignment = Alignment.NEUTRAL;
			return dmg;
		} else {
			return super.damageRoll();
		}
	}

	public void stopHiding(){
		state = FLEEING;
		//haste for 2 turns if attacking
		if (alignment == Alignment.NEUTRAL){
			Buff.affect(this, Haste.class, 2f);
		} else {
			Buff.affect(this, Haste.class, 1f);
		}
		if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
			enemy = Dungeon.hero;
			target = Dungeon.hero.pos;
			enemySeen = true;
			GLog.w(Messages.get(this, "reveal") );
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
			Sample.INSTANCE.play(Assets.Sounds.MIMIC, 1, 1.25f);
		}
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (alignment == Alignment.NEUTRAL && enemy == Dungeon.hero){
			steal( Dungeon.hero );

		} else {
			steal( Dungeon.hero );

			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				if (Dungeon.level.passable[pos+i] && Actor.findChar(pos+i) == null){
					candidates.add(pos + i);
				}
			}

			if (!candidates.isEmpty()){
				ScrollOfTeleportation.appear(enemy, Random.element(candidates));
			}

			if (alignment == Alignment.ENEMY) state = FLEEING;
		}
		return super.attackProc(enemy, damage);
	}

	protected void steal( Hero hero ) {

		int tries = 10;
		Item item;
		do {
			item = hero.belongings.randomUnequipped();
		} while (tries-- > 0 && (item == null || item.unique || item.level() > 0));

		if (item != null && !item.unique && item.level() < 1 ) {

			GLog.w( Messages.get(this, "ate", item.name()) );
			if (!item.stackable) {
				Dungeon.quickslot.convertToPlaceholder(item);
			}
			item.updateQuickslot();

			if (item instanceof Honeypot){
				items.add(((Honeypot)item).shatter(this, this.pos));
				item.detach( hero.belongings.backpack );
			} else {
				items.add(item.detach( hero.belongings.backpack ));
				if ( item instanceof Honeypot.ShatteredPot)
					((Honeypot.ShatteredPot)item).pickupPot(this);
			}

		}
	}

	@Override
	public void setLevel(int level) {
		super.setLevel(Math.round(level*1.66f));
	}

	@Override
	protected void generatePrize() {
		super.generatePrize();
		//all existing prize items are guaranteed uncursed
		for (Item i : items){
			if (i instanceof EquipableItem || i instanceof Wand){
				i.cursed = false;
				i.cursedKnown = true;
				if (i instanceof Weapon && ((Weapon) i).hasCurseEnchant()){
					((Weapon) i).enchant(null);
				}
				if (i instanceof Armor && ((Armor) i).hasCurseGlyph()){
					((Armor) i).inscribe(null);
				}
			}
		}
	}

	private class Fleeing extends Mob.Fleeing{
		@Override
		protected void nowhereToRun() {
			if (buff( Terror.class ) == null && buff( Corruption.class ) == null) {
				if (enemySeen) {
					sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Mob.class, "rage"));
					state = HUNTING;
				} else if (!Dungeon.level.heroFOV[pos] && Dungeon.level.distance(Dungeon.hero.pos, pos) >= 6) {
					GLog.n(Messages.get(StrangeMimic.class, "escaped"));
					if (Dungeon.level.heroFOV[pos]) CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
					destroy();
					sprite.killAndErase();
					Knight.Quest.lost = true;
				} else {
					state = WANDERING;
				}
			} else {
				super.nowhereToRun();
			}
		}
	}

}
