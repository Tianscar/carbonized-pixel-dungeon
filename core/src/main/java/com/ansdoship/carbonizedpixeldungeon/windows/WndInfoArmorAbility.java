package com.ansdoship.carbonizedpixeldungeon.windows;

import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.ui.HeroIcon;
import com.ansdoship.carbonizedpixeldungeon.ui.TalentsPane;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WndInfoArmorAbility extends WndTitledMessage {

	public WndInfoArmorAbility(HeroClass cls, ArmorAbility ability){
		super( new HeroIcon(ability), Messages.titleCase(ability.name()), ability.desc());

		ArrayList<LinkedHashMap<Talent, Integer>> talentList = new ArrayList<>();
		Talent.initArmorTalents(ability, talentList);

		TalentsPane.TalentTierPane talentPane = new TalentsPane.TalentTierPane(talentList.get(3), 4, false);
		talentPane.title.text( Messages.titleCase(Messages.get(WndHeroInfo.class, "talents")));
		talentPane.setRect(0, height + 5, width, talentPane.height());
		add(talentPane);
		resize(width, (int) talentPane.bottom());

	}

}
