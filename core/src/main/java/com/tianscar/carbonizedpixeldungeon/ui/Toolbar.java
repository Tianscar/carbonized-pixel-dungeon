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

package com.tianscar.carbonizedpixeldungeon.ui;

import com.tianscar.carbonizedpixeldungeon.*;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.scenes.CellSelector;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.scenes.PixelScene;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.tiles.DungeonTerrainTilemap;
import com.tianscar.carbonizedpixeldungeon.windows.WndBag;
import com.tianscar.carbonizedpixeldungeon.windows.WndKeyBindings;
import com.tianscar.carbonizedpixeldungeon.windows.WndQuickBag;
import com.tianscar.pixeldungeonclasses.input.GameAction;
import com.tianscar.pixeldungeonclasses.noosa.*;
import com.tianscar.pixeldungeonclasses.noosa.audio.Sample;
import com.tianscar.pixeldungeonclasses.noosa.ui.Component;
import com.tianscar.pixeldungeonclasses.utils.Point;
import com.tianscar.pixeldungeonclasses.utils.PointF;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnExamine;
	private Tool btnInventory;
	private SwitchQuickSlotTool btnChangeSlot;
	private QuickslotTool[] btnQuick;
	private int sumVisibleSlots;

	private PickedUpItem pickedUp;

	private boolean lastEnabled = true;
	public boolean examining = false;

	private static Toolbar instance;

	public enum Mode {
		SPLIT,
		GROUP,
		CENTER
	}

	public Toolbar() {
		super();

		instance = this;

		height = btnInventory.height();
	}

	@Override
	protected void createChildren() {

		sumVisibleSlots = 2;
		if (PixelScene.uiCamera.width > 120) sumVisibleSlots++;
		if (PixelScene.uiCamera.width > 138) sumVisibleSlots++;
		if (PixelScene.uiCamera.width > 156) sumVisibleSlots++;
		if (PixelScene.uiCamera.width > 174) sumVisibleSlots++;
		if (PixelScene.uiCamera.width > 192) sumVisibleSlots++;
		if (PixelScene.uiCamera.width > 210) sumVisibleSlots++;
		if (PixelScene.uiCamera.width > 228) sumVisibleSlots++;

		if (sumVisibleSlots < QuickSlot.SIZE) {
			sumVisibleSlots--;
			add( btnChangeSlot = new SwitchQuickSlotTool( 125, 0, 16, 16) );
		}

		btnQuick = new QuickslotTool[QuickSlot.SIZE];
		for (int i = 0; i < btnQuick.length; i++){
			add( btnQuick[i] = new QuickslotTool(64, 0, 22, 24, i) );
		}

		add(btnWait = new Tool(24, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(false);
				}
			}

			@Override
			public GameAction keyAction() {
				return PDAction.WAIT;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "wait"));
			}

			protected boolean onLongClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(true);
				}
				return true;
			}
		});

		add(new Button(){
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready && !GameScene.cancel()) {
					examining = false;
					Dungeon.hero.rest(true);
				}
			}

			@Override
			public GameAction keyAction() {
				if (btnWait.active) return PDAction.REST;
				else				return null;
			}
		});

		add(btnExamine = new Tool(44, 0, 20, 26) {
			@Override
			protected void onClick() {
				if (Dungeon.hero.ready) {
					if (!examining && !GameScene.cancel()) {
						GameScene.selectCell(informer);
						examining = true;
					} else if (examining) {
						informer.onSelect(null);
						Dungeon.hero.search(true);
					}
				}
			}

			@Override
			public GameAction keyAction() {
				return PDAction.EXAMINE;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "examine"));
			}

			@Override
			protected boolean onLongClick() {
				Dungeon.hero.search(true);
				return true;
			}
		});

		addToBack(btnInventory = new Tool(0, 0, 24, 26) {
			private GoldIndicator gold;

			@Override
			protected void onClick() {
				if (Dungeon.hero.ready || !Dungeon.hero.isAlive()) {
					if (!GameScene.cancel()) {
						GameScene.show(new WndBag(Dungeon.hero.belongings.backpack));
					}
				}
			}

			@Override
			public GameAction keyAction() {
				return PDAction.INVENTORY;
			}

			@Override
			protected String hoverText() {
				return Messages.titleCase(Messages.get(WndKeyBindings.class, "inventory"));
			}

			@Override
			protected boolean onLongClick() {
				GameScene.show(new WndQuickBag(null));
				return true;
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				gold = new GoldIndicator();
				add(gold);
			}

			@Override
			protected void layout() {
				super.layout();
				gold.fill(this);
			}
		});

		add(pickedUp = new PickedUpItem());
	}

	@Override
	protected void layout() {

		float right = width;

		final int quickSlots = sumVisibleSlots == QuickSlot.SIZE ? 0 : PDSettings.quickSlots();
		int slotIndex;
		for(int i = 0; i < sumVisibleSlots; i++) {
			slotIndex = boundIndex(quickSlots + i);
			if (i == 0 && !PDSettings.flipToolbar() ||
					i == sumVisibleSlots-1 && PDSettings.flipToolbar()){
				btnQuick[slotIndex].border(0, 2);
				btnQuick[slotIndex].frame(106, 0, 19, 24);
			} else if (i == 0 && PDSettings.flipToolbar() ||
					i == sumVisibleSlots-1 && !PDSettings.flipToolbar()){
				btnQuick[slotIndex].border(2, 1);
				btnQuick[slotIndex].frame(86, 0, 20, 24);
			} else {
				btnQuick[slotIndex].border(0, 1);
				btnQuick[slotIndex].frame(88, 0, 18, 24);
			}
		}

		boolean[] visible = new boolean[QuickSlot.SIZE];
		slotIndex = boundIndex(quickSlots);
		visible[slotIndex] = true;
		switch(Mode.valueOf(PDSettings.toolbarMode())){
			case SPLIT:
				btnWait.setPos(x, y);
				btnExamine.setPos(btnWait.right(), y);

				btnInventory.setPos(right - btnInventory.width(), y);

				btnQuick[slotIndex].setPos(btnInventory.left() - btnQuick[slotIndex].width(), y + 2);
				for (int i = 1; i < sumVisibleSlots; i++) {
					slotIndex = boundIndex(quickSlots + i);
					visible[slotIndex] = true;
					btnQuick[slotIndex].setPos(btnQuick[boundIndex(quickSlots+i-1)].left() - btnQuick[slotIndex].width(), y + 2);
				}
				if (sumVisibleSlots < QuickSlot.SIZE) btnChangeSlot.setPos(btnQuick[slotIndex].left() - btnChangeSlot.width(), y + 10);

				break;

			//center = group but.. well.. centered, so all we need to do is pre-emptively set the right side further in.
			case CENTER:
				float toolbarWidth = btnWait.width() + btnExamine.width() + btnInventory.width();
				for(Button slot : btnQuick){
					if (slot.visible) toolbarWidth += slot.width();
				}
				if (sumVisibleSlots < QuickSlot.SIZE) toolbarWidth += btnChangeSlot.width();
				right = (width + toolbarWidth)/2;

			case GROUP:
				btnWait.setPos(right - btnWait.width(), y);
				btnExamine.setPos(btnWait.left() - btnExamine.width(), y);
				btnInventory.setPos(btnExamine.left() - btnInventory.width(), y);

				btnQuick[slotIndex].setPos(btnInventory.left() - btnQuick[slotIndex].width(), y + 2);
				for (int i = 1; i < sumVisibleSlots; i++) {
					slotIndex = boundIndex(quickSlots + i);
					visible[slotIndex] = true;
					btnQuick[slotIndex].setPos(btnQuick[boundIndex(quickSlots+i-1)].left() - btnQuick[slotIndex].width(), y + 2);
				}
				if (sumVisibleSlots < QuickSlot.SIZE) btnChangeSlot.setPos(btnQuick[slotIndex].left() - btnChangeSlot.width(), y + 10);

				break;
		}
		for (int i = 0; i < visible.length; i ++) {
			btnQuick[i].visible = visible[i];
			if (!visible[i]) btnQuick[i].setPos(Integer.MAX_VALUE, Integer.MAX_VALUE);
		}
		right = width;

		if (PDSettings.flipToolbar()) {

			btnWait.setPos( (right - btnWait.right()), y);
			btnExamine.setPos( (right - btnExamine.right()), y);
			btnInventory.setPos( (right - btnInventory.right()), y);

			for(int i = 0; i < sumVisibleSlots; i++) {
				slotIndex = boundIndex(quickSlots + i);
				btnQuick[slotIndex].setPos( right - btnQuick[slotIndex].right(), y+2);
			}
			if (sumVisibleSlots < QuickSlot.SIZE) btnChangeSlot.setPos(right - btnChangeSlot.right(), y+10);

		}

	}

	public static void updateLayout(){
		if (instance != null) instance.layout();
	}

	@Override
	public void update() {
		super.update();

		if (lastEnabled != (Dungeon.hero.ready && Dungeon.hero.isAlive())) {
			lastEnabled = (Dungeon.hero.ready && Dungeon.hero.isAlive());

			for (Gizmo tool : members.toArray(new Gizmo[0])) {
				if (tool instanceof Tool) {
					((Tool)tool).enable( lastEnabled );
				}
			}
		}

		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable(true);
		}
	}

	public void pickup( Item item, int cell ) {
		pickedUp.reset( item,
				cell,
				btnInventory.centerX(),
				btnInventory.centerY());
	}
	
	private int boundIndex(int index) {
		int result = index;
		while (result < 0) result += QuickSlot.SIZE;
		while (result >= QuickSlot.SIZE) result -= QuickSlot.SIZE;
		return result;
	}

	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			instance.examining = false;
			GameScene.examineCell( cell );
		}
		@Override
		public String prompt() {
			return Messages.get(Toolbar.class, "examine_prompt");
		}
	};

	private static class SwitchQuickSlotTool extends Tool {

		ColorBlock bg;
		ColorBlock[] fill;

		public SwitchQuickSlotTool(int x, int y, int width, int height) {
			super(x, y, width, height);
		}

		@Override
		protected void onClick() {
			PDSettings.quickSlots(instance.boundIndex(PDSettings.quickSlots() + instance.sumVisibleSlots));
			instance.layout();
		}

		@Override
		protected void createChildren() {

			bg = new ColorBlock(16, 16, 0xFF3E4039);
			add(bg);
			fill = new ColorBlock[9];
			for (int i = 0; i < fill.length; i ++) {
				fill[i] = new ColorBlock(4, 4, 0xFFFFFF44);
				add(fill[i]);
			}

			super.createChildren();

		}

		protected void setFill(int quickslots) {
			boolean[] visible = new boolean[9];
			for (int i = 0; i < instance.sumVisibleSlots; i ++) {
				visible[instance.boundIndex(i + quickslots)] = true;
			}
			fill[0].x = fill[3].x = fill[6].x = x + 2;
			fill[1].x = fill[4].x = fill[7].x = x + 6;
			fill[2].x = fill[5].x = fill[8].x = x + 10;
			fill[0].y = fill[1].y = fill[2].y = y + 2;
			fill[3].y = fill[4].y = fill[5].y = y + 6;
			fill[6].y = fill[7].y = fill[8].y = y + 10;
			for (int i = 0; i < visible.length; i ++) {
				PixelScene.align(fill[i]);
				fill[i].visible = visible[i];
			}
		}

		@Override
		protected void onPointerDown() {
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
			super.onPointerDown();
		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			PixelScene.align(bg);
			super.layout();
			setFill(PDSettings.quickSlots());
		}

		@Override
		public GameAction keyAction() {
			return PDAction.QUICKSLOT_SWITCH;
		}

		@Override
		protected String hoverText() {
			return Messages.titleCase(Messages.get(WndKeyBindings.class, "quickslot_switch"));
		}

	}

	private static class Tool extends Button {

		private static final int BGCOLOR = 0x7B8073;

		private Image base;

		public Tool( int x, int y, int width, int height ) {
			super();

			hotArea.blockLevel = PointerArea.ALWAYS_BLOCK;
			frame(x, y, width, height);
		}

		public void frame( int x, int y, int width, int height) {
			base.frame( x, y, width, height );

			this.width = width;
			this.height = height;
		}

		@Override
		protected void createChildren() {
			super.createChildren();

			base = new Image( Assets.Interfaces.TOOLBAR );
			add( base );
		}

		@Override
		protected void layout() {
			super.layout();

			base.x = x;
			base.y = y;
		}

		@Override
		protected void onPointerDown() {
			base.brightness( 1.4f );
		}

		@Override
		protected void onPointerUp() {
			if (active) {
				base.resetColor();
			} else {
				base.tint( BGCOLOR, 0.7f );
			}
		}

		public void enable( boolean value ) {
			if (value != active) {
				if (value) {
					base.resetColor();
				} else {
					base.tint( BGCOLOR, 0.7f );
				}
				active = value;
			}
		}

	}

	private static class QuickslotTool extends Tool {

		private QuickSlotButton slot;
		private int borderLeft = 2;
		private int borderRight = 2;

		public QuickslotTool( int x, int y, int width, int height, int slotNum ) {
			super( x, y, width, height );

			slot = new QuickSlotButton( slotNum );
			add( slot );
		}

		public void border( int left, int right ){
			borderLeft = left;
			borderRight = right;
			layout();
		}

		@Override
		protected void layout() {
			super.layout();
			slot.setRect( x, y, width, height );
			slot.slotMargins(borderLeft, 2, borderRight, 2);
		}

		@Override
		public void enable( boolean value ) {
			super.enable( value );
			slot.enable( value );
		}
	}

	public static class PickedUpItem extends ItemSprite {

		private static final float DURATION = 0.5f;

		private float startScale;
		private float startX, startY;
		private float endX, endY;
		private float left;

		public PickedUpItem() {
			super();

			originToCenter();

			active =
					visible =
							false;
		}

		public void reset( Item item, int cell, float endX, float endY ) {
			view( item );

			active =
					visible =
							true;

			PointF tile = DungeonTerrainTilemap.raisedTileCenterToWorld(cell);
			Point screen = Camera.main.cameraToScreen(tile.x, tile.y);
			PointF start = camera().screenToCamera(screen.x, screen.y);

			x = this.startX = start.x - width() / 2;
			y = this.startY = start.y - width() / 2;

			this.endX = endX - width() / 2;
			this.endY = endY - width() / 2;
			left = DURATION;

			scale.set( startScale = Camera.main.zoom / camera().zoom );

		}

		@Override
		public void update() {
			super.update();

			if ((left -= Game.elapsed) <= 0) {

				visible =
						active =
								false;
				if (emitter != null) emitter.on = false;

			} else {
				float p = left / DURATION;
				scale.set( startScale * (float)Math.sqrt( p ) );

				x = startX*p + endX*(1-p);
				y = startY*p + endY*(1-p);
			}
		}
	}
}
