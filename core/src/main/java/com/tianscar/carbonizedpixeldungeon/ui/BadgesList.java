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

package com.tianscar.carbonizedpixeldungeon.ui;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Badges;
import com.tianscar.carbonizedpixeldungeon.effects.BadgeBanner;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.windows.WndBadge;
import com.tianscar.pixeldungeonclasses.noosa.Game;
import com.tianscar.pixeldungeonclasses.noosa.Image;
import com.tianscar.pixeldungeonclasses.noosa.audio.Sample;
import com.tianscar.pixeldungeonclasses.noosa.ui.Component;

import java.util.ArrayList;

public class BadgesList extends ScrollPane {

	private ArrayList<ListItem> items = new ArrayList<>();
	
	public BadgesList( boolean global ) {
		super( new Component() );
		
		for (Badges.Badge badge : Badges.filterReplacedBadges( global )) {
			
			if (badge.image == -1) {
				continue;
			}
			
			ListItem item = new ListItem( badge );
			content.add( item );
			items.add( item );
		}
	}
	
	@Override
	protected void layout() {
		
		float pos = 0;
		
		int size = items.size();
		for (int i=0; i < size; i++) {
			items.get( i ).setRect( 0, pos, width, ListItem.HEIGHT );
			pos += ListItem.HEIGHT;
		}
		
		content.setSize( width, pos );

		super.layout();
	}
	
	@Override
	public void onClick( float x, float y ) {
		int size = items.size();
		for (int i=0; i < size; i++) {
			if (items.get( i ).onClick( x, y )) {
				break;
			}
		}
	}

	private class ListItem extends Component {
		
		private static final float HEIGHT	= 20;
		
		private Badges.Badge badge;
		
		private Image icon;
		private RenderedTextBlock label;
		
		public ListItem( Badges.Badge badge ) {
			super();
			
			this.badge = badge;
			icon.copy( BadgeBanner.image( badge.image ));
			label.text( badge.desc() );
		}
		
		@Override
		protected void createChildren() {
			icon = new Image();
			add( icon );
			
			label = PixelScene.renderTextBlock( 6 );
			add( label );
		}
		
		@Override
		protected void layout() {
			icon.x = x;
			icon.y = y + (height - icon.height) / 2;
			PixelScene.align(icon);
			
			label.setPos(
					icon.x + icon.width + 2,
					y + (height - label.height()) / 2
			);
			PixelScene.align(label);
		}
		
		public boolean onClick( float x, float y ) {
			if (inside( x, y )) {
				Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
				Game.scene().add( new WndBadge( badge, true ) );
				return true;
			} else {
				return false;
			}
		}
	}
}