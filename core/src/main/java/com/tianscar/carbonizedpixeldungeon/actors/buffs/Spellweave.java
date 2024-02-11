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

package com.tianscar.carbonizedpixeldungeon.actors.buffs;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Blob;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Electricity;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Fire;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Freezing;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.effects.CellEmitter;
import com.tianscar.carbonizedpixeldungeon.effects.particles.ElmoParticle;
import com.tianscar.carbonizedpixeldungeon.items.spells.ElementalHeart;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.Terrain;
import com.tianscar.carbonizedpixeldungeon.levels.traps.Trap;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Camera;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.noosa.particles.Emitter;
import com.tianscar.carbonizedpixeldungeon.scenes.CellSelector;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.ui.ActionIndicator;
import com.tianscar.carbonizedpixeldungeon.ui.BuffIcon;
import com.tianscar.carbonizedpixeldungeon.ui.BuffIndicator;
import com.tianscar.carbonizedpixeldungeon.utils.BArray;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.windows.WndWovenSpell;

import java.util.ArrayList;

public class Spellweave extends Buff implements ActionIndicator.Action {

	private float timeLeft = 0;
	private static final int MAX_TIME = 80;

	private int weaveStacks() {
		return (int) Math.floor(timeLeft / 10);
	}

	@Override
	public int icon() {
		int stacks = weaveStacks();
		if (stacks >= WovenSpell.PREDICT.stackReq) return BuffIndicator.HASTE;
		else if (stacks >= WovenSpell.TIME_BENT.stackReq) return BuffIndicator.TIME;
		else return BuffIndicator.VERTIGO;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0x6859b3);
	}

	@Override
	public float iconFadePercent() {
		float rem = timeLeft % 10;
		return rem == 0f ? 0 : (10f - rem) / 10f;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	public void stack() {
		timeLeft = Math.min(timeLeft + 10.f, MAX_TIME);
	}

	public void stack(int num) {
		timeLeft = Math.min(timeLeft + 10.f * num, MAX_TIME);
	}

	@Override
	public void detach() {
		super.detach();
		ActionIndicator.clearAction(this);
	}

	@Override
	public boolean act() {
		timeLeft-=TICK;
		spend(TICK);
		if (timeLeft <= 0) {
			detach();
		}
		else if (getHighestSpell() == null) {
			ActionIndicator.clearAction(this);
		}
		else if (ActionIndicator.action != this) {
			ActionIndicator.setAction( this );
		}
		else {
			ActionIndicator.updateIcon();
		}
		return true;
	}

	@Override
	public String desc() {
		float rem = timeLeft % 10;
		return Messages.get(this, "desc", weaveStacks(), dispTurns(timeLeft > 0 ? (rem == 0f ? 10 : rem) : 10));
	}

	private static final String TIME = "timeLeft";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TIME, timeLeft);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		timeLeft = bundle.getFloat( TIME );

		if (getHighestSpell() != null) ActionIndicator.setAction(this);
	}

	@Override
	public String actionName() {
		return Messages.get(this, "action_name");
	}

	@Override
	public Image actionIcon() {
		Image im = new BuffIcon(icon(), true);
		im.hardlight(0x99992E);
		return im;
	}

	@Override
	public void doAction() {
		GameScene.show(new WndWovenSpell(this));
	}

	public enum WovenSpell {
		TORRENT  (2, BuffIndicator.VERTIGO),
		TIME_BENT(4, BuffIndicator.TIME),
		PREDICT  (6, BuffIndicator.HASTE);

		public final int stackReq;
		public final int icon;

		WovenSpell(int stackReq, int icon) {
			this.stackReq = stackReq;
			this.icon = icon;
		}

		public String desc() {
			if (this == WovenSpell.TORRENT) return Messages.get(this, name() + "_desc",
					Math.min(100, 50 + 25 * Dungeon.hero.pointsInTalent(Talent.DEVASTATOR)));
			else return Messages.get(this, name() + "_desc");
		}

	}

	public WovenSpell getHighestSpell() {
		WovenSpell best = null;
		int stacks = weaveStacks();
		for (WovenSpell spell : WovenSpell.values()) {
			if (stacks >= spell.stackReq) {
				best = spell;
			}
		}
		return best;
	}

	public boolean canUseSpell(WovenSpell spell) {
		return spell.stackReq <= weaveStacks();
	}

	public void useSpell(WovenSpell spell) {
		if (spell == WovenSpell.TORRENT) {
			if (Dungeon.hero.buffs(ElementalHeart.Focus.class).isEmpty()) {
				GLog.w(Messages.get(ElementalHeart.class, "no_focus"));
				return;
			}
			GameScene.selectCell(torrent);
		}
		else if (spell == WovenSpell.TIME_BENT) {
			Buff.affect(Dungeon.hero, TimeBent.class).reset();
			detach();
		}
		else if (spell == WovenSpell.PREDICT) {
			Hero hero = Dungeon.hero;
			Buff.count(hero, Predict.class, 2 + hero.pointsInTalent(Talent.FUTURESIGHT));
			detach();
		}
	}

	public static class Predict extends CounterBuff {
		{
			type = buffType.POSITIVE;
			announced = true;
		}
		@Override
		public void countDown(float inc) {
			super.countDown(inc);
			if (count() < 1) detach();
		}
		@Override
		public String toString() {
			return Messages.get(this, "name");
		}
	}

	public static class TimeBent extends Buff {

		{
			type = buffType.POSITIVE;
			announced = true;
		}

		private float left;
		ArrayList<Integer> presses = new ArrayList<>();

		@Override
		public int icon() {
			return BuffIndicator.TIME;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0x6859b3);
		}

		@Override
		public float iconFadePercent() {
			float max = Math.min(8f, 4f + 2 * Dungeon.hero.pointsInTalent(Talent.TIME_TRAVELER));
			return Math.max(0, (max - left) / max);
		}

		public void reset() {
			left = Math.min(8f, 4f + 2 * Dungeon.hero.pointsInTalent(Talent.TIME_TRAVELER));
		}

		@Override
		public String toString() {
			return Messages.get(this, "name");
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns(left));
		}

		public void processTime(float time){
			left -= time;

			//use 1/1,000 to account for rounding errors
			if (left < -0.001f){
				detach();
			}

		}

		public void setDelayedPress(int cell){
			if (!presses.contains(cell)) {
				presses.add(cell);
			}
		}

		public void triggerPresses() {
			for (int cell : presses) {
				Dungeon.level.pressCell(cell);
			}

			presses = new ArrayList<>();
		}

		public void disarmPressedTraps(){
			for (int cell : presses){
				Trap t = Dungeon.level.traps.get(cell);
				if (t != null && t.disarmedByActivation) t.disarm();
			}

			presses = new ArrayList<>();
		}

		@Override
		public void detach(){
			super.detach();
			triggerPresses();
			target.next();
		}

		@Override
		public void fx(boolean on) {
			Emitter.freezeEmitters = on;
			if (on){
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob.sprite != null) mob.sprite.add(CharSprite.State.PARALYSED);
				}
			} else {
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob.paralysed <= 0) mob.sprite.remove(CharSprite.State.PARALYSED);
				}
			}
		}

		private static final String PRESSES = "presses";
		private static final String LEFT = "left";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			int[] values = new int[presses.size()];
			for (int i = 0; i < values.length; i ++)
				values[i] = presses.get(i);
			bundle.put( PRESSES , values );

			bundle.put( LEFT, left);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			int[] values = bundle.getIntArray( PRESSES );
			for (int value : values)
				presses.add(value);

			left = bundle.getFloat(LEFT);
		}

	}

	private final CellSelector.Listener torrent = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {

			Hero hero = (Hero) target;

			if (cell == null) return;
			if (!Dungeon.level.heroFOV[cell]) {
				GLog.w(Messages.get(Spellweave.class, "no_sight"));
				return;
			}

			ElementalHeart heart = hero.belongings.getItem(ElementalHeart.class);
			if (heart == null) {
				GLog.w(Messages.get(Spellweave.class, "no_heart"));
				return;
			}

			hero.busy();

			hero.sprite.zap(cell, new Callback() {
				@Override
				public void call() {

					ElementalHeart.FireFocus fireFocus = hero.buff(ElementalHeart.FireFocus.class);
					ElementalHeart.FrostFocus frostFocus = hero.buff(ElementalHeart.FrostFocus.class);
					ElementalHeart.ShockFocus shockFocus = hero.buff(ElementalHeart.ShockFocus.class);

					Sample.INSTANCE.play( Assets.Sounds.BLAST );

					Camera.main.shake(2, 0.3f);

					Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );
					Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );

					PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), 1 );

					for (int i : PathFinder.NEIGHBOURS9) {
						if (!Dungeon.level.solid[cell + i]) {
							i = cell + i;

							if (Dungeon.level.heroFOV[i]) {
								CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
							}

							float factor = Math.min(1f, 0.5f + 0.25f * hero.pointsInTalent(Talent.DEVASTATOR));
							if (i != cell && hero.pointsInTalent(Talent.DEVASTATOR) <= 2) {
								factor *= 0.5f;
							}

							int damage = 0;

							//### Cell effects ###
							//*** Shock Impact ***
							if (shockFocus != null){
								if (Dungeon.level.water[i]){
									GameScene.add( Blob.seed( i, 4, Electricity.class ) );
								}

								damage += shockFocus.damageRoll(heart);

								shockFocus.detach();

							}
							//*** Fire Impact ***
							if (fireFocus != null){
								if (Dungeon.level.map[i] == Terrain.DOOR){
									Level.set(i, Terrain.OPEN_DOOR);
									GameScene.updateMap(i);
								}
								if (freeze != null){
									freeze.clear(i);
								}
								if (Dungeon.level.flamable[i]){
									GameScene.add( Blob.seed( i, 4, Fire.class ) );
								}

								damage += fireFocus.damageRoll(heart);

								fireFocus.detach();

							}
							//*** Frost Impact ***
							if (frostFocus != null){
								if (fire != null){
									fire.clear(i);
								}

								damage += frostFocus.damageRoll(heart);

								frostFocus.detach();
							}

							//### Deal damage ###
							Char mob = Actor.findChar(i);

							if (mob != null) {
								damage = Math.round(damage * factor);

								if (damage > 0 && mob.alignment != Char.Alignment.ALLY) {
									mob.damage(damage, heart);
								}

								//### Other Char Effects ###
								if (mob != hero) {
									//*** Shock Impact ***
									if (shockFocus != null) {
										if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
											Buff.affect(mob, Paralysis.class, Paralysis.DURATION / 2);
										}

									}
									//*** Fire Impact ***
									if (fireFocus != null) {
										if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
											Buff.affect(mob, Burning.class).reignite(mob);
										}

									}
									//*** Frost Impact ***
									if (frostFocus != null) {
										if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
											Buff.affect(mob, Frost.class, Frost.DURATION);
										}

									}
								}
							}
						}
					}

					//### Self-Effects ###
					//*** Frost Impact ***
					/*
					if (frostFocus != null){
						if ((hero.buff(Burning.class)) != null) {
							hero.buff(Burning.class).detach();
						}

					}

					 */

					Invisibility.dispel();

					detach();

					hero.spendAndNext(Actor.TICK);
				}
			});

		}

		@Override
		public String prompt() {
			return Messages.get(Spellweave.class, "prompt");
		}

	};

}
