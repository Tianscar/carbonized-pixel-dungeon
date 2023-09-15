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

import com.tianscar.carbonizedpixeldungeon.Chrome;
import com.tianscar.carbonizedpixeldungeon.PDSettings;
import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.messages.Languages;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.services.news.News;
import com.tianscar.carbonizedpixeldungeon.services.news.NewsArticle;
import com.tianscar.carbonizedpixeldungeon.sprites.CharSprite;
import com.tianscar.carbonizedpixeldungeon.ui.Archs;
import com.tianscar.carbonizedpixeldungeon.ui.ExitButton;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.RedButton;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.carbonizedpixeldungeon.ui.StyledButton;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.windows.WndTitledMessage;
import com.tianscar.carbonizedpixeldungeon.noosa.BitmapText;
import com.tianscar.carbonizedpixeldungeon.noosa.Camera;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.noosa.NinePatch;
import com.tianscar.carbonizedpixeldungeon.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Calendar;

public class NewsScene extends PixelScene {

	boolean displayingNoArticles = false;

	private static final int BTN_HEIGHT = 22;
	private static final int BTN_WIDTH = 100;

	@Override
	public void create() {
		super.create();

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		int fullWidth = PixelScene.landscape() ? 202 : 100;
		int left = (w - fullWidth)/2;

		Archs archs = new Archs();
		archs.setSize(w, h);
		add(archs);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos(w - btnExit.width(), 0);
		add(btnExit);

		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(w - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);

		float top = 18;

		displayingNoArticles = !News.articlesAvailable();
		if (displayingNoArticles || Messages.lang() != Languages.SI_CHINESE) {

			Component newsInfo = new NewsInfo();
			newsInfo.setRect(left, 20, fullWidth, 0);
			add(newsInfo);

			top = newsInfo.bottom();

		}

		if (!displayingNoArticles) {
			ArrayList<NewsArticle> articles = News.articles();

			float articleSpace = h - top - 2;
			int rows = articles.size();
			if (PixelScene.landscape()){
				rows /= 2;
			}
			rows++;

			while ((articleSpace) / (BTN_HEIGHT+0.5f) < rows) {
				articles.remove(articles.size() - 1);
				if (PixelScene.landscape()) {
					articles.remove(articles.size() - 1);
				}
				rows--;
			}

			float gap = ((articleSpace) - (BTN_HEIGHT * rows)) / (float)rows;

			boolean rightCol = false;
			for (NewsArticle article : articles) {
				StyledButton b = new ArticleButton(article);
				b.multiline = true;
				if (!rightCol) {
					top += gap;
					b.setRect( left, top, BTN_WIDTH, BTN_HEIGHT);
				} else {
					b.setRect( left + fullWidth - BTN_WIDTH, top, BTN_WIDTH, BTN_HEIGHT);
				}
				align(b);
				add(b);
				if (!PixelScene.landscape()) {
					top += BTN_HEIGHT;
				} else {
					if (rightCol){
						top += BTN_HEIGHT;
					}
					rightCol = !rightCol;
				}
			}
			top += gap;
		} else {
			top += 20;
		}

		StyledButton btnSite = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "read_more")){
			@Override
			protected void onClick() {
				super.onClick();
				Game.platform.openURI("https://carbonizedpd.tianscar.com");
			}
		};
		btnSite.icon(Icons.get(Icons.NEWS));
		btnSite.textColor(Window.TITLE_COLOR);
		btnSite.setRect(left, top, fullWidth, BTN_HEIGHT);
		add(btnSite);

	}

	@Override
	public void onBackPressed() {
		CarbonizedPixelDungeon.switchNoFade( TitleScene.class );
	}

	@Override
	public void update() {
		if (displayingNoArticles && News.articlesAvailable()){
			CarbonizedPixelDungeon.seamlessResetScene();
		}
		super.update();
	}

	private static class NewsInfo extends Component {

		NinePatch bg;
		RenderedTextBlock text;
		RedButton button;

		@Override
		protected void createChildren() {
			bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
			add(bg);
			
			String message = "";

			if (Messages.lang() != Languages.SI_CHINESE){
				message += Messages.get(this, "chinese_warn");
			}
			
			if (!News.articlesAvailable()){
				if (PDSettings.news()) {
					if (PDSettings.WiFi() && !Game.platform.connectedToUnmeteredNetwork()) {
						message += "\n\n" + Messages.get(this, "metered_network");

						button = new RedButton(Messages.get(this, "enable_data")) {
							@Override
							protected void onClick() {
								super.onClick();
								PDSettings.WiFi(false);
								News.checkForNews();
								CarbonizedPixelDungeon.seamlessResetScene();
							}
						};
						add(button);
					} else {
						message += "\n\n" + Messages.get(this, "no_internet");
					}
				} else {
					message += "\n\n" + Messages.get(this, "news_disabled");

					button = new RedButton(Messages.get(this, "enable_news")) {
						@Override
						protected void onClick() {
							super.onClick();
							PDSettings.news(true);
							News.checkForNews();
							CarbonizedPixelDungeon.seamlessResetScene();
						}
					};
					add(button);
				}
			}

			if (message.startsWith("\n\n")) message = message.replaceFirst("\n\n", "");
			
			text = PixelScene.renderTextBlock(message, 6);
			text.hardlight(CharSprite.WARNING);
			add(text);
		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;

			text.maxWidth((int)width - bg.marginHor());
			text.setPos(x + bg.marginLeft(), y + bg.marginTop()+1);

			height = (text.bottom()) - y;

			if (button != null){
				height += 4;
				button.multiline = true;
				button.setSize(width - bg.marginHor(), 16);
				button.setSize(width - bg.marginHor(), Math.max(button.reqHeight(), 16));
				button.setPos(x + (width - button.width())/2, y + height);
				height = button.bottom() - y;
			}

			height += bg.marginBottom() + 1;

			bg.size(width, height);

		}
	}

	private static class ArticleButton extends StyledButton {

		NewsArticle article;

		BitmapText date;

		public ArticleButton(NewsArticle article) {
			super(Chrome.Type.GREY_BUTTON_TR, article.title, 6);
			this.article = article;

			icon(News.parseArticleIcon(article));
			long lastRead = PDSettings.newsLastRead();
			if (lastRead > 0 && article.date.getTime() > lastRead) {
				textColor(Window.SHPX_COLOR);
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(article.date);
			date = new BitmapText( News.parseArticleDate(article), pixelFont);
			date.scale.set(PixelScene.align(0.5f));
			date.hardlight( 0x888888 );
			date.measure();
			add(date);
		}

		@Override
		protected void layout() {
			super.layout();

			icon.x = x + bg.marginLeft() + (16-icon.width())/2f;
			PixelScene.align(icon);
			text.setPos(x + bg.marginLeft() + 18, text.top());

			if (date != null) {
				date.x = x + width - bg.marginRight() - date.width() + 1;
				date.y = y + height - bg.marginBottom() - date.height() + 2.5f;
				align(date);
			}
		}

		@Override
		protected void onClick() {
			super.onClick();
			textColor(Window.WHITE);
			if (article.date.getTime() > PDSettings.newsLastRead()){
				PDSettings.newsLastRead(article.date.getTime());
			}
			CarbonizedPixelDungeon.scene().addToFront(new WndArticle(article));
		}
	}

	private static class WndArticle extends WndTitledMessage {

		public WndArticle(NewsArticle article ) {
			super(News.parseArticleIcon(article), article.title, article.summary);

			RedButton link = new RedButton(Messages.get(NewsScene.class, "read_more")){
				@Override
				protected void onClick() {
					super.onClick();
					Game.platform.openURI(article.URL);
				}
			};
			link.setRect(0, height + 2, width, BTN_HEIGHT);
			add(link);
			resize(width, (int) link.bottom());
		}


	}

}
