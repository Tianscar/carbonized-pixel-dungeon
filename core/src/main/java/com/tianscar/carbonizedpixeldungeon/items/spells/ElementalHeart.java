package com.tianscar.carbonizedpixeldungeon.items.spells;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Blob;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Fire;
import com.tianscar.carbonizedpixeldungeon.actors.blobs.Web;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Barrier;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Buff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Burning;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Chill;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Cripple;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.FlavourBuff;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Invisibility;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.MagicImmune;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Paralysis;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Spellweave;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Elemental;
import com.tianscar.carbonizedpixeldungeon.effects.CellEmitter;
import com.tianscar.carbonizedpixeldungeon.effects.Lightning;
import com.tianscar.carbonizedpixeldungeon.effects.MagicMissile;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.effects.particles.FlameParticle;
import com.tianscar.carbonizedpixeldungeon.effects.particles.RainbowParticle;
import com.tianscar.carbonizedpixeldungeon.effects.particles.SparkParticle;
import com.tianscar.carbonizedpixeldungeon.items.Heap;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfLightning;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.tianscar.carbonizedpixeldungeon.levels.Level;
import com.tianscar.carbonizedpixeldungeon.levels.Terrain;
import com.tianscar.carbonizedpixeldungeon.mechanics.Ballistica;
import com.tianscar.carbonizedpixeldungeon.mechanics.ConeAOE;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Camera;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.tiles.DungeonTilemap;
import com.tianscar.carbonizedpixeldungeon.ui.BuffIndicator;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.utils.Bundle;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;
import com.tianscar.carbonizedpixeldungeon.utils.PathFinder;
import com.tianscar.carbonizedpixeldungeon.utils.Random;
import com.tianscar.carbonizedpixeldungeon.windows.WndOptions;
import com.tianscar.carbonizedpixeldungeon.windows.WndTitledMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.tianscar.carbonizedpixeldungeon.items.wands.WandOfBlastWave.throwChar;

public class ElementalHeart extends TargetedSpell {

    public boolean curseInfusionBonus = false;

    public static final String AC_FOCUS = "FOCUS";
    public static final String AC_TWEAK = "TWEAK";

