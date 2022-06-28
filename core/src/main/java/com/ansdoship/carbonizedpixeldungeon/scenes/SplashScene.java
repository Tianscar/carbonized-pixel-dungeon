package com.ansdoship.carbonizedpixeldungeon.scenes;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.ansdoship.carbonizedpixeldungeon.PDSettings;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.GameMath;

public class SplashScene extends PixelScene {

    private static float alpha = 0f;
    private static float sec = 0f;
    private static boolean played = false;
    private static boolean done = false;

    private Image gdx;
    private Image ansdoShip;
    private float originalGdxWidth;
    private float originalAnsdoShipWidth;
    private static float gdxScaleFactor = 1.0f;
    private static float ansdoShipScaleFactor = 1.0f;

    @Override
    public void create() {
        super.create();

        if (PDSettings.splashScreen() < 1 || done || Game.platform.isDebug()) {
            CarbonizedPixelDungeon.switchForceFade(WelcomeScene.class);
            return;
        }

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        gdx = new Image(Assets.Splashes.GDX) {
            @Override
            public void update() {
                if (PDSettings.splashScreen() > 0 && sec < 1f) {
                    alpha(GameMath.gate(0, alpha += Game.elapsed, 1));
                    sec += Game.elapsed;
                }
                else if (PDSettings.splashScreen() > 1 && sec < 2f) {
                    alpha(alpha = 1f);
                    sec += Game.elapsed;
                }
                else if (sec < (PDSettings.splashScreen() > 1 ? 3f : 2f)) {
                    alpha(GameMath.gate(0, alpha -= Game.elapsed, 1));
                    sec += Game.elapsed;
                    gdxScaleFactor += Game.elapsed / 10.f;
                    gdx.scale.set(w/originalGdxWidth/(landscape() ? 4f : 3f) * gdxScaleFactor);
                    gdx.x = (w - gdx.width())/2f;
                    gdx.y = (h - gdx.height())/2f;
                    PixelScene.align(gdx);
                }
                else if (PDSettings.splashScreen() > 1 && sec < 4f) {
                    if (gdx.visible) gdx.visible = false;
                    sec += Game.elapsed;
                }
                else if (!ansdoShip.visible) {
                    if (gdx.visible) gdx.visible = false;
                    ansdoShip.visible = true;
                }
            }
        };
        originalGdxWidth = gdx.width();
        gdx.scale.set(w/gdx.width()/(landscape() ? 4f : 3f) * gdxScaleFactor);

        gdx.x = (w - gdx.width())/2f;
        gdx.y = (h - gdx.height())/2f;
        PixelScene.align(gdx);
        if (sec >= 3f) gdx.visible = false;
        add(gdx);

        ansdoShip = new Image(Assets.Splashes.ANSDOSHIP) {
            @Override
            public void update() {
                if (!visible) return;
                if (sec >= (PDSettings.splashScreen() > 1 ? 4f : 2f) && sec < (PDSettings.splashScreen() > 1 ? 5f : 3f)) {
                    if (!played && PDSettings.splashScreen() > 1) {
                        played = true;
                        Sample.INSTANCE.play( Assets.Sounds.ANSDOSHIP, 2 );
                    }
                    alpha(GameMath.gate(0, alpha += Game.elapsed, 1));
                    sec += Game.elapsed;
                }
                else if (PDSettings.splashScreen() > 1 && sec < 7f) {
                    alpha(alpha = 1f);
                    sec += Game.elapsed;
                }
                else if (sec < (PDSettings.splashScreen() > 1 ? 8f : 4f)) {
                    alpha(GameMath.gate(0, alpha -= Game.elapsed, 1));
                    sec += Game.elapsed;
                    ansdoShipScaleFactor += Game.elapsed / 10.f;
                    ansdoShip.scale.set(w/originalAnsdoShipWidth/(landscape() ? 3f : 2f) * ansdoShipScaleFactor);
                    ansdoShip.x = (w - ansdoShip.width())/2f;
                    ansdoShip.y = (h - ansdoShip.height())/2f;
                    PixelScene.align(ansdoShip);
                }
                else if (!done) {
                    done = true;
                    if (ansdoShip.visible) ansdoShip.visible = false;
                    CarbonizedPixelDungeon.switchForceFade(WelcomeScene.class);
                }
            }
        };
        originalAnsdoShipWidth = ansdoShip.width();
        ansdoShip.scale.set(w/ansdoShip.width()/(landscape() ? 3f : 2f) * ansdoShipScaleFactor);

        ansdoShip.x = (w - ansdoShip.width())/2f;
        ansdoShip.y = (h - ansdoShip.height())/2f;
        PixelScene.align(ansdoShip);
        ansdoShip.visible = false;
        add(ansdoShip);

    }
}
