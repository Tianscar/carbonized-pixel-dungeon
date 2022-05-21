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
import com.ansdoship.carbonizedpixeldungeon.ui.*;
import com.ansdoship.pixeldungeonclasses.noosa.Camera;
import com.ansdoship.pixeldungeonclasses.noosa.Game;
import com.ansdoship.pixeldungeonclasses.noosa.Image;
import com.ansdoship.pixeldungeonclasses.noosa.NinePatch;
import com.ansdoship.pixeldungeonclasses.noosa.ui.Component;

public class FeedbackScene extends PixelScene {

	private static final int BTN_HEIGHT = 22;
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
		sponsorLink.setSize(elementWidth, BTN_HEIGHT);
		add(sponsorLink);

		StyledButton feedbackLink = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "feedback_link")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "https://github.com/AnsdoShip/carbonized-pixel-dungeon";
				Game.platform.openURI(link);
			}
		};
		feedbackLink.icon(Icons.get(Icons.GITHUB));
		feedbackLink.setSize(landscape() ? ((elementWidth - GAP) * 0.5f) : elementWidth, BTN_HEIGHT);
		add(feedbackLink);

		StyledButton feedbackMail = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "feedback_mail")){
			@Override
			protected void onClick() {
				super.onClick();
				String link = "mailto://tianscar.protonmail.com";
				Game.platform.openURI(link);
			}
		};
		feedbackMail.icon(Icons.get(Icons.MAIL));
		feedbackMail.setSize(feedbackLink.width(), BTN_HEIGHT);
		add(feedbackMail);

		float elementHeight = msg.height() + BTN_HEIGHT * (landscape() ? 2 : 3) + GAP;

		float top = 16 + (h - 16 - elementHeight)/2f;
		float left = (w-elementWidth)/2f;

		msg.setPos(left, top);
		align(msg);

		sponsorLink.setPos(left, msg.bottom()+GAP);
		align(sponsorLink);

		feedbackLink.setPos(left, sponsorLink.bottom()+GAP);
		align(feedbackLink);

		feedbackMail.setPos(landscape() ? (feedbackLink.right()+GAP) : left, (landscape() ? sponsorLink.bottom() : feedbackLink.bottom()) +GAP);
		align(feedbackMail);

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

			icon = Icons.get(Icons.TIANSCAR);
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
