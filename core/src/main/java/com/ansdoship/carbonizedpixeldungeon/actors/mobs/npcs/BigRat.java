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

package com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.PDSettings;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.messages.Languages;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.plants.Sungrass;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.BigRatSprite;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class BigRat extends NPC {

	{
		spriteClass = BigRatSprite.class;
		
		state = SLEEPING;
	}

	protected int count = 0;
	protected boolean seedGiven = false;

	private static final String CONVERSATION_COUNT = "conversation_count";
	private static final String SEED_GIVEN	       = "seed_given";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(CONVERSATION_COUNT, count);
		bundle.put(SEED_GIVEN, seedGiven);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt(CONVERSATION_COUNT);
		seedGiven = bundle.getBoolean(SEED_GIVEN);
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public float speed() {
		return 2f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	//***This functionality is for when rat king may be summoned by a distortion trap

	@Override
	protected void onAdd() {
		super.onAdd();
		if (Dungeon.depth != 5){
			yell(Messages.get(this, "confused"));
		}
	}

	@Override
	protected boolean act() {
		if (Dungeon.depth < 5){
			if (pos == Dungeon.level.exit){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.exit;
			}
		} else if (Dungeon.depth > 5){
			if (pos == Dungeon.level.entrance){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.entrance;
			}
		}
		return super.act();
	}

	//***

	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );

		if (c != Dungeon.hero){
			return super.interact(c);
		}

		Languages lang = PDSettings.language();
		if (state == SLEEPING) {
			notice();
			yell( Messages.get(this, (lang == Languages.CHINESE || lang == Languages.TR_CHINESE) ?
					("not_sleeping_" + Random.Int(1, 4)) : "not_sleeping") );
			state = WANDERING;
		} else if (count >= 4 && !seedGiven) {
			seedGiven = true;
			Item seeds = new Sungrass.Seed().quantity(Random.Int(1, 4));
			yell( Messages.get(this, (lang == Languages.CHINESE || lang == Languages.TR_CHINESE) ?
					("seed_" + Random.Int(1, 4)) : "seed") );
			if (seeds.collect( Dungeon.hero.belongings.backpack )) {
				GameScene.pickUp( seeds, Dungeon.hero.pos );
				GLog.i( Messages.get(Dungeon.hero, "you_now_have", seeds.name()) );
				Sample.INSTANCE.play( Assets.Sounds.ITEM );
			} else {
				Dungeon.level.drop(seeds, Dungeon.hero.pos).sprite.drop();
			}
		} else yell( Messages.get(this, (lang == Languages.CHINESE || lang == Languages.TR_CHINESE) ?
				("text_" + Random.Int(1, count >= 10 ? 55 : 54)) : "what_is_it") );

		if (count < 54) count ++;

		return true;
	}
	
	@Override
	public String description() {
		return ((BigRatSprite)sprite).festive ?
				Messages.get(this, "desc_festive")
				: super.description();
	}
}
