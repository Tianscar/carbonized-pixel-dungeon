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

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.PDAction;
import com.tianscar.carbonizedpixeldungeon.input.GameAction;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.windows.WndKeyBindings;

public class ResumeIndicator extends Tag {

	private Image icon;

	public ResumeIndicator() {
		super(0xCDD5C0);

		setSize( 24, 24 );

		visible = false;

	}
	
	@Override
	public GameAction keyAction() {
		return PDAction.TAG_RESUME;
	}

	@Override
	protected void createChildren() {
		super.createChildren();

		icon = Icons.get( Icons.ARROW);
		add( icon );
	}

	@Override
	protected void layout() {
		super.layout();

		icon.x = x+1 + (width - icon.width) / 2f;
		icon.y = y + (height - icon.height) / 2f;
		PixelScene.align(icon);
	}

	@Override
	protected void onClick() {
		Dungeon.hero.resume();
	}

	@Override
	protected String hoverText() {
		return Messages.titleCase(Messages.get(WndKeyBindings.class, "tag_resume"));
	}

	@Override
	public void update() {
		if (!Dungeon.hero.isAlive())
			visible = false;
		else if (visible != (Dungeon.hero.lastAction != null)){
			visible = Dungeon.hero.lastAction != null;
			if (visible)
				flash();
		}
		super.update();
	}
}
