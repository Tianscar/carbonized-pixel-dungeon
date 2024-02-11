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

package com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.mage;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Blob;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Electricity;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Fire;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Freezing;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Amok;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Barrier;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Blindness;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Burning;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Charm;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Corrosion;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Frost;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Invisibility;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Light;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Paralysis;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Recharging;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Roots;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.effects.MagicMissile;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.effects.particles.ShadowParticle;
import com.tianscar.carbonizedpixeldungeon.items.armor.ClassArmor;
import com.tianscar.carbonizedpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.tianscar.carbonizedpixeldungeon.items.wands.Wand;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfBlastWave;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfCorrosion;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfCorruption;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfDisintegration;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfFireblast;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfFrost;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfLightning;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfLivingEarth;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfMagicMissile;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfPrismaticLight;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfRegrowth;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfTransfusion;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfWarding;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.Terrain;
import com.tianscar.carbonizedpixeldungeon.mechanics.Ballistica;
import com.tianscar.carbonizedpixeldungeon.mechanics.ConeAOE;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.ui.HeroIcon;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.utils.Reflection;

import java.util.HashMap;

public class ElementalBlast extends ArmorAbility {

	private static final HashMap<Class<?extends Wand>, Integer> effectTypes = new HashMap<>();
	static {
		effectTypes.put(WandOfMagicMissile.class,   MagicMissile.MAGIC_MISS_CONE);
		effectTypes.put(WandOfLightning.class,      MagicMissile.SPARK_CONE);
		effectTypes.put(WandOfDisintegration.class, MagicMissile.PURPLE_CONE);
		effectTypes.put(WandOfFireblast.class,      MagicMissile.FIRE_CONE);
		effectTypes.put(WandOfCorrosion.class,      MagicMissile.CORROSION_CONE);
		effectTypes.put(WandOfBlastWave.class,      MagicMissile.FORCE_CONE);
		effectTypes.put(WandOfLivingEarth.class,    MagicMissile.EARTH_CONE);
		effectTypes.put(WandOfFrost.class,          MagicMissile.FROST_CONE);
		effectTypes.put(WandOfPrismaticLight.class, MagicMissile.RAINBOW_CONE);
		effectTypes.put(WandOfWarding.class,        MagicMissile.WARD_CONE);
		effectTypes.put(WandOfTransfusion.class,    MagicMissile.BLOOD_CONE);
		effectTypes.put(WandOfCorruption.class,     MagicMissile.SHADOW_CONE);
		effectTypes.put(WandOfRegrowth.class,       MagicMissile.FOLIAGE_CONE);
	}

	private static final HashMap<Class<?extends Wand>, Float> damageFactors = new HashMap<>();
	static {
		damageFactors.put(WandOfMagicMissile.class,     0.5f);
		damageFactors.put(WandOfLightning.class,        1f);
		damageFactors.put(WandOfDisintegration.class,   1f);
		damageFactors.put(WandOfFireblast.class,        1f);
		damageFactors.put(WandOfCorrosion.class,        0f);
		damageFactors.put(WandOfBlastWave.class,        0.67f);
		damageFactors.put(WandOfLivingEarth.class,      0.5f);
		damageFactors.put(WandOfFrost.class,            1f);
		damageFactors.put(WandOfPrismaticLight.class,   0.67f);
		damageFactors.put(WandOfWarding.class,          0f);
		damageFactors.put(WandOfTransfusion.class,      0f);
		damageFactors.put(WandOfCorruption.class,       0f);
		damageFactors.put(WandOfRegrowth.class,         0f);
	}

