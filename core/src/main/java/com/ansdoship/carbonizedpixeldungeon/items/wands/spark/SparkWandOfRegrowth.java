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
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Roots;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.ansdoship.carbonizedpixeldungeon.effects.MagicMissile;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfRegrowth.Dewcatcher;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfRegrowth.Seedpod;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfRegrowth.Lotus;
import com.ansdoship.carbonizedpixeldungeon.items.Generator;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.ansdoship.carbonizedpixeldungeon.levels.Level;
import com.ansdoship.carbonizedpixeldungeon.levels.Terrain;
import com.ansdoship.carbonizedpixeldungeon.mechanics.Ballistica;
import com.ansdoship.carbonizedpixeldungeon.mechanics.ConeAOE;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.plants.Plant;
import com.ansdoship.carbonizedpixeldungeon.plants.Sungrass;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.*;

import java.util.ArrayList;
import java.util.Iterator;

public class SparkWandOfRegrowth extends SparkWand {

	{
		image = ItemSpriteSheet.SPARKWAND_REGROWTH;

		//only used for targeting, actual projectile logic is Ballistica.STOP_SOLID
		collisionProperties = Ballistica.WONT_STOP;
	}

	private int totChrgUsed = 0;
	private int chargesOverLimit = 0;

	ConeAOE cone;
	int target;

