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
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.Patrol;
import com.tianscar.carbonizedpixeldungeon.items.Gold;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.weapon.Weapon;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.ui.RedButton;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;

public class WndPatrol extends Window {

	private static final int WIDTH      = 120;
	private static final int BTN_HEIGHT = 20;
	private static final int GAP        = 2;

	public WndPatrol(final Item item) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item.image(), null ) );
		titlebar.label( Messages.titleCase( item.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "message"), 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );
		
		RedButton btnReward = new RedButton( Messages.get(this, "reward") ) {
			@Override
			protected void onClick() {
				takeReward( item, Patrol.Quest.weapon, Patrol.Quest.enchant );
			}
		};
		btnReward.setRect( 0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnReward );
		
		resize( WIDTH, (int)btnReward.bottom() );
	}

	private void takeReward( Item item, Weapon reward, Weapon.Enchantment enchant ) {

		hide();

		new Gold( 300 ).doPickUp( Dungeon.hero );

		item.detach( Dungeon.hero.belongings.backpack );
		if (reward == null) return;

		reward.identify();

		if (enchant != null) reward.enchant( enchant );

		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Messages.get(Dungeon.hero, "you_now_have", reward.name()) );
		} else {
			Dungeon.level.drop( reward, Dungeon.hero.pos ).sprite.drop();
		}

		Patrol.Quest.complete();
	}

}