    {
        image = ItemSpriteSheet.ELEMENTAL_HEART;

        unique = true;
        bones = false;
    }

    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_FOCUS );
        actions.add( AC_TWEAK );
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals( AC_CAST )) onZap( hero );
        else if (action.equals( AC_FOCUS )) focus();
        else if (action.equals( AC_TWEAK )) {
            if (Dungeon.hero.buffs(Focus.class).isEmpty()) {
                Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                GLog.w( Messages.get(ElementalHeart.class, "no_focus") );
            }
            else tweak();
        }
    }

    private void focus() {
        if (curUser.buff(MagicImmune.class) != null) {
            Dungeon.hero.sprite.operate(Dungeon.hero.pos);
            GLog.w( Messages.get(this, "no_magic") );
            return;
        }
        List<String> optionList = new ArrayList<>();
        optionList.add(Messages.get(Elemental.FireElemental.class, "name"));
        optionList.add(Messages.get(Elemental.FrostElemental.class, "name"));
        optionList.add(Messages.get(Elemental.ShockElemental.class, "name"));
        if (Dungeon.hero.hasTalent(Talent.CHAOS_IMPACT)) optionList.add(Messages.get(Elemental.ChaosElemental.class, "name"));
        GameScene.show(new WndOptions(
                new ItemSprite(ItemSpriteSheet.ELEMENTAL_HEART),
                Messages.get(ElementalHeart.class, "name"),
                Messages.get(ElementalHeart.class, "focus_title"),
                optionList.toArray(new String[0])) {
            @Override
            protected void onSelect(int index) {
                switch (index) {
                    default:
                        return;
                    case 0:
                        Buff.prolong(Dungeon.hero, FireFocus.class, FireFocus.duration()).fx();
                        FireFocus.burnTile(Dungeon.hero);
                        break;
                    case 1:
                        Buff.prolong(Dungeon.hero, FrostFocus.class, FrostFocus.duration()).fx();
                        break;
                    case 2:
                        Buff.prolong(Dungeon.hero, ShockFocus.class, ShockFocus.duration()).fx();
                        break;
                    case 3:
                        Buff.prolong(Dungeon.hero, ChaosFocus.class, ChaosFocus.duration()).fx();
                        break;
                }
                Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                Dungeon.hero.busy();
                Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

                Invisibility.dispel(Dungeon.hero.pointsInTalent(Talent.TIME_TRAVELER) <= 2);

                curUser.spendAndNext(Actor.TICK);
            }

            @Override
            protected boolean hasInfo(int index) {
                return true;
            }

            @Override
            protected void onInfo(int index) {
                Class<? extends Focus> elemental;
                switch (index) {
                    default:
                        return;
                    case 0:
                        elemental = FireFocus.class;
                        break;
                    case 1:
                        elemental = FrostFocus.class;
                        break;
                    case 2:
                        elemental = ShockFocus.class;
                        break;
                    case 3:
                        elemental = ChaosFocus.class;
                        break;
                }
                GameScene.show(new WndTitledMessage(
                        Icons.get(Icons.INFO),
                        Messages.titleCase(Messages.get(elemental, "name")),
                        Messages.get(elemental, "desc")));
            }
        });
    }

    private void tweak() {
        if (curUser.buff(MagicImmune.class) != null) {
            Dungeon.hero.sprite.operate(Dungeon.hero.pos);
            GLog.w( Messages.get(this, "no_magic") );
            return;
        }
        Map<String, Class<? extends Focus>> optionMap = new LinkedHashMap<>();
        if (Dungeon.hero.buff(FireFocus.class) != null)
            optionMap.put(Messages.get(Elemental.FireElemental.class, "name"), FireFocus.class);
        if (Dungeon.hero.buff(FrostFocus.class) != null)
            optionMap.put(Messages.get(Elemental.FrostElemental.class, "name"), FrostFocus.class);
        if (Dungeon.hero.buff(ShockFocus.class) != null)
            optionMap.put(Messages.get(Elemental.ShockElemental.class, "name"), ShockFocus.class);
        if (Dungeon.hero.buff(ChaosFocus.class) != null)
            optionMap.put(Messages.get(Elemental.ChaosElemental.class, "name"), ChaosFocus.class);
        String[] options = optionMap.keySet().toArray(new String[0]);
        GameScene.show(new WndOptions(
                new ItemSprite(ItemSpriteSheet.ELEMENTAL_HEART),
                Messages.get(ElementalHeart.class, "name"),
                Messages.get(ElementalHeart.class, "tweak_title"),
                options) {
            @Override
            protected void onSelect(int index) {
                Dungeon.hero.buff(FocusTracker.class).elemental = optionMap.get(options[index]);
                Dungeon.hero.sprite.operate(Dungeon.hero.pos);
            }
        });
    }

    @Override
    protected void onCast(Hero hero) {
        // prevent default
    }

    protected void onZap(Hero hero) {
        if (tweakedFocus() == null) {
            usesTargeting = false;
            if (!hasFocus()) focus();
            else tweak();
        }
        else {
            usesTargeting = true;
            super.onCast(hero);
        }
    }

    @Override
    protected int collisionProperties(int target) {
        if (tweakedFocus() == null) return Ballistica.MAGIC_BOLT;
        else return tweakedFocus().collisionProperties(target);
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        if (tweakedFocus() != null) {
            tweakedFocus().fx(bolt, new Callback() {
                @Override
                public void call() {
                    ElementalHeart.this.affectTarget(bolt, curUser);
                    Invisibility.dispel();
                    Item.updateQuickslot();
                    curUser.spendAndNext( 1f );
                }
            });
        }

    }

    @Override
    protected void affectTarget(Ballistica bolt, Hero hero) {
        if (tweakedFocus() != null) {
            tweakedFocus().affectTarget(this, bolt, hero);
            if (hero.subClass == HeroSubClass.SPELLWEAVER) Buff.affect(hero, Spellweave.class).stack();
        }
    }

    @Override
    public String desc() {
        String desc = super.desc();
        desc += "\n\n" + (tweakedFocus() == null ?
                Messages.get(ElementalHeart.class, "impact_desc") : tweakedFocus().impactDesc(this));
        return desc;
    }
    
    private boolean hasFocus() {
        return Dungeon.hero.buff(FocusTracker.class) != null;
    }
    
    private Focus tweakedFocus() {
        return hasFocus() ? Dungeon.hero.buff(Dungeon.hero.buff(FocusTracker.class).elemental) : null;
    }

    private static final String CURSE_INFUSION_BONUS= "curse_infusion_bonus";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( CURSE_INFUSION_BONUS, curseInfusionBonus );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        curseInfusionBonus = bundle.getBoolean( CURSE_INFUSION_BONUS );
    }

    @Override
    public int level() {
        return (Dungeon.hero == null ? 0 : Dungeon.hero.lvl/5) + (curseInfusionBonus ? 1 : 0);
    }

    @Override
    public int buffedLvl() {
        //level isn't affected by buffs/debuffs
        return level();
    }

    public static class FocusTracker extends Buff {

        {
            actPriority = BUFF_PRIO - 1;
        }

        @Override
        public boolean attachTo(Char target) {
            if (target.buff(FocusTracker.class) != null) return false;
            else return super.attachTo(target);
        }

        int count;

        @Override
        public boolean act() {
            if (target.buff(ShockFocus.class) != null && !((Hero) target).hasTalent(Talent.INSULATED_ELECTRICITY) &&
                    Dungeon.level.water[target.pos] && !target.flying) {
                target.buff(ShockFocus.class).detach();
            }
            if (target.buff(FireFocus.class) != null && target.buff(FrostFocus.class) != null) {
                target.buff(FireFocus.class).detach();
                target.buff(FrostFocus.class).detach();

                Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.75f);
                CellEmitter.get( target.pos ).burst( Speck.factory( Speck.STEAM ), 10 );
                Dungeon.level.setCellToWater(false, target.pos);

                if (((Hero) target).hasTalent(Talent.HYDROMANCER)) {
                    ArrayList<Integer> waterCells = new ArrayList<>();
                    for (int i : PathFinder.NEIGHBOURS8) {
                        waterCells.add(target.pos+i);
                    }
                    Random.shuffle(waterCells);
                    for (int cell : waterCells) {
                        Char ch = Actor.findChar(cell);
                        if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                            //trace a ballistica to our target (which will also extend past them
                            Ballistica trajectory = new Ballistica(target.pos, ch.pos, Ballistica.STOP_TARGET);
                            //trim it to just be the part that goes past them
                            trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
                            //knock them back along that ballistica, ensuring they don't fall into a pit
                            int dist = ((Hero) target).pointsInTalent(Talent.HYDROMANCER);
                            if (!ch.flying) {
                                while (dist > trajectory.dist ||
                                        (dist > 0 && Dungeon.level.pit[trajectory.path.get(dist)])) {
                                    dist--;
                                }
                            }
                            throwChar(ch, trajectory, dist, true, false);
                        }
                    }
                    if (((Hero) target).pointsInTalent(Talent.HYDROMANCER) == 1) {
                        waterCells.remove(0);
                        waterCells.remove(0);
                        waterCells.remove(0);
                    }
                    for (int cell : waterCells) {
                        CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 10 );
                        Dungeon.level.setCellToWater(false, cell);
                    }
                }
            }
            if (count < 1) detach();
            else {
                Hero hero = (Hero) target;
                boolean bind = hero.subClass == HeroSubClass.BINDER &&
                        hero.belongings.armor() != null &&
                        target.buff(FrostFocus.class) != null;
                if (hero.hasTalent(Talent.PROTECTIVE_ELEMENTALS) || bind) {
                    int talent = 0;
                    Barrier barrier = Buff.affect(target, Barrier.class);
                    //barrier every 2/1 turns, to a max of 3/5
                    if (hero.hasTalent(Talent.PROTECTIVE_ELEMENTALS)) {
                        talent = 1 + 2 * hero.pointsInTalent(Talent.PROTECTIVE_ELEMENTALS);
                        if (barrier.shielding() < talent) {
                            barrierInc += 0.5f * hero.pointsInTalent(Talent.PROTECTIVE_ELEMENTALS);
                        }
                        if ( barrierInc >= 1 ) {
                            barrierInc = 0;
                            barrier.incShield(1);
                        }
                    }
                    //barrier every 2 turns
                    if (bind) {
                        if (barrier.shielding() < 1 + talent + hero.belongings.armor.buffedLvl()) {
                            barrierInc += 0.5f;
                        }
                        if ( barrierInc >= 1 ) {
                            barrierInc = 0;
                            barrier.incShield(1);
                        }
                    }
                }
            }
            spend( TICK );
            return true;
        }

        Class<? extends Focus> elemental = null;
        float barrierInc = 0.5f;

        private static final String ELEMENTAL = "elemental";
        private static final String BARRIER_INC = "barrier_inc";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( ELEMENTAL, elemental );
            bundle.put( BARRIER_INC, barrierInc );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            elemental = (Class<? extends Focus>) (Class<?>) bundle.getClass( ELEMENTAL );
            barrierInc = bundle.getFloat( BARRIER_INC );
        }

    }

    public static abstract class Focus extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        private static final float DURATION = 3f;

        public static float duration() {
            return DURATION + Dungeon.hero.pointsInTalent(Talent.EXTENDED_FOCUS);
        }

        protected float fadeTime() {
            return duration();
        }

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)) {
                Buff.affect(target, FocusTracker.class).count ++;
                return true;
            }
            else return false;
        }

        @Override
        public void detach() {
            super.detach();
            FocusTracker tracker = target.buff(FocusTracker.class);
            if (tracker != null) {
                tracker.count --;
                if (tracker.elemental == getClass()) tracker.elemental = null;
            }
        }

        @Override
        public float iconFadePercent() {
            final float duration = fadeTime();
            return Math.max(0, (duration - visualcooldown()) / duration);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(ElementalHeart.Focus.class, "desc", elemName(), Messages.get(this, "desc"), dispTurns());
        }

        public abstract String elemName();

        public String impactDesc(ElementalHeart heart) {
            return Messages.get(ElementalHeart.Focus.class, "impact_desc", Messages.get(this, "impact_name")) + Messages.get(this, "impact_desc", min(heart), max(heart));
        }

        public abstract void fx();

        protected abstract void fx(Ballistica bolt, Callback callback);

        protected void affectTarget(ElementalHeart heart, Ballistica bolt, Hero hero) {
            Buff.detach(hero, getClass());
        }

        protected int collisionProperties(int target) {
            return Ballistica.MAGIC_BOLT;
        }

        public int min(ElementalHeart heart) {
            return min(heart.buffedLvl());
        }

        public abstract int min(int lvl);

        public int max(ElementalHeart heart) {
            return max(heart.buffedLvl());
        }

        public abstract int max(int lvl);

        public int damageRoll(ElementalHeart heart) {
            return damageRoll(heart.buffedLvl());
        }

        public int damageRoll(int lvl) {
            return Random.NormalIntRange(min(lvl), max(lvl)) +
                    (((Hero) target).hasTalent(Talent.ELEMENTAL_MASTER) ? 1 + ((Hero) target).pointsInTalent(Talent.ELEMENTAL_MASTER) : 0);
        }

    }

    public static class FireFocus extends Focus {

        public static float duration() {
            float duration = Focus.duration();
            if (Dungeon.hero.subClass == HeroSubClass.BINDER) {
                int level = 0;
                if (Dungeon.hero.belongings.weapon() != null && !(Dungeon.hero.belongings.weapon() instanceof MissileWeapon)) {
                    level = Dungeon.hero.belongings.weapon().buffedLvl();
                }
                if (Dungeon.hero.belongings.extra() != null) {
                    level = Math.max(level, Dungeon.hero.belongings.extra().buffedLvl());
                }
                if (level > 2) duration += (level - 1) / 2;
            }
            return duration;
        }

        @Override
        protected float fadeTime() {
            return FireFocus.duration();
        }

        @Override
        public int icon() {
            return BuffIndicator.FIRE;
        }

        public static void burnTile(Char target) {
            int pos = target.pos;
            if ( !target.flying && Dungeon.level.map[pos] == Terrain.WATER ) {
                Level.set( pos, Terrain.EMPTY );
                GameScene.updateMap( pos );
                CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 10 );
            }
            if ( Dungeon.level.flamable[pos] ) {

                Sample.INSTANCE.play( Assets.Sounds.BURNING );
                Dungeon.level.destroy( pos );
                GameScene.updateMap( pos );

            }
        }

        @Override
        public String elemName() {
            return Messages.get(Elemental.FireElemental.class, "name");
        }

        @Override
        public void fx() {
            target.sprite.emitter().start( FlameParticle.FACTORY, 0.06f, 10 );
        }

        @Override
        protected void fx(Ballistica bolt, Callback callback) {
            MagicMissile.boltFromChar(curUser.sprite.parent,
                    MagicMissile.FIRE,
                    curUser.sprite,
                    bolt.collisionPos,
                    callback);
            Sample.INSTANCE.play(Assets.Sounds.ZAP);
        }

        @Override
        protected void affectTarget(ElementalHeart heart, Ballistica bolt, Hero hero) {
            super.affectTarget(heart, bolt, hero);

            int cell = bolt.collisionPos;

            Char ch = Actor.findChar( cell );
            if (ch == null) {
                Dungeon.level.pressCell(bolt.collisionPos);
                GameScene.add( Blob.seed(cell, 2, com.tianscar.carbonizedpixeldungeon.actors.blobs.Fire.class) );
                CellEmitter.center( cell ).burst( FlameParticle.FACTORY, heart.buffedLvl() / 2 + 2 );
            }
            else {
                ch.damage( damageRoll(heart), heart );

                if (!ch.isAlive() && Dungeon.hero.hasTalent(Talent.WILDFIRE) &&
                        Random.Int(3) < 1+Dungeon.hero.pointsInTalent(Talent.WILDFIRE)) {
                    Buff.prolong(target, FireFocus.class, 3).fx();
                }

                if (ch.isAlive()) Buff.affect( ch, Burning.class ).reignite( ch );

                ch.sprite.emitter().burst( FlameParticle.FACTORY, heart.buffedLvl() / 2 + 2 );
            }

        }

        @Override
        public int min(int lvl) {
            return 2+lvl;
        }

        @Override
        public int max(int lvl) {
            return 8+5*lvl;
        }

    }

    public static class FrostFocus extends Focus {

        public static float duration() {
            float duration = Focus.duration();
            if (Dungeon.hero.subClass == HeroSubClass.BINDER) {
                int level = 0;
                if (Dungeon.hero.belongings.armor() != null) {
                    level = Dungeon.hero.belongings.armor().buffedLvl();
                }
                if (level > 2) duration += (level - 1) / 2;
            }
            return duration;
        }

        @Override
        protected float fadeTime() {
            return FrostFocus.duration();
        }

        @Override
        public int icon() {
            return BuffIndicator.FROST;
        }

        @Override
        public String elemName() {
            return Messages.get(Elemental.FrostElemental.class, "name");
        }

        @Override
        public void fx() {
            target.sprite.emitter().start( MagicMissile.MagicParticle.FACTORY, 0.06f, 10 );
        }

        @Override
        protected void fx(Ballistica bolt, Callback callback) {

            //copied from WandOfFrost.java

            MagicMissile.boltFromChar(curUser.sprite.parent,
                    MagicMissile.FROST,
                    curUser.sprite,
                    bolt.collisionPos,
                    callback);
            Sample.INSTANCE.play(Assets.Sounds.ZAP);
        }

        @Override
        protected void affectTarget(ElementalHeart heart, Ballistica bolt, Hero hero) {
            super.affectTarget(heart, bolt, hero);

            //copied from WandOfFrost.java

            Heap heap = Dungeon.level.heaps.get(bolt.collisionPos);
            if (heap != null) {
                heap.freeze();
            }

            Char ch = Actor.findChar(bolt.collisionPos);
            if (ch != null) {

                int damage = damageRoll(heart);

                if (ch.buff(com.tianscar.carbonizedpixeldungeon.actors.buffs.Frost.class) != null){
                    return; //do nothing, can't affect a frozen target
                }
                if (ch.buff(Chill.class) != null){
                    //6.67% less damage per turn of chill remaining, to a max of 10 turns (50% dmg)
                    float chillturns = Math.min(10, ch.buff(Chill.class).cooldown());
                    damage = (int)Math.round(damage * Math.pow(0.9333f, chillturns));
                } else {
                    ch.sprite.burst( 0xFF99CCFF, heart.buffedLvl() / 2 + 2 );
                }

                ch.damage(damage, heart);
                Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, 1.1f * Random.Float(0.87f, 1.15f) );

                if (ch.isAlive()){
                    if (Dungeon.level.water[ch.pos])
                        Buff.affect(ch, Chill.class, 4+heart.buffedLvl());
                    else
                        Buff.affect(ch, Chill.class, 2+heart.buffedLvl());
                }
            } else {
                Dungeon.level.pressCell(bolt.collisionPos);
            }
        }

        @Override
        public int min(int lvl) {
            return 2+lvl;
        }

        @Override
        public int max(int lvl) {
            return 8+5*lvl;
        }

    }

    public static class ShockFocus extends Focus {

        public static float duration() {
            float duration = Focus.duration();
            if (Dungeon.hero.subClass == HeroSubClass.BINDER) {
                int level = 0;
                for (Item item : Dungeon.hero.belongings.getAllSimilar(new MissileWeapon.PlaceHolder())) {
                    level = Math.max(level, item.buffedLvl());
                }
                if (level > 2) duration += (level - 1) / 2;
            }
            return duration;
        }

        @Override
        protected float fadeTime() {
            return ShockFocus.duration();
        }

        @Override
        public int icon() {
            return BuffIndicator.RECHARGING;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1, 1, 0);
        }

        private ArrayList<Char> affected = new ArrayList<>();

        private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

        @Override
        public String elemName() {
            return Messages.get(Elemental.ShockElemental.class, "name");
        }

        @Override
        public void fx() {
            target.sprite.emitter().start( SparkParticle.STATIC, 0.06f, 10 );
        }

        @Override
        protected void fx(Ballistica bolt, Callback callback) {

            //copied from WandOfLightning.java

            affected.clear();
            arcs.clear();

            int cell = bolt.collisionPos;

            Char ch = Actor.findChar( cell );
            if (ch != null) {
                affected.add( ch );
                arcs.add( new Lightning.Arc(curUser.sprite.center(), ch.sprite.center()));
                WandOfLightning.arc(arcs, affected, ch);
            } else {
                arcs.add( new Lightning.Arc(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(bolt.collisionPos)));
                CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
            }

            //don't want to wait for the effect before processing damage.
            curUser.sprite.parent.addToFront( new Lightning( arcs, null ) );
            Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );
            callback.call();
        }

        @Override
        protected void affectTarget(ElementalHeart heart, Ballistica bolt, Hero hero) {
            super.affectTarget(heart, bolt, hero);

            //copied from WandOfLightning.java

            //lightning deals less damage per-target, the more targets that are hit.
            float multipler = 0.4f + (0.6f/affected.size());
            //if the main target is in water, all affected take full damage
            if (Dungeon.level.water[bolt.collisionPos]) multipler = 1f;

            for (Char ch : affected) {
                if (ch == Dungeon.hero) Camera.main.shake( 2, 0.3f );
                ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
                ch.sprite.flash();

                if (ch != curUser && ch.alignment == curUser.alignment && ch.pos != bolt.collisionPos){
                    continue;
                }
                if (ch == curUser) {
                    ch.damage(Math.round(damageRoll(heart) * multipler * 0.5f), heart);
                } else {
                    ch.damage(Math.round(damageRoll(heart) * multipler), heart);
                }
            }

            if (!curUser.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n(Messages.get(WandOfLightning.class, "ondeath"));
            }
        }

        @Override
        public int min(int lvl) {
            return 5+lvl;
        }

        @Override
        public int max(int lvl) {
            return 10+5*lvl;
        }

    }

    public static class ChaosFocus extends Focus {

        ConeAOE cone;

        @Override
        public int icon() {
            return BuffIndicator.VERTIGO;
        }

        @Override
        public String elemName() {
            return Messages.get(Elemental.ChaosElemental.class, "name");
        }

        @Override
        public void fx() {
            target.sprite.emitter().start( RainbowParticle.BURST, 0.025f, 10 );
        }

        @Override
        protected void fx(Ballistica bolt, Callback callback) {

            // Copied from WandOfFireblast.java

            //need to perform flame spread logic here so we can determine what cells to put flames in.

            // 5/7/9 distance
            int maxDist = 3 + 2*chargesPerCast();
            int dist = Math.min(bolt.dist, maxDist);

            cone = new ConeAOE( bolt,
                    maxDist,
                    30 + 20*chargesPerCast(),
                    Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);

            //cast to cells at the tip, rather than all cells, better performance.
            for (Ballistica ray : cone.outerRays){
                ((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
                        MagicMissile.RAINBOW_CONE,
                        curUser.sprite,
                        ray.path.get(ray.dist),
                        null
                );
            }

            //final zap at half distance, for timing of the actual wand effect
            MagicMissile.boltFromChar( curUser.sprite.parent,
                    MagicMissile.RAINBOW_CONE,
                    curUser.sprite,
                    bolt.path.get(dist/2),
                    callback );
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
        }

        @Override
        protected void affectTarget(ElementalHeart heart, Ballistica bolt, Hero hero) {
            super.affectTarget(heart, bolt, hero);

            // Copied from WandOfFireblast.java

            ArrayList<Char> affectedChars = new ArrayList<>();
            //ArrayList<Integer> adjacentCells = new ArrayList<>();
            for( int cell : cone.cells ){

                //ignore caster cell
                if (cell == bolt.sourcePos){
                    continue;
                }

                //knock doors open
                if (Dungeon.level.map[cell] == Terrain.DOOR){
                    Level.set(cell, Terrain.OPEN_DOOR);
                    GameScene.updateMap(cell);
                }
                //close door
                else if (Dungeon.level.map[cell] == Terrain.OPEN_DOOR){
                    Level.set(cell, Terrain.DOOR);
                    GameScene.updateMap(cell);
                }

                //only transform cells directly near caster
                if (!Dungeon.level.adjacent(bolt.sourcePos, cell)) {

                    // damm webs!
                    Web web = (Web) Dungeon.level.blobs.get(Web.class);
                    if (web != null && web.volume > 0) {
                        web.clear(cell);
                    }

                    // grass and water
                    int t = Dungeon.level.map[cell];
                    if (t == Terrain.WATER) {
                        Level.set(cell, Terrain.EMPTY);
                        GameScene.updateMap(cell);
                        CellEmitter.get(cell).burst(Speck.factory(Speck.STEAM), 10);
                    } else if (t == Terrain.EMBERS) {
                        GameScene.add(Blob.seed(cell, 1+chargesPerCast(), Fire.class));
                    } else if (t == Terrain.EMPTY || t == Terrain.EMPTY_DECO) {
                        Level.set(cell, Terrain.GRASS);
                        GameScene.updateMap(cell);
                    } else if (t == Terrain.GRASS) {
                        Level.set(cell, Terrain.FURROWED_GRASS);
                        GameScene.updateMap(cell);
                    } else if (t == Terrain.HIGH_GRASS || t == Terrain.FURROWED_GRASS) {
                        Level.set(cell, Terrain.WATER);
                        GameScene.updateMap(cell);
                    }
                }

                //only ignite cells directly near caster if they are flammable
                /*
                if (Dungeon.level.adjacent(bolt.sourcePos, cell) && !Dungeon.level.flamable[cell]){
                    adjacentCells.add(cell);
                } else {
                    GameScene.add( Blob.seed( cell, 1+chargesPerCast(), Fire.class ) );
                }

                 */

                Char ch = Actor.findChar( cell );
                if (ch != null) {
                    affectedChars.add(ch);
                }
            }

            //ignite cells that share a side with an adjacent cell, are flammable, and are further from the source pos
            //This prevents short-range casts not igniting barricades or bookshelves
            /*
            for (int cell : adjacentCells){
                for (int i : PathFinder.NEIGHBOURS4){
                    if (Dungeon.level.trueDistance(cell+i, bolt.sourcePos) > Dungeon.level.trueDistance(cell, bolt.sourcePos)
                            && Dungeon.level.flamable[cell+i]
                            && com.tianscar.carbonizedpixeldungeon.actors.blobs.Fire.volumeAt(cell+i, Fire.class) == 0){
                        GameScene.add( Blob.seed( cell+i, 1+chargesPerCast(), Fire.class ) );
                    }
                }
            }

             */

            for ( Char ch : affectedChars ){
                ch.damage(damageRoll(heart), this);
                if (ch.isAlive()) {
                    Buff.affect(ch, Burning.class).reignite(ch);
                    switch (chargesPerCast()) {
                        case 1:
                            break; //no effects
                        case 2:
                            Buff.affect(ch, Cripple.class, 4f);
                            break;
                        case 3:
                            Buff.affect(ch, Paralysis.class, 4f);
                            break;
                    }
                }
                else if (Dungeon.hero.hasTalent(Talent.WILDFIRE) &&
                        Random.Int(3) < 1+Dungeon.hero.pointsInTalent(Talent.WILDFIRE)) {
                    Buff.prolong(curUser, ElementalHeart.FireFocus.class, 3).fx();
                }
            }
        }

        @Override
        protected int collisionProperties(int target) {
            return Ballistica.WONT_STOP;
        }

        protected int chargesPerCast() {
            return curUser.pointsInTalent(Talent.CHAOS_IMPACT);
        }

        //1x/2x/3x damage
        @Override
        public int min(int lvl){
            return (1+lvl) * chargesPerCast();
        }

        //1x/2x/3x damage
        @Override
        public int max(int lvl){
            return (6+2*lvl) * chargesPerCast();
        }

    }
}