	@Override
	public boolean tryToZap(Hero owner, int target) {
		if (super.tryToZap(owner, target)){
			this.target = target;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onZap(Ballistica bolt) {

		ArrayList<Integer> cells = new ArrayList<>(cone.cells);

		float furrowedChance = 0;
		if (totChrgUsed >= chargeLimit(Dungeon.hero.lvl)){
			furrowedChance = (chargesOverLimit+1)/5f;
		}

		int chrgUsed = chargesPerCast();
		int grassToPlace = Math.round((3.67f+buffedLvl()/3f)*chrgUsed);

		//ignore cells which can't have anything grow in them.
		for (Iterator<Integer> i = cells.iterator(); i.hasNext();) {
			int cell = i.next();
			int terr = Dungeon.level.map[cell];
			if (!(terr == Terrain.EMPTY || terr == Terrain.EMBERS || terr == Terrain.EMPTY_DECO ||
					terr == Terrain.GRASS || terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS)) {
				i.remove();
			} else if (Char.hasProp(Actor.findChar(cell), Char.Property.IMMOVABLE)) {
				i.remove();
			} else if (Dungeon.level.plants.get(cell) != null){
				i.remove();
			} else {
				if (terr != Terrain.HIGH_GRASS && terr != Terrain.FURROWED_GRASS) {
					Level.set(cell, Terrain.GRASS);
					GameScene.updateMap( cell );
				}
				Char ch = Actor.findChar(cell);
				if (ch != null){
					wandProc(ch, chargesPerCast());
					Buff.prolong( ch, Roots.class, 4f * chrgUsed );
				}
			}
		}

		Random.shuffle(cells);

		if (chargesPerCast() >= 3){
			Lotus l = new Lotus();
			l.setLevel(buffedLvl());
			if (cells.contains(target) && Actor.findChar(target) == null){
				cells.remove((Integer)target);
				l.pos = target;
				GameScene.add(l);
			} else {
				for (int i = bolt.path.size()-1; i >= 0; i--){
					int c = bolt.path.get(i);
					if (cells.contains(c) && Actor.findChar(c) == null){
						cells.remove((Integer)c);
						l.pos = c;
						GameScene.add(l);
						break;
					}
				}
			}
		}

		//places grass along center of cone
		for (int cell : bolt.path){
			if (grassToPlace > 0 && cells.contains(cell)){
				if (Random.Float() > furrowedChance) {
					Level.set(cell, Terrain.HIGH_GRASS);
				} else {
					Level.set(cell, Terrain.FURROWED_GRASS);
				}
				GameScene.updateMap( cell );
				grassToPlace--;
				//moves cell to the back
				cells.remove((Integer)cell);
				cells.add(cell);
			}
		}

		if (!cells.isEmpty() && Random.Float() > furrowedChance &&
				(Random.Int(6) < chrgUsed)){ // 16%/33%/50% chance to spawn a seed pod or dewcatcher
			int cell = cells.remove(0);
			Dungeon.level.plant( Random.Int(2) == 0 ? new Seedpod.Seed() : new Dewcatcher.Seed(), cell);
		}

		if (!cells.isEmpty() && Random.Float() > furrowedChance &&
				(Random.Int(3) < chrgUsed)){ // 33%/66%/100% chance to spawn a plant
			int cell = cells.remove(0);
			Dungeon.level.plant((Plant.Seed) Generator.randomUsingDefaults(Generator.Category.SEED), cell);
		}

		for (int cell : cells){
			if (grassToPlace <= 0 || bolt.path.contains(cell)) break;

			if (Dungeon.level.map[cell] == Terrain.HIGH_GRASS) continue;

			if (Random.Float() > furrowedChance) {
				Level.set(cell, Terrain.HIGH_GRASS);
			} else {
				Level.set(cell, Terrain.FURROWED_GRASS);
			}
			GameScene.updateMap( cell );
			grassToPlace--;
		}

		if (totChrgUsed < chargeLimit(Dungeon.hero.lvl)) {
			chargesOverLimit = 0;
			totChrgUsed += chrgUsed;
			if (totChrgUsed > chargeLimit(Dungeon.hero.lvl)){
				chargesOverLimit = totChrgUsed - chargeLimit(Dungeon.hero.lvl);
				totChrgUsed = chargeLimit(Dungeon.hero.lvl);
			}
		} else {
			chargesOverLimit += chrgUsed;
		}

	}

	private int chargeLimit( int heroLvl ){
		if (level() >= 10){
			return Integer.MAX_VALUE;
		} else {
			//8 charges at base, plus:
			//2/3.33/5/7/10/14/20/30/50/110/infinite charges per hero level, based on wand level
			float lvl = level();
			return Math.round(8 + heroLvl * (2+lvl) * (1f + (lvl/(10 - lvl))));
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//like pre-nerf vampiric enchantment, except with herbal healing buff, only in grass
		boolean grass = false;
		int terr = Dungeon.level.map[attacker.pos];
		if (terr == Terrain.GRASS || terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS){
			grass = true;
		}
		terr = Dungeon.level.map[defender.pos];
		if (terr == Terrain.GRASS || terr == Terrain.HIGH_GRASS || terr == Terrain.FURROWED_GRASS){
			grass = true;
		}

		if (grass) {
			int level = Math.max(0, staff.buffedLvl());

			// lvl 0 - 16%
			// lvl 1 - 21%
			// lvl 2 - 25%
			int healing = Math.round(damage * (level + 2f) / (level + 6f) / 2f);
			Buff.affect(attacker, Sungrass.Health.class).boost(healing);
		}

	}

	public void fx(Ballistica bolt, Callback callback) {

		// 4/6/8 distance
		int maxDist = 2 + 2*chargesPerCast();
		int dist = Math.min(bolt.dist, maxDist);

		cone = new ConeAOE( bolt,
				maxDist,
				20 + 10*chargesPerCast(),
				Ballistica.STOP_SOLID | Ballistica.STOP_TARGET);

		//cast to cells at the tip, rather than all cells, better performance.
		for (Ballistica ray : cone.outerRays){
			((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
					MagicMissile.FOLIAGE_CONE,
					curUser.sprite,
					ray.path.get(ray.dist),
					null
			);
		}

		//final zap at half distance, for timing of the actual wand effect
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.FOLIAGE_CONE,
				SPEED_FACTOR,
				curUser.sprite,
				bolt.path.get(dist/2),
				callback );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	protected int chargesPerCast() {
		if (charger != null && charger.target.buff(WildMagic.WildMagicTracker.class) != null){
			return 1;
		}
		//consumes 30% of current charges, rounded up, with a min of 1 and a max of 3.
		return (int) GameMath.gate(1, (int)Math.ceil(curCharges*0.3f), 3);
	}

	@Override
	public String statsDesc() {
		String desc = Messages.get(this, "stats_desc", chargesPerCast());
		if (isIdentified()){
			int chargeLeft = chargeLimit(Dungeon.hero.lvl) - totChrgUsed;
			if (chargeLeft < 10000) desc += " " + Messages.get(this, "degradation", Math.max(chargeLeft, 0));
		}
		return desc;
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( ColorMath.random(0x004400, 0x88CC44) );
		particle.am = 1f;
		particle.setLifespan(1f);
		particle.setSize( 1f, 1.5f);
		particle.shuffleXY(0.5f);
		float dst = Random.Float(11f);
		particle.x -= dst;
		particle.y += dst;
	}

	private static final String TOTAL = "totChrgUsed";
	private static final String OVER = "chargesOverLimit";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( TOTAL, totChrgUsed );
		bundle.put( OVER, chargesOverLimit);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		totChrgUsed = bundle.getInt(TOTAL);
		chargesOverLimit = bundle.getInt(OVER);
	}

}
