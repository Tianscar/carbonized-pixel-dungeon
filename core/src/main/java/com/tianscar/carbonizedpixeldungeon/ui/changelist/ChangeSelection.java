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

package com.tianscar.carbonizedpixeldungeon.ui.changelist;

import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.ui.RedButton;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.pixeldungeonclasses.noosa.ColorBlock;
import com.tianscar.pixeldungeonclasses.noosa.Image;
import com.tianscar.pixeldungeonclasses.noosa.PointerArea;
import com.tianscar.pixeldungeonclasses.noosa.ui.Component;

public class ChangeSelection extends Component {

	private static final int BTN_HEIGHT	    = 18;

	protected ColorBlock line;

	private RenderedTextBlock title;

	private RedButton btnSelect;

	public ChangeSelection(String title, String text) {
		super();

		this.title = PixelScene.renderTextBlock( title, 9 );
		line = new ColorBlock( 1, 1, 0xFF222222);
		add(line);
		
		add(this.title);

		this.btnSelect = new RedButton(text) {
			{
				hotArea.blockLevel = PointerArea.NEVER_BLOCK;
			}
			@Override
			public void onClick() {
				ChangeSelection.this.onClick();
			}
		};
		add(this.btnSelect);
		
	}

	public Image icon() {
		return btnSelect.icon();
	}

	public void icon(Image icon) {
		btnSelect.icon(icon);
	}

	public void onClick() {

	}
	
	public void hardlight( int color ){
		title.hardlight( color );
	}
	
	@Override
	protected void layout() {
		float posY = this.y + 5;
		
		title.setPos(
				x + (width - title.width()) / 2f,
				posY
		);
		PixelScene.align( title );
		posY += title.height() + 4;

		float posX = x;
		btnSelect.setRect(posX, posY, width(), BTN_HEIGHT);
		PixelScene.align(btnSelect);
		posY += BTN_HEIGHT;
		
		height = posY - this.y;

		line.size(width(), 1);
		line.x = x;
		line.y = y+2;
	}

}