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
import com.ansdoship.carbonizedpixeldungeon.Badges;
import com.ansdoship.carbonizedpixeldungeon.actors.mobs.npcs.BigRat;
import com.ansdoship.carbonizedpixeldungeon.effects.BadgeBanner;
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

		add_v0_0_7p7_changes(changeInfos);
		add_v0_0_7p6_changes(changeInfos);
		add_v0_0_7p5_changes(changeInfos);
		add_v0_0_7p4_changes(changeInfos);
		add_v0_0_7p3_changes(changeInfos);
		add_v0_0_7p2_changes(changeInfos);
		add_v0_0_7p1_changes(changeInfos);
		add_v0_0_7_changes(changeInfos);
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

	public static void add_v0_0_7p7_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.7p7", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"补全了部分游戏文本"));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"现在物品的力量需求不会显示在右上角了"));

		changes.addButton(new ChangeButton(Icons.get(Icons.INFO), "六属性实装！",
				"_现在六属性全部起作用了！_\n\n" +
						"_- 力量_影响近战和远程武器的伤害和命中\n" +
						"_- 体质_影响生命上限\n" +
						"_- 敏捷_影响投掷和远程武器的伤害和命中\n" +
						"_- 智力_影响法杖的使用\n" +
						"_- 感知_影响探测地块的范围\n" +
						"_- 魅力_能够让商店打折"));
	}

	public static void add_v0_0_7p6_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.7p6", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"补全了部分游戏文本"));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"修改了关于界面"));

		changes.addButton(new ChangeButton(Icons.get(Icons.PREFS), Messages.get(ChangesScene.class, "misc"),
				"将启动界面和过场动画的设置分开"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 过场动画的闪退问题"));
	}

	public static void add_v0_0_7p5_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.7p5", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"_-_ 添加了桌面端的中文崩溃界面\n" +
						"_-_ 添加了中文的GitHub更新解析"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 过场动画的闪退问题"));
	}

	public static void add_v0_0_7p4_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.7p4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"添加了安卓端的中文崩溃界面"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 过场动画的崩溃问题"));
	}

	public static void add_v0_0_7p3_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.7p3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"补全了部分游戏文本"));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"优化了过场动画界面"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 战士“实验对象”天赋导致崩溃的Bug"));
	}

	public static void add_v0_0_7p2_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.7p2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"补全了部分游戏文本"));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"修改了死亡界面"));

		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了：\n" +
						"_-_ 玩家死后仍然活着的Bug\n" +
						"_-_ 启动界面的相关Bug"));
	}

	public static void add_v0_0_7p1_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.7p1", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), Messages.get(ChangesScene.class, "language"),
				"补全了部分游戏文本"));
	}

	public static void add_v0_0_7_changes( ArrayList<ChangeInfo> changeInfos ) {
		ChangeInfo changes = new ChangeInfo("v0.0.7", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CARBON_STEEL), "开发者的留言",
				"_-_ 2022年6月28日更新\n" +
						"_-_ 距离碳化的像素地牢v0.0.6更新31天\n" +
						"\n" +
						"由于生活所迫，v0.0.7版本可能会是最后一次更新。\n\n" +
						"本次更新完全是半成品，大多数本该加入的机制都没有实装。\n\n" +
						"欢迎各位在Bug的海洋中游泳。\n\n" +
						"接下来也许会有patch版本，也许会更新内容，但是谁知道呢？\n\n" +
						"说不出“敬请期待”这样的话了。"));

		changes.addButton( new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"_-_ 换回了原来的图标\n" +
						"_-_ 添加了启动界面\n" +
						"_-_ 添加了针对东亚语言的自定义像素字体\n" +
						"_-_ 实验性的添加了手柄支持\n" +
						"_-_ 快捷栏改为3～9格\n" +
						"_-_ 修改了支持和反馈界面\n" +
						"_-_ 修改了关于界面\n" +
						"_-_ 对部分其他界面进行了小的调整/改进"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_ARMBAND), "神偷袖章重制！",
				"_来自破碎的像素地牢v1.2.3_\n\n" +
						"现在神偷袖章能够随获取经验充能，并且可以偷窃怪物和商店老板身上的金币。"));

		changes.addButton( new ChangeButton(BadgeBanner.image(Badges.Badge.MONSTERS_SLAIN_5.image), "新的徽章系统！",
				"_来自破碎的像素地牢v1.2.3_\n" +
				        "_徽章现在有名字了，并且添加了8个新徽章！_\n\n" +
						"这些新的徽章都是现有系列徽章的一部分（例如击败X个敌人），主要存在于黄金级别左右。\n" +
						"'已玩过的游戏'徽章也被调整为在玩过的游戏数量多或赢过的游戏数量少时解锁。"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SUMMON_ELE), "新的法术结晶！",
				"_来自破碎的像素地牢v1.2.3_\n" +
						"添加了_两个新的法术结晶：_\n\n" +
						"_- 唤魔晶柱_需要元素余烬和一个奥术催化剂。它可以用来召唤一个友好的元素为你战斗，甚至可以用其他物品为它提供能量！\n" +
						"_- 念力结晶_需要一些液金和一个奥术催化剂。它可以用来远程抓取物品，甚至是粘在敌人身上的投掷物！\n\n" +
						"由于对法术结晶的重新设计，商人信标和信使结晶显得多余，并已从游戏中移除。商店现在出售一些炼金棱晶来替代商人信标。"));

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.EXOTIC_ISAZ), "合剂和秘卷重制！",
				"_来自破碎的像素地牢v1.2.3_\n" +
						"部分合剂和秘卷已经被重制，更加强大和值得使用：\n\n" +
						"_- 神圣祝福合剂_现在是_神意启发合剂_，它可以提供两点额外的天赋点。\n" +
						"_- 魅惑秘卷_现在是_塞壬之歌秘卷_，它可以使敌人永久地变成盟友。\n" +
						"_- 迷乱秘卷_现在是_决斗秘卷_，它会吸引敌人，但同时也会创造一个竞技场，使你受到的伤害减少。" ));

		changes.addButton( new ChangeButton( new ItemSprite(ItemSpriteSheet.EXOTIC_AMBER), "合剂和秘卷增强！",
				"_来自破碎的像素地牢v1.2.3_\n" +
				        "部分合剂和秘卷得到了些许增强：\n\n" +
						"_- 暗夜迷雾合剂、暴风骤雨合剂和腐蚀酸雾合剂_的初始AOE范围从1x1上升到3x3\n" +
						"_- 暗夜迷雾合剂_现在只会遮挡敌人的视野，并且气体增加了50%以上\n" +
						"_- 腐蚀酸雾合剂_的初始伤害+1\n" +
						"_- 魔能透视合剂_的视野范围从8上升到12\n" +
						"_- 全面净化合剂_现在有持续5回合的debuff免疫效果\n\n" +
						"_- 先见秘卷_的探测范围从2上升到8，但是生效回合从600降低到400\n" +
						"_- 虹卫秘卷_召唤的虹色幻影生命值+2并且伤害+20%"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_TOOLKIT), "神器增强！",
				"_来自破碎的像素地牢v1.2.3_\n" +
						"部分神器得到了些许增强：\n\n" +
						"_- 炼金工具箱_现在在敌人周围也能使用了。\n" +
						"_- 丰饶之角_现在有了“小吃一口”选项，消耗1点充能；为了平衡，充能上限减半，但恢复的饱食度翻倍\n" +
						"_- 干枯玫瑰_召唤的悲伤幽灵的生命回复翻倍"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AQUA_BLAST), "炼金增强！",
				"_来自破碎的像素地牢v1.2.3_\n" +
				        "部分炼金产物得到了增强：\n\n" +
						"_- 炼狱魔药、冰暴魔药和淤泥魔药_需要的炼金能量降低1\n" +
						"_- 转移结晶_需要的炼金能量从6降低到4\n" +
						"_- 强能晶柱_需要的炼金能量从6降低到4\n" +
						"_- 返回晶柱_需要的炼金能量从8降低到6\n" +
						"_- 炼金棱晶_需要的炼金能量从3降低到2\n\n" +
						"_- 淤泥魔药_每回合造成的伤害提升1\n" +
						"_- 炼狱魔药和冰暴魔药_的初始AOE范围从1x1上升到3x3\n" +
						"_- 雷鸣魔药_的AOE范围从5x5上升到7x7\n\n" +
						"_- 转移结晶_现在会让任何被转移的生物陷入迷惑\n" +
						"_- 水爆晶核_现在等效于激流陷阱，但一次合成的数量从12个降低到8个\n" +
						"_- 陷阱晶核_一次合成的数量从3个增加到4个\n" +
						"_- 诅咒棱晶_现在对高等级装备的提升超过了+1，一次合成的数量从3增加到4\n" +
						"_- 转换棱晶_一次合成的数量从8个增加到12个，需要的炼金能量从6上升到8"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ROT_DART), "飞镖增强！",
				"_来自破碎的像素地牢v1.2.3_\n" +
						"部分涂药飞镖得到了增强：\n\n" +
						"_- 酸蚀飞镖_的使用次数从1提升到5\n" +
						"_- 激素涌动飞镖_的持续回合从10提升到30\n" +
						"_- 电击飞镖_的伤害现在会随着层数有些许提升\n" +
						"_- 剧毒飞镖_的伤害得到了些许提升\n" +
						"_- 祝福飞镖_的持续回合从30提升到100\n" +
						"_- 传送飞镖_现在能更稳定地将敌人传送走"));
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
