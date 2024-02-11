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

package com.tianscar.carbonizedpixeldungeon.items;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.ui.ActionIndicator;
import com.tianscar.carbonizedpixeldungeon.utils.BArray;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.windows.WndOptions;

abstract public class KindOfWeapon extends EquipableItem {
	
	protected static final float TIME_TO_EQUIP = 1f;

	protected String hitSound = Assets.Sounds.HIT;
	protected float hitSoundPitch = 1f;
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.weapon() == this || hero.belongings.extra() == this;
	}
	
	@Override
	public boolean doEquip( Hero hero ) {

		boolean equipFull = false;
		boolean shouldEquip = false;
		if (this instanceof MeleeWeapon && ((MeleeWeapon) this).twoHanded) {
			if (hero.belongings.weapon == null && hero.belongings.extra == null) shouldEquip = true;
			else if (hero.belongings.weapon == null) {
				shouldEquip = hero.belongings.extra.doUnequip(hero, true);
			}
			else if (hero.belongings.extra == null) {
				shouldEquip = hero.belongings.weapon.doUnequip(hero, true);
			}
			else {
				shouldEquip = hero.belongings.weapon.doUnequip(hero, true) && hero.belongings.extra.doUnequip(hero, true);
			}
			if (shouldEquip) {
				hero.belongings.weapon = this;
				hero.belongings.extra = null;
			}
		}
		else {
			if (hero.belongings.weapon == null) {
				hero.belongings.weapon = this;
				shouldEquip = true;
			}
			else if (hero.belongings.weapon instanceof MeleeWeapon && ((MeleeWeapon) hero.belongings.weapon).twoHanded) {
				if (hero.belongings.weapon.doUnequip( hero, true )) {
					hero.belongings.weapon = this;
					shouldEquip = true;
				}
			}
			else if (hero.belongings.extra == null) {
				hero.belongings.extra = this;
				shouldEquip = true;
			}
			else {
				equipFull = true;
			}
		}
		if (equipFull) {

			GameScene.show(
					new WndOptions(new ItemSprite(this),
							Messages.get(KindofMisc.class, "unequip_title"),
							Messages.get(KindofMisc.class, "unequip_message"),
							hero.belongings.weapon == null ? "---" : Messages.titleCase(hero.belongings.weapon.toString()),
							hero.belongings.extra == null ? "---" : Messages.titleCase(hero.belongings.extra.toString())) {

						@Override
						protected void onSelect(int index) {

							boolean shouldEquip = false;

							switch (index) {
								case 0: default:
									if (hero.belongings.weapon.doUnequip( hero, true )) {
										hero.belongings.weapon = KindOfWeapon.this;
										shouldEquip = true;
									}
									break;
								case 1:
									if (hero.belongings.extra.doUnequip( hero, true )) {
										hero.belongings.extra = KindOfWeapon.this;
										shouldEquip = true;
									}
									break;
							}

							detachAll( hero.belongings.backpack );

							if (shouldEquip) {
								
								activate( hero );
								Talent.onItemEquipped(hero, KindOfWeapon.this);
								ActionIndicator.updateIcon();
								updateQuickslot();

								cursedKnown = true;
								if (cursed) {
									equipCursed( hero );
									GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
								}

								hero.spendAndNext( TIME_TO_EQUIP );

							} else {

								collect( hero.belongings.backpack );
							}

						}
					});

			return false;
		}
		else {

			detachAll( hero.belongings.backpack );

			if (shouldEquip) {
				
				activate( hero );
				Talent.onItemEquipped(hero, this);
				ActionIndicator.updateIcon();
				updateQuickslot();

				cursedKnown = true;
				if (cursed) {
					equipCursed( hero );
					GLog.n( Messages.get(KindOfWeapon.class, "equip_cursed") );
				}

				hero.spendAndNext( TIME_TO_EQUIP );
				return true;

			} else {

				collect( hero.belongings.backpack );
				return false;
			}

		}
	}

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			if (hero.belongings.weapon == this) hero.belongings.weapon = null;
			else if (hero.belongings.extra == this) hero.belongings.extra = null;
			return true;

		} else {

			return false;

		}
	}

	public int min(){
		return min(buffedLvl());
	}

	public int max(){
		return max(buffedLvl());
	}

	abstract public int min(int lvl);
	abstract public int max(int lvl);

	public int damageRoll( Char owner ) {
		return Random.NormalIntRange( min(), max() );
	}
	
	public float accuracyFactor( Char owner ) {
		return 1f;
	}
	
	public float delayFactor(Char owner ) {
		return 1f;
	}

	public int reachFactor( Char owner ){
		return 1;
	}
	
	public boolean canReach( Char owner, int target) {
		if (Dungeon.level.distance( owner.pos, target ) > reachFactor(owner)){
			return false;
		} else {
			boolean[] passable = BArray.not(Dungeon.level.solid, null);
			for (Char ch : Actor.chars()) {
				if (ch != owner) passable[ch.pos] = false;
			}
			
			PathFinder.buildDistanceMap(target, passable, reachFactor(owner));
			
			return PathFinder.distance[owner.pos] <= reachFactor(owner);
		}
	}

	public int defenseFactor( Char owner ) {
		return 0;
	}
	
	public int proc( Char attacker, Char defender, int damage ) {
		return damage;
	}

	public final void hitSound( float pitch ){
		hitSound(1, pitch);
	}

	public void hitSound( float volume, float pitch ){
		Sample.INSTANCE.play(hitSound, volume, pitch * hitSoundPitch);
	}
	
}
