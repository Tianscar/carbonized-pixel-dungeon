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
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.elementalist.AetherBlink;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.elementalist.Resonance;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Arsonist;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.GoldenStatue;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.Gardner;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.Knight;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.Patrol;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.PlagueDoctor;
import com.tianscar.carbonizedpixeldungeon.items.food.Cheese;
import com.tianscar.carbonizedpixeldungeon.items.food.SmallRation;
import com.tianscar.carbonizedpixeldungeon.items.spells.MagicalPorter;
import com.tianscar.carbonizedpixeldungeon.items.spells.SummonElemental;
import com.tianscar.carbonizedpixeldungeon.items.spells.TelekineticGrab;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.GoldenSword;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.scenes.ChangesScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.TalentIcon;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeButton;
import com.tianscar.carbonizedpixeldungeon.ui.changelist.ChangeInfo;
import com.tianscar.carbonizedpixeldungeon.windows.WndHeroInfo;

import java.util.ArrayList;

public class v0_2_X_Changes {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.X", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add( changes );
		add_v0_2_7_Changes( changeInfos );
		add_v0_2_6_Changes( changeInfos );
		add_v0_2_5_Changes( changeInfos );
		add_v0_2_4_Changes( changeInfos );
		add_v0_2_3_Changes( changeInfos );
		add_v0_2_2_Changes( changeInfos );
		add_v0_2_1_Changes( changeInfos );
		add_v0_2_0_Changes( changeInfos );

	}

	public static void add_v0_2_7_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.7", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.WARNING),
				"内容未完成",
				"一些文本未被翻译成英文，并且元素使的第三个护甲技能未被添加"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.2的 Bug）：\n" +
						"_-_ 元素使绝缘电流天赋导致电击无法传导的问题"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.2的 Bug）：\n" +
						"_-_ 镜像攻击敌人导致闪退的问题"));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SUMMON_ELE),
				Messages.get(SummonElemental.class, "name"),
				Messages.get(SummonElemental.class, "desc")));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.TELE_GRAB),
				Messages.get(TelekineticGrab.class, "name"),
				Messages.get(TelekineticGrab.class, "desc")));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_CRIMSON),
				"合剂改动",
				"_-_ _激素涌动合剂_现在可以永久提升 1 点力量了。\n" +
						"_-_ _神圣祝福合剂_改为_神意启发合剂_，可以增加 2 点额外的天赋点，每层限一次。"));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_KAUNAN),
				"秘卷改动",
				"_-_ _石化秘卷_现在_石化_而不是_麻痹_敌人。石化状态下的敌人不会因为受到伤害而解除石化。\n" +
						"_-_ _迷惑秘卷_改为_决斗秘卷_，可以展开一个_决斗区域_，区域内增幅英雄并削弱敌人。"));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_TOOLKIT),
				"神器改动",
				"_-_ _炼金工具箱_现在出现在第一个_炼金房_。\n" +
						"_-_ _时光沙漏_现在出现在第一个_商店_。\n" +
						"_-_ _干枯玫瑰_现在由_粘咕_掉落。"));
		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGIC_PORTER),
				Messages.get(MagicalPorter.class, "name"),
				"移除了_信使结晶_的炼金配方。"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.KNIGHT, 0, 0, 12, 16),
				Messages.get(Knight.class, "name"),
				"增强了骑士任务的怪物，现在它们会逃走了。怪物逃走后骑士仍然会给予一些金币作为报酬。"));
		changes.addButton( new ChangeButton(Icons.get(Icons.STAIRS),
				"房间改动",
				"从后续版本的_破碎的像素地牢_移植了_献祭房_与_毒气房_。"));

	}

	public static void add_v0_2_6_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.6", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.WARNING),
				"内容未完成",
				"一些文本未被翻译成英文，并且元素使的第三个护甲技能未被添加"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.3的 Bug）：\n" +
						"_-_ 采摘蓝莓音效无限播放的问题"));

	}

	public static void add_v0_2_5_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.5", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.WARNING),
				"内容未完成",
				"一些文本未被翻译成英文，并且元素使的第三个护甲技能未被添加"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.3的 Bug）：\n" +
						"_-_ 采摘蓝莓中途取消不掉落蓝莓的问题"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.2的 Bug）：\n" +
						"_-_ 无法解锁元素使的问题"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.4的 Bug）：\n" +
						"_-_ 骑士任务界面文本缺失的问题"));

	}

	public static void add_v0_2_4_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.4", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.WARNING),
				"内容未完成",
				"一些文本未被翻译成英文，并且元素使的第三个护甲技能未被添加"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.3的 Bug）：\n" +
						"_-_ 瘟疫巫医和巡逻队员不生成的Bug"));
		changes.addButton( new ChangeButton(new Image(Assets.Sprites.THIEF, 0, 26, 12, 13),
				Messages.get(Arsonist.class, "name"),
				"会在监狱层出现，类似于疯狂强盗，但点燃英雄而非使英雄中毒"));
		changes.addButton( new ChangeButton(new Image(Assets.Sprites.GOLDEN, 0, 0, 12, 15),
				Messages.get(GoldenStatue.class, "name"),
				"有概率在矮人层和恶魔层的金币间出现，必定手持昂贵的黄金剑！"));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GOLDEN_SWORD),
				Messages.get(GoldenSword.class, "name"),
				Messages.get(GoldenSword.class, "desc")));
		String knightIntro = Messages.get(Knight.class, "intro");
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.KNIGHT, 0, 0, 12, 16),
				Messages.get(Knight.class, "name"),
				knightIntro.substring(knightIntro.lastIndexOf("\n") + 1)));
		changes.addButton(new ChangeButton(new TalentIcon(Talent.ICEMAIL.icon()),
				Talent.ICEMAIL.title(),
				"现在晶触之力（晶触秘药的buff）也能像冻伤状态一样触发元素使的寒冰铠甲天赋"));
		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"_-_ 更新了字体\n" +
						"_-_ 微调了部分贴图"));

	}

	public static void add_v0_2_3_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.3", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.WARNING),
				"内容未完成",
				"一些文本未被翻译成英文，并且元素使的第三个护甲技能未被添加"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.2.2的 Bug）：\n" +
						"_-_ 徽章贴图显示错误的问题"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16),
				Messages.get(ChangesScene.class, "bugfixes"),
				"尝试修复了（v0.1.0的 Bug）：\n" +
						"_-_ 英雄选择界面文本缺失的问题"));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHEESE),
				Messages.get(Cheese.class, "name"),
				"在老鼠王的房间会固定刷新一块。效果等价于全肉大饼。"));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BLUEBERRY),
				"蓝莓花园",
				"在下水道的每层（Boss层除外）固定刷新一个蓝莓花园（如果是大型层则两个），每个蓝莓花园会刷新1-2个可供采摘的蓝莓丛。蓝莓能回复少量饱食度。"));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.OVERPRICED),
				Messages.get(SmallRation.class, "name"),
				"在下水道的每层（Boss层除外）固定刷新一个小包口粮（如果是大型层则两个）。"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.GARDNER, 0, 0, 12, 16),
				Messages.get(Gardner.class, "name"),
				Messages.get(Gardner.class, "quest")));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.PLAGUEDR, 0, 0, 18, 16),
				Messages.get(PlagueDoctor.class, "name"),
				Messages.get(PlagueDoctor.class, "quest")));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.PATROL, 0, 0, 12, 16),
				Messages.get(Patrol.class, "name"),
				Messages.get(Patrol.class, "quest")));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.BARMAID, 0, 0, 16, 16),
				"酒馆",
				"0层的酒馆开张！"));

	}

	public static void add_v0_2_2_Changes( ArrayList<ChangeInfo> changeInfos ) {

		ChangeInfo changes = new ChangeInfo("v0.2.2", false, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.WARNING),
				"内容未完成",
				"一些文本未被翻译成英文，并且元素使的第三个护甲技能未被添加"));
		changes.addButton(new ChangeButton(Icons.get(Icons.DISPLAY), "界面改进",
				"_-_ 微调了游戏主界面的玩家信息显示\n" +
						 "_-_ 重制了安卓端的崩溃日志界面"));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.ELEMENTALIST, 0, 15, 12, 15),
				HeroClass.ELEMENTALIST.title(), HeroClass.ELEMENTALIST.unlockMsg()));
		changes.addButton(new ChangeButton(new Image(Assets.Sprites.ELEMENTALIST, 0, 90, 12, 15),
				Messages.get(WndHeroInfo.class, "subclasses"),
				"_" + HeroSubClass.BINDER.title() + "_\n\n" +
				HeroSubClass.BINDER.shortDesc() + "\n\n" +
				"_" + HeroSubClass.SPELLWEAVER.title() + "_\n\n" +
				HeroSubClass.SPELLWEAVER.shortDesc()));
		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_ELEMENTALIST),
				Messages.get(WndHeroInfo.class, "abilities"),
				"_" + Messages.get(Resonance.class, "name") + "_\n\n" +
				Messages.get(Resonance.class, "desc") + "\n\n" +
				"_" + Messages.get(AetherBlink.class, "name") + "_\n\n" +
				Messages.get(AetherBlink.class, "desc")));

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
