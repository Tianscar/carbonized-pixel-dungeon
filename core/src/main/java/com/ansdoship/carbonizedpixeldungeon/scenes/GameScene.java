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

package com.ansdoship.carbonizedpixeldungeon.scenes;

import com.ansdoship.carbonizedpixeldungeon.*;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.Blob;
import com.ansdoship.carbonizedpixeldungeon.actors.buffs.ChampionEnemy;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.DemonSpawner;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Mob;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.Snake;
import com.ansdoship.carbonizedpixeldungeon.effects.BannerSprites;
import com.ansdoship.carbonizedpixeldungeon.effects.BlobEmitter;
import com.ansdoship.carbonizedpixeldungeon.effects.CircleArc;
import com.ansdoship.carbonizedpixeldungeon.effects.EmoIcon;
import com.ansdoship.carbonizedpixeldungeon.effects.Flare;
import com.ansdoship.carbonizedpixeldungeon.effects.FloatingText;
import com.ansdoship.carbonizedpixeldungeon.effects.Ripple;
import com.ansdoship.carbonizedpixeldungeon.effects.SpellSprite;
import com.ansdoship.carbonizedpixeldungeon.items.Ankh;
import com.ansdoship.carbonizedpixeldungeon.items.Heap;
import com.ansdoship.carbonizedpixeldungeon.items.Honeypot;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.DriedRose;
import com.ansdoship.carbonizedpixeldungeon.items.journal.Guidebook;
import com.ansdoship.carbonizedpixeldungeon.items.potions.Potion;
import com.ansdoship.carbonizedpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.ansdoship.carbonizedpixeldungeon.journal.Document;
import com.ansdoship.carbonizedpixeldungeon.journal.Journal;
import com.ansdoship.carbonizedpixeldungeon.levels.Level;
import com.ansdoship.carbonizedpixeldungeon.levels.RegularLevel;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.Room;
import com.ansdoship.carbonizedpixeldungeon.levels.rooms.secret.SecretRoom;
import com.ansdoship.carbonizedpixeldungeon.levels.traps.Trap;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.plants.Plant;
import com.ansdoship.carbonizedpixeldungeon.sprites.CharSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.DiscardedItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.HeroSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.tiles.CustomTilemap;
import com.ansdoship.carbonizedpixeldungeon.tiles.DungeonTerrainTilemap;
import com.ansdoship.carbonizedpixeldungeon.tiles.DungeonTileSheet;
import com.ansdoship.carbonizedpixeldungeon.tiles.DungeonTilemap;
import com.ansdoship.carbonizedpixeldungeon.tiles.DungeonWallsTilemap;
import com.ansdoship.carbonizedpixeldungeon.tiles.FogOfWar;
import com.ansdoship.carbonizedpixeldungeon.tiles.GridTileMap;
import com.ansdoship.carbonizedpixeldungeon.tiles.RaisedTerrainTilemap;
import com.ansdoship.carbonizedpixeldungeon.tiles.TerrainFeaturesTilemap;
import com.ansdoship.carbonizedpixeldungeon.tiles.WallBlockingTilemap;
import com.ansdoship.carbonizedpixeldungeon.ui.*;
import com.ansdoship.carbonizedpixeldungeon.utils.GLog;
import com.ansdoship.carbonizedpixeldungeon.windows.*;
import com.ansdoship.pixeldungeonclasses.glwrap.Blending;
import com.ansdoship.pixeldungeonclasses.input.PointerEvent;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.Gizmo;
import com.ansdoship.pixeldungeonclasses.noosa.Group;
import com.ansdoship.pixeldungeonclasses.noosa.NoosaScript;
import com.ansdoship.pixeldungeonclasses.noosa.NoosaScriptNoLighting;
import com.ansdoship.pixeldungeonclasses.noosa.SkinnedBlock;
import com.ansdoship.pixeldungeonclasses.noosa.Visual;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.noosa.particles.Emitter;
import com.ansdoship.pixeldungeonclasses.utils.GameMath;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class GameScene extends PixelScene {

	static GameScene scene;

	private SkinnedBlock water;
	private DungeonTerrainTilemap tiles;
	private GridTileMap visualGrid;
	private TerrainFeaturesTilemap terrainFeatures;
	private RaisedTerrainTilemap raisedTerrain;
	private DungeonWallsTilemap walls;
	private WallBlockingTilemap wallBlocking;
	private FogOfWar fog;
	private HeroSprite hero;

	private StatusPane pane;
	
	private GameLog log;
	
	private BusyIndicator busy;
	private CircleArc counter;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group customTiles;
	private Group levelVisuals;
	private Group customWalls;
	private Group ripples;
	private Group plants;
	private Group traps;
	private Group heaps;
	private Group mobs;
	private Group floorEmitters;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;
	private Group overFogEffects;
	private Group healthIndicators;
	
	private Toolbar toolbar;
	private Toast prompt;

	private AttackIndicator attack;
	private LootIndicator loot;
	private ActionIndicator action;
	private ResumeIndicator resume;

	{
		inGameScene = true;
	}
	
	@Override
	public void create() {
		
		if (Dungeon.hero == null || Dungeon.level == null){
			CarbonizedPixelDungeon.switchNoFade(TitleScene.class);
			return;
		}

		Dungeon.level.playLevelMusic();

		PDSettings.lastClass(Dungeon.hero.heroClass.ordinal());
		
		super.create();
		Camera.main.zoom( GameMath.gate(minZoom, defaultZoom + PDSettings.zoom(), maxZoom));

		scene = this;

		terrain = new Group();
		add( terrain );

		water = new SkinnedBlock(
			Dungeon.level.width() * DungeonTilemap.SIZE,
			Dungeon.level.height() * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() ){

			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				//water has no alpha component, this improves performance
				Blending.disable();
				super.draw();
				Blending.enable();
			}
		};
		water.autoAdjust = true;
		terrain.add( water );

		ripples = new Group();
		terrain.add( ripples );

		DungeonTileSheet.setupVariance(Dungeon.level.map.length, Dungeon.seedCurDepth());
		
		tiles = new DungeonTerrainTilemap();
		terrain.add( tiles );

		customTiles = new Group();
		terrain.add(customTiles);

		for( CustomTilemap visual : Dungeon.level.customTiles){
			addCustomTile(visual);
		}

		visualGrid = new GridTileMap();
		terrain.add( visualGrid );

		terrainFeatures = new TerrainFeaturesTilemap(Dungeon.level.plants, Dungeon.level.traps);
		terrain.add(terrainFeatures);
		
		levelVisuals = Dungeon.level.addVisuals();
		add(levelVisuals);

		floorEmitters = new Group();
		add(floorEmitters);

		heaps = new Group();
		add( heaps );
		
		for ( Heap heap : Dungeon.level.heaps.valueList() ) {
			addHeapSprite( heap );
		}

		emitters = new Group();
		effects = new Group();
		healthIndicators = new Group();
		emoicons = new Group();
		overFogEffects = new Group();
		
		mobs = new Group();
		add( mobs );

		hero = new HeroSprite();
		hero.place( Dungeon.hero.pos );
		hero.updateArmor();
		mobs.add( hero );
		
		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite( mob );
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}
		
		raisedTerrain = new RaisedTerrainTilemap();
		add( raisedTerrain );

		walls = new DungeonWallsTilemap();
		add(walls);

		customWalls = new Group();
		add(customWalls);

		for( CustomTilemap visual : Dungeon.level.customWalls){
			addCustomWall(visual);
		}

		wallBlocking = new WallBlockingTilemap();
		add (wallBlocking);

		add( emitters );
		add( effects );

		gases = new Group();
		add( gases );

		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}


		fog = new FogOfWar( Dungeon.level.width(), Dungeon.level.height() );
		add( fog );

		spells = new Group();
		add( spells );

		add(overFogEffects);
		
		statuses = new Group();
		add( statuses );
		
		add( healthIndicators );
		//always appears ontop of other health indicators
		add( new TargetHealthIndicator() );
		
		add( emoicons );
		
		add( cellSelector = new CellSelector( tiles ) );

		pane = new StatusPane();
		pane.camera = uiCamera;
		pane.setSize( uiCamera.width, 0 );
		add( pane );
		
		attack = new AttackIndicator();
		attack.camera = uiCamera;
		add( attack );

		loot = new LootIndicator();
		loot.camera = uiCamera;
		add( loot );

		action = new ActionIndicator();
		action.camera = uiCamera;
		add( action );

		resume = new ResumeIndicator();
		resume.camera = uiCamera;
		add( resume );

		log = new GameLog();
		log.camera = uiCamera;
		log.newLine();
		add( log );

		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect( 0,uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height() );
		add( toolbar );

		layoutTags();

		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = pane.bottom() + 1;
		add( busy );
		
		counter = new CircleArc(18, 4.25f);
		counter.color( 0x808080, true );
		counter.camera = uiCamera;
		counter.show(this, busy.center(), 0f);
		
		switch (InterlevelScene.mode) {
			case RESURRECT:
				Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
				ScrollOfTeleportation.appear( Dungeon.hero, Dungeon.hero.pos );
				SpellSprite.show(Dungeon.hero, SpellSprite.ANKH);
				new Flare( 5, 16 ).color( 0xFFFF00, true ).show( hero, 4f ) ;
				break;
			case RETURN:
				ScrollOfTeleportation.appear(  Dungeon.hero, Dungeon.hero.pos );
				break;
			case DESCEND:
			case FALL:
				switch (Dungeon.depth) {
				case 1:
					WndStory.showChapter( WndStory.ID_SEWERS );
					break;
				case 6:
					WndStory.showChapter( WndStory.ID_PRISON );
					break;
				case 11:
					WndStory.showChapter( WndStory.ID_CAVES );
					break;
				case 16:
					WndStory.showChapter( WndStory.ID_CITY );
					break;
				case 21:
					WndStory.showChapter( WndStory.ID_HALLS );
					break;
				}
				if (Dungeon.hero.isAlive()) {
					Badges.validateNoKilling();
				}
				break;
		}

		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell( null );
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
				} else if (item instanceof Plant.Seed && !Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
					Dungeon.level.plant( (Plant.Seed)item, pos );
				} else if (item instanceof Honeypot) {
					Dungeon.level.drop(((Honeypot) item).shatter(null, pos), pos);
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}
		
		ArrayList<Item> ported = Dungeon.portedItems.get( Dungeon.depth );
		if (ported != null){
			//TODO currently items are only ported to boss rooms, so this works well
			//might want to have a 'near entrance' function if items can be ported elsewhere
			int pos;
			//try to find a tile with no heap, otherwise just stick items onto a heap.
			int tries = 100;
			do {
				pos = Dungeon.level.randomRespawnCell( null );
				tries--;
			} while (tries > 0 && Dungeon.level.heaps.get(pos) != null);
			for (Item item : ported) {
				Dungeon.level.drop( item, pos ).type = Heap.Type.CHEST;
			}
			Dungeon.level.heaps.get(pos).type = Heap.Type.CHEST;
			Dungeon.level.heaps.get(pos).sprite.link(); //sprite reset to show chest
			Dungeon.portedItems.remove( Dungeon.depth );
		}

		Dungeon.hero.next();

		switch (InterlevelScene.mode){
			case FALL: case DESCEND: case CONTINUE:
				Camera.main.snapTo(hero.center().x, hero.center().y - DungeonTilemap.SIZE * (defaultZoom/Camera.main.zoom));
				break;
			case ASCEND:
				Camera.main.snapTo(hero.center().x, hero.center().y + DungeonTilemap.SIZE * (defaultZoom/Camera.main.zoom));
				break;
			default:
				Camera.main.snapTo(hero.center().x, hero.center().y);
		}
		Camera.main.panTo(hero.center(), 2.5f);

		if (InterlevelScene.mode != InterlevelScene.Mode.NONE) {
			if (Dungeon.depth == Statistics.deepestFloor
					&& (InterlevelScene.mode == InterlevelScene.Mode.DESCEND || InterlevelScene.mode == InterlevelScene.Mode.FALL)) {
				GLog.h(Messages.get(this, "descend"), Dungeon.depth);
				Sample.INSTANCE.play(Assets.Sounds.DESCEND);
				
				for (Char ch : Actor.chars()){
					if (ch instanceof DriedRose.GhostHero){
						((DriedRose.GhostHero) ch).sayAppeared();
					}
				}

				int spawnersAbove = Statistics.spawnersAlive;
				if (spawnersAbove > 0 && Dungeon.depth <= 25) {
					for (Mob m : Dungeon.level.mobs) {
						if (m instanceof DemonSpawner && ((DemonSpawner) m).spawnRecorded) {
							spawnersAbove--;
						}
					}

					if (spawnersAbove > 0) {
						if (Dungeon.bossLevel()) {
							GLog.n(Messages.get(this, "spawner_warn_final"));
						} else {
							GLog.n(Messages.get(this, "spawner_warn"));
						}
					}
				}
				
			} else if (InterlevelScene.mode == InterlevelScene.Mode.RESET) {
				GLog.h(Messages.get(this, "warp"));
			} else if (InterlevelScene.mode == InterlevelScene.Mode.RESURRECT) {
				GLog.h(Messages.get(this, "resurrect"), Dungeon.depth);
			} else {
				GLog.h(Messages.get(this, "return"), Dungeon.depth);
			}

			if (Dungeon.hero.hasTalent(Talent.ROGUES_FORESIGHT)
					&& Dungeon.level instanceof RegularLevel){
				int reqSecrets = Dungeon.level.feeling == Level.Feeling.SECRETS ? 2 : 1;
				for (Room r : ((RegularLevel) Dungeon.level).rooms()){
					if (r instanceof SecretRoom) reqSecrets--;
				}

				//50%/75% chance, use level's seed so that we get the same result for the same level
				Random.pushGenerator(Dungeon.seedCurDepth());
					if (reqSecrets <= 0 && Random.Int(4) <= Dungeon.hero.pointsInTalent(Talent.ROGUES_FORESIGHT)){
						GLog.p(Messages.get(this, "secret_hint"));
					}
				Random.popGenerator();
			}

			boolean unspentTalents = false;
			for (int i = 1; i <= Dungeon.hero.talents.size(); i++){
				if (Dungeon.hero.talentPointsAvailable(i) > 0){
					unspentTalents = true;
					break;
				}
			}
			if (unspentTalents){
				GLog.newLine();
				GLog.w( Messages.get(Dungeon.hero, "unspent") );
				StatusPane.talentBlink = 10f;
				WndHero.lastIdx = 1;
			}

			switch (Dungeon.level.feeling) {
				case CHASM:     GLog.w(Messages.get(this, "chasm"));    break;
				case WATER:     GLog.w(Messages.get(this, "water"));    break;
				case GRASS:     GLog.w(Messages.get(this, "grass"));    break;
				case DARK:      GLog.w(Messages.get(this, "dark"));     break;
				case LARGE:     GLog.w(Messages.get(this, "large"));    break;
				case TRAPS:     GLog.w(Messages.get(this, "traps"));    break;
				case SECRETS:   GLog.w(Messages.get(this, "secrets"));  break;
			}

			for (Mob mob : Dungeon.level.mobs) {
				if (!mob.buffs(ChampionEnemy.class).isEmpty()) {
					GLog.w(Messages.get(ChampionEnemy.class, "warn"));
				}
			}

			InterlevelScene.mode = InterlevelScene.Mode.NONE;

			
		}

		if (Rankings.INSTANCE.totalNumber > 0 && !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_DIEING)){
			GLog.p(Messages.get(Guidebook.class, "hint"));
			GameScene.flashForDocument(Document.GUIDE_DIEING);
		}
		
		fadeIn();

		//re-show WndResurrect if needed
		if (!Dungeon.hero.isAlive()){
			//check if hero has an unblessed ankh
			boolean hasAnkh = false;
			for (Ankh i : Dungeon.hero.belongings.getAllItems(Ankh.class)){
				if (!i.isBlessed()){
					hasAnkh = true;
				}
			}
			if (hasAnkh) {
				add(new WndResurrect());
			}
			else gameOver();
		}

	}
	
	public void destroy() {
		
		//tell the actor thread to finish, then wait for it to complete any actions it may be doing.
		if (!waitForActorThread( 4500 )){
			Throwable t = new Throwable();
			t.setStackTrace(actorThread.getStackTrace());
			throw new RuntimeException("timeout waiting for actor thread! ", t);
		}

		Emitter.freezeEmitters = false;
		
		scene = null;
		Badges.saveGlobal();
		Journal.saveGlobal();
		
		super.destroy();
	}
	
	public static void endActorThread(){
		if (actorThread != null && actorThread.isAlive()){
			Actor.keepActorThreadAlive = false;
			actorThread.interrupt();
		}
	}

	public boolean waitForActorThread(int msToWait ){
		if (actorThread == null || !actorThread.isAlive()) {
			return true;
		}
		synchronized (actorThread) {
			actorThread.interrupt();
			try {
				actorThread.wait(msToWait);
			} catch (InterruptedException e) {
				CarbonizedPixelDungeon.reportException(e);
			}
			return !Actor.processing();
		}
	}
	
	@Override
	public synchronized void onPause() {
		try {
			waitForActorThread(500);
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			CarbonizedPixelDungeon.reportException(e);
		}
	}

	private static Thread actorThread;
	
	//sometimes UI changes can be prompted by the actor thread.
	// We queue any removed element destruction, rather than destroying them in the actor thread.
	private ArrayList<Gizmo> toDestroy = new ArrayList<>();

	//the actor thread processes at a maximum of 60 times a second
	//this caps the speed of resting for higher refresh rate displays
	private float notifyDelay = 1/60f;
	
	@Override
	public synchronized void update() {
		if (Dungeon.hero == null || scene == null) {
			return;
		}

		super.update();

		if (notifyDelay > 0) notifyDelay -= Game.elapsed;

		if (!Emitter.freezeEmitters) water.offset( 0, -5 * Game.elapsed );

		if (!Actor.processing() && Dungeon.hero.isAlive()) {
			if (actorThread == null || !actorThread.isAlive()) {
				
				actorThread = new Thread() {
					@Override
					public void run() {
						Actor.process();
					}
				};
				
				//if cpu cores are limited, game should prefer drawing the current frame
				if (Runtime.getRuntime().availableProcessors() == 1) {
					actorThread.setPriority(Thread.NORM_PRIORITY - 1);
				}
				actorThread.setName("CBPD Actor Thread");
				Thread.currentThread().setName("CBPD Render Thread");
				Actor.keepActorThreadAlive = true;
				actorThread.start();
			} else if (notifyDelay <= 0f) {
				notifyDelay += 1/60f;
				synchronized (actorThread) {
					actorThread.notify();
				}
			}
		}

		counter.setSweep((1f - Actor.now()%1f)%1f);
		
		if (Dungeon.hero.ready && Dungeon.hero.paralysed == 0) {
			log.newLine();
		}

		if (tagAttack != attack.active ||
				tagLoot != loot.visible ||
				tagAction != action.visible ||
				tagResume != resume.visible) {

			//we only want to change the layout when new tags pop in, not when existing ones leave.
			boolean tagAppearing = (attack.active && !tagAttack) ||
									(loot.visible && !tagLoot) ||
									(action.visible && !tagAction) ||
									(resume.visible && !tagResume);

			tagAttack = attack.active;
			tagLoot = loot.visible;
			tagAction = action.visible;
			tagResume = resume.visible;

			if (tagAppearing) layoutTags();
		}

		cellSelector.enable(Dungeon.hero.ready);
		
		for (Gizmo g : toDestroy){
			g.destroy();
		}
		toDestroy.clear();
	}

	private boolean tagAttack    = false;
	private boolean tagLoot      = false;
	private boolean tagAction    = false;
	private boolean tagResume    = false;

	public static void layoutTags() {

		if (scene == null) return;

		float tagLeft = PDSettings.flipTags() ? 0 : uiCamera.width - scene.attack.width();

		if (PDSettings.flipTags()) {
			scene.log.setRect(scene.attack.width(), scene.toolbar.top()-2, uiCamera.width - scene.attack.width(), 0);
		} else {
			scene.log.setRect(0, scene.toolbar.top()-2, uiCamera.width - scene.attack.width(),  0 );
		}

		float pos = scene.toolbar.top();

		//FIXME adjusting this to position even without visibility resulted in deadlocks
		if (scene.tagAttack){
			scene.attack.setPos( tagLeft, pos - scene.attack.height());
			scene.attack.flip(tagLeft == 0);
			pos = scene.attack.top();
		}

		if (scene.tagLoot) {
			scene.loot.setPos( tagLeft, pos - scene.loot.height() );
			scene.loot.flip(tagLeft == 0);
			pos = scene.loot.top();
		}

		if (scene.tagAction) {
			scene.action.setPos( tagLeft, pos - scene.action.height() );
			scene.action.flip(tagLeft == 0);
			pos = scene.action.top();
		}

		if (scene.tagResume) {
			scene.resume.setPos(tagLeft, pos - scene.resume.height());
			scene.resume.flip(tagLeft == 0);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (!cancel()) {
			add( new WndGame() );
		}
	}

	public void addCustomTile( CustomTilemap visual){
		customTiles.add( visual.create() );
	}

	public void addCustomWall( CustomTilemap visual){
		customWalls.add( visual.create() );
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add( heap.sprite );
	}
	
	private void addPlantSprite( Plant plant ) {

	}

	private void addTrapSprite( Trap trap ) {

	}
	
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}
	
	private void addMobSprite( Mob mob ) {
		CharSprite sprite = mob.sprite();
		sprite.visible = Dungeon.level.heroFOV[mob.pos];
		mobs.add( sprite );
		sprite.link( mob );
	}
	
	private synchronized void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			toDestroy.add(prompt);
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );
			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		addToFront( banner );
	}
	
	// -------------------------------------------------------

	public static void add( Plant plant ) {
		if (scene != null) {
			scene.addPlantSprite( plant );
		}
	}

	public static void add( Trap trap ) {
		if (scene != null) {
			scene.addTrapSprite( trap );
		}
	}
	
	public static void add( Blob gas ) {
		Actor.add( gas );
		if (scene != null) {
			scene.addBlobSprite( gas );
		}
	}
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite( heap );
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite( heap );
		}
	}
	
	public static void add( Mob mob ) {
		Dungeon.level.mobs.add( mob );
		scene.addMobSprite( mob );
		Actor.add( mob );
	}

	public static void addSprite( Mob mob ) {
		scene.addMobSprite( mob );
	}
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		scene.addMobSprite( mob );
		Actor.addDelayed( mob, delay );
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add( icon );
	}
	
	public static void add( CharHealthIndicator indicator ){
		if (scene != null) scene.healthIndicators.add(indicator);
	}
	
	public static void add( CustomTilemap t, boolean wall ){
		if (scene == null) return;
		if (wall){
			scene.addCustomWall(t);
		} else {
			scene.addCustomTile(t);
		}
	}
	
	public static void effect( Visual effect ) {
		if (scene != null) scene.effects.add( effect );
	}

	public static void effectOverFog( Visual effect ) {
		scene.overFogEffects.add( effect );
	}
	
	public static Ripple ripple( int pos ) {
		if (scene != null) {
			Ripple ripple = (Ripple) scene.ripples.recycle(Ripple.class);
			ripple.reset(pos);
			return ripple;
		} else {
			return null;
		}
	}
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}

	public static Emitter floorEmitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.floorEmitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item, int pos ) {
		if (scene != null) scene.toolbar.pickup( item, pos );
	}

	public static void pickUpJournal( Item item, int pos ) {
		if (scene != null) scene.pane.pickup( item, pos );
	}

	//TODO currently only works with guidebooks
	public static void flashForDocument( String page ){
		if (scene != null) scene.pane.flashForPage( page );
	}
	
	public static void updateKeyDisplay(){
		if (scene != null) scene.pane.updateKeys();
	}

	public static void showlevelUpStars(){
		if (scene != null) scene.pane.showStarParticles();
	}

	public static void resetMap() {
		if (scene != null) {
			scene.tiles.map(Dungeon.level.map, Dungeon.level.width() );
			scene.visualGrid.map(Dungeon.level.map, Dungeon.level.width() );
			scene.terrainFeatures.map(Dungeon.level.map, Dungeon.level.width() );
			scene.raisedTerrain.map(Dungeon.level.map, Dungeon.level.width() );
			scene.walls.map(Dungeon.level.map, Dungeon.level.width() );
		}
		updateFog();
	}

	//updates the whole map
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updateMap();
			scene.visualGrid.updateMap();
			scene.terrainFeatures.updateMap();
			scene.raisedTerrain.updateMap();
			scene.walls.updateMap();
			updateFog();
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updateMapCell( cell );
			scene.visualGrid.updateMapCell( cell );
			scene.terrainFeatures.updateMapCell( cell );
			scene.raisedTerrain.updateMapCell( cell );
			scene.walls.updateMapCell( cell );
			//update adjacent cells too
			updateFog( cell, 1 );
		}
	}

	public static void plantSeed( int cell ) {
		if (scene != null) {
			scene.terrainFeatures.growPlant( cell );
		}
	}
	
	//todo this doesn't account for walls right now
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover( pos, oldValue );
		}
	}
	
	public static void show( Window wnd ) {
		if (scene != null) {
			cancelCellSelector();
			scene.addToFront(wnd);
		}
	}

	public static boolean showingWindow(){
		if (scene == null) return false;

		for (Gizmo g : scene.members){
			if (g instanceof Window) return true;
		}

		return false;
	}

	public static boolean interfaceBlockingHero(){
		if (scene == null) return false;

		if (showingWindow()) return true;

		return false;
	}

	public static void updateFog(){
		if (scene != null) {
			scene.fog.updateFog();
			scene.wallBlocking.updateMap();
		}
	}

	public static void updateFog(int x, int y, int w, int h){
		if (scene != null) {
			scene.fog.updateFogArea(x, y, w, h);
			scene.wallBlocking.updateArea(x, y, w, h);
		}
	}
	
	public static void updateFog( int cell, int radius ){
		if (scene != null) {
			scene.fog.updateFog( cell, radius );
			scene.wallBlocking.updateArea( cell, radius );
		}
	}
	
	public static void afterObserve() {
		if (scene != null) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.sprite != null)
					mob.sprite.visible = Dungeon.level.heroFOV[mob.pos];
			}
		}
	}

	public static void flash( int color ) {
		flash( color, true);
	}

	public static void flash( int color, boolean lightmode ) {
		//greater than 0 to account for negative values (which have the first bit set to 1)
		if (color > 0 && color < 0x01000000) {
			scene.fadeIn(0xFF000000 | color, lightmode);
		} else {
			scene.fadeIn(color, lightmode);
		}
	}

	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );

		StyledButton restart = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(StartScene.class, "new"), 9){
			@Override
			protected void onClick() {
				InterlevelScene.noStory = true;
				GamesInProgress.selectedClass = Dungeon.hero.heroClass;
				GamesInProgress.curSlot = GamesInProgress.firstEmpty();
				CarbonizedPixelDungeon.switchScene(HeroSelectScene.class);
			}

			@Override
			public void update() {
				alpha(gameOver.am);
				super.update();
			}
		};
		restart.icon(Icons.get(Icons.ENTER));
		restart.alpha(0);
		restart.camera = uiCamera;
		float offset = Camera.main.centerOffset.y;
		restart.setSize(Math.max(80, restart.reqWidth()), 20);
		restart.setPos(
				align(uiCamera, (restart.camera.width - restart.width()) / 2),
				align(uiCamera, (restart.camera.height - restart.height()) / 2 + restart.height()/2 + 16 - offset)
		);
		scene.add(restart);

		StyledButton menu = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(WndKeyBindings.class, "menu"), 9){
			@Override
			protected void onClick() {
				GameScene.show(new WndGame());
			}

			@Override
			public void update() {
				alpha(gameOver.am);
				super.update();
			}
		};
		menu.icon(Icons.get(Icons.PREFS));
		menu.alpha(0);
		menu.camera = uiCamera;
		menu.setSize(Math.max(80, menu.reqWidth()), 20);
		menu.setPos(
				align(uiCamera, (menu.camera.width - menu.width()) / 2),
				restart.bottom() + 2
		);
		scene.add(menu);
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.Sounds.BOSS );
		}
	}
	
	public static void handleCell( int cell ) {
		cellSelector.select( cell, PointerEvent.LEFT );
	}
	
	public static void selectCell( CellSelector.Listener listener ) {
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener){
			cellSelector.listener.onSelect(null);
		}
		cellSelector.listener = listener;
		if (scene != null)
			scene.prompt( listener.prompt() );
	}
	
	private static boolean cancelCellSelector() {
		cellSelector.resetKeyHold();
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}
	
	public static WndBag selectItem( WndBag.ItemSelector listener ) {
		cancelCellSelector();

		WndBag wnd = WndBag.getBag( listener );

		if (scene != null) scene.addToFront( wnd );
		
		return wnd;
	}
	
	public static boolean cancel() {
		if (Dungeon.hero != null && (Dungeon.hero.curAction != null || Dungeon.hero.resting)) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.resting = false;
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlotButton.cancel();
		if (scene != null && scene.toolbar != null) scene.toolbar.examining = false;
	}
	
	public static void checkKeyHold(){
		cellSelector.processKeyHold();
	}
	
	public static void resetKeyHold(){
		cellSelector.resetKeyHold();
	}

	public static void examineCell( Integer cell ) {
		if (cell == null
				|| cell < 0
				|| cell > Dungeon.level.length()
				|| (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
			return;
		}

		ArrayList<String> names = new ArrayList<>();
		final ArrayList<Object> objects = new ArrayList<>();

		if (cell == Dungeon.hero.pos) {
			objects.add(Dungeon.hero);
			names.add(Dungeon.hero.className().toUpperCase(Locale.ENGLISH));
		} else {
			if (Dungeon.level.heroFOV[cell]) {
				Mob mob = (Mob) Actor.findChar(cell);
				if (mob != null) {
					objects.add(mob);
					names.add(Messages.titleCase( mob.name() ));
				}
			}
		}

		Heap heap = Dungeon.level.heaps.get(cell);
		if (heap != null && heap.seen) {
			objects.add(heap);
			names.add(Messages.titleCase( heap.toString() ));
		}

		Plant plant = Dungeon.level.plants.get( cell );
		if (plant != null) {
			objects.add(plant);
			names.add(Messages.titleCase( plant.plantName ));
		}

		Trap trap = Dungeon.level.traps.get( cell );
		if (trap != null && trap.visible) {
			objects.add(trap);
			names.add(Messages.titleCase( trap.name() ));
		}

		if (objects.isEmpty()) {
			GameScene.show(new WndInfoCell(cell));
		} else if (objects.size() == 1){
			examineObject(objects.get(0));
		} else {
			GameScene.show(new WndOptions(Icons.get(Icons.INFO),
					Messages.get(GameScene.class, "choose_examine"),
					Messages.get(GameScene.class, "multiple_examine"),
					names.toArray(new String[names.size()])){
				@Override
				protected void onSelect(int index) {
					examineObject(objects.get(index));
				}
			});

		}
	}

	public static void examineObject(Object o){
		if (o == Dungeon.hero){
			GameScene.show( new WndHero() );
		} else if ( o instanceof Mob ){
			GameScene.show(new WndInfoMob((Mob) o));
			if (o instanceof Snake && !Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_SURPRISE_ATKS)){
				GLog.p(Messages.get(Guidebook.class, "hint"));
				GameScene.flashForDocument(Document.GUIDE_SURPRISE_ATKS);
			}
		} else if ( o instanceof Heap ){
			GameScene.show(new WndInfoItem((Heap)o));
		} else if ( o instanceof Plant ){
			GameScene.show( new WndInfoPlant((Plant) o) );
		} else if ( o instanceof Trap ){
			GameScene.show( new WndInfoTrap((Trap) o));
		} else {
			GameScene.show( new WndMessage( Messages.get(GameScene.class, "dont_know") ) ) ;
		}
	}

	
	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (Dungeon.hero.handle( cell )) {
				Dungeon.hero.next();
			}
		}
		@Override
		public String prompt() {
			return null;
		}
	};
}
