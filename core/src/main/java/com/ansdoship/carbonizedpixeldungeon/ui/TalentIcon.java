package com.ansdoship.carbonizedpixeldungeon.ui;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Talent;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.TextureFilm;

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
