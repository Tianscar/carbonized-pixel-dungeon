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

package com.tianscar.carbonizedpixeldungeon.scenes;

import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.effects.Flare;
import com.tianscar.carbonizedpixeldungeon.ui.Archs;
import com.tianscar.carbonizedpixeldungeon.ui.ExitButton;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.carbonizedpixeldungeon.ui.ScrollPane;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.pixeldungeonclasses.input.PointerEvent;
import com.tianscar.pixeldungeonclasses.noosa.*;
import com.tianscar.pixeldungeonclasses.noosa.ui.Component;

public class AboutScene extends PixelScene {

	@Override
	public void create() {
		super.create();

		final float colWidth = 120;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		//darkens the arches
		add(new ColorBlock(w, h, 0x88000000));

		ScrollPane list = new ScrollPane( new Component() );
		add( list );

		Component content = list.content();
		content.clear();

		//*** Carbonized Pixel Dungeon Credits ***

		final int TIANS_COLOR = 0xBBBBBB;
		CreditsBlock tians = new CreditsBlock(true, TIANS_COLOR,
				"Carbonized Pixel Dungeon",
				Icons.get(Icons.CAPD),
				"Developed by: _Karstian Lee_\nBased on ShatteredPD's open source",
				"carbonizedpd.tianscar.com",
				"https://carbonizedpd.tianscar.com");
		tians.setRect((w - colWidth)/2f, 7, 120, 0);
		content.add(tians);

		CreditsBlock daniel = new CreditsBlock(false, TIANS_COLOR,
				"Art:",
				Icons.get(Icons.DANIEL),
				"Daniel Calan",
				null,
				null);
		daniel.setRect(w/2f - colWidth/4f, tians.bottom()+5, colWidth/2f, 0);
		content.add(daniel);

		CreditsBlock ptr = new CreditsBlock(false, TIANS_COLOR,
				"Music:",
				Icons.PTR.get(),
				"Progressive Tune",
				"progressive-tune.github.io",
				"https://progressive-tune.github.io/ptr", true);
		ptr.setSize(colWidth/2f, 0);
		CreditsBlock jason = new CreditsBlock(false, TIANS_COLOR,
				null,
				null,
				"Jasφn",
				"music.163.com/artist/...",
				"https://music.163.com/artist?id=48991962&userid=3270966253");
		jason.setSize(colWidth/2f, 0);
		ptr.setRect(w / 2f - colWidth/4f, daniel.bottom() + 5, colWidth/2f, 0);
		jason.setRect(w / 2f - colWidth/4f, ptr.bottom() + 5, colWidth/2f, 0);
		content.add(ptr);
		content.add(jason);

		//*** Shattered Pixel Dungeon Credits ***

		String shpxLink = "https://ShatteredPixel.com";
		//tracking codes, so that the website knows where this pageview came from
		shpxLink += "?utm_source=shatteredpd";
		shpxLink += "&utm_medium=about_page";
		shpxLink += "&utm_campaign=ingame_link";

		CreditsBlock shpx = new CreditsBlock(true, Window.SHPX_COLOR,
				"Shattered Pixel Dungeon",
				Icons.SHPX.get(),
				"Developed by: _Evan Debenham_\nBased on Pixel Dungeon's open source",
				"ShatteredPixel.com",
				shpxLink);
		shpx.setRect(tians.left(), jason.bottom() + 12, colWidth, 0);
		content.add(shpx);

		CreditsBlock alex = new CreditsBlock(false, Window.SHPX_COLOR,
				"Hero Art & Design:",
				Icons.ALEKS.get(),
				"Aleksandar Komitov",
				"alekskomitov.com",
				"https://www.alekskomitov.com");
		alex.setSize(colWidth/2f, 0);
		alex.setPos(w/2f - colWidth/2f, shpx.bottom()+5);
		content.add(alex);

		addLine(shpx.top() - 4, content);

		CreditsBlock celesti = new CreditsBlock(false, Window.SHPX_COLOR,
				"Sound Effects:",
				Icons.CELESTI.get(),
				"Celesti",
				"s9menine.itch.io",
				"https://s9menine.itch.io");
		celesti.setRect(alex.right(), alex.top(), colWidth/2f, 0);
		content.add(celesti);

		CreditsBlock kristjan = new CreditsBlock(false, Window.SHPX_COLOR,
				"Music:",
				Icons.KRISTJAN.get(),
				"Kristjan Haaristo",
				"youtube.com/user/...",
				"https://www.youtube.com/channel/UCL1e7SgzSWbD_DQxB_5YcLA");
		kristjan.setRect(alex.right() - colWidth/4f, alex.bottom() + 5, colWidth/2f, 0);
		content.add(kristjan);

		//*** Pixel Dungeon Credits ***

		final int WATA_COLOR = 0x55AAFF;
		CreditsBlock wata = new CreditsBlock(true, WATA_COLOR,
				"Pixel Dungeon",
				Icons.WATABOU.get(),
				"Developed by: _Watabou_\nInspired by Brian Walker's Brogue",
				"pixeldungeon.watabou.ru",
				"http://pixeldungeon.watabou.ru");
		wata.setRect(shpx.left(), kristjan.bottom() + 12, colWidth, 0);
		content.add(wata);

		addLine(wata.top() - 4, content);

		CreditsBlock cube = new CreditsBlock(false, WATA_COLOR,
				"Music:",
				Icons.CUBE_CODE.get(),
				"Cube\\_Code",
				null,
				null);
		cube.setSize(colWidth/2f, 0);
		cube.setPos(alex.left() + colWidth/4f, wata.bottom()+5);
		content.add(cube);

		//*** libGDX Credits ***

		final int GDX_COLOR = 0xE44D3C;
		CreditsBlock gdx = new CreditsBlock(true,
				GDX_COLOR,
				"libGDX",
				Icons.LIBGDX.get(),
				"ShatteredPD is powered by _libGDX_!",
				"libGDX.com",
				"https://libGDX.com/");
		gdx.setRect(wata.left(), cube.bottom() + 12, colWidth, 0);
		content.add(gdx);

		addLine(gdx.top() - 4, content);

		CreditsBlock arcnor = new CreditsBlock(false, GDX_COLOR,
				"Pixel Dungeon GDX:",
				Icons.ARCNOR.get(),
				"Edu García",
				"twitter.com/arcnor",
				"https://twitter.com/arcnor");
		arcnor.setSize(colWidth/2f, 0);
		arcnor.setPos(alex.left(), gdx.bottom()+5);
		content.add(arcnor);

		CreditsBlock purigro = new CreditsBlock(false, GDX_COLOR,
				"Shattered GDX Help:",
				Icons.PURIGRO.get(),
				"Kevin MacMartin",
				"github.com/prurigro",
				"https://github.com/prurigro/");
		purigro.setRect(arcnor.right()+2, arcnor.top(), colWidth/2f, 0);
		content.add(purigro);

		//*** Transifex Credits ***

		CreditsBlock transifex = new CreditsBlock(true,
				Window.TITLE_COLOR,
				null,
				null,
				"Shattered Pixel Dungeon is community-translated via _Transifex_! Thank you so much to all of Shattered's volunteer translators!",
				"www.transifex.com/shattered-pixel/",
				"https://www.transifex.com/shattered-pixel/shattered-pixel-dungeon/");
		transifex.setRect((Camera.main.width - colWidth)/2f, purigro.bottom() + 12, colWidth, 0);
		content.add(transifex);

		addLine(transifex.top() - 4, content);

		//*** Freesound Credits ***

		CreditsBlock freesound = new CreditsBlock(true,
				Window.TITLE_COLOR,
				null,
				null,
				"Carbonized Pixel Dungeon uses the following sound samples from _freesound.org_:\n\n" +

				"Creative Commons Attribution License:\n" +
				"_SFX ATTACK SWORD 001.wav_ by _JoelAudio_\n" +
				"_Pack: Slingshots and Longbows_ by _saturdaysoundguy_\n" +
				"_Cracking/Crunching, A.wav_ by _InspectorJ_\n" +
				"_Extracting a sword.mp3_ by _Taira Komori_\n" +
				"_Pack: Uni Sound Library_ by _timmy h123_\n\n" +

				"Creative Commons Zero License:\n" +
				"_Pack: Movie Foley: Swords_ by _Black Snow_\n" +
				"_machine gun shot 2.flac_ by _qubodup_\n" +
				"_m240h machine gun burst 4.flac_ by _qubodup_\n" +
				"_Pack: Onomatopoeia_ by _Adam N_\n" +
				"_Pack: Watermelon_ by _lolamadeus_\n" +
				"_metal chain_ by _Mediapaja2009_\n" +
				"_Pack: Sword Clashes Pack_ by _JohnBuhr_\n" +
				"_Pack: Metal Clangs and Pings_ by _wilhellboy_\n" +
				"_Pack: Stabbing Stomachs & Crushing Skulls_ by _TheFilmLook_\n" +
				"_Sheep bleating_ by _zachrau_\n" +
				"_Lemon,Juicy,Squeeze,Fruit.wav_ by _Filipe Chagas_\n" +
				"_Lemon,Squeeze,Squishy,Fruit.wav_ by _Filipe Chagas_",
				"www.freesound.org",
				"https://www.freesound.org");
		freesound.setRect(transifex.left()-10, transifex.bottom() + 12, colWidth+20, 0);
		content.add(freesound);

		addLine(freesound.top() - 4, content);

		//*** Maoken Credits ***

		CreditsBlock maoken = new CreditsBlock(true,
				Window.TITLE_COLOR,
				null,
				null,
				"Carbonized Pixel Dungeon uses the following fonts from _maoken.com_:\n\n" +

						"SIL Open Font License:\n" +
						"_fusion-pixel.ttf_ by _TakWolf_",
				"www.maoken.com",
				"https://www.maoken.com");
		maoken.setRect(transifex.left()-10, freesound.bottom() + 12, colWidth+20, 0);
		content.add(maoken);

		addLine(maoken.top() - 4, content);

		content.setSize( colWidth, maoken.bottom()+10 );

		list.setRect( 0, 0, w, h );
		list.scrollTo(0, 0);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		fadeIn();
	}
	
