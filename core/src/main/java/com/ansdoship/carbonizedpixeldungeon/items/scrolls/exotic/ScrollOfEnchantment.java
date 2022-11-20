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

package com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Belongings;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.effects.Enchanting;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.armor.Armor;
import com.ansdoship.carbonizedpixeldungeon.items.bags.Bag;
import com.ansdoship.carbonizedpixeldungeon.items.stones.StoneOfEnchantment;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.SpiritBow;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.Weapon;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.ui.Icons;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndBag;
import com.ansdoship.carbonizedpixeldungeon.windows.WndOptions;
import com.ansdoship.carbonizedpixeldungeon.windows.WndTitledMessage;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class ScrollOfEnchantment extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_ENCHANT;

		unique = true;
	}
	
	@Override
	public void doRead() {
		identify();
		
		GameScene.selectItem( itemSelector );
	}

	public static boolean enchantable( Item item ){
		return (item instanceof MeleeWeapon || item instanceof SpiritBow || item instanceof Armor);
	}
	
	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(ScrollOfEnchantment.class, "inv_title");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return Belongings.Backpack.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return enchantable(item);
		}

		@Override
		public void onSelect(final Item item) {

			final int size = curUser.subClass == HeroSubClass.LOREMASTER ? 5 : 3;
			if (item instanceof Weapon){

				final Weapon.Enchantment enchants[] = new Weapon.Enchantment[size];
				
				Class<? extends Weapon.Enchantment> existing = ((Weapon) item).enchantment != null ? ((Weapon) item).enchantment.getClass() : null;
				enchants[0] = Weapon.Enchantment.randomCommon( existing );
				enchants[1] = Weapon.Enchantment.randomUncommon( existing );
				enchants[2] = Weapon.Enchantment.random( existing, enchants[0].getClass(), enchants[1].getClass());
				if (curUser.subClass == HeroSubClass.LOREMASTER) {
					enchants[3] = Weapon.Enchantment.random( existing, enchants[0].getClass(), enchants[1].getClass());
					enchants[4] = Weapon.Enchantment.random( existing, enchants[0].getClass(), enchants[1].getClass());
				}
				String[] texts = new String[size + 1];
				for (int i = 0; i < size; i ++) {
					texts[i] = enchants[i].name();
				}
				texts[size] = Messages.get(ScrollOfEnchantment.class, "cancel");

				GameScene.show(new WndOptions(new ItemSprite(ScrollOfEnchantment.this),
						Messages.titleCase(ScrollOfEnchantment.this.name()),
						Messages.get(ScrollOfEnchantment.class, "weapon") +
						"\n\n" +
						Messages.get(ScrollOfEnchantment.class, "cancel_warn"),
						texts) {
					
					@Override
					protected void onSelect(int index) {
						doRecord(new Callback() {
							@Override
							public void call() {

								if (index < size) {
									((Weapon) item).enchant(enchants[index]);
									GLog.p(Messages.get(StoneOfEnchantment.class, "weapon"));
									((ScrollOfEnchantment)curItem).readAnimation();

									Sample.INSTANCE.play( Assets.Sounds.READ );
									Enchanting.show(curUser, item);
									Talent.onUpgradeScrollUsed( Dungeon.hero );
								}

							}
						});
					}
					
					@Override
					protected boolean hasInfo(int index) {
						return index < size;
					}

					@Override
					protected void onInfo( int index ) {
						GameScene.show(new WndTitledMessage(
								Icons.get(Icons.INFO),
								Messages.titleCase(enchants[index].name()),
								enchants[index].desc()));
					}

					@Override
					public void onBackPressed() {
						//do nothing, reader has to cancel
					}
				});
			
			} else if (item instanceof Armor) {
				
				final Armor.Glyph glyphs[] = new Armor.Glyph[size];
				
				Class<? extends Armor.Glyph> existing = ((Armor) item).glyph != null ? ((Armor) item).glyph.getClass() : null;
				glyphs[0] = Armor.Glyph.randomCommon( existing );
				glyphs[1] = Armor.Glyph.randomUncommon( existing );
				glyphs[2] = Armor.Glyph.random( existing, glyphs[0].getClass(), glyphs[1].getClass());
				if (curUser.subClass == HeroSubClass.LOREMASTER) {
					glyphs[3] = Armor.Glyph.random( existing, glyphs[0].getClass(), glyphs[1].getClass());
					glyphs[4] = Armor.Glyph.random( existing, glyphs[0].getClass(), glyphs[1].getClass());
				}

				String[] texts = new String[size + 1];
				for (int i = 0; i < size; i ++) {
					texts[i] = glyphs[i].name();
				}
				texts[size] = Messages.get(ScrollOfEnchantment.class, "cancel");
				
				GameScene.show(new WndOptions( new ItemSprite(ScrollOfEnchantment.this),
						Messages.titleCase(ScrollOfEnchantment.this.name()),
						Messages.get(ScrollOfEnchantment.class, "armor") +
						"\n\n" +
						Messages.get(ScrollOfEnchantment.class, "cancel_warn"),
						texts) {
					
					@Override
					protected void onSelect(int index) {
						doRecord(new Callback() {
							@Override
							public void call() {

								if (index < size) {
									((Armor) item).inscribe(glyphs[index]);
									GLog.p(Messages.get(StoneOfEnchantment.class, "armor"));
									((ScrollOfEnchantment)curItem).readAnimation();

									Sample.INSTANCE.play( Assets.Sounds.READ );
									Enchanting.show(curUser, item);
									Talent.onUpgradeScrollUsed( Dungeon.hero );
								}

							}
						});

					}

					@Override
					protected boolean hasInfo(int index) {
						return index < size;
					}

					@Override
					protected void onInfo( int index ) {
						GameScene.show(new WndTitledMessage(
								Icons.get(Icons.INFO),
								Messages.titleCase(glyphs[index].name()),
								glyphs[index].desc()));
					}
					
					@Override
					public void onBackPressed() {
						//do nothing, reader has to cancel
					}
				});
			} else {
				//TODO if this can ever be found un-IDed, need logic for that
				if (!anonymous) curItem.collect();
			}
		}
	};
}
