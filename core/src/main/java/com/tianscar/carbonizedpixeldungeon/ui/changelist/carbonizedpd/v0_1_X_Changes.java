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
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.Buckler;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.scenes.ChangesScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeButton;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeInfo;

import java.util.ArrayList;

public class v0_1_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo( "v0.1.X", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add( changes );
		add_v0_1_4_Changes( changeInfos );
		add_v0_1_3_Changes( changeInfos );
		add_v0_1_2_Changes( changeInfos );
		add_v0_1_1_Changes( changeInfos );
		add_v0_1_0_Changes( changeInfos );

	}

	public static void add_v0_1_4_Changes( ArrayList<ChangeInfo> changeInfos ){

		ChangeInfo changes = new ChangeInfo("v0.1.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.1.0的 Bug）：\n" +
						"_-_ 桌面端窗口标题乱码的问题"));

	}

	public static void add_v0_1_3_Changes( ArrayList<ChangeInfo> changeInfos ){

		ChangeInfo changes = new ChangeInfo("v0.1.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.1.0 的 Bug）：\n" +
						"_-_ 镜像/虹卫无限连刃、暗影映像没伤害的问题\n" +
						"_-_ 物品栏居中显示错误的问题\n" +
						"_-_ 场景 BGM 不能正确播放的问题"));

	}

	public static void add_v0_1_2_Changes( ArrayList<ChangeInfo> changeInfos ){

		ChangeInfo changes = new ChangeInfo("v0.1.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BUCKLER), Messages.titleCase(Messages.get(Buckler.class, "name")),
						"_战士开局携带第二把初始武器：小型圆盾_\n\n" +
								Messages.get(Buckler.class, "desc")));

		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
						"_-_ 修改了部分贴图（来自Daniel Calan）\n" +
						"_-_ 修改了幸福结局界面"));

	}

	public static void add_v0_1_1_Changes( ArrayList<ChangeInfo> changeInfos ){

		ChangeInfo changes = new ChangeInfo("v0.1.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.RAT, 0, 75, 16, 15), "老鼠、老鼠和老鼠！",
				"_三种新老鼠的贴图来自过度生长的像素地牢_\n\n" +
						"添加了三种老鼠：\n" +
						"_- 壮年老鼠_的体能更强，反应力也更快。\n" +
						"_- 老年老鼠_的体能较弱，但丰富的战斗经验弥补了这一点。\n" +
						"_- 棕色老鼠_是一种常见的啮齿小鼠品种，攻击伤害更低，但和白化老鼠一样，会造成流血效果。"));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "BGM更新",
						"_-_ 关于界面（破碎的像素地牢部分）：来自破碎的像素地牢\n" +
						"_-_ 排行榜界面：破碎的像素地牢的已废弃幸福结局BGM"));

		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"_-_ 修改了标题界面（有彩蛋！）\n" +
						"_-_ 修改了部分贴图（来自 Daniel Calan）\n" +
						"_-_ 修改了英雄选择界面，加回了标题，并且背景图现在每小时都会不一样！"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI, null), "武器双持改动",
				"现在武器双持的命中率为双武器最大值的1.2倍。"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.1.0 的 Bug）：\n" +
						"_-_ 死亡界面的崩溃问题\n" +
						"_-_ 更新界面只有英文的问题"));

	}

	public static void add_v0_1_0_Changes( ArrayList<ChangeInfo> changeInfos ){

		ChangeInfo changes = new ChangeInfo("v0.1.0", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.CAPD), "开发者的留言",
						"_-_ 2023 年 2 月 25 日 更新\n" +
						"\n" +
						"v0.1.0 是碳素地牢的第一个重载版本！这次完全是重制。该版本仍然基于破碎的像素地牢 v1.0.3。\n" +
						"\n" +
						"理由仍是如此：因为不喜欢炼金能量更新，所以我选择了基于炼金能量更新之前的最后一个版本来修改。\n" +
						"\n" +
						"这个版本合入了部分原碳素地牢的内容，包括武器双持，不包括六属性、炼金/神器/药水/卷轴更新、护甲更新、职业更新、大佬鼠等。\n" +
						"六属性以后应该不会再加入了，其他的看情况吧，如果希望某些元素回归可以直接邮件/Discord 联系我给我发信息。"));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "BGM 更新",
						"_-_ 标题界面：由 Progressive Tune 厂牌的 Jasφn 创作\n" +
						"_-_ 英雄选择界面：来自 NYRDS 的 Remixed Dungeon\n" +
						"_-_ 地牢各个区域：来自后续版本的破碎的像素地牢"));

		changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), "游戏新闻",
				"_现在游戏新闻界面会显示碳素地牢的游戏新闻了！_\n\n" +
						"这个功能会从 carbonizedpd.tianscar.com 拉来博客文章，然后在游戏中显示。它还会在有新文章时通知玩家。\n\n" +
						"感谢 Evan 的 Atom 订阅代码和 Hexo 博客框架！我以后会尽量保持游戏新闻的更新的！"));

		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"_-_ 现在英雄的饱食度会在状态栏上显示\n" +
						"_-_ 快捷栏增加到最多 9 格，并且可以自由切换以适配窄屏幕\n" +
						"_-_ 修改了关于界面\n" +
						"_-_ 完全重做了英雄选择界面\n" +
						"_-_ 更改了应用图标\n" +
						"_-_ 添加了针对东亚语言的自定义像素字体（来自 TakWolf）\n" +
						"_-_ 更新了部分物品/界面贴图（来自 Daniel Calan）\n" +
						"_-_ 更改了主界面的背景图（来自 Remixed Dungeon）\n" +
						"_-_ 游戏内菜单的缩放调节按钮回归\n" +
						"_-_ 对部分其他界面进行了小的调整/改进"));

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"_-_ 新的语言：繁体中文！\n" +
						"_-_ 添加了安卓/桌面端的简体/繁体中文崩溃界面\n" +
						"_-_ 添加了简体/繁体中文的 GitHub 更新解析"));

		changes.addButton( new ChangeButton(Icons.get(Icons.BACKPACK), "背包增强",
				"背包储存空间从 20 格提升到 30 格，其他容器从 19 格提升到 29 格。"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI, null), "新机制：武器双持",
				"_将近战武器分为单手和双手，现在可以同时装备两把单手武器了！_\n\n" +
						"武器双持的效果：\n" +
						"_-_ 如果其中一个武器导致无法伏击，则无法伏击\n" +
						"_-_ 格挡量为双武器格挡量的总和\n" +
						"_-_ 双武器都能攻击到敌人时，“武器双持”生效\n" +
						"_-_ “武器双持”生效时，命中率取双武器最大值的 1.1 倍，伤害/攻速取平均值，双武器附魔各检定一次" +
						"\n\n" +
						"以下为单手武器:\n" +
						"_暗杀之刃_" +
						"，_小刀_" +
						"，_长匕首_" +
						"，_手斧_" +
						"，_铁头棍_" +
						"，_硬头锤_" +
						"，_老魔杖_" +
						"，_圆盾_" +
						"，_符文之刃_" +
						"，_弯刀_" +
						"，_短剑_" +
						"，_单手剑_" +
						"，_长鞭_" +
						"，_链锤_" +
						"，_破损的短剑_" +
						"\n\n" +
						"以下为双手武器:\n" +
						"_战斧_" +
						"，_十字弩_" +
						"，_魔岩拳套_" +
						"，_关刀_" +
						"，_镶钉手套_" +
						"，_巨斧_" +
						"，_巨型方盾_" +
						"，_巨剑_" +
						"，_长剑_" +
						"，_双钗_" +
						"，_长矛_" +
						"，_战锤_"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), "重生十字章增强",
				"现在未祝福的重生十字章可以保留三个物品了。"));

	}
	
}