	@Override
	public void onBackPressed() {
		CarbonizedPixelDungeon.switchScene(TitleScene.class);
	}

	private void addLine( float y, Group content ){
		ColorBlock line = new ColorBlock(Camera.main.width, 1, 0xFF333333);
		line.y = y;
		content.add(line);
	}

	private static class CreditsBlock extends Component {

		boolean large;
		boolean bodyAlign;
		RenderedTextBlock title;
		Image avatar;
		Flare flare;
		RenderedTextBlock body;

		RenderedTextBlock link;
		ColorBlock linkUnderline;
		PointerArea linkButton;

		private CreditsBlock(boolean large, int highlight, String title, Image avatar, String body, String linkText, String linkUrl) {
			this(large, highlight, title, avatar, body, linkText, linkUrl, false);
		}

		//many elements can be null, but body is assumed to have content.
		private CreditsBlock(boolean large, int highlight, String title, Image avatar, String body, String linkText, String linkUrl,
							 boolean bodyAlign) {
			super();

			this.large = large;
			this.bodyAlign = bodyAlign;

			if (title != null) {
				this.title = PixelScene.renderTextBlock(title, large ? 8 : 6);
				if (highlight != -1) this.title.hardlight(highlight);
				add(this.title);
			}

			if (avatar != null){
				this.avatar = avatar;
				add(this.avatar);
			}

			if (large && highlight != -1 && this.avatar != null){
				this.flare = new Flare( 7, 24 ).color( highlight, true ).show(this.avatar, 0);
				this.flare.angularSpeed = 20;
			}

			this.body = PixelScene.renderTextBlock(body, 6);
			if (highlight != -1) this.body.setHightlighting(true, highlight);
			if (large || bodyAlign) this.body.align(RenderedTextBlock.CENTER_ALIGN);
			add(this.body);

			if (linkText != null && linkUrl != null){

				int color = 0xFFFFFFFF;
				if (highlight != -1) color = 0xFF000000 | highlight;
				this.linkUnderline = new ColorBlock(1, 1, color);
				add(this.linkUnderline);

				this.link = PixelScene.renderTextBlock(linkText, 6);
				if (highlight != -1) this.link.hardlight(highlight);
				add(this.link);

				linkButton = new PointerArea(0, 0, 0, 0){
					@Override
					protected void onClick( PointerEvent event ) {
						Game.platform.openURI( linkUrl );
					}
				};
				add(linkButton);
			}

		}

