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

package com.ansdoship.carbonizedpixeldungeon.ui.changelist.cbpd;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs.BigRat;
import com.ansdoship.carbonizedpixeldungeon.items.artifacts.DriedRose;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.ChangesScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.sprites.StatueSprite;
import com.ansdoship.carbonizedpixeldungeon.ui.Icons;
import com.ansdoship.carbonizedpixeldungeon.ui.Window;
import com.ansdoship.carbonizedpixeldungeon.ui.changelist.ChangeButton;
import com.ansdoship.carbonizedpixeldungeon.ui.changelist.ChangeInfo;
import com.ansdoship.pixeldungeonclasses.noosa.Image;

import java.util.ArrayList;

public class v0_0_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo( "v0.0.X", true, "");
		changes.hardlight( Window.TITLE_COLOR);
		changeInfos.add(changes);

		add_v0_0_6p2_changes(changeInfos);
		add_v0_0_6p1_changes(changeInfos);
		add_v0_0_6_changes(changeInfos);
		add_v0_0_5p3_changes(changeInfos);
		add_v0_0_5p2_changes(changeInfos);
		add_v0_0_5p1_changes(changeInfos);
		add_v0_0_5_changes(changeInfos);
		add_v0_0_4p2_changes(changeInfos);
		add_v0_0_4p1_changes(changeInfos);
		add_v0_0_4_changes(changeInfos);
		add_v0_0_3p2_changes(changeInfos);
		add_v0_0_3p1_changes(changeInfos);
		add_v0_0_3_changes(changeInfos);
		add_v0_0_2p3_changes(changeInfos);
		add_v0_0_2p2_changes(changeInfos);
		add_v0_0_2p1_changes(changeInfos);
		add_v0_0_2_Changes(changeInfos);
		add_v0_0_1_Changes(changeInfos);
	}

	public static void add_v0_0_6p2_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.6p2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 远程武器命中率算法的Bug"));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"更换了新的应用程序图标"));
	}

	public static void add_v0_0_6p1_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.6p1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 冒险手册闪退的Bug\n" +
						"_-_ 背包界面物品文字重叠的Bug"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CROSSBOW), "十字弩重制！",
				"添加了新的武器种类：_远程武器_！\n\n" +
						"远程武器需要装填弹药，并且一般需要装备，才能进行射击，能大幅提升弹药的攻击力！但是_受诅咒的_远程武器_命中率会严重下降_。\n\n" +
						"现版本远程武器只有_2阶_的_轻型十字弩_（单手）和_4阶_的_十字弩_（双手）"));
	}

	public static void add_v0_0_6_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.6", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CARBON_STEEL), "开发者的留言",
				"_-_ 2022年5月28日更新\n" +
						"_-_ 距离碳化的像素地牢v0.0.5更新2天\n" +
						"\n" +
						"v0.0.6版本将会是一个里程碑式的更新！\n\n" +
						"添加了新的生物：大佬鼠！（代替了原来的老鼠王）\n\n" +
						"新的冒险手册页面：武器双持！\n\n" +
						"并且会在接下来的几个patch版本添加新的地区：仓库番（类似发芽的像素地牢的推羊关）！\n\n" +
						"敬请期待！"));

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.GUIDEBOOK, null), "新的冒险手册页面！",
				"在玩家得到第二把单手武器时，冒险手册会有相关的提示！"));

		changes.addButton( new ChangeButton(new Image(Assets.Sprites.BIGRAT, 0, 2, 16, 16), Messages.get(BigRat.class, "name"),
				"新生物：大佬鼠将代替老鼠王在地牢第五层出现！\n\n" +
						"它不会接受_矮人王冠_，而是会在一定次数的对话后给英雄一些_阳春草种子_！\n\n" +
						"如果你使用简体中文或繁体中文进行游戏，它会从一个一共50多条的词库里随机挑选一部分来与你对话！\n（注：大佬鼠的原型为群友Catand，词库也是他写的，_全是骚话_）"));
	}

	public static void add_v0_0_5p3_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.5p3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 新闻界面链接跳转有误的Bug\n" +
						"_-_ BOSS战结束后要刷新楼层才会播放BGM的Bug\n" +
						"_-_ 战士天赋无法鉴定副手武器的Bug\n" +
						"_-_ 角斗士连击标志只显示主手武器的Bug\n" +
						"_-_ 诅咒陷阱无法诅咒副手武器的Bug\n" +
						"_-_ 缴械陷阱无法缴械副手武器的Bug\n" +
						"_-_ 英雄镜像和虹色幻影不计算副手武器的Bug\n" +
						"_-_ 镐子可以被嬗变的Bug"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), "重生十字章增强",
				"现在未祝福的重生十字章可以保留三个物品了。"));

		changes.addButton( new ChangeButton(new DriedRose(),
				"现在悲伤幽灵可以双持武器了。"));

		changes.addButton( new ChangeButton(new Image(new StatueSprite()), "活化石像改动",
				"现在活化石像、装甲石像和石像守卫有概率双持武器了。"));
	}

	public static void add_v0_0_5p2_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.5p2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 无法正常从GitHub获取更新信息的Bug"));
	}

	public static void add_v0_0_5p1_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.5p1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI, null), "武器双持改动",
				"现在双持武器的消耗回合数降低为相加的一半。"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 新闻界面链接跳转有误的Bug"));
	}

	public static void add_v0_0_5_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.5", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CARBON_STEEL), "开发者的留言",
				"_-_ 2022年5月26日更新\n" +
						"_-_ 距离碳化的像素地牢v0.0.4更新2天\n" +
						"\n" +
						"v0.0.5版本将后续版本的破碎的像素地牢的BGM全部加入，并实装了游戏新闻界面！"));

		changes.addButton(new ChangeButton(Icons.get(Icons.NEWS), "游戏新闻界面实装！",
				"_现在游戏新闻界面会显示碳化的像素地牢的游戏新闻了！_\n\n" +
						"这个功能会从cbpd.tianscar.com拉来博客文章，然后在游戏中显示。它还会在有新文章时通知玩家。\n\n" +
						"感谢Evan的Atom订阅代码和Hexo博客框架！我以后会尽量保持游戏新闻的更新的！"));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "新的BGM！",
				"_所有场景的都有BGM了！_\n\n" +
						"这些BGM来自后续版本的破碎的像素地牢。"));
	}

	public static void add_v0_0_4p2_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.4p2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 正常游戏中有Debug物品的Bug\n" +
						"_-_ 菜单界面饱食度显示错误的Bug"));
	}

	public static void add_v0_0_4p1_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.4p1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ v0.0.2p3版本没有在改动界面显示的Bug\n" +
						"_-_ 改动界面的部分文本Bug\n" +
						"_-_ 投掷武器的伤害Bug"));

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"添加了新的语言：繁体中文！"));
	}

	public static void add_v0_0_4_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CARBON_STEEL), "开发者的留言",
				"_-_ 2022年5月24日更新\n" +
						"_-_ 距离碳化的像素地牢v0.0.3更新1天\n" +
						"\n" +
						"v0.0.4版本添加了新的改动界面，修改了支持和反馈界面，以及应用程序图标。"));

		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"_-_ 添加了新的改动界面\n" +
						"_-_ 修改了支持和反馈界面\n" +
						"_-_ 对部分其他界面进行了小的调整/改进"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 镐子只有在主武器栏位才能完成击杀蝙蝠任务的Bug"));
	}

	public static void add_v0_0_3p2_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.3p2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 武器没有伤害的Bug"));
	}

	public static void add_v0_0_3p1_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.3p1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
						 "尝试修复了：\n" +
								"_-_ 正常游戏中有Debug物品的Bug\n" +
								"_-_ 空手无法攻击的Bug"));
	}

	public static void add_v0_0_3_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CARBON_STEEL), "开发者的留言",
				"_-_ 2022年5月23日更新\n" +
						"_-_ 距离碳化的像素地牢v0.0.2更新1天\n" +
						"\n" +
						"v0.0.3版本让英雄的饱食度能够在状态栏上显示，并且将快捷栏增加到了最多6格。"));

		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
						"_-_ 现在英雄的饱食度会在状态栏上显示！\n" +
						"_-_ 快捷栏增加到最多6格！\n" +
						"_-_ 对部分其他界面进行了小的调整/改进"));
	}

	public static void add_v0_0_2p3_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.2p3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI, null), "武器双持改动",
				"现在双持武器的伤害和消耗回合数都降低为相加的2/3。"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 双持4倍伤害的的Bug\n" +
						"_-_ 副武器不触发防御音效的Bug"));
	}

	public static void add_v0_0_2p2_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.2p2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 无法伏击的Bug"));
	}

	public static void add_v0_0_2p1_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.2p1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI, null), "武器双持改动",
						"现在双持武器的攻击距离取最小值，也不能触发两次角斗士的连击了。\n\n" +
						"以下武器改为单手:\n" +
						"_战斧_" +
						"，_链锤_" +
						"，_铁头棍_"));
	}

	public static void add_v0_0_2_Changes( ArrayList<ChangeInfo> changeInfos ){

		ChangeInfo changes = new ChangeInfo("v0.0.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CARBON_STEEL), "开发者的留言",
				"_-_ 2022年5月22日更新\n" +
						"_-_ 距离碳化的像素地牢v0.0.1更新1天\n" +
						"\n" +
						"v0.0.2版本添加了武器双持机制，将近战武器全部分为了单手武器和双手武器。\n" +
						"\n" +
						"我并不擅长写更新日志，所以接下来的更新日志可能不会涵盖到所有更新的游戏内容。\n" +
						"\n" +
						"v0.1.0之前的版本为Beta测试版本，大概率会有很多Bug，平衡性也无法得到保证。"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SAI, null), "新机制：武器双持",
				"现在可以同时装备两把单手武器了！\n\n" +
						"同时装备两把单手武器，能够发挥出两把武器相加100%的防御力和伤害，命中率和消耗的回合数都是两把武器相加的50%，而且攻击距离取最大值！也能触发两次角斗士的连击！\n\n" +
						"以下为单手武器:\n" +
						"_暗杀之刃_" +
						"，_小刀_" +
						"，_长匕首_" +
						"，_手斧_" +
						"，_长剑_" +
						"，_硬头锤_" +
						"，_老魔杖_" +
						"，_圆盾_" +
						"，_符文之刃_" +
						"，_弯刀_" +
						"，_短剑_" +
						"，_单手剑_" +
						"，_长鞭_" +
						"，_破损的短剑_" +
						"\n\n" +
						"以下为双手武器:\n" +
						"_战斧_" +
						"，_十字弩_" +
						"，_链锤_" +
						"，_魔岩拳套_" +
						"，_关刀_" +
						"，_镶钉手套_" +
						"，_巨斧_" +
						"，_巨型方盾_" +
						"，_巨剑_" +
						"，_铁头棍_" +
						"，_双钗_" +
						"，_长矛_" +
						"，_战锤_"));

		changes.addButton( new ChangeButton(Icons.get(Icons.BACKPACK), "背包增强",
				"背包储存空间从20格提升到30格，其他容器从19格提升到29格。"));
	}
	
	public static void add_v0_0_1_Changes( ArrayList<ChangeInfo> changeInfos ){
		
		ChangeInfo changes = new ChangeInfo("v0.0.1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CARBON_STEEL), "开发者的留言",
				"_-_ 2022年5月21日更新\n" +
				"_-_ 距离破碎的像素地牢v1.0.3更新280天\n" +
				"\n" +
				"v0.0.1是碳化的像素地牢的第一个版本！该版本基于破碎的像素地牢v1.0.3。\n" +
				"\n" +
				"因为不喜欢炼金能量更新，所以我选择了基于炼金能量更新之前的最后一个版本来修改。\n" +
				"\n" +
				"这个版本没有添加任何新的元素，仅仅修改了界面和标题界面的BGM。但是，如果不出意外，接下来的几个版本会对游戏机制进行大改。"));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO), "新的BGM！",
				"_标题界面的BGM已经更换！_\n\n" +
						"这首曲子由Progressive Tune厂牌的Jason创作。"));

		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"对几乎所有窗体进行了各种小的调整/改进"));
	}
	
}
