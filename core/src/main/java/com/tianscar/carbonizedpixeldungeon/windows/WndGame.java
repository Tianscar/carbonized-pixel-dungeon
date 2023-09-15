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

package com.tianscar.carbonizedpixeldungeon.windows;

import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.GamesInProgress;
import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.*;
import com.tianscar.carbonizedpixeldungeon.services.updates.Updates;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.RedButton;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;

import java.io.IOException;

public class WndGame extends Window {

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP		= 2;
	
	private int pos;
	
	public WndGame() {
		
		super();

		//settings
		RedButton curBtn;
		addButton( curBtn = new RedButton( Messages.get(this, "settings") ) {
			@Override
			protected void onClick() {
				//hide();
				GameScene.show(new WndSettings());
			}
		});
		curBtn.icon(Icons.get(Icons.PREFS));

		//zoom
		addZoomButtons();

		//install prompt
		if (Updates.isInstallable()){
			addButton( curBtn = new RedButton( Messages.get(this, "install") ) {
				@Override
				protected void onClick() {
					Updates.launchInstall();
				}
			} );
			curBtn.textColor(Window.SHPX_COLOR);
			curBtn.icon(Icons.get(Icons.CHANGES));
		}

		// Challenges window
		if (Dungeon.challenges > 0) {
			addButton( curBtn = new RedButton( Messages.get(this, "challenges") ) {
				@Override
				protected void onClick() {
					//hide();
					GameScene.show( new WndChallenges( Dungeon.challenges, false ) );
				}
			} );
			curBtn.icon(Icons.get(Icons.CHALLENGE_ON));
		}

		// Restart
		if (Dungeon.hero == null || !Dungeon.hero.isAlive()) {

			addButton( curBtn = new RedButton( Messages.get(this, "start") ) {
				@Override
				protected void onClick() {
					InterlevelScene.noStory = true;
					GamesInProgress.selectedClass = Dungeon.hero.heroClass;
					GamesInProgress.curSlot = GamesInProgress.firstEmpty();
					CarbonizedPixelDungeon.switchScene(HeroSelectScene.class);
				}
			} );
			curBtn.icon(Icons.get(Icons.ENTER));
			curBtn.textColor(Window.TITLE_COLOR);
			
			addButton( curBtn = new RedButton( Messages.get(this, "rankings") ) {
				@Override
				protected void onClick() {
					InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
					Game.switchScene( RankingsScene.class );
				}
			} );
			curBtn.icon(Icons.get(Icons.RANKINGS));
		}

		// Main menu
		addButton(curBtn = new RedButton( Messages.get(this, "menu") ) {
			@Override
			protected void onClick() {
				try {
					Dungeon.saveAll();
				} catch (IOException e) {
					CarbonizedPixelDungeon.reportException(e);
				}
				Game.switchScene(TitleScene.class);
			}
		} );
		curBtn.icon(Icons.get(Icons.DISPLAY));

		resize( WIDTH, pos );
	}
	
	private void addButton( RedButton btn ) {
		add( btn );
		btn.setRect( 0, pos > 0 ? pos += GAP : 0, WIDTH, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}

	private void addButtons( RedButton btn1, RedButton btn2 ) {
		add( btn1 );
		btn1.setRect( 0, pos > 0 ? pos += GAP : 0, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btn2 );
		btn2.setRect( btn1.right() + GAP, btn1.top(), WIDTH - btn1.right() - GAP, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}

	private void addZoomButtons() {
		RedButton btn1 = new RedButton("") {
			@Override
			protected void onClick() {
				GameScene.zoom(GameScene.zoom() - 1f);
			}
		};
			btn1.icon(Icons.get(Icons.MINUS_LRG));
		add( btn1 );
		btn1.setRect( 0, pos > 0 ? pos += GAP : 0, BTN_HEIGHT, BTN_HEIGHT );
		RedButton btn2 = new RedButton("") {
			@Override
			protected void onClick() {
				GameScene.zoom(GameScene.zoom() + 1f);
			}
		};
		btn2.icon(Icons.get(Icons.PLUS_LRG));
		add( btn2 );
		btn2.setRect( WIDTH - BTN_HEIGHT, btn1.top(), BTN_HEIGHT, BTN_HEIGHT );
		RedButton btn3 = new RedButton( Messages.get(this, "zoom") ) {
			@Override
			protected void onClick() {
				GameScene.zoom(PixelScene.defaultZoom);
			}
		};
		add( btn3 );
		btn3.setRect( btn1.right() + GAP, btn1.top(), WIDTH - BTN_HEIGHT*2 - GAP*2, BTN_HEIGHT );
		pos += BTN_HEIGHT;
	}

}
