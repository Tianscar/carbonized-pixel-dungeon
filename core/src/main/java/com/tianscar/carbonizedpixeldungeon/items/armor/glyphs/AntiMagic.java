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

package com.tianscar.carbonizedpixeldungeon.items.armor.glyphs;

import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Charm;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Degrade;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Hex;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.MagicalSleep;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Vulnerable;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Weakness;
import com.tianscar.carbonizedpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.DM100;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Eye;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Shaman;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Warlock;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.YogFist;
import com.tianscar.carbonizedpixeldungeon.items.armor.Armor;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfBlastWave;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfDisintegration;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfFireblast;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfFrost;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfLightning;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfLivingEarth;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfMagicMissile;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfPrismaticLight;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfTransfusion;
import com.tianscar.carbonizedpixeldungeon.items.wands.WandOfWarding;
import com.tianscar.carbonizedpixeldungeon.levels.traps.DisintegrationTrap;
import com.tianscar.carbonizedpixeldungeon.levels.traps.GrimTrap;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.pixeldungeonclasses.utils.Random;

import java.util.HashSet;

public class AntiMagic extends Armor.Glyph {

	private static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x88EEFF );
	
	public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( MagicalSleep.class );
		RESISTS.add( Charm.class );
		RESISTS.add( Weakness.class );
		RESISTS.add( Vulnerable.class );
		RESISTS.add( Hex.class );
		RESISTS.add( Degrade.class );
		
		RESISTS.add( DisintegrationTrap.class );
		RESISTS.add( GrimTrap.class );

		RESISTS.add( WandOfBlastWave.class );
		RESISTS.add( WandOfDisintegration.class );
		RESISTS.add( WandOfFireblast.class );
		RESISTS.add( WandOfFrost.class );
		RESISTS.add( WandOfLightning.class );
		RESISTS.add( WandOfLivingEarth.class );
		RESISTS.add( WandOfMagicMissile.class );
		RESISTS.add( WandOfPrismaticLight.class );
		RESISTS.add( WandOfTransfusion.class );
		RESISTS.add( WandOfWarding.Ward.class );

		RESISTS.add( WarpBeacon.class );
		
		RESISTS.add( DM100.LightningBolt.class );
		RESISTS.add( Shaman.EarthenBolt.class );
		RESISTS.add( Warlock.DarkBolt.class );
		RESISTS.add( Eye.DeathGaze.class );
		RESISTS.add( YogFist.BrightFist.LightBeam.class );
		RESISTS.add( YogFist.DarkFist.DarkBolt.class );
	}
	
	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, see Hero.damage
		return damage;
	}
	
	public static int drRoll( int level ){
		return Random.NormalIntRange(level, 3 + Math.round(level*1.5f));
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return TEAL;
	}

}