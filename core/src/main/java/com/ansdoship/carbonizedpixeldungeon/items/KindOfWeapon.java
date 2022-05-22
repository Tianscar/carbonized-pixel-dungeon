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
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.Artifact;
import com.ansdoship.carbonizedpixeldungeon.items.rings.Ring;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.ui.ActionIndicator;
import com.ansdoship.carbonizedpixeldungeon.utils.BArray;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndOptions;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.PathFinder;
import com.ansdoship.pixeldungeonclasses.utils.Random;

abstract public class KindOfWeapon extends EquipableItem {

	public boolean twoHanded;
	
	protected static final float TIME_TO_EQUIP = 1f;

	protected String hitSound = Assets.Sounds.HIT;
	protected float hitSoundPitch = 1f;
	
	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.weapon() == this || hero.belongings.weapon2() == this;
	}
	
	@Override
	public boolean doEquip( Hero hero ) {

		boolean equipFull = false;
		boolean shouldEquip = false;
		if (twoHanded) {
			KindOfWeapon tmpWeapon = hero.belongings.weapon;
			KindOfWeapon tmpWeapon2 = hero.belongings.weapon2;
			if ((hero.belongings.weapon == null || hero.belongings.weapon.doUnequip( hero, true )) &&
					(hero.belongings.weapon2 == null || hero.belongings.weapon2.doUnequip( hero, true ))) {
				hero.belongings.weapon = this;
				hero.belongings.weapon2 = null;
				shouldEquip = true;
			}
			else { // Rollback if failed to equip
				hero.belongings.weapon = tmpWeapon;
				hero.belongings.weapon2 = tmpWeapon2;
			}
		}
		else {
			if (hero.belongings.weapon == null) {
				hero.belongings.weapon = this;
				shouldEquip = true;
			}
			else if (hero.belongings.weapon.twoHanded) {
				if (hero.belongings.weapon.doUnequip( hero, true )) {
					hero.belongings.weapon = this;
					shouldEquip = true;
				}
			}
			else if (hero.belongings.weapon2 == null) {
				hero.belongings.weapon2 = this;
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
							hero.belongings.weapon2 == null ? "---" : Messages.titleCase(hero.belongings.weapon2.toString())) {

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
									if (hero.belongings.weapon2.doUnequip( hero, true )) {
										hero.belongings.weapon2 = KindOfWeapon.this;
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
			else if (hero.belongings.weapon2 == this) hero.belongings.weapon2 = null;
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
	
	public boolean canReach( Char owner, int target){
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

	public void hitSound( float pitch ){
		Sample.INSTANCE.play(hitSound, 1, pitch * hitSoundPitch);
	}
	
}