	{
		baseChargeUse = 35f;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {
		Ballistica aim;
		//Basically the direction of the aim only matters if it goes outside the map
		//So we just ensure it won't do that.
		if (hero.pos % Dungeon.level.width() > 10){
			aim = new Ballistica(hero.pos, hero.pos - 1, Ballistica.WONT_STOP);
		} else {
			aim = new Ballistica(hero.pos, hero.pos + 1, Ballistica.WONT_STOP);
		}

		Class<? extends Wand> wandCls = null;
		if (hero.belongings.getItem(MagesStaff.class) != null) {
			wandCls = hero.belongings.getItem(MagesStaff.class).wandClass();
		}

		if (wandCls == null){
			return;
		}

		int aoeSize = 4 + hero.pointsInTalent(Talent.BLAST_RADIUS);

		int projectileProps = Ballistica.STOP_SOLID | Ballistica.STOP_TARGET;

		//### Special Projectile Properties ###
		//*** Wand of Disintegration ***
		if (wandCls == WandOfDisintegration.class){
			projectileProps = Ballistica.STOP_TARGET;

		//*** Wand of Fireblast ***
		} else if (wandCls == WandOfFireblast.class){
			projectileProps = projectileProps | Ballistica.IGNORE_SOFT_SOLID;

		//*** Wand of Warding ***
		} else if (wandCls == WandOfWarding.class){
			projectileProps = Ballistica.STOP_TARGET;

		}

		ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);

		for (Ballistica ray : aoe.outerRays){
			((MagicMissile)hero.sprite.parent.recycle( MagicMissile.class )).reset(
					effectTypes.get(wandCls),
					hero.sprite,
					ray.path.get(ray.dist),
					null
			);
		}

		final float effectMulti = 1f + 0.2f*hero.pointsInTalent(Talent.ELEMENTAL_POWER);

		//cast a ray 2/3 the way, and do effects
		Class<? extends Wand> finalWandCls = wandCls;
		((MagicMissile)hero.sprite.parent.recycle( MagicMissile.class )).reset(
				effectTypes.get(wandCls),
				hero.sprite,
				aim.path.get(aoeSize / 2),
				new Callback() {
					@Override
					public void call() {

						int charsHit = 0;
						Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );
						Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
						for (int cell : aoe.cells) {

							//### Cell effects ###
							//*** Wand of Lightning ***
							if (finalWandCls == WandOfLightning.class){
								if (Dungeon.level.water[cell]){
									GameScene.add( Blob.seed( cell, 4, Electricity.class ) );
								}

							//*** Wand of Fireblast ***
							} else if (finalWandCls == WandOfFireblast.class){
								if (Dungeon.level.map[cell] == Terrain.DOOR){
									Level.set(cell, Terrain.OPEN_DOOR);
									GameScene.updateMap(cell);
								}
								if (freeze != null){
									freeze.clear(cell);
								}
								if (Dungeon.level.flamable[cell]){
									GameScene.add( Blob.seed( cell, 4, Fire.class ) );
								}

							//*** Wand of Frost ***
							} else if (finalWandCls == WandOfFrost.class){
								if (fire != null){
									fire.clear(cell);
								}

							//*** Wand of Prismatic Light ***
							} else if (finalWandCls == WandOfPrismaticLight.class){
								for (int n : PathFinder.NEIGHBOURS9) {
									int c = cell+n;

									if (Dungeon.level.discoverable[c]) {
										Dungeon.level.mapped[c] = true;
									}

									int terr = Dungeon.level.map[c];
									if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

										Dungeon.level.discover(c);

										GameScene.discoverTile(c, terr);
										ScrollOfMagicMapping.discover(c);

									}
								}

							//*** Wand of Regrowth ***
							} else if (finalWandCls == WandOfRegrowth.class){
								//TODO: spend 3 charges worth of regrowth energy from staff?
								int t = Dungeon.level.map[cell];
								if (Random.Float() < 0.33f*effectMulti) {
									if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
											|| t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
											&& Dungeon.level.plants.get(cell) == null) {
										Level.set(cell, Terrain.HIGH_GRASS);
										GameScene.updateMap(cell);
									}
								}
							}

							//### Deal damage ###
							Char mob = Actor.findChar(cell);
							int damage = Math.round(Random.NormalIntRange(15, 25)
									* effectMulti
									* damageFactors.get(finalWandCls));

							if (mob != null && damage > 0 && mob.alignment != Char.Alignment.ALLY){
								mob.damage(damage, Reflection.newInstance(finalWandCls));
								charsHit++;
							}

							//### Other Char Effects ###
							if (mob != null && mob != hero){
								//*** Wand of Lightning ***
								if (finalWandCls == WandOfLightning.class){
									if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
										Buff.affect( mob, Paralysis.class, effectMulti*Paralysis.DURATION/2 );
									}

								//*** Wand of Fireblast ***
								} else if (finalWandCls == WandOfFireblast.class){
									if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
										Buff.affect( mob, Burning.class ).reignite( mob );
									}

								//*** Wand of Corrosion ***
								} else if (finalWandCls == WandOfCorrosion.class){
									if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
										Buff.affect( mob, Corrosion.class ).set(4, Math.round(6*effectMulti));
										charsHit++;
									}

								//*** Wand of Blast Wave ***
								} else if (finalWandCls == WandOfBlastWave.class){
									if (mob.alignment != Char.Alignment.ALLY) {
										Ballistica aim = new Ballistica(hero.pos, mob.pos, Ballistica.WONT_STOP);
										int knockback = aoeSize + 1 - (int)Dungeon.level.trueDistance(hero.pos, mob.pos);
										knockback *= effectMulti;
										WandOfBlastWave.throwChar(mob,
												new Ballistica(mob.pos, aim.collisionPos, Ballistica.MAGIC_BOLT),
												knockback,
												true);
									}

								//*** Wand of Frost ***
								} else if (finalWandCls == WandOfFrost.class){
									if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
										Buff.affect( mob, Frost.class, effectMulti*Frost.DURATION );
									}

