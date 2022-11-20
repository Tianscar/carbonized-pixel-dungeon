package com.ansdoship.carbonizedpixeldungeon.items.wands.spark;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.effects.MagicMissile;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfWarding.Ward;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.MagesStaff;
import com.ansdoship.carbonizedpixeldungeon.mechanics.Ballistica;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;
import com.ansdoship.pixeldungeonclasses.utils.PointF;
import com.ansdoship.pixeldungeonclasses.utils.Random;

public class SparkWandOfWarding extends SparkWand {

	{
		image = ItemSpriteSheet.SPARKWAND_WARDING;
	}

	@Override
	public int collisionProperties(int target) {
		if (Dungeon.level.heroFOV[target])  return Ballistica.STOP_TARGET;
		else                                return Ballistica.PROJECTILE;
	}

	private boolean wardAvailable = true;

	@Override
	public boolean tryToZap(Hero owner, int target) {

		int currentWardEnergy = 0;
		for (Char ch : Actor.chars()){
			if (ch instanceof Ward){
				currentWardEnergy += ((Ward) ch).tier;
			}
		}

		int maxWardEnergy = 0;
		for (Buff buff : curUser.buffs()){
			if (buff instanceof Charger){
				if (((Charger) buff).wand() instanceof SparkWandOfWarding){
					maxWardEnergy += 2 + ((Charger) buff).wand().level();
				}
			}
		}

		wardAvailable = (currentWardEnergy < maxWardEnergy);

		Char ch = Actor.findChar(target);
		if (ch instanceof Ward){
			if (!wardAvailable && ((Ward) ch).tier <= 3){
				GLog.w( Messages.get(this, "no_more_wards"));
				return false;
			}
		} else {
			if ((currentWardEnergy + 1) > maxWardEnergy){
				GLog.w( Messages.get(this, "no_more_wards"));
				return false;
			}
		}

		return super.tryToZap(owner, target);
	}

	@Override
	public void onZap(Ballistica bolt) {

		int target = bolt.collisionPos;
		Char ch = Actor.findChar(target);
		if (ch != null && !(ch instanceof Ward)){
			if (bolt.dist > 1) target = bolt.path.get(bolt.dist-1);

			ch = Actor.findChar(target);
			if (ch != null && !(ch instanceof Ward)){
				GLog.w( Messages.get(this, "bad_location"));
				Dungeon.level.pressCell(bolt.collisionPos);
				return;
			}
		}

		if (!Dungeon.level.passable[target]){
			GLog.w( Messages.get(this, "bad_location"));
			Dungeon.level.pressCell(target);

		} else if (ch != null){
			if (ch instanceof Ward){
				if (wardAvailable) {
					((Ward) ch).upgrade( buffedLvl() );
				} else {
					((Ward) ch).wandHeal( buffedLvl() );
				}
				ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, ((Ward) ch).tier);
			} else {
				GLog.w( Messages.get(this, "bad_location"));
				Dungeon.level.pressCell(target);
			}

		} else {
			Ward ward = new Ward();
			ward.pos = target;
			ward.wandLevel = buffedLvl();
			GameScene.add(ward, 1f);
			Dungeon.level.occupyCell(ward);
			ward.sprite.emitter().burst(MagicMissile.WardParticle.UP, ward.tier);
			Dungeon.level.pressCell(target);

		}
	}

	@Override
	public void fx(Ballistica bolt, Callback callback) {
		MagicMissile m = MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.WARD,
				SPEED_FACTOR,
				curUser.sprite,
				bolt.collisionPos,
				callback);

		if (bolt.dist > 10){
			m.setSpeed(bolt.dist*20);
		}
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, staff.buffedLvl() );

		// lvl 0 - 20%
		// lvl 1 - 33%
		// lvl 2 - 43%
		if (Random.Int( level + 5 ) >= 4) {
			for (Char ch : Actor.chars()){
				if (ch instanceof Ward){
					((Ward) ch).wandHeal(staff.buffedLvl());
					ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, ((Ward) ch).tier);
				}
			}
		}
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0x8822FF );
		particle.am = 0.3f;
		particle.setLifespan(3f);
		particle.speed.polar(Random.Float(PointF.PI2), 0.3f);
		particle.setSize( 1f, 2f);
		particle.radiateXY(2.5f);
	}

	@Override
	public String statsDesc() {
		if (levelKnown)
			return Messages.get(this, "stats_desc", level()+2);
		else
			return Messages.get(this, "stats_desc", 2);
	}

}
