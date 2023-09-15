package com.tianscar.carbonizedpixeldungeon.items.journal;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.journal.Document;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.windows.WndJournal;
import com.tianscar.carbonizedpixeldungeon.windows.WndStory;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;

public class Guidebook extends Item {

	{
		image = ItemSpriteSheet.MASTERY;
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