								//*** Wand of Prismatic Light ***
								} else if (finalWandCls == WandOfPrismaticLight.class){
									if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
										Buff.prolong(mob, Blindness.class, effectMulti*Blindness.DURATION/2);
										charsHit++;
									}

								//*** Wand of Warding ***
								} else if (finalWandCls == WandOfWarding.class){
									if (mob instanceof WandOfWarding.Ward){
										((WandOfWarding.Ward) mob).wandHeal(0, effectMulti);
										charsHit++;
									}

								//*** Wand of Transfusion ***
								} else if (finalWandCls == WandOfTransfusion.class){
									if(mob.alignment == Char.Alignment.ALLY || mob.buff(Charm.class) != null){
										int healing = Math.round(10*effectMulti);
										int shielding = (mob.HP + healing) - mob.HT;
										if (shielding > 0){
											healing -= shielding;
											Buff.affect(mob, Barrier.class).setShield(shielding);
										} else {
											shielding = 0;
										}
										mob.HP += healing;

										mob.sprite.emitter().burst(Speck.factory(Speck.HEALING), 4);
										mob.sprite.showStatus(CharSprite.POSITIVE, "+%dHP", healing + shielding);
									} else {
										if (!mob.properties().contains(Char.Property.UNDEAD)) {
											Charm charm = Buff.affect(mob, Charm.class, effectMulti*Charm.DURATION/2f);
											charm.object = hero.id();
											charm.ignoreHeroAllies = true;
											mob.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 3);
										} else {
											damage = Math.round(Random.NormalIntRange(15, 25) * effectMulti);
											mob.damage(damage, Reflection.newInstance(finalWandCls));
											mob.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
										}
									}
									charsHit++;

								//*** Wand of Corruption ***
								} else if (finalWandCls == WandOfCorruption.class){
									if (mob.isAlive() && mob.alignment != Char.Alignment.ALLY) {
										Buff.prolong(mob, Amok.class, effectMulti*5f);
										charsHit++;
									}

								//*** Wand of Regrowth ***
								} else if (finalWandCls == WandOfRegrowth.class){
									if (mob.alignment != Char.Alignment.ALLY) {
										Buff.prolong( mob, Roots.class, effectMulti*Roots.DURATION );
										charsHit++;
									}
								}
							}

						}

						//### Self-Effects ###
						//*** Wand of Magic Missile ***
						if (finalWandCls == WandOfMagicMissile.class) {
							Buff.affect(hero, Recharging.class, effectMulti* Recharging.DURATION / 2f);

						//*** Wand of Living Earth ***
						} else if (finalWandCls == WandOfLivingEarth.class && charsHit > 0){
							for (Mob m : Dungeon.level.mobs){
								if (m instanceof WandOfLivingEarth.EarthGuardian){
									((WandOfLivingEarth.EarthGuardian) m).setInfo(hero, 0, Math.round(effectMulti*charsHit*5));
									m.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + charsHit);
									break;
								}
							}

						//*** Wand of Frost ***
						} else if (finalWandCls == WandOfFrost.class){
							if ((hero.buff(Burning.class)) != null) {
								hero.buff(Burning.class).detach();
							}

						//*** Wand of Prismatic Light ***
						} else if (finalWandCls == WandOfPrismaticLight.class){
							Buff.prolong( hero, Light.class, effectMulti*50f);

						}

						charsHit = Math.min(5, charsHit);
						if (charsHit > 0 && hero.hasTalent(Talent.REACTIVE_BARRIER)){
							Buff.affect(hero, Barrier.class).setShield(charsHit*2*hero.pointsInTalent(Talent.REACTIVE_BARRIER));
						}

						hero.spendAndNext(Actor.TICK);
					}
				}
		);

		hero.sprite.operate( hero.pos );
		Invisibility.dispel();
		hero.busy();

		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();

		Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );

	}

	@Override
	public String desc() {
		String desc = Messages.get(this, "desc");
		if (Game.scene() instanceof GameScene){
			MagesStaff staff = Dungeon.hero.belongings.getItem(MagesStaff.class);
			if (staff != null && staff.wandClass() != null){
				desc += "\n\n" + Messages.get(staff.wandClass(), "eleblast_desc");
			} else {
				desc += "\n\n" + Messages.get(this, "generic_desc");
			}
		} else {
			desc += "\n\n" + Messages.get(this, "generic_desc");
		}
		desc += "\n\n" + Messages.get(this, "cost", (int)baseChargeUse);
		return desc;
	}

	@Override
	public int icon() {
		return HeroIcon.ELEMENTAL_BLAST;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.BLAST_RADIUS, Talent.ELEMENTAL_POWER, Talent.REACTIVE_BARRIER, Talent.HEROIC_ENERGY};
	}
}
