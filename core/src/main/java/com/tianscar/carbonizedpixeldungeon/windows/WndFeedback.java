package com.tianscar.carbonizedpixeldungeon.windows;

import com.tianscar.carbonizedpixeldungeon.PDSettings;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.ui.*;
import com.tianscar.pixeldungeonclasses.noosa.Game;

public class WndFeedback extends Window {

    protected static final int WIDTH_P    = 122;
    protected static final int WIDTH_L    = 223;
    private static final int BTN_HEIGHT	    = 18;
    private static final float GAP          = 2;

    public WndFeedback(boolean ingame) {

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        IconTitle title = new IconTitle(Icons.get( Icons.CAPD ), Messages.get(this, ingame ? "title_ingame" : "title"));
        title.setRect( 0, 0, width, 0 );
        add(title);

        IconButton btnDiscord = new IconButton( Icons.get(Icons.DISCORD) ) {
            @Override
            protected void onClick() {
                super.onClick();
                String link = "https://discord.gg/24Bykt4YfJ";
                Game.platform.openURI(link);
            }

            @Override
            protected String hoverText() {
                return Messages.get(WndFeedback.this, "discord_link");
            }
        };

        btnDiscord.icon();
        btnDiscord.setRect( width - 18, 0, 16, 16 );

        title.setSize(title.width() - btnDiscord.width(), title.height());

        add( btnDiscord );

        String message = Messages.get(this, ingame ? "intro_ingame" : "intro");
        message += "\n\n" + Messages.get(this, "sponsor_msg");
        message += "\n\n" + Messages.get(this, "feedback_msg");
        message += "\n" + Messages.get(this, "feedback_msg_pr");
        message += "\n\n" + Messages.get(this, "thanks");
        message += "\n\n_-_ Tianscar";

        RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
        text.text( message, width );
        text.setPos( title.left(), title.bottom() + 4 );
        add( text );

        RedButton btnSponsor = new RedButton(Messages.get(this, "sponsor_link")){
            @Override
            protected void onClick() {
                super.onClick();
                String link = "https://afdian.net/a/tianscar";
                Game.platform.openURI(link);
            }
        };
        btnSponsor.icon(Icons.get(Icons.GOLD));
        if (PixelScene.landscape()) {
            btnSponsor.setRect(0, text.bottom() + GAP*2, (width - GAP) * 0.5f, BTN_HEIGHT);
        } else {
            btnSponsor.setRect(0, text.bottom() + GAP*2, width, BTN_HEIGHT);
        }
        add(btnSponsor);

        RedButton btnFeedback = new RedButton(Messages.get(this, "feedback_link")){
            @Override
            protected void onClick() {
                super.onClick();
                String link = "https://github.com/Tianscar/carbonized-pixel-dungeon";
                Game.platform.openURI(link);
            }
        };
        btnFeedback.icon(Icons.get(Icons.GITHUB));
        if (PixelScene.landscape()) {
            btnFeedback.setRect(btnSponsor.right() + 2, text.bottom() + GAP*2, (width - 2) * 0.5f, BTN_HEIGHT);
        } else {
            btnFeedback.setRect(0, btnSponsor.bottom() + GAP, width, BTN_HEIGHT);
        }
        add(btnFeedback);

        if (ingame) {
            RedButton btnClose = new RedButton(Messages.get(this, "close")){
                @Override
                protected void onClick() {
                    super.onClick();
                    PDSettings.supportNagged(true);
                    WndFeedback.super.hide();
                }
            };
            btnClose.setRect(0, btnFeedback.bottom() + GAP, width, BTN_HEIGHT);
            add(btnClose);

            resize(width, (int)btnClose.bottom());
        }
        else resize(width, (int) btnFeedback.bottom());

    }

    @Override
    public void hide() {
        //do nothing, have to close via the close button
    }

}
