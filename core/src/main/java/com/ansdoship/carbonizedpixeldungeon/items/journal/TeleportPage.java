package com.ansdoship.carbonizedpixeldungeon.items.journal;

import com.ansdoship.carbonizedpixeldungeon.journal.Document;
import com.ansdoship.carbonizedpixeldungeon.messages.Messages;
import com.ansdoship.carbonizedpixeldungeon.sprites.ItemSpriteSheet;

public class TeleportPage extends DocumentPage {

    {
        image = ItemSpriteSheet.TP_PAGE;
    }

    @Override
    public Document document() {
        return Document.TELEPORT_GUIDE;
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", document().pageTitle(page()));
    }

}
