package com.tianscar.carbonizedpixeldungeon.levels;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.Char;
import com.tianscar.carbonizedpixeldungeon.actors.hero.HeroClass;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.Mob;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.Barmaid;
import com.tianscar.carbonizedpixeldungeon.actors.mobs.npcs.Customer;
import com.tianscar.carbonizedpixeldungeon.levels.painters.Painter;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.Tilemap;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Music;
import com.tianscar.carbonizedpixeldungeon.tiles.CustomTilemap;

public class InnLevel extends Level {

    {
        preventHunger = true;
        viewDistance = 20;

        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_INN;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_INN;
    }

    public void playLevelMusic(){
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.INN},
                new float[]{1},
                false);
    }

    private static final int[] customerPos = new int[] {
            4 + 8 * 17,
            5 + 8 * 17,
            3 + 9 * 17,
            3 + 10 * 17,
            4 + 11 * 17,
            5 + 11 * 17,
            6 + 9 * 17,
            6 + 10 * 17,
            11 + 8 * 17
    };

    @Override
    protected boolean build() {
        setSize(17, 15);

        Painter.fill(this, 2, 1, 13, 1, Terrain.BOOKSHELF);

        Painter.fill(this, 2, 2, 13, 11, Terrain.EMPTY);

        Painter.fill(this, 4, 4, 4, 3, Terrain.SIGN);
        Painter.fill(this, 5, 4, 2, 2, Terrain.EMPTY);

        Barmaid barmaid = new Barmaid();
        barmaid.pos = pointToCell(6, 5);
        mobs.add(barmaid);

        Bar bar = new Bar();
        bar.pos(4, 3);
        customTiles.add(bar);
        BarWall barWall = new BarWall();
        barWall.pos(4, 3);
        customWalls.add(barWall);

        Painter.fill(this, 14, 3, 1, 4, Terrain.STATUE);

        Painter.fill(this, 10, 5, 2, 1, Terrain.SIGN);
        FireplaceWall fireplaceWall = new FireplaceWall();
        fireplaceWall.pos(10, 4);
        customWalls.add(fireplaceWall);
        Fireplace fireplace = new Fireplace();
        fireplace.pos(10, 5);
        customTiles.add(fireplace);

        Painter.fill(this, 4, 9, 2, 2, Terrain.SIGN);
        Table table = new Table();
        table.pos(4, 8);
        customTiles.add(table);
        Painter.fill(this, 11, 9, 2, 2, Terrain.SIGN);
        table = new Table();
        table.alt = true;
        table.pos(11, 8);
        customTiles.add(table);

        for (HeroClass cl : HeroClass.values()) {
            if (cl == Dungeon.hero.heroClass) continue;
            if (cl.ordinal() > 3 && cl != HeroClass.ELEMENTALIST) continue;
            Customer customer = Customer.of( cl );
            customer.pos = customerPos[cl.ordinal()];
            mobs.add(customer);
        }

        Painter.fill(this, 2, 12, 5, 1, Terrain.GRASS);
        Painter.fill(this, 10, 12, 5, 1, Terrain.GRASS);

        //entrance = pointToCell(8, 7);
        entrance = customerPos[Dungeon.hero.heroClass.ordinal()];

        exit = pointToCell(8, 13);
        map[exit] = Terrain.EXIT;

        return true;
    }

    public static class FireplaceWall extends CustomTilemap {

        {
            texture = Assets.Environment.INN_EXTRA;

            tileW = 2;
            tileH = 1;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(new int[] {32, 33}, 2);
            return v;
        }

        @Override
        public Image image(int tileX, int tileY) {
            return null;
        }

    }

    public static class Fireplace extends CustomTilemap {

        {
            texture = Assets.Environment.INN_EXTRA;

            tileW = 2;
            tileH = 1;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(new int[] {40, 41}, 2);
            return v;
        }

        @Override
        public String name(int tileX, int tileY) {
            return Messages.get(InnLevel.class, "fireplace_name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            return Messages.get(InnLevel.class, "fireplace_desc");
        }

    }

    public static class Table extends CustomTilemap {

        {
            texture = Assets.Environment.INN_EXTRA;

            tileW = 2;
            tileH = 3;
        }

        public boolean alt = false;

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(alt ? new int[] {6, 7, 14, 15, 22, 23}: new int[] {4, 5, 12, 13, 20, 21}, 2);
            return v;
        }

        @Override
        public Image image(int tileX, int tileY) {
            if (tileY == 0) return null;
            else return super.image(tileX, tileY);
        }

        @Override
        public String name(int tileX, int tileY) {
            if (tileY == 0) return null;
            else return Messages.get(InnLevel.class, "table_name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            if (tileY == 0) return null;
            else return Messages.get(InnLevel.class, "table_desc");
        }

    }

    public static class BarWall extends CustomTilemap {

        {
            texture = Assets.Environment.INN_EXTRA;

            tileW = 4;
            tileH = 3;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(new int[] {0, 1, 2, 3, 8, 9, 10, 11, 16, 17, 18, 19}, tileW);
            return v;
        }

        @Override
        public Image image(int tileX, int tileY) {
            return null;
        }

    }

    public static class Bar extends CustomTilemap {

        {
            texture = Assets.Environment.INN_EXTRA;

            tileW = 4;
            tileH = 4;
        }

        @Override
        public Tilemap create() {
            Tilemap v = super.create();
            v.map(new int[] {0, 1, 2, 3, 8, 9, 10, 11, 16, 17, 18, 19, 24, 25, 26, 27}, tileW);
            return v;
        }

        @Override
        public Image image(int tileX, int tileY) {
            if ((tileX == 0 && tileY == 0) || (tileX == 3 && tileY == 0) ||
                    (tileX == 1 && tileY == 1) || (tileX == 1 && tileY == 2) ||
                    (tileX == 2 && tileY == 1) || (tileX == 2 && tileY == 2)) return null;
            else return super.image(tileX, tileY);
        }

        @Override
        public String name(int tileX, int tileY) {
            if ((tileX == 0 && tileY == 0) || (tileX == 3 && tileY == 0) ||
                    (tileX == 1 && tileY == 1) || (tileX == 1 && tileY == 2) ||
                    (tileX == 2 && tileY == 1) || (tileX == 2 && tileY == 2)) return super.name(tileX, tileY);
            else return Messages.get(InnLevel.class, "bar_name");
        }

        @Override
        public String desc(int tileX, int tileY) {
            if ((tileX == 0 && tileY == 0) || (tileX == 3 && tileY == 0) ||
                    (tileX == 1 && tileY == 1) || (tileX == 1 && tileY == 2) ||
                    (tileX == 2 && tileY == 1) || (tileX == 2 && tileY == 2)) return super.desc(tileX, tileY);
            else return Messages.get(InnLevel.class, "bar_desc");
        }

    }

    @Override
    public Mob createMob() {
        return null;
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        return entrance;
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.BOOKSHELF:
                return Messages.get(InnLevel.class, "bookshelf_name");
            case Terrain.WATER:
                return Messages.get(InnLevel.class, "water_name");
            case Terrain.GRASS:
                return Messages.get(InnLevel.class, "grass_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(InnLevel.class, "high_grass_name");
            case Terrain.FURROWED_GRASS:
                return Messages.get(InnLevel.class, "furrowed_grass_name");
            case Terrain.EXIT:
            case Terrain.UNLOCKED_EXIT:
                return Messages.get(InnLevel.class, "exit_name");
            case Terrain.STATUE:
                return Messages.get(InnLevel.class, "statue_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.BOOKSHELF:
                return Messages.get(InnLevel.class, "bookshelf_desc");
            case Terrain.WATER:
                return Messages.get(InnLevel.class, "water_desc");
            case Terrain.EXIT:
            case Terrain.UNLOCKED_EXIT:
                return Messages.get(InnLevel.class, "exit_desc");
            case Terrain.EMBERS:
                return Messages.get(InnLevel.class, "embers_desc");
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return Messages.get(InnLevel.class, "statue_desc");
            case Terrain.EMPTY_DECO:
                return Messages.get(InnLevel.class, "empty_deco_desc");
            default:
                return super.tileDesc(tile);
        }
    }

}
