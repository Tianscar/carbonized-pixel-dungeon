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

import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.Blob;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.Web;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.effects.Beam;
import com.ansdoship.carbonizedpixeldungeon.effects.CellEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.particles.PurpleParticle;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.ansdoship.carbonizedpixeldungeon.mechanics.Ballistica;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.tiles.DungeonTilemap;
import com.ansdoship.pixeldungeonclasses.utils.Callback;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.util.ArrayList;

public class SparkWandOfDisintegration extends DamageSparkWand {

	{
		image = ItemSpriteSheet.SPARKWAND_DISINTEGRATION;

		collisionProperties = Ballistica.WONT_STOP;
	}


	public int min(int lvl){
		return 2+lvl;
	}

	public int max(int lvl){
		return 8+4*lvl;
	}
	
	@Override
	public int targetingPos(Hero user, int dst) {
		return dst;
	}

	@Override
	public void onZap(Ballistica beam) {
		
		boolean terrainAffected = false;
		
		int level = buffedLvl();
		
		int maxDistance = Math.min(distance(), beam.dist);
		
		ArrayList<Char> chars = new ArrayList<>();

		Blob web = Dungeon.level.blobs.get(Web.class);

		int terrainPassed = 2, terrainBonus = 0;
		for (int c : beam.subPath(1, maxDistance)) {
			
			Char ch;
			if ((ch = Actor.findChar( c )) != null) {

				//we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
				//terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
				terrainBonus += terrainPassed/3;
				terrainPassed = terrainPassed%3;

				chars.add( ch );
			}

			if (Dungeon.level.solid[c]) {
				terrainPassed++;
			}

			if (Dungeon.level.flamable[c]) {

				Dungeon.level.destroy( c );
				GameScene.updateMap( c );
				terrainAffected = true;
				
			}
			
			CellEmitter.center( c ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
		}
		
		if (terrainAffected) {
			Dungeon.observe();
		}
		
		int lvl = level + (chars.size()-1) + terrainBonus;
		for (Char ch : chars) {
			wandProc(ch, chargesPerCast());
			ch.damage( damageRoll(lvl), this );
			ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
			ch.sprite.flash();
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//no direct effect, see magesStaff.reachfactor
	}

	private int distance() {
		return buffedLvl()*2 + 6;
	}
	
	@Override
	public void fx(Ballistica beam, Callback callback) {
		
		int cell = beam.path.get(Math.min(beam.dist, distance()));
		curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0x220022);
		particle.am = 0.6f;
		particle.setLifespan(1f);
		particle.acc.set(10, -10);
		particle.setSize( 0.5f, 3f);
		particle.shuffleXY(1f);
	}

}
