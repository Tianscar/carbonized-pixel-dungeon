package com.ansdoship.carbonizedpixeldungeon.windows;

import com.ansdoship.carbonizedpixeldungeon.PDSettings;
import com.ansdoship.carbonizedpixeldungeon.messages.Languages;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.scenes.PixelScene;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSprite;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.ansdoship.carbonizedpixeldungeon.ui.*;
import com.ansdoship.pixeldungeonclasses.noosa.ColorBlock;
import com.ansdoship.pixeldungeonclasses.noosa.Game;

public class WndFeedback extends Window {

    protected static final int WIDTH_P    = 122;
    protected static final int WIDTH_L    = 223;
    private static final int BTN_HEIGHT	    = 18;
    private static final float GAP          = 2;

    public WndFeedback(boolean ingame){

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        IconTitle title = new IconTitle(new ItemSprite( ItemSpriteSheet.CARBON_STEEL ), Messages.get(this, ingame ? "title_ingame" : "title"));
        title.setRect( 0, 0, width, 0 );
        add(title);

        IconButton btnQQ = new IconButton( Icons.get(Icons.QQ) ){
            @Override
            protected void onClick() {
                super.onClick();
                String link = "https://jq.qq.com/?_wv=1027&k=ntjMkAsW";
                Game.platform.openURI(link);
            }

            @Override
            protected String hoverText() {
                return Messages.get(WndFeedback.this, "qq_link");
            }
        };
        btnQQ.setSize(16 , 16);

        title.setSize(title.width() - btnQQ.width(), title.height());

        btnQQ.setPos(width - 18, 0);
        add(btnQQ);

        ColorBlock sep1 = new ColorBlock(1, 1, 0xFF000000);
        sep1.size(width, 1);
        sep1.y = title.bottom() + 1;
        add(sep1);

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
        btnDiscord.setSize( 16, 16 );

        title.setSize(title.width() - btnDiscord.width(), title.height());

        if (PDSettings.language() == Languages.CHINESE) {
            btnDiscord.setPos(width - 18, 0);
            btnQQ.setPos(btnDiscord.left() - 18, btnDiscord.top());
        }
        else {
            btnDiscord.setPos(btnQQ.left() - 18, 0);
        }
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

        ColorBlock sep2 = new ColorBlock(1, 1, 0xFF000000);
        sep2.size(width, 1);
        sep2.y = text.bottom() + GAP;
        add(sep2);

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
            btnSponsor.setRect(0, text.bottom() + GAP*3, (width - GAP) * 0.5f, BTN_HEIGHT);
        } else {
            btnSponsor.setRect(0, text.bottom() + GAP*3, width, BTN_HEIGHT);
        }
        add(btnSponsor);

        RedButton btnFeedback = new RedButton(Messages.get(this, "feedback_link")){
            @Override
            protected void onClick() {
                super.onClick();
                String link = "https://github.com/AnsdoShip/carbonized-pixel-dungeon";
                Game.platform.openURI(link);
            }
        };
        btnFeedback.icon(Icons.get(Icons.GITHUB));
        if (PixelScene.landscape()) {
            btnFeedback.setRect(btnSponsor.right() + 2, text.bottom() + GAP*3, (width - 2) * 0.5f, BTN_HEIGHT);
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
