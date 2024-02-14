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

package com.tianscar.carbonizedpixeldungeon.windows;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.PlagueDoctor;
import com.tianscar.carbonizedpixeldungeon.items.armor.Armor;
import com.tianscar.carbonizedpixeldungeon.items.quest.RatHide;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.ui.RedButton;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;

public class WndPlagueDoctor extends Window {

	private static final int WIDTH      = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP        = 2;

	public WndPlagueDoctor(final RatHide hides) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( hides.image(), null ) );
		titlebar.label( Messages.titleCase( hides.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "message"), 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );
		
		RedButton btnReward = new RedButton( Messages.get(this, "reward") ) {
			@Override
			protected void onClick() {
				takeReward( hides, PlagueDoctor.Quest.armor, PlagueDoctor.Quest.glyph );
			}
		};
		btnReward.setRect( 0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnReward );
		
		resize( WIDTH, (int)btnReward.bottom() );
	}

	private void takeReward( RatHide hides, Armor reward, Armor.Glyph glyph ) {

		hide();

		hides.detachAll( Dungeon.hero.belongings.backpack );
		if (reward == null) return;

		reward.identify();

		if (glyph != null) reward.inscribe( glyph );

		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Messages.get(Dungeon.hero, "you_now_have", reward.name()) );
		} else {
			Dungeon.level.drop( reward, Dungeon.hero.pos ).sprite.drop();
		}

		PlagueDoctor.Quest.complete();
	}

}
