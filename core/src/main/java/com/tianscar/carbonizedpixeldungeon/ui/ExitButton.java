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

import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.windows.WndKeyBindings;
import com.tianscar.carbonizedpixeldungeon.input.GameAction;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;

public class ExitButton extends IconButton {

	public ExitButton() {
		super(Icons.EXIT.get());

		width = 20;
		height = 20;
	}

	@Override
	protected void onClick() {
		Game.scene().onBackPressed();
	}

	@Override
	public GameAction keyAction() {
		return GameAction.BACK;
	}

	@Override
	protected String hoverText() {
		return Messages.titleCase(Messages.get(WndKeyBindings.class, "back"));
	}

}
