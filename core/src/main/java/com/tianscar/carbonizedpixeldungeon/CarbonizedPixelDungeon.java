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

package com.tianscar.carbonizedpixeldungeon;

import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.scenes.TitleScene;
import com.tianscar.carbonizedpixeldungeon.scenes.WelcomeScene;
import com.tianscar.pixeldungeonclasses.noosa.Game;
import com.tianscar.pixeldungeonclasses.noosa.audio.Music;
import com.tianscar.pixeldungeonclasses.noosa.audio.Sample;
import com.tianscar.pixeldungeonclasses.utils.Bundle;
import com.tianscar.pixeldungeonclasses.utils.PlatformSupport;

public class CarbonizedPixelDungeon extends Game {

	public static final int v0_1_0   = 100;
	
	public CarbonizedPixelDungeon(PlatformSupport platform ) {
		super( sceneClass == null ? WelcomeScene.class : sceneClass, platform );

		//v1.0.0
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfFear.class,
				"com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfAffection" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfDeepSleep.class,
				"com.tianscar.carbonizedpixeldungeon.items.stones.StoneOfDeepenedSleep" );

		//v0.9.3
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.actors.mobs.Tengu.class,
				"com.tianscar.carbonizedpixeldungeon.actors.mobs.NewTengu" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.PrisonBossLevel.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewPrisonBossLevel" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.PrisonBossLevel.ExitVisual.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewPrisonBossLevel$exitVisual" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.PrisonBossLevel.ExitVisualWalls.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewPrisonBossLevel$exitVisualWalls" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.actors.mobs.DM300.class,
				"com.tianscar.carbonizedpixeldungeon.actors.mobs.NewDM300" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.CavesBossLevel.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewCavesBossLevel" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.CavesBossLevel.PylonEnergy.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewCavesBossLevel$PylonEnergy" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.CavesBossLevel.ArenaVisuals.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewCavesBossLevel$ArenaVisuals" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.CavesBossLevel.CityEntrance.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewCavesBossLevel$CityEntrance" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.CavesBossLevel.EntranceOverhang.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewCavesBossLevel$EntranceOverhang" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.CityBossLevel.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewCityBossLevel" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.CityBossLevel.CustomGroundVisuals.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewCityBossLevel$CustomGroundVisuals" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.CityBossLevel.CustomWallVisuals.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewCityBossLevel$CustomWallVisuals" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.HallsBossLevel.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewHallsBossLevel" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.HallsBossLevel.CenterPieceVisuals.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewHallsBossLevel$CenterPieceWalls" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.levels.HallsBossLevel.CenterPieceWalls.class,
				"com.tianscar.carbonizedpixeldungeon.levels.NewHallsBossLevel$CenterPieceWalls" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.items.Waterskin.class,
				"com.tianscar.carbonizedpixeldungeon.items.DewVial" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.items.TengusMask.class,
				"com.tianscar.carbonizedpixeldungeon.items.TomeOfMastery" );
		Bundle.addAlias(
				com.tianscar.carbonizedpixeldungeon.items.KingsCrown.class,
				"com.tianscar.carbonizedpixeldungeon.items.ArmorKit" );
		
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