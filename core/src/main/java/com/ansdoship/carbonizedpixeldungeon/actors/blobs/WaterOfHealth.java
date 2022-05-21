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

package com.ansdoship.carbonizedpixeldungeon.actors.blobs;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Hunger;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.effects.BlobEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.CellEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.Speck;
import com.ansdoship.carbonizedpixeldungeon.effects.particles.ShadowParticle;
import com.ansdoship.carbonizedpixeldungeon.effects.particles.ShaftParticle;
import com.ansdoship.carbonizedpixeldungeon.items.Ankh;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.Waterskin;
import com.ansdoship.carbonizedpixeldungeon.items.potions.PotionOfHealing;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.ansdoship.carbonizedpixeldungeon.journal.Notes.Landmark;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;

public class WaterOfHealth extends WellWater {
	
	@Override
	protected boolean affectHero( Hero hero ) {
		
		if (!hero.isAlive()) return false;
		
		Sample.INSTANCE.play( Assets.Sounds.DRINK );

		PotionOfHealing.cure( hero );
		hero.belongings.uncurseEquipped();
		hero.buff( Hunger.class ).satisfy( Hunger.STARVING );

		hero.HP = hero.HT;
		hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
		
		CellEmitter.get( hero.pos ).start( ShaftParticle.FACTORY, 0.2f, 3 );

		Dungeon.hero.interrupt();
	
		GLog.p( Messages.get(this, "procced") );
		
		return true;
	}
	
	@Override
	protected Item affectItem( Item item, int pos ) {
		if (item instanceof Waterskin && !((Waterskin)item).isFull()) {
			((Waterskin)item).fill();
			CellEmitter.get( pos ).start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
			return item;
		} else if ( item instanceof Ankh && !(((Ankh) item).isBlessed())){
			((Ankh) item).bless();
			CellEmitter.get( pos ).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
			return item;
		} else if (ScrollOfRemoveCurse.uncursable(item)) {
			if (ScrollOfRemoveCurse.uncurse( null, item )){
				CellEmitter.get( pos ).start( ShadowParticle.UP, 0.05f, 10 );
			}
			Sample.INSTANCE.play( Assets.Sounds.DRINK );
			return item;
		}
		return null;
	}
	
	@Override
	protected Landmark record() {
		return Landmark.WELL_OF_HEALTH;
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		emitter.start( Speck.factory( Speck.HEALING ), 0.5f, 0 );
	}
	
	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