		@Override
		protected void layout() {
			super.layout();

			float topY = top();

			if (title != null){
				title.maxWidth((int)width());
				title.setPos( x + (width() - title.width())/2f, topY);
				topY += title.height() + (large ? 2 : 1);
			}

			if (large || bodyAlign){

				if (avatar != null){
					avatar.x = x + (width()-avatar.width())/2f;
					avatar.y = topY;
					PixelScene.align(avatar);
					if (flare != null){
						flare.point(avatar.center());
					}
					topY = avatar.y + avatar.height() + 2;
				}

				body.maxWidth((int)width());
				body.setPos( x + (width() - body.width())/2f, topY);
				topY += body.height() + 2;

			} else {

				if (avatar != null){
					avatar.x = x;
					body.maxWidth((int)(width() - avatar.width - 1));

					if (avatar.height() > body.height()){
						avatar.y = topY;
						body.setPos( avatar.x + avatar.width() + 1, topY + (avatar.height() - body.height())/2f);
						topY += avatar.height() + 1;
					} else {
						avatar.y = topY + (body.height() - avatar.height())/2f;
						PixelScene.align(avatar);
						body.setPos( avatar.x + avatar.width() + 1, topY);
						topY += body.height() + 2;
					}

				} else {
					topY += 1;
					body.maxWidth((int)width());
					body.setPos( x, topY);
					topY += body.height()+2;
				}

			}

			if (link != null){
				if (large) topY += 1;
				link.maxWidth((int)width());
				link.setPos( x + (width() - link.width())/2f, topY);
				topY += link.height() + 2;

				linkButton.x = link.left()-1;
				linkButton.y = link.top()-1;
				linkButton.width = link.width()+2;
				linkButton.height = link.height()+2;

				linkUnderline.size(link.width(), PixelScene.align(0.49f));
				linkUnderline.x = link.left();
				linkUnderline.y = link.bottom()+1;

			}

			topY -= 2;

			height = Math.max(height, topY - top());
		}
	}
}
