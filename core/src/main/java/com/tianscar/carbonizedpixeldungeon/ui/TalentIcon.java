package com.tianscar.carbonizedpixeldungeon.ui;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Talent;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.TextureFilm;

public class TalentIcon extends Image {

	private static TextureFilm film;
	private static final int SIZE = 16;

	public TalentIcon( Talent talent ) {
		this(talent.icon(), false);
	}

	public TalentIcon( int icon ) {
		this(icon, false);
	}

	public TalentIcon( int icon, boolean old ) {
		super( old ? Assets.Interfaces.TALENT_ICONS_OLD : Assets.Interfaces.TALENT_ICONS );

		if (film == null) film = new TextureFilm(texture, SIZE, SIZE);

		frame(film.get(icon));
	}

}
