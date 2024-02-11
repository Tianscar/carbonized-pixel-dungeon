package com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.elementalist;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Invisibility;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Spellweave;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.effects.CellEmitter;
import com.tianscar.carbonizedpixeldungeon.effects.CheckedCell;
import com.tianscar.carbonizedpixeldungeon.effects.MagicMissile;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.armor.ClassArmor;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfBlastWave;
import com.tianscar.carbonizedpixeldungeon.mechanics.ShadowCaster;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.noosa.particles.Emitter;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.ui.HeroIcon;
import com.tianscar.carbonizedpixeldungeon.utils.BArray;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;

public class AetherBlink extends ArmorAbility {

	{
		baseChargeUse = 30f;
	}

	@Override
	public float chargeUse( Hero hero ) {
		float chargeUse = super.chargeUse(hero);
		if (hero.buff(Tether.class) != null) {
			//see below how this work
			return 0f;
		}
		return chargeUse;
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(ScrollOfTeleportation.class, "prompt");
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		if (target != null) {

			Tether tether = hero.buff(Tether.class);
			PathFinder.buildDistanceMap(hero.pos, BArray.not(Dungeon.level.solid,null), 8);
			// check tether available
			if (tether != null) {
				// if she has tether, it can be far more than 3, so check it "tether.pos ?= target"
				if (Dungeon.level.distance(hero.pos, target) > 3 && tether.pos != target) {
					GLog.w( Messages.get(WarpBeacon.class, "too_far") );
					return;
				}
			} else {
				if (Dungeon.level.distance(hero.pos, target) > 3) {
					GLog.w( Messages.get(WarpBeacon.class, "too_far") );
					return;
				}
			}

			if (Actor.findChar( target ) != null) {
				GLog.w( Messages.get(this, "not_empty") );
				return;
			}

			if (!Dungeon.level.heroFOV[target]
					&& !Dungeon.level.mapped[target]
					&& !Dungeon.level.visited[target]) {
				GLog.w( Messages.get(Spellweave.class, "no_sight") );
				return;
			}

			// with tether
			if (tether != null && tether.pos == target && tether.depth == Dungeon.depth) {
				Buff.detach(hero, Tether.class);

				if (hero.hasTalent(Talent.AFTERSHOCK)) {
					for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
						if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {
							mob.damage(Random.NormalIntRange(3 * hero.pointsInTalent(Talent.AFTERSHOCK),
									3 + 3 * hero.pointsInTalent(Talent.AFTERSHOCK)), this);
						}
					}
					WandOfBlastWave.BlastWave.blast(hero.pos);
				}

			} else {
				if (tether != null && armor.charge < super.chargeUse(hero)) {
					GLog.w( Messages.get(ClassArmor.class, "low_charge") );
					return;
				}

				Buff.detach(hero, Tether.class);

				//common usage
				armor.charge -= chargeUse(hero);
				Item.updateQuickslot();

				if (hero.hasTalent(Talent.AFTERSHOCK)) {
					for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
						if (Dungeon.level.adjacent(mob.pos, hero.pos) && mob.alignment != Char.Alignment.ALLY) {
							mob.damage(Random.NormalIntRange(3 * hero.pointsInTalent(Talent.AFTERSHOCK),
									3 + 3 * hero.pointsInTalent(Talent.AFTERSHOCK)), this);
						}
					}
					WandOfBlastWave.BlastWave.blast(hero.pos);
				}

				if (hero.hasTalent(Talent.AETHER_TETHER)) {
					Buff.detach( hero, Tether.class );

					Tether newTether = new Tether();
					newTether.pos = hero.pos;
					newTether.depth = Dungeon.depth;
					newTether.left = 2*hero.pointsInTalent(Talent.AETHER_TETHER );
					newTether.attachTo(hero);
				}
			}

			//now blink to target position!
			ScrollOfTeleportation.appear( hero, target );

			//reveal secrets & mobs
			if (hero.hasTalent(Talent.AETHER_VISION)) {
				int pos = hero.pos;

				int points = Dungeon.hero.pointsInTalent(Talent.AETHER_VISION);
				boolean circular = points == 3;
				int distance;
				if (points >= 3) distance = 3;
				else distance = points;

				int cx = pos % Dungeon.level.width();
				int cy = pos / Dungeon.level.width();
				int ax = cx - distance;
				if (ax < 0) {
					ax = 0;
				}
				int bx = cx + distance;
				if (bx >= Dungeon.level.width()) {
					bx = Dungeon.level.width() - 1;
				}
				int ay = cy - distance;
				if (ay < 0) {
					ay = 0;
				}
				int by = cy + distance;
				if (by >= Dungeon.level.height()) {
					by = Dungeon.level.height() - 1;
				}

				boolean noticed = false;

				for (int y = ay; y <= by; y++) {
					for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {

						if (circular && Math.abs(x - cx)-1 > ShadowCaster.rounding[distance][distance - Math.abs(y - cy)]){
							continue;
						}

						if (p != pos) {

							GameScene.effectOverFog( new CheckedCell( p, pos ) );
							Dungeon.level.mapped[p] = true;

							if (Dungeon.level.secret[p]) {
								Dungeon.level.discover(p);

								if (Dungeon.level.heroFOV[p]) {
									GameScene.discoverTile(p, Dungeon.level.map[p]);
									ScrollOfMagicMapping.discover(p);
									noticed = true;
								}
							}

						}
					}
				}

				if (noticed) Sample.INSTANCE.play( Assets.Sounds.SECRET );

			}

			Dungeon.level.occupyCell( hero );
			Dungeon.observe();
			GameScene.updateFog();
			Invisibility.dispel();
			hero.spendAndNext(Actor.TICK);
		}
	}

	public static class Tether extends Buff {

		int pos;
		int depth;

		Emitter e;

		protected float left;

		private static final String LEFT	= "left";

		@Override
		public boolean act() {
			if (target.isAlive()) {

				spend( TICK );

				if (--left <= 0) {
					detach();
					return true;
				}

			} else {

				detach();

			}

			return true;
		}

		@Override
		public void fx(boolean on) {
			if (on && depth == Dungeon.depth) {
				e = CellEmitter.center(pos);
				e.pour(MagicMissile.MagicParticle.FACTORY, 0.03f);
			}
			else if (e != null) e.on = false;
		}

		public static final String POS = "pos";
		public static final String DEPTH = "depth";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( POS, pos );
			bundle.put( DEPTH, depth );
			bundle.put( LEFT, left );
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt( POS );
			depth = bundle.getInt( DEPTH );
			left = bundle.getFloat( LEFT );
		}
	}

	@Override
	public int icon() {
		return HeroIcon.AETHER_BLINK;
	}

	@Override
	public Talent[] talents() {
		return new Talent[] { Talent.AFTERSHOCK, Talent.AETHER_VISION, Talent.AETHER_TETHER, Talent.HEROIC_ENERGY };
	}

}
