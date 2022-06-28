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

package com.ansdoship.carbonizedpixeldungeon.windows;

import com.ansdoship.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.GamesInProgress;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.Hero;
import com.ansdoship.carbonizedpixeldungeon.actors.hero.HeroSubClass;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.InterlevelScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.PixelScene;
import com.ansdoship.carbonizedpixeldungeon.scenes.StartScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.HeroSprite;
import com.ansdoship.carbonizedpixeldungeon.ui.*;
import com.ansdoship.pixeldungeonclasses.noosa.ColorBlock;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.ui.Button;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.FileUtils;

import java.io.IOException;
import java.util.Locale;

public class WndGameInProgress extends Window {
	
	private static final int WIDTH    = 120;
	
	private int GAP	  = 4;
	
	private float pos;
	
	public WndGameInProgress(final int slot){
		
		final GamesInProgress.Info info = GamesInProgress.check(slot);
		
		String className = null;
		if (info.subClass != HeroSubClass.NONE){
			className = info.subClass.title();
		} else {
			className = info.heroClass.title();
		}
		
		IconTitle title = new IconTitle();
		title.icon( HeroSprite.avatar(info.heroClass, info.armorTier) );
		title.label((Messages.get(this, "title", info.level, className)).toUpperCase(Locale.ENGLISH));
		title.color(Window.TITLE_COLOR);
		title.setRect( 0, 0, WIDTH, 0 );
		add(title);
		
		//manually produces debug information about a run, mainly useful for levelgen errors
		Button debug = new Button(){
			@Override
			protected boolean onLongClick() {
				try {
					Bundle bundle = FileUtils.bundleFromFile(GamesInProgress.gameFile(slot));
					CarbonizedPixelDungeon.scene().addToFront(new WndMessage("_Debug Info:_\n\n" +
							"Version: " + Game.version + " (" + Game.versionCode + ")\n" +
							"Seed: " + bundle.getLong("seed") + "\n" +
							"Challenge Mask: " + info.challenges));
				} catch (IOException ignored) { }
				return true;
			}
		};
		debug.setRect(0, 0, title.imIcon.width(), title.imIcon.height);
		add(debug);
		
		if (info.challenges > 0) {
			IconButton btnChallenges = new IconButton( Icons.get(Icons.CHALLENGE_ON) ) {
				@Override
				protected void onClick() {
					Game.scene().add( new WndChallenges( info.challenges, false ) );
				}

				@Override
				protected String hoverText() {
					return Messages.titleCase(Messages.get(WndGameInProgress.this, "challenges"));
				}
			};
			btnChallenges.setRect( WIDTH - 16, pos, 16, 16 );
			title.setSize(title.width() - 16, title.height());
			add( btnChallenges );
		}

		ColorBlock sep1 = new ColorBlock(1, 1, 0xFF000000);
		sep1.size(WIDTH, 1);
		sep1.y = title.bottom() + 2;
		add(sep1);

		pos = sep1.y + GAP;

		int strBonus = info.STRBonus;
		if (strBonus > 0)           statSlot( Messages.get(this, "str"), info.STR + " + " + strBonus );
		else if (strBonus < 0)      statSlot( Messages.get(this, "str"), info.STR + " - " + -strBonus );
		else                        statSlot( Messages.get(this, "str"), info.STR );
		statSlot( Messages.get(this, "con"), info.CON );
		statSlot( Messages.get(this, "dex"), info.DEX );
		statSlot( Messages.get(this, "int"), info.INT );
		statSlot( Messages.get(this, "wis"), info.WIS );
		statSlot( Messages.get(this, "cha"), info.CHA );
		if (info.shld > 0)  statSlot( Messages.get(this, "health"), info.HP + "+" + info.shld + "/" + info.HT );
		else                statSlot( Messages.get(this, "health"), (info.HP) + "/" + info.HT );
		statSlot( Messages.get(this, "hunger"), info.hunger + "/" + Hero.MAX_HUNGER );
		statSlot( Messages.get(this, "exp"), info.exp + "/" + Hero.maxExp(info.level) );
		
		pos += GAP;
		statSlot( Messages.get(this, "gold"), info.goldCollected );
		statSlot( Messages.get(this, "depth"), info.maxDepth );

		ColorBlock sep2 = new ColorBlock(1, 1, 0xFF000000);
		sep2.size(WIDTH, 1);
		sep2.y = pos;
		add(sep2);

		pos += GAP;
		
		RedButton cont = new RedButton(Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				
				GamesInProgress.curSlot = slot;
				
				Dungeon.hero = null;
				ActionIndicator.action = null;
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				CarbonizedPixelDungeon.switchScene(InterlevelScene.class);
			}
		};
		
		RedButton erase = new RedButton( Messages.get(this, "erase")){
			@Override
			protected void onClick() {
				super.onClick();
				
				CarbonizedPixelDungeon.scene().add(new WndOptions(Icons.get(Icons.WARNING),
						Messages.get(WndGameInProgress.class, "erase_warn_title"),
						Messages.get(WndGameInProgress.class, "erase_warn_body"),
						Messages.get(WndGameInProgress.class, "erase_warn_yes"),
						Messages.get(WndGameInProgress.class, "erase_warn_no") ) {
					@Override
					protected void onSelect( int index ) {
						if (index == 0) {
							FileUtils.deleteDir(GamesInProgress.gameFolder(slot));
							GamesInProgress.setUnknown(slot);
							CarbonizedPixelDungeon.switchNoFade(StartScene.class);
						}
					}
				} );
			}
		};

		cont.icon(Icons.get(Icons.ENTER));
		cont.setRect(0, pos, WIDTH/2 -1, 20);
		add(cont);

		erase.icon(Icons.get(Icons.CLOSE));
		erase.setRect(WIDTH/2 + 1, pos, WIDTH/2 - 1, 20);
		add(erase);
		
		resize(WIDTH, (int)cont.bottom()+1);
	}
	
	private void statSlot( String label, String value ) {
		
		RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
		txt.setPos(0, pos);
		add( txt );
		
		txt = PixelScene.renderTextBlock( value, 7 );
		txt.setPos(WIDTH * 0.6f, pos);
		PixelScene.align(txt);
		add( txt );
		
		pos += GAP + txt.height();
	}
	
	private void statSlot( String label, int value ) {
		statSlot( label, Integer.toString( value ) );
	}
}
