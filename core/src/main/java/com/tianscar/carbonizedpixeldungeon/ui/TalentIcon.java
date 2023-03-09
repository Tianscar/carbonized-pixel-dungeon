package com.tianscar.carbonizedpixeldungeon.ui;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.pixeldungeonclasses.noosa.Image;
import com.tianscar.pixeldungeonclasses.noosa.TextureFilm;

public class TalentIcon extends Image {

	private static TextureFilm film;
	private static final int SIZE = 16;

	public TalentIcon(Talent talent){
		this(talent.icon());
	}

	public TalentIcon(int icon){
		super( Assets.Interfaces.TALENT_ICONS );

		if (film == null) film = new TextureFilm(texture, SIZE, SIZE);

		frame(film.get(icon));
	}

}
