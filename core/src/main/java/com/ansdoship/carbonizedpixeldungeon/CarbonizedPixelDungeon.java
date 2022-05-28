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

package com.ansdoship.carbonizedpixeldungeon;

import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.PixelScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.TitleScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.WelcomeScene;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Music;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.PlatformSupport;

public class CarbonizedPixelDungeon extends Game {

	public static final int v0_0_6   = 16;
	public static final int v0_0_5   = 12;
	public static final int v0_0_4   = 9;
	
	public CarbonizedPixelDungeon(PlatformSupport platform ) {
		super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );

		//CBPD v0.0.6
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs.BigRat.class,
				"com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs.RatKing.class" );

		//CBPD v0.0.4
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.items.weapon.melee.Pickaxe.class,
				"com.ansdoship.carbonizedpixeldungeon.items.quest.Pickaxe" );

		//SHPD v1.0.0
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.items.stones.StoneOfFear.class,
				"com.ansdoship.carbonizedpixeldungeon.items.stones.StoneOfAffection" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.items.stones.StoneOfDeepSleep.class,
				"com.ansdoship.carbonizedpixeldungeon.items.stones.StoneOfDeepenedSleep" );

		//SHPD v0.9.3
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.actors.mobs.Tengu.class,
				"com.ansdoship.carbonizedpixeldungeon.actors.mobs.NewTengu" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.PrisonBossLevel.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewPrisonBossLevel" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.PrisonBossLevel.ExitVisual.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewPrisonBossLevel$exitVisual" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.PrisonBossLevel.ExitVisualWalls.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewPrisonBossLevel$exitVisualWalls" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.actors.mobs.DM300.class,
				"com.ansdoship.carbonizedpixeldungeon.actors.mobs.NewDM300" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.CavesBossLevel.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewCavesBossLevel" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.CavesBossLevel.PylonEnergy.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewCavesBossLevel$PylonEnergy" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.CavesBossLevel.ArenaVisuals.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewCavesBossLevel$ArenaVisuals" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.CavesBossLevel.CityEntrance.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewCavesBossLevel$CityEntrance" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.CavesBossLevel.EntranceOverhang.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewCavesBossLevel$EntranceOverhang" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.CityBossLevel.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewCityBossLevel" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.CityBossLevel.CustomGroundVisuals.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewCityBossLevel$CustomGroundVisuals" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.CityBossLevel.CustomWallVisuals.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewCityBossLevel$CustomWallVisuals" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.HallsBossLevel.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewHallsBossLevel" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.HallsBossLevel.CenterPieceVisuals.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewHallsBossLevel$CenterPieceWalls" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.levels.HallsBossLevel.CenterPieceWalls.class,
				"com.ansdoship.carbonizedpixeldungeon.levels.NewHallsBossLevel$CenterPieceWalls" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.items.Waterskin.class,
				"com.ansdoship.carbonizedpixeldungeon.items.DewVial" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.items.TengusMask.class,
				"com.ansdoship.carbonizedpixeldungeon.items.TomeOfMastery" );
		Bundle.addAlias(
				com.ansdoship.carbonizedpixeldungeon.items.KingsCrown.class,
				"com.ansdoship.carbonizedpixeldungeon.items.ArmorKit" );
		
	}
	
	@Override
	public void create() {
		super.create();

		updateSystemUI();
		PDAction.loadBindings();
		
		Music.INSTANCE.enable( PDSettings.music() );
		Music.INSTANCE.volume( PDSettings.musicVol()* PDSettings.musicVol()/100f );
		Sample.INSTANCE.enable( PDSettings.soundFx() );
		Sample.INSTANCE.volume( PDSettings.SFXVol()* PDSettings.SFXVol()/100f );

		Sample.INSTANCE.load( Assets.Sounds.all );
		
	}

	@Override
	public void finish() {
		if (!Game.platform.isiOS()) {
			super.finish();
		} else {
			//can't exit on iOS (Apple guidelines), so just go to title screen
			switchScene(TitleScene.class);
		}
	}

	public static void switchNoFade(Class<? extends PixelScene> c){
		switchNoFade(c, null);
	}

	public static void switchNoFade(Class<? extends PixelScene> c, SceneChangeCallback callback) {
		PixelScene.noFade = true;
		switchScene( c, callback );
	}
	
	public static void seamlessResetScene(SceneChangeCallback callback) {
		if (scene() instanceof PixelScene){
			((PixelScene) scene()).saveWindows();
			switchNoFade((Class<? extends PixelScene>) sceneClass, callback );
		} else {
			resetScene();
		}
	}
	
	public static void seamlessResetScene(){
		seamlessResetScene(null);
	}
	
	@Override
	protected void switchScene() {
		super.switchScene();
		if (scene instanceof PixelScene){
			((PixelScene) scene).restoreWindows();
		}
	}
	
	@Override
	public void resize( int width, int height ) {
		if (width == 0 || height == 0){
			return;
		}

		if (scene instanceof PixelScene &&
				(height != Game.height || width != Game.width)) {
			PixelScene.noFade = true;
			((PixelScene) scene).saveWindows();
		}

		super.resize( width, height );

		updateDisplaySize();

	}
	
	@Override
	public void destroy(){
		super.destroy();
		GameScene.endActorThread();
	}
	
	public void updateDisplaySize(){
		platform.updateDisplaySize();
	}

	public static void updateSystemUI() {
		platform.updateSystemUI();
	}
}