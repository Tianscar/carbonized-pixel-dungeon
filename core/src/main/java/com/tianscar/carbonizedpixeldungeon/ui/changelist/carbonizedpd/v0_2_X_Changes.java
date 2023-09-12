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

package com.tianscar.carbonizedpixeldungeon.ui.changelist.carbonizedpd;

import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeButton;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeInfo;

import java.util.ArrayList;

public class v0_2_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.X", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add( changes );
		add_v0_2_0_Changes( changeInfos );

	}

	public static void add_v0_2_0_Changes( ArrayList<ChangeInfo> changeInfos ){

		ChangeInfo changes = new ChangeInfo("v0.2.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.CHANGES), "更新机制重制",
				"现在会从 carbonizedpd.tianscar.com 而不是 GitHub 拉取更新。"));
		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"_-_ 更新了字体\n" +
						"_-_ 微调了部分贴图"));

	}
	
}
