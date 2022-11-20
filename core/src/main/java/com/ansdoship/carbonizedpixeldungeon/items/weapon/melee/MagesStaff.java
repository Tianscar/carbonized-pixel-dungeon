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

package com.ansdoship.carbonizedpixeldungeon.items.weapon.melee;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Badges;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Barrier;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.Buff;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.FlavourBuff;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.effects.particles.ElmoParticle;
import com.ansdoship.carbonizedpixeldungeon.items.ArcaneResin;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.Artifact;
import com.ansdoship.carbonizedpixeldungeon.items.bags.Bag;
import com.ansdoship.carbonizedpixeldungeon.items.bags.MagicalHolster;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.Scroll;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.ansdoship.carbonizedpixeldungeon.items.wands.Wand;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfCorrosion;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfCorruption;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfDisintegration;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfLivingEarth;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfRegrowth;
import com.ansdoship.carbonizedpixeldungeon.items.wands.spark.*;
import com.ansdoship.carbonizedpixeldungeon.items.weapon.Weapon;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.ui.BuffIndicator;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.WndBag;
import com.ansdoship.carbonizedpixeldungeon.windows.WndOptions;
import com.ansdoship.carbonizedpixeldungeon.windows.WndUseItem;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.noosa.particles.Emitter;
import com.ansdoship.pixeldungeonclasses.noosa.particles.PixelParticle;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.Callback;
import com.ansdoship.pixeldungeonclasses.utils.Random;
import com.ansdoship.pixeldungeonclasses.utils.Reflection;

import java.util.ArrayList;
import java.util.Collection;

public class MagesStaff extends MeleeWeapon {

	private Wand wand;

	public Wand getWand() {
		return wand;
	}

	public static final String AC_IMBUE = "IMBUE";
	public static final String AC_ZAP	= "ZAP";

	private static final float STAFF_SCALE_FACTOR = 0.75f;

	public ArrayList<Scroll> records = new ArrayList<>();
	public static final String RECORDS  = "RECORDS";

	public static final String AC_READ  = "READ";

	public static final String AC_SPARK	= "SPARK";

	{
		image = ItemSpriteSheet.MAGES_STAFF;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1.1f;

		tier = 1;
		twoHanded = false;

		defaultAction = AC_ZAP;
		usesTargeting = true;

		unique = true;
		bones = false;

		RCH = 2;
	}

	public MagesStaff() {
		wand = null;
	}

	@Override
	public int max(int lvl) {
		return  Math.round(3.5f*(tier+1)) + //7 base damage, down from 10
				lvl*(tier+1);               //scaling unaffected
	}

