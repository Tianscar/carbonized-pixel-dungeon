package com.tianscar.carbonizedpixeldungeon.ui;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.TextureFilm;

//icons for hero subclasses and abilities atm, maybe add classes?
public class HeroIcon extends Image {

	private static TextureFilm film;
	private static final int SIZE = 16;

	//transparent icon
	public static final int NONE    = 31;

	//subclasses
	public static final int BERSERKER   = 0;
	public static final int GLADIATOR   = 1;
	public static final int BATTLEMAGE  = 2;
	public static final int WARLOCK     = 3;
	public static final int ASSASSIN    = 4;
	public static final int FREERUNNER  = 5;
	public static final int SNIPER      = 6;
	public static final int WARDEN      = 7;
	public static final int BINDER      = 16;
	public static final int SPELLWEAVER = 17;

	//abilities
	public static final int HEROIC_LEAP     = 32;
	public static final int SHOCKWAVE       = 33;
	public static final int ENDURE          = 34;
	public static final int ELEMENTAL_BLAST = 35;
	public static final int WILD_MAGIC      = 36;
	public static final int WARP_BEACON     = 37;
	public static final int SMOKE_BOMB      = 38;
	public static final int DEATH_MARK      = 39;
	public static final int SHADOW_CLONE    = 40;
	public static final int SPECTRAL_BLADES = 41;
	public static final int NATURES_POWER   = 42;
	public static final int SPIRIT_HAWK     = 43;
	public static final int RESONANCE       = 56;
	public static final int AETHER_BLINK    = 57;
	public static final int ELEMENTAL_CONDUIT = 58;
	public static final int RATMOGRIFY      = 79;

	public HeroIcon(HeroSubClass subCls){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(subCls.icon()));
	}

	public HeroIcon(ArmorAbility abil){
		super( Assets.Interfaces.HERO_ICONS );
		if (film == null){
			film = new TextureFilm(texture, SIZE, SIZE);
		}
		frame(film.get(abil.icon()));
	}

}
