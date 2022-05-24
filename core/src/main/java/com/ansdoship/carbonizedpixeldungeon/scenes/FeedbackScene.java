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

package com.ansdoship.carbonizedpixeldungeon.scenes;

import com.ansdoship.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.ansdoship.carbonizedpixeldungeon.Chrome;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.ui.*;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.NinePatch;
import com.ansdoship.pixeldungeonclasses.noosa.ui.Component;

public class FeedbackScene extends PixelScene {

	private static final int BTN_HEIGHT = 20;
	private static final int GAP = 2;

	@Override
	public void create() {
		super.create();

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		int elementWidth = PixelScene.landscape() ? 202 : 120;

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

		FeedbackMessage msg = new FeedbackMessage();
		msg.setSize(elementWidth, 0);
		add(msg);

		StyledButton sponsorLink = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "sponsor_link")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://sponsor.tianscar.com";
				Game.platform.openURI(link);
			}
		};
		sponsorLink.icon(Icons.get(Icons.GOLD));
		sponsorLink.textColor(Window.TITLE_COLOR);
		sponsorLink.setSize(landscape() ? (elementWidth - GAP) * 0.5f : elementWidth, BTN_HEIGHT);
		add(sponsorLink);

		StyledButton feedbackLink = new StyledButton(Chrome.Type.GREY_BUTTON_TR, ""){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://github.com/AnsdoShip/carbonized-pixel-dungeon";
				Game.platform.openURI(link);
			}
		};
		feedbackLink.icon(Icons.get(Icons.GITHUB));
		feedbackLink.setSize((sponsorLink.width() - GAP * 2) / 3, BTN_HEIGHT);
		add(feedbackLink);

		StyledButton qqLink = new StyledButton(Chrome.Type.GREY_BUTTON_TR, ""){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://jq.qq.com/?_wv=1027&k=Af0aT21o";
				Game.platform.openURI(link);
			}
		};
		qqLink.icon(Icons.get(Icons.QQ));
		qqLink.setSize(feedbackLink.width(), BTN_HEIGHT);
		add(qqLink);

		StyledButton discordLink = new StyledButton(Chrome.Type.GREY_BUTTON_TR, ""){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://discord.gg/24Bykt4YfJ";
				Game.platform.openURI(link);
			}
		};
		discordLink.icon(Icons.get(Icons.DISCORD));
		discordLink.setSize(feedbackLink.width(), BTN_HEIGHT);
		add(discordLink);

		float elementHeight = msg.height() + (landscape() ? BTN_HEIGHT : BTN_HEIGHT * 2) + GAP;

		float top = 16 + (h - 16 - elementHeight)/2f;
		float left = (w-elementWidth)/2f;

		msg.setPos(left, top);
		align(msg);

		sponsorLink.setPos(left, msg.bottom()+GAP);
		align(sponsorLink);

		feedbackLink.setPos(landscape() ? sponsorLink.right() + GAP : left, landscape() ? msg.bottom()+GAP : sponsorLink.bottom() + GAP);
		align(feedbackLink);

		qqLink.setPos(feedbackLink.right() + GAP, feedbackLink.top());
		align(qqLink);

		discordLink.setPos(qqLink.right() + GAP, feedbackLink.top());
		align(discordLink);

	}

	@Override
	protected void onBackPressed() {
		CarbonizedPixelDungeon.switchNoFade( TitleScene.class );
	}

	private static class FeedbackMessage extends Component {

		NinePatch bg;
		RenderedTextBlock text;
		Image icon;

		@Override
		protected void createChildren() {
			bg = Chrome.get(Chrome.Type.GREY_BUTTON_TR);
			add(bg);

			String message = Messages.get(FeedbackScene.class, "intro");
			message += "\n\n" + Messages.get(FeedbackScene.class, "sponsor_msg");
			message += "\n\n" + Messages.get(FeedbackScene.class, "feedback_msg");
			message += "\n" + Messages.get(FeedbackScene.class, "feedback_msg_pr");
			message += "\n\n" + Messages.get(FeedbackScene.class, "thanks");
			message += "\n\n- Tianscar";

			text = PixelScene.renderTextBlock(message, 6);
			add(text);

			icon = new ItemSprite( ItemSpriteSheet.CARBON_STEEL );
			add(icon);

		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;

			text.maxWidth((int)width - bg.marginHor());
			text.setPos(x + bg.marginLeft(), y + bg.marginTop() + 1);

			icon.y = text.bottom() - icon.height() + 4;
			icon.x = x + 33;

			height = (text.bottom() + 3) - y;

			height += bg.marginBottom();

			bg.size(width, height);

		}

	}

}
