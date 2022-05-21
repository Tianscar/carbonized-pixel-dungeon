package com.ansdoship.carbonizedpixeldungeon.windows;

import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.ui.HeroIcon;
import com.ansdoship.carbonizedpixeldungeon.ui.TalentsPane;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndInfoSubclass extends WndTitledMessage {

	public WndInfoSubclass(HeroClass cls, HeroSubClass subCls){
		super( new HeroIcon(subCls), Messages.titleCase(subCls.title()), subCls.desc());

		ArrayList<LinkedHashMap<Talent, Integer>> talentList = new ArrayList<>();
		Talent.initClassTalents(cls, talentList);
		Talent.initSubclassTalents(subCls, talentList);

		TalentsPane.TalentTierPane talentPane = new TalentsPane.TalentTierPane(talentList.get(2), 3, false);
		talentPane.title.text( Messages.titleCase(Messages.get(WndHeroInfo.class, "talents")));
		talentPane.setRect(0, height + 5, width, talentPane.height());
		add(talentPane);
		resize(width, (int) talentPane.bottom());

	}

}
