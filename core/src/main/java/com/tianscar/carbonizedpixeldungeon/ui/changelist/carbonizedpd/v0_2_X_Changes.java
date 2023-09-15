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

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.ChangesScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeButton;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeInfo;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;

import java.util.ArrayList;

public class v0_2_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.X", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add( changes );
		add_v0_2_1_Changes( changeInfos );
		add_v0_2_0_Changes( changeInfos );

	}

	public static void add_v0_2_1_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.0的 Bug）：\n" +
						"_-_ 无法正确拉取更新的问题"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.1.0的 Bug）：\n" +
						"_-_ 沉浸模式在安卓端不起作用的问题\n" +
						"_-_ 部分物品贴图错误的问题"));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CARBON_STEEL), "新的炼金配方！",
				"加入了两个新的炼金配方！它们主要用于帮助废物利用你不想使用的近战武器和戒指。\n\n" +
						"_碳钢锭_由近战武器融化而来，并且可以进一步融化为液金。\n\n" +
						"_魔金锭_由戒指裂解而来，可以用于升级其他低级戒指。"));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CROSSBOW), "新的武器类别！",
				"添加了新的武器类别：_远程武器_！\n\n" +
						"远程武器需要装填弹药，并且一般需要装备才能进行射击，能大幅提升弹药的攻击力！\n\n" +
						"值得注意的是：_受诅咒的_远程武器命中率会_严重_下降。\n\n" +
						"现版本远程武器只有 _2 阶_的_轻型十字弩_（单手）和 _4 阶_的_十字弩_（双手）。"));
		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"_-_ 微调了游戏主界面的玩家信息显示\n" +
						"_-_ 重制了桌面端的崩溃日志界面"));

	}

	public static void add_v0_2_0_Changes( ArrayList<ChangeInfo> changeInfos ) {

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
