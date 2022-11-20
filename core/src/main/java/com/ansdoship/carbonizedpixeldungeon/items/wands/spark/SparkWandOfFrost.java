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

package com.ansdoship.carbonizedpixeldungeon.items.wands.spark;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Chill;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.FlavourBuff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Frost;
import com.ansdoship.carbonizedpixeldungeon.effects.MagicMissile;
import com.ansdoship.carbonizedpixeldungeon.items.Heap;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.ansdoship.carbonizedpixeldungeon.mechanics.Ballistica;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;
import com.ansdoship.pixeldungeonclasses.utils.PointF;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class SparkWandOfFrost extends DamageSparkWand {

	{
		image = ItemSpriteSheet.SPARKWAND_FROST;
	}

	public int min(int lvl){
		return 2+lvl;
	}

	public int max(int lvl){
		return 8+5*lvl;
	}

	@Override
	public void onZap(Ballistica bolt) {

		Heap heap = Dungeon.level.heaps.get(bolt.collisionPos);
		if (heap != null) {
			heap.freeze();
		}

		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null){

			int damage = damageRoll();

			if (ch.buff(Frost.class) != null){
				return; //do nothing, can't affect a frozen target
			}
			if (ch.buff(Chill.class) != null){
				//6.67% less damage per turn of chill remaining, to a max of 10 turns (50% dmg)
				float chillturns = Math.min(10, ch.buff(Chill.class).cooldown());
				damage = (int)Math.round(damage * Math.pow(0.9333f, chillturns));
			} else {
				ch.sprite.burst( 0xFF99CCFF, buffedLvl() / 2 + 2 );
			}

			wandProc(ch, chargesPerCast());
			ch.damage(damage, this);
			Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 1.1f * Random.Float(0.87f, 1.15f) );

			if (ch.isAlive()){
				if (Dungeon.level.water[ch.pos])
					Buff.affect(ch, Chill.class, 4+buffedLvl());
				else
					Buff.affect(ch, Chill.class, 2+buffedLvl());
			}
		} else {
			Dungeon.level.pressCell(bolt.collisionPos);
		}
	}

	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.FROST,
				SPEED_FACTOR,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		Chill chill = defender.buff(Chill.class);
		if (chill != null && Random.IntRange(2, (int)Chill.DURATION) <= chill.cooldown()){
			//need to delay this through an actor so that the freezing isn't broken by taking damage from the staff hit.
			new FlavourBuff(){
				{actPriority = VFX_PRIO;}
				public boolean act() {
					Buff.affect(target, Frost.class, Frost.DURATION);
					return super.act();
				}
			}.attachTo(defender);
		}
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0x88CCFF);
		particle.am = 0.6f;
		particle.setLifespan(2f);
		float angle = Random.Float(PointF.PI2);
		particle.speed.polar( angle, 2f);
		particle.acc.set( 0f, 1f);
		particle.setSize( 0f, 1.5f);
		particle.radiateXY(Random.Float(1f));
	}

}
