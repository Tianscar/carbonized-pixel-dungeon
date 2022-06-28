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

package com.ansdoship.carbonizedpixeldungeon.items.spells;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic.ScrollOfPassage;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.plants.Swiftthistle;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.InterlevelScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndOptions;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.PathFinder;

public class BeaconOfReturning extends Spell {

	{
		image = ItemSpriteSheet.RETURN_BEACON;
	}

	public int returnDepth	= -1;
	public int returnPos;

	@Override
	protected void onCast(final Hero hero) {

		if (returnDepth == -1){
			setBeacon(hero);
		} else {
			GameScene.show(new WndOptions(new ItemSprite(this),
					Messages.titleCase(name()),
					Messages.get(BeaconOfReturning.class, "wnd_body"),
					Messages.get(BeaconOfReturning.class, "wnd_set"),
					Messages.get(BeaconOfReturning.class, "wnd_return")){
				@Override
				protected void onSelect(int index) {
					if (index == 0){
						setBeacon(hero);
					} else if (index == 1){
						returnBeacon(hero);
					}
				}
			});

		}
	}

	//we reset return depth when beacons are dropped to prevent
	//having two stacks of beacons with different return locations

	@Override
	protected void onThrow(int cell) {
		returnDepth = -1;
		super.onThrow(cell);
	}

	@Override
	public void doDrop(Hero hero) {
		returnDepth = -1;
		super.doDrop(hero);
	}

	private void setBeacon(Hero hero ){
		returnDepth = Dungeon.depth;
		returnPos = hero.pos;

		hero.spend( 1f );
		hero.busy();

		GLog.i( Messages.get(this, "set") );

		hero.sprite.operate( hero.pos );
		Sample.INSTANCE.play( Assets.Sounds.BEACON );
		updateQuickslot();
	}

	private void returnBeacon( Hero hero ){
		if (Dungeon.level.locked) {
			GLog.w( Messages.get(this, "preventing") );
			return;
		}

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char ch = Actor.findChar(hero.pos + PathFinder.NEIGHBOURS8[i]);
			if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
				GLog.w( Messages.get(this, "creatures") );
				return;
			}
		}

		if (returnDepth == Dungeon.depth) {
			if (!Dungeon.level.passable[returnPos] && !Dungeon.level.avoid[returnPos]){
				returnPos = Dungeon.level.entrance;
			}
			ScrollOfTeleportation.appear( hero, returnPos );
			for(Mob m : Dungeon.level.mobs){
				if (m.pos == hero.pos){
					//displace mob
					for(int i : PathFinder.NEIGHBOURS8){
						if (Actor.findChar(m.pos+i) == null && Dungeon.level.passable[m.pos + i]){
							m.pos += i;
							m.sprite.point(m.sprite.worldToCamera(m.pos));
							break;
						}
					}
				}
			}
			Dungeon.level.occupyCell(hero );
			Dungeon.observe();
			GameScene.updateFog();
		} else {

			TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
			if (timeFreeze != null) timeFreeze.disarmPressedTraps();
			Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
			if (timeBubble != null) timeBubble.disarmPressedTraps();

			InterlevelScene.mode = InterlevelScene.Mode.RETURN;
			InterlevelScene.returnDepth = returnDepth;
			InterlevelScene.returnPos = returnPos;
			Game.switchScene( InterlevelScene.class );
		}
		detach(hero.belongings.backpack);
	}

	@Override
	public String desc() {
		String desc = super.desc();
		if (returnDepth != -1){
			desc += "\n\n" + Messages.get(this, "desc_set", returnDepth);
		}
		return desc;
	}

	private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );

	@Override
	public ItemSprite.Glowing glowing() {
		return returnDepth != -1 ? WHITE : null;
	}

	private static final String DEPTH	= "depth";
	private static final String POS		= "pos";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DEPTH, returnDepth );
		if (returnDepth != -1) {
			bundle.put( POS, returnPos );
		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		returnDepth	= bundle.getInt( DEPTH );
		returnPos	= bundle.getInt( POS );
	}

	@Override
	public int value() {
		//prices of ingredients, divided by output quantity
		return Math.round(quantity * ((50 + 40) / 5f));
	}
	
	public static class Recipe extends com.ansdoship.carbonizedpixeldungeon.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfPassage.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};

			cost = 6;

			output = BeaconOfReturning.class;
			outQuantity = 5;
		}

	}
}