	public MagesStaff(Wand wand) {
		this();
		wand.identify();
		wand.cursed = false;
		this.wand = wand;
		updateWand(false);
		wand.curCharges = wand.maxCharges;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_IMBUE);
		if (wand!= null && wand.curCharges > 0) {
			actions.add( AC_ZAP );
		}
		if (wand instanceof SparkWand) {
			actions.add( AC_SPARK );
		}
		if (records.size() > 0 && hero.buff(ReadRecordCooldown.class) == null) {
			actions.add( AC_READ );
		}
		return actions;
	}

	@Override
	public void activate( Char ch ) {
		applyWandChargeBuff(ch);
		if (ch instanceof Hero) updateWand((Hero) ch, false);
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_IMBUE)) {

			curUser = hero;
			GameScene.selectItem(itemSelector);

		} else if (action.equals(AC_ZAP)) {

			if (wand == null) {
				GameScene.show(new WndUseItem(null, this));
				return;
			}

			if (cursed || hasCurseEnchant()) wand.cursed = true;
			else                             wand.cursed = false;
			if (hero.hasTalent(Talent.FORBIDDEN_KNOWLEDGE)) wand.cursed = false;
			wand.execute(hero, AC_ZAP);
		} else if (action.equals(AC_SPARK)) {

			if (wand == null) {
				GameScene.show(new WndUseItem(null, this));
				return;
			}

			if (cursed || hasCurseEnchant()) wand.cursed = true;
			else                             wand.cursed = false;
			if (hero.hasTalent(Talent.FORBIDDEN_KNOWLEDGE)) wand.cursed = false;
			wand.execute(hero, AC_SPARK);
		} else if (action.equals(AC_READ)) {

			if (wand == null) {
				GameScene.show(new WndUseItem(null, this));
				return;
			}

			if (records.size() == 1) readRecord( hero, 0 );
			else {
				String[] scrollNames = new String[records.size()];
				for (int i = 0; i < records.size(); i ++) {
					scrollNames[i] = records.get(i).trueName();
				}
				ItemSprite sprite = new ItemSprite();
				GameScene.show(new WndOptions( sprite,
						Messages.titleCase(Messages.get(this, "recorded_scrolls")),
						Messages.get(this, "prompt_record"), scrollNames) {
					{
						sprite.view(MagesStaff.this);
					}
					@Override
					protected void onSelect(int index) {
						readRecord( hero, index );
					}
				});
			}
		}
	}

	private void readRecord( Hero hero, int index ) {
		Scroll scroll = records.get(index);
		int charges = 1;
		if (scroll instanceof ExoticScroll) charges ++;
		if (MagesStaff.this.wand.curCharges < charges) {
			GLog.w(Messages.get(Wand.class, "fizzles"));
		}
		else {
			MagesStaff.this.wand.curCharges -= charges;
			records.remove(scroll);
			scroll.anonymize();
			curItem = scroll;
			curUser = hero;
			scroll.doRead();
			Buff.affect(curUser, ReadRecordCooldown.class, ReadRecordCooldown.DURATION);
		}
	}

	@Override
	public int buffedLvl() {
		if (wand != null){
			return Math.max(super.buffedLvl(), wand.buffedLvl());
		} else {
			return super.buffedLvl();
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (attacker instanceof Hero && ((Hero) attacker).hasTalent(Talent.KNOWLEDGE_IS_POWER) && records.size() > 0) {
			damage += Math.floor(records.size() * (2 + ((Hero) attacker).pointsInTalent(Talent.KNOWLEDGE_IS_POWER)));
		}
		if (attacker.buff(Talent.EmpoweredStrikeTracker.class) != null){
			attacker.buff(Talent.EmpoweredStrikeTracker.class).detach();
			damage = Math.round( damage * (1f + Dungeon.hero.pointsInTalent(Talent.EMPOWERED_STRIKE)/3f));
		}

		if (wand.curCharges >= wand.maxCharges && attacker instanceof Hero) {
			int shield = (int) Math.floor(buffedLvl()*((Hero) attacker).pointsInTalent(Talent.EXCESS_CHARGE)*0.5f);
			Barrier barrier = attacker.buff(Barrier.class);
			if (shield > 0) {
				if (barrier == null) Buff.affect(attacker, Barrier.class).setShield(shield);
				else {
					int max = buffedLvl() * 2;
					if (barrier.shielding() >= max) {
						Buff.affect(attacker, Barrier.class).setShield(shield);
					}
					else {
						if (barrier.shielding() + shield > max) shield = max - barrier.shielding();
						Buff.affect(attacker, Barrier.class).incShield(shield);
					}
				}
			}
		}

		if (attacker instanceof Hero && ((Hero) attacker).hasTalent(Talent.MYSTICAL_CHARGE)){
			Hero hero = (Hero) attacker;
			for (Buff b : hero.buffs()){
				if (b instanceof Artifact.ArtifactBuff) ((Artifact.ArtifactBuff) b).charge(hero, hero.pointsInTalent(Talent.MYSTICAL_CHARGE)/2f);
			}
		}

		if (wand != null &&
				attacker instanceof Hero && ((Hero)attacker).subClass == HeroSubClass.BATTLEMAGE) {
			if (wand.curCharges < wand.maxCharges) wand.partialCharge += 0.5f;
			ScrollOfRecharging.charge((Hero)attacker);
			wand.onHit(this, attacker, defender, damage);
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int reachFactor(Char owner) {
		int reach = super.reachFactor(owner);
		if (owner instanceof Hero
				&& (wand instanceof WandOfDisintegration || wand instanceof SparkWandOfDisintegration)
				&& ((Hero)owner).subClass == HeroSubClass.BATTLEMAGE){
			reach++;
		}
		return reach;
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)) {
			if (container.owner != null) {
				applyWandChargeBuff(container.owner);
				if (container.owner instanceof Hero) updateWand((Hero) container.owner, false);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach( ) {
		if (wand != null) wand.stopCharging();
	}

	public Item imbueWand( Wand wand, Char owner ) {
		return imbueWand(wand, owner, true);
	}

	public Item imbueWand( Wand wand, Char owner, boolean preserve ) {

		int oldStaffcharges = this.wand.curCharges;

		if (preserve && owner == Dungeon.hero && Dungeon.hero.hasTalent(Talent.WAND_PRESERVATION)) {

			Talent.WandPreservationCounter counter = Buff.affect(Dungeon.hero, Talent.WandPreservationCounter.class);
			if (counter.count() < 3) {
				counter.countUp(1);
				if (Dungeon.hero.pointsInTalent(Talent.WAND_PRESERVATION) == 2) {
					int curLvl = this.wand.level();
					int wandLvl = wand.level();
					final int lostLvl;
					if (wandLvl >= curLvl) {
						lostLvl = curLvl - 1;
					}
					else {
						lostLvl = wandLvl;
					}
					final int resins = lostLvl * 2 - 1;
					if (resins > 0) {
						Item item = new ArcaneResin().quantity(resins);
						if (!item.collect()) {
							Dungeon.level.drop(item, owner.pos);
						}
					}
				}
				this.wand.level(0);
				if (!this.wand.collect()) {
					Dungeon.level.drop(this.wand, owner.pos);
				}

				GLog.newLine();
				GLog.p(Messages.get(this, "preserved"));
			}
		}

		this.wand = null;

		wand.resinBonus = 0;
		wand.updateLevel();

		//syncs the level of the two items.
		int targetLevel = Math.max(this.level() - (curseInfusionBonus ? 1 : 0), wand.level());

		//if the staff's level is being overridden by the wand, preserve 1 upgrade
		if (wand.level() >= this.level() && this.level() > (curseInfusionBonus ? 1 : 0)) targetLevel++;
		
		level(targetLevel);
		this.wand = wand;
		if (wand instanceof SparkWand) {
			image = ItemSpriteSheet.MASTERS_SPARKWAND;
		}
		else image = ItemSpriteSheet.MAGES_STAFF;
		updateWand(false);
		wand.curCharges = Math.min(wand.maxCharges, wand.curCharges+oldStaffcharges);
		if (owner != null) wand.charge(owner);

		//This is necessary to reset any particles.
		//FIXME this is gross, should implement a better way to fully reset quickslot visuals
		int slot = Dungeon.quickslot.getSlot(this);
		if (slot != -1){
			Dungeon.quickslot.clearSlot(slot);
			updateQuickslot();
			Dungeon.quickslot.setSlot( slot, this );
			updateQuickslot();
		}
		
		Badges.validateItemLevelAquired(this);

		return this;
	}

	public void gainCharge( float amt ){
		gainCharge(amt, false);
	}

	public void gainCharge( float amt, boolean overcharge ){
		if (wand != null){
			wand.gainCharge(amt, overcharge);
		}
	}

	public void applyWandChargeBuff(Char owner) {
		if (wand != null){
			wand.charge(owner, STAFF_SCALE_FACTOR);
		}
	}

	public Class<?extends Wand> wandClass(){
		return wand != null ? wand.getClass() : null;
	}

	@Override
	public Item upgrade(boolean enchant) {
		super.upgrade( enchant );

		updateWand(true);

		return this;
	}

	@Override
	public Item degrade() {
		super.degrade();

		updateWand(false);

		return this;
	}

	public final void updateWand( boolean levelled ) {
		updateWand(Dungeon.hero, levelled);
	}
	
	public void updateWand( Hero hero, boolean levelled ) {
		if (wand != null) {
			int curCharges = wand.curCharges;
			wand.level(level());
			//gives the wand one additional max charge
			wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
			if ((cursed || hasCurseEnchant()) && hero.hasTalent(Talent.FORBIDDEN_KNOWLEDGE)) {
				wand.maxCharges += 2 * hero.pointsInTalent(Talent.FORBIDDEN_KNOWLEDGE);
			}
			wand.curCharges = Math.min(curCharges + (levelled ? 1 : 0), wand.maxCharges);
			updateQuickslot();
		}
	}

	@Override
	public String status() {
		if (wand == null) return super.status();
		else return wand.status();
	}

	@Override
	public String name() {
		if (wand == null) {
			return super.name();
		} else {
			String name = Messages.get(wand, "staff_name");
			return enchantment != null && (cursedKnown || !enchantment.curse()) ? enchantment.name( name ) : name;
		}
	}

	@Override
	public String info() {
		String info = super.info();

		if (wand != null) {
			info += "\n\n" + Messages.get(this, "has_wand", Messages.get(wand, "name"));
			info += wand.info();
			/*
			if (!cursed || !cursedKnown)    info += " " + wand.statsDesc();
			else                            info += " " + Messages.get(this, "cursed_wand");

			if (Dungeon.hero.subClass == HeroSubClass.BATTLEMAGE){
				info += "\n\n" + Messages.get(wand, "bmage_desc");
			}
			 */
		}
		if (!records.isEmpty()) {
			String recordsText = "";
			for (Scroll record : records) {
				recordsText += "_" + record.name() + "_, ";
			}
			recordsText = recordsText.substring(0, recordsText.length() - 2);
			info += "\n\n" + Messages.get( this, "records", Messages.replaceComma( recordsText ));
		}

		return info;
	}

	@Override
	public Emitter emitter() {
		if (wand == null) return null;
		Emitter emitter = new Emitter();
		emitter.pos(14, 1);
		emitter.fillTarget = false;
		emitter.pour(StaffParticleFactory, 0.1f);
		return emitter;
	}

	private static final String WAND = "wand";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND, wand);
		bundle.put(RECORDS, records);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		wand = (Wand) bundle.get(WAND);
		if (wand != null) {
			wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
			if (wand instanceof SparkWand) image = ItemSpriteSheet.MASTERS_SPARKWAND;
		}
		records = new ArrayList<>((Collection<Scroll>) ((Collection<?>) bundle.getCollection(RECORDS)));
	}

	public void record(Scroll scroll, Callback afterRecord) {
		if (Dungeon.hero.subClass != HeroSubClass.LOREMASTER ||
				scroll instanceof ScrollOfUpgrade || scroll instanceof ScrollOfTransmutation) {
			if (afterRecord != null) afterRecord.call();
			return;
		}
		int max = 1 + Dungeon.hero.pointsInTalent(Talent.MAGIC_TRACE);
		if (records.size() == max) {
			String[] texts = new String[records.size() + 1];
			for (int i = 0; i < records.size(); i ++) {
				texts[i] = records.get(i).trueName();
			}
			texts[texts.length-1] = Messages.get(MagesStaff.class, "cancel");
			GameScene.show(new WndOptions(new ItemSprite(scroll),
					Messages.titleCase(scroll.trueName()),
					Messages.get(MagesStaff.class, "override_record"),
					texts) {
				@Override
				protected void onSelect(int index) {
					if (index < records.size()) {
						GLog.newLine();
						GLog.w( Messages.get(MagesStaff.class, "lost_record", Messages.titleCase(records.remove(index).trueName())) );
						records.add(Reflection.newInstance(scroll.getClass()));
						GLog.i( Messages.get(MagesStaff.class, "record", Messages.titleCase(scroll.trueName())) );
					}
					if (afterRecord != null) afterRecord.call();
				}
				@Override
				public void onBackPressed() {
				}
			});
		}
		else {
			records.add(Reflection.newInstance(scroll.getClass()));
			GLog.newLine();
			GLog.i( Messages.get(this, "record", Messages.titleCase(scroll.trueName())) );
			if (afterRecord != null) afterRecord.call();
		}
	}

	@Override
	public int value() {
		return 0;
	}
	
	@Override
	public Weapon enchant(Enchantment ench) {
		if (curseInfusionBonus && (ench == null || !ench.curse())){
			curseInfusionBonus = false;
			updateWand(false);
		}
		return super.enchant(ench);
	}
	
	private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(MagesStaff.class, "prompt");
		}

		@Override
		public Class<?extends Bag> preferredBag(){
			return MagicalHolster.class;
		}

		@Override
		public boolean itemSelectable(Item item) {
			return item instanceof Wand;
		}

		@Override
		public void onSelect( final Item item ) {
			if (item != null) {

				if (!item.isIdentified()) {
					GLog.w(Messages.get(MagesStaff.class, "id_first"));
					return;
				} else if (item.cursed){
					GLog.w(Messages.get(MagesStaff.class, "cursed"));
					return;
				}

				if (wand == null){
					applyWand((Wand)item);
				} else {
					int newLevel;
					int itemLevel = item.level();
					itemLevel -= ((Wand)item).resinBonus;
					if (itemLevel >= level()){
						if (level() > 0)    newLevel = itemLevel + 1;
						else                newLevel = itemLevel;
					} else {
						newLevel = level();
					}

					String bodyText = Messages.get(MagesStaff.class, "imbue_desc", newLevel);
					int preservesLeft = Dungeon.hero.hasTalent(Talent.WAND_PRESERVATION) ? 3 : 0;
					if (Dungeon.hero.buff(Talent.WandPreservationCounter.class) != null){
						preservesLeft -= Dungeon.hero.buff(Talent.WandPreservationCounter.class).count();
					}
					if (preservesLeft > 0){
						bodyText += "\n\n" + Messages.get(MagesStaff.class, "imbue_talent", preservesLeft);
					} else {
						bodyText += "\n\n" + Messages.get(MagesStaff.class, "imbue_lost");
					}

					GameScene.show(
							new WndOptions(new ItemSprite(item),
									Messages.titleCase(item.name()),
									bodyText,
									Messages.get(MagesStaff.class, "yes"),
									Messages.get(MagesStaff.class, "no")) {
								@Override
								protected void onSelect(int index) {
									if (index == 0) {
										applyWand((Wand)item);
									}
								}
							}
					);
				}
			}
		}

		private void applyWand(Wand wand){
			Sample.INSTANCE.play(Assets.Sounds.BURNING);
			curUser.sprite.emitter().burst( ElmoParticle.FACTORY, 12 );
			evoke(curUser);

			Dungeon.quickslot.clearItem(wand);

			wand.detach(curUser.belongings.backpack);

			GLog.p( Messages.get(MagesStaff.class, "imbue", wand.name()));
			imbueWand( wand, curUser );

			updateQuickslot();
		}
	};

	private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
		@Override
		//reimplementing this is needed as instance creation of new staff particles must be within this class.
		public void emit( Emitter emitter, int index, float x, float y ) {
			StaffParticle c = (StaffParticle)emitter.getFirstAvailable(StaffParticle.class);
			if (c == null) {
				c = new StaffParticle();
				emitter.add(c);
			}
			c.reset(x, y);
		}

		@Override
		//some particles need light mode, others don't
		public boolean lightMode() {
			return !((wand instanceof WandOfDisintegration)
					|| (wand instanceof WandOfCorruption)
					|| (wand instanceof WandOfCorrosion)
					|| (wand instanceof WandOfRegrowth)
					|| (wand instanceof WandOfLivingEarth)
			        || (wand instanceof SparkWandOfDisintegration)
					|| (wand instanceof SparkWandOfCorruption)
					|| (wand instanceof SparkWandOfCorrosion)
					|| (wand instanceof SparkWandOfRegrowth)
					|| (wand instanceof SparkWandOfLivingEarth));
		}
	};

	//determines particle effects to use based on wand the staff owns.
	public class StaffParticle extends PixelParticle{

		private float minSize;
		private float maxSize;
		public float sizeJitter = 0;

		public StaffParticle(){
			super();
		}

		public void reset( float x, float y ) {
			revive();

			speed.set(0);

			this.x = x;
			this.y = y;

			if (wand != null)
				wand.staffFx( this );

		}

		public void setSize( float minSize, float maxSize ){
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public void setLifespan( float life ){
			lifespan = left = life;
		}

		public void shuffleXY(float amt){
			x += Random.Float(-amt, amt);
			y += Random.Float(-amt, amt);
		}

		public void radiateXY(float amt){
			float hypot = (float)Math.hypot(speed.x, speed.y);
			this.x += speed.x/hypot*amt;
			this.y += speed.y/hypot*amt;
		}

		@Override
		public void update() {
			super.update();
			size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
		}
	}

	public static class ReadRecordCooldown extends FlavourBuff {

		public static final float DURATION = 10f;
		public int icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f,0.5f,1); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / DURATION); }
		public String toString() { return Messages.get(this, "name"); }
		public String desc() { return Messages.get(this, "desc", dispTurns(visualcooldown())); }
	};

	public void wandToSpark() {
		if (wand != null && !(wand instanceof SparkWand)) {
			imbueWand(wand.wandToSpark(), curUser, false);
		}
	}

	public void sparkToWand() {
		if (wand != null && wand instanceof SparkWand) {
			imbueWand(wand.sparkToWand(), curUser, false);
		}
	}

}
