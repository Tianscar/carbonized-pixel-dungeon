package com.ansdoship.carbonizedpixeldungeon.items.journal;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.items.Item;
import com.ansdoship.carbonizedpixeldungeon.journal.Document;
import com.ansdoship.carbonizedpixeldungeon.scenes.GameScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.windows.WndJournal;
import com.ansdoship.carbonizedpixeldungeon.windows.WndStory;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.Callback;

public class Guidebook extends Item {

	{
		image = ItemSpriteSheet.GUIDEBOOK;
	}

	@Override
	public final boolean doPickUp(Hero hero) {
		GameScene.pickUpJournal(this, hero.pos);
		String page = Document.GUIDE_INTRO;
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndStory(WndJournal.GuideTab.iconForPage(page),
						Document.ADVENTURERS_GUIDE.pageTitle(page),
						Document.ADVENTURERS_GUIDE.pageBody(page)){

					float elapsed = 0;

					@Override
					public void update() {
						elapsed += Game.elapsed;
						super.update();
					}

					@Override
					public void hide() {
						//prevents accidentally closing
						if (elapsed >= 1) {
							super.hide();
						}
					}
				});
			}
		});
		Document.ADVENTURERS_GUIDE.readPage(Document.GUIDE_INTRO);
		Sample.INSTANCE.play( Assets.Sounds.ITEM );
		hero.spendAndNext( TIME_TO_PICK_UP );
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

}
