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

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.Badges;
import com.tianscar.carbonizedpixeldungeon.Chrome;
import com.tianscar.carbonizedpixeldungeon.Dungeon;
import com.tianscar.carbonizedpixeldungeon.CarbonizedPixelDungeon;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Belongings;
import com.tianscar.carbonizedpixeldungeon.effects.Speck;
import com.tianscar.carbonizedpixeldungeon.items.Item;
import com.tianscar.carbonizedpixeldungeon.items.Recipe;
import com.tianscar.carbonizedpixeldungeon.items.armor.Armor;
import com.tianscar.carbonizedpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.tianscar.carbonizedpixeldungeon.items.rings.Ring;
import com.tianscar.carbonizedpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.tianscar.carbonizedpixeldungeon.items.weapon.missiles.darts.Dart;
import com.tianscar.carbonizedpixeldungeon.journal.Journal;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSprite;
import com.tianscar.carbonizedpixeldungeon.sprites.ItemSpriteSheet;
import com.tianscar.carbonizedpixeldungeon.ui.ExitButton;
import com.tianscar.carbonizedpixeldungeon.ui.IconButton;
import com.tianscar.carbonizedpixeldungeon.ui.Icons;
import com.tianscar.carbonizedpixeldungeon.ui.ItemSlot;
import com.tianscar.carbonizedpixeldungeon.ui.RedButton;
import com.tianscar.carbonizedpixeldungeon.ui.RenderedTextBlock;
import com.tianscar.carbonizedpixeldungeon.ui.Window;
import com.tianscar.carbonizedpixeldungeon.windows.WndBag;
import com.tianscar.carbonizedpixeldungeon.windows.WndInfoItem;
import com.tianscar.carbonizedpixeldungeon.windows.WndJournal;
import com.tianscar.carbonizedpixeldungeon.gltextures.TextureCache;
import com.tianscar.carbonizedpixeldungeon.glwrap.Blending;
import com.tianscar.carbonizedpixeldungeon.noosa.Camera;
import com.tianscar.carbonizedpixeldungeon.noosa.ColorBlock;
import com.tianscar.carbonizedpixeldungeon.noosa.Game;
import com.tianscar.carbonizedpixeldungeon.noosa.Image;
import com.tianscar.carbonizedpixeldungeon.noosa.NinePatch;
import com.tianscar.carbonizedpixeldungeon.noosa.NoosaScript;
import com.tianscar.carbonizedpixeldungeon.noosa.NoosaScriptNoLighting;
import com.tianscar.carbonizedpixeldungeon.noosa.SkinnedBlock;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.noosa.particles.Emitter;
import com.tianscar.carbonizedpixeldungeon.noosa.ui.Component;

import java.io.IOException;
import java.util.*;

public class AlchemyScene extends PixelScene {
	
	private static ItemButton[] inputs = new ItemButton[3];
	private ItemSlot output;
	
	private Emitter smokeEmitter;
	private Emitter bubbleEmitter;
	
	private Emitter lowerBubbles;
	private SkinnedBlock water;
	
	private RenderedTextBlock energyLeft;
	private RenderedTextBlock energyCost;
	
	private RedButton btnCombine;

	private final HashMap<Item, Integer> equipstats = new HashMap<>();
	private static final int STAT_WEAPON = 0, STAT_EXTRA = 1, STAT_RING = 2, STAT_MISC = 3, STAT_ARMOR = 4;
	
	private static final int BTN_SIZE	= 28;

	{
		inGameScene = true;
	}
	
	@Override
	public void create() {
		super.create();
		
		water = new SkinnedBlock(
				Camera.main.width, Camera.main.height,
				Dungeon.level.waterTex() ){
			
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}
			
			@Override
			public void draw() {
				//water has no alpha component, this improves performance
				Blending.disable();
				super.draw();
				Blending.enable();
			}
		};
		water.autoAdjust = true;
		add(water);
		
		Image im = new Image(TextureCache.createGradient(0x66000000, 0x88000000, 0xAA000000, 0xCC000000, 0xFF000000));
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height/5f;
		im.scale.y = Camera.main.width;
		add(im);
		
		
		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(Camera.main.width - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);
		
		int w = 50 + Camera.main.width/2;
		int left = (Camera.main.width - w)/2;
		
		int pos = (Camera.main.height - 100)/2;
		
		RenderedTextBlock desc = PixelScene.renderTextBlock(6);
		desc.maxWidth(w);
		desc.text( Messages.get(AlchemyScene.class, "text") );
		desc.setPos(left + (w - desc.width())/2, pos);
		add(desc);
		
		pos += desc.height() + 6;
		
		synchronized (inputs) {
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = new ItemButton() {
					@Override
					protected void onClick() {
						super.onClick();
						if (item != null) {
							restoreEquipment( item );
							if (!(item instanceof AlchemistsToolkit) && !item.isEquipped( Dungeon.hero )) {
								if (!item.collect()) {
									Dungeon.level.drop(item, Dungeon.hero.pos);
								}
							}
							item = null;
							slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
							updateState();
						}
						AlchemyScene.this.addToFront(WndBag.getBag( itemSelector ));
					}

					@Override
					protected boolean onLongClick() {
						if (item != null) {
							Game.scene().addToFront(new WndInfoItem(item));
							return true;
						}
						return false;
					}
				};
				inputs[i].setRect(left + 10, pos, BTN_SIZE, BTN_SIZE);
				add(inputs[i]);
				pos += BTN_SIZE + 2;
			}
		}
		
		btnCombine = new RedButton(""){
			Image arrow;
			
			@Override
			protected void createChildren() {
				super.createChildren();
				
				arrow = Icons.get(Icons.ARROW);
				add(arrow);
			}
			
			@Override
			protected void layout() {
				super.layout();
				arrow.x = x + (width - arrow.width)/2f;
				arrow.y = y + (height - arrow.height)/2f;
				PixelScene.align(arrow);
			}
			
			@Override
			public void enable(boolean value) {
				super.enable(value);
				if (value){
					arrow.tint(1, 1, 0, 1);
					arrow.alpha(1f);
					bg.alpha(1f);
				} else {
					arrow.color(0, 0, 0);
					arrow.alpha(0.6f);
					bg.alpha(0.6f);
				}
			}
			
			@Override
			protected void onClick() {
				super.onClick();
				combine();
			}
		};
		btnCombine.enable(false);
		btnCombine.setRect(left + (w-30)/2f, inputs[1].top()+5, 30, inputs[1].height()-10);
		add(btnCombine);
		
		output = new ItemSlot() {
			@Override
			protected void onClick() {
				super.onClick();
				if (visible && item.trueName() != null) {
					AlchemyScene.this.addToFront(new WndInfoItem(item));
				}
			}
		};
		output.setRect(left + w - BTN_SIZE - 10, inputs[1].top(), BTN_SIZE, BTN_SIZE);
		
		ColorBlock outputBG = new ColorBlock(output.width(), output.height(), 0x9991938C);
		outputBG.x = output.left();
		outputBG.y = output.top();
		add(outputBG);
		
		add(output);
		output.visible = false;
		
		bubbleEmitter = new Emitter();
		smokeEmitter = new Emitter();
		bubbleEmitter.pos(0, 0, Camera.main.width, Camera.main.height);
		smokeEmitter.pos(outputBG.x + (BTN_SIZE-16)/2f, outputBG.y + (BTN_SIZE-16)/2f, 16, 16);
		bubbleEmitter.autoKill = false;
		smokeEmitter.autoKill = false;
		add(bubbleEmitter);
		add(smokeEmitter);
		
		pos += 10;
		
		lowerBubbles = new Emitter();
		lowerBubbles.pos(0, pos, Camera.main.width, Math.max(0, Camera.main.height-pos));
		add(lowerBubbles);
		lowerBubbles.pour(Speck.factory( Speck.BUBBLE ), 0.1f );
		
		ExitButton btnExit = new ExitButton() {
			@Override
			protected void onClick() {
				Game.switchScene(GameScene.class);
			}
		};
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		IconButton btnGuide = new IconButton( new ItemSprite(ItemSpriteSheet.ALCH_PAGE, null)) {
			@Override
			protected void onClick() {
				super.onClick();
				restoreEquipments();
				clearSlots();
				updateState();
				AlchemyScene.this.addToFront(new Window() {
				
					{
						WndJournal.AlchemyTab t = new WndJournal.AlchemyTab();
						int w, h;
						if (landscape()) {
							w = WndJournal.WIDTH_L; h = WndJournal.HEIGHT_L;
						} else {
							w = WndJournal.WIDTH_P; h = WndJournal.HEIGHT_P;
						}
						resize(w, h);
						add(t);
						t.setRect(0, 0, w, h);
					}
				
				});
			}
		};
		btnGuide.setRect(0, 0, 20, 20);
		add(btnGuide);
		
		energyLeft = PixelScene.renderTextBlock(Messages.get(AlchemyScene.class, "energy", availableEnergy()), 9);
		energyLeft.setPos(
				(Camera.main.width - energyLeft.width())/2,
				Camera.main.height - 5 - energyLeft.height()
		);
		add(energyLeft);
		
		energyCost = PixelScene.renderTextBlock(6);
		add(energyCost);
		
		fadeIn();
		
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			CarbonizedPixelDungeon.reportException(e);
		}
	}

	private void restoreEquipment( Item item ) {
		if (equipstats.containsKey( item )) {
			switch (equipstats.get( item )) {
				case STAT_WEAPON:
					Dungeon.hero.belongings.weapon = (MeleeWeapon) item;
					break;
				case STAT_EXTRA:
					Dungeon.hero.belongings.extra = (MeleeWeapon) item;
					break;
				case STAT_RING:
					Dungeon.hero.belongings.ring = (Ring) item;
					break;
				case STAT_MISC:
					Dungeon.hero.belongings.misc = (Ring) item;
					break;
				case STAT_ARMOR:
					Dungeon.hero.belongings.armor = (Armor) item;
					break;
			}
		}
		equipstats.remove( item );
	}

	private void restoreEquipments() {
		Set<Map.Entry<Item, Integer>> entriesToRemove = new LinkedHashSet<>();
		for (Map.Entry<Item, Integer> entry : equipstats.entrySet()) {
			switch (entry.getValue()) {
				case STAT_WEAPON:
					Dungeon.hero.belongings.weapon = (MeleeWeapon) entry.getKey();
					break;
				case STAT_EXTRA:
					Dungeon.hero.belongings.extra = (MeleeWeapon) entry.getKey();
					break;
				case STAT_RING:
					Dungeon.hero.belongings.ring = (Ring) entry.getKey();
					break;
				case STAT_MISC:
					Dungeon.hero.belongings.misc = (Ring) entry.getKey();
					break;
				case STAT_ARMOR:
					Dungeon.hero.belongings.armor = (Armor) entry.getKey();
					break;
			}
			entriesToRemove.add(entry);
		}
		equipstats.entrySet().removeAll(entriesToRemove);
	}
	
	@Override
	public void update() {
		super.update();
		water.offset( 0, -5 * Game.elapsed );
	}
	
	@Override
	public void onBackPressed() {
		Game.switchScene(GameScene.class);
	}
	
	protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

		@Override
		public String textPrompt() {
			return Messages.get(AlchemyScene.class, "select");
		}

		@Override
		public boolean itemSelectable(Item item) {
			return Recipe.usableInRecipe(item);
		}

		@Override
		public void onSelect( Item item ) {
			synchronized (inputs) {
				if (item != null && inputs[0] != null) {
					for (int i = 0; i < inputs.length; i++) {
						if (inputs[i].item == null) {
							if (item instanceof Dart) {
								inputs[i].item(item.detachAll(Dungeon.hero.belongings.backpack));
							} else if (item instanceof AlchemistsToolkit) {
								clearSlots();
								inputs[i].item(item);
							} else if (item.isEquipped(Dungeon.hero)) {
								int stat;
								if (item == Dungeon.hero.belongings.weapon) {
									Dungeon.hero.belongings.weapon = null;
									stat = STAT_WEAPON;
								}
								else if (item == Dungeon.hero.belongings.extra) {
									Dungeon.hero.belongings.extra = null;
									stat = STAT_EXTRA;
								}
								else if (item == Dungeon.hero.belongings.ring) {
									Dungeon.hero.belongings.ring = null;
									stat = STAT_RING;
								}
								else if (item == Dungeon.hero.belongings.misc) {
									Dungeon.hero.belongings.misc = null;
									stat = STAT_MISC;
								}
								else if (item == Dungeon.hero.belongings.armor) {
									Dungeon.hero.belongings.armor = null;
									stat = STAT_ARMOR;
								}
								else throw new IllegalStateException();
								inputs[i].item(item);
								equipstats.put(item, stat);
							} else {
								inputs[i].item(item.detach(Dungeon.hero.belongings.backpack));
							}
							break;
						}
					}
					updateState();
				}
			}
		}
	};
	
	private<T extends Item> ArrayList<T> filterInput(Class<? extends T> itemClass){
		ArrayList<T> filtered = new ArrayList<>();
		for (int i = 0; i < inputs.length; i++){
			Item item = inputs[i].item;
			if (item != null && itemClass.isInstance(item)){
				filtered.add((T)item);
			}
		}
		return filtered;
	}
	
	private void updateState() {
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		Recipe recipe = Recipe.findRecipe(ingredients);
		
		if (recipe != null){
			int cost = recipe.cost(ingredients);
			
			output.item(recipe.sampleOutput(ingredients));
			output.visible = true;
			
			energyCost.text( Messages.get(AlchemyScene.class, "cost", cost) );
			energyCost.setPos(
					btnCombine.left() + (btnCombine.width() - energyCost.width())/2,
					btnCombine.top() - energyCost.height()
			);
			
			energyCost.visible = (cost > 0);
			
			if (cost <= availableEnergy()) {
				btnCombine.enable(true);
				energyCost.resetColor();
			} else {
				btnCombine.enable(false);
				energyCost.hardlight(0xFF0000);
			}
			
		} else {
			btnCombine.enable(false);
			output.visible = false;
			energyCost.visible = false;
		}
		
	}
	
	private void combine() {
		
		ArrayList<Item> ingredients = filterInput(Item.class);
		Recipe recipe = Recipe.findRecipe(ingredients);
		
		Item result = null;
		
		if (recipe != null) {
			provider.spendEnergy(recipe.cost(ingredients));
			energyLeft.text(Messages.get(AlchemyScene.class, "energy", availableEnergy()));
			energyLeft.setPos(
					(Camera.main.width - energyLeft.width())/2,
					Camera.main.height - 5 - energyLeft.height()
			);
			
			result = recipe.brew(ingredients);
		}
		
		if (result != null) {
			bubbleEmitter.start(Speck.factory( Speck.BUBBLE ), 0.01f, 100 );
			smokeEmitter.burst(Speck.factory( Speck.WOOL ), 10 );
			Sample.INSTANCE.play( Assets.Sounds.PUFF );

			output.item(result);
			if (!(result instanceof AlchemistsToolkit)) {
				if (!result.collect()) {
					Dungeon.level.drop(result, Dungeon.hero.pos);
				}
			}
			
			try {
				Dungeon.saveAll();
			} catch (IOException e) {
				CarbonizedPixelDungeon.reportException(e);
			}
			
			synchronized (inputs) {
				for (int i = 0; i < inputs.length; i++) {
					if (inputs[i] != null && inputs[i].item != null) {
						if (inputs[i].item.quantity() <= 0 || inputs[i].item instanceof AlchemistsToolkit) {
							inputs[i].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
							inputs[i].item = null;
						} else {
							inputs[i].slot.item(inputs[i].item);
						}
					}
				}
			}
			equipstats.clear();
			
			btnCombine.enable(false);
		}
		
	}
	
	public void populate(ArrayList<Item> toFind, Belongings inventory) {
		restoreEquipments();
		clearSlots();
		
		int curslot = 0;
		for (Item finding : toFind) {
			int needed = finding.quantity();
			ArrayList<Item> found = inventory.getAllSimilar(finding);
			while (!found.isEmpty() && needed > 0) {
				Item detached;
				if (finding instanceof Dart) {
					detached = found.get(0).detachAll(inventory.backpack);
				} else if (finding.isEquipped(Dungeon.hero)) {
					int stat;
					if (finding == Dungeon.hero.belongings.weapon) {
						Dungeon.hero.belongings.weapon = null;
						stat = 0;
					}
					else if (finding == Dungeon.hero.belongings.extra) {
						Dungeon.hero.belongings.extra = null;
						stat = 1;
					}
					else if (finding == Dungeon.hero.belongings.ring) {
						Dungeon.hero.belongings.ring = null;
						stat = 2;
					}
					else if (finding == Dungeon.hero.belongings.misc) {
						Dungeon.hero.belongings.misc = null;
						stat = 3;
					}
					else throw new IllegalStateException();
					detached = finding;
					equipstats.put(finding, stat);
				} else {
					detached = found.get(0).detach(inventory.backpack);
				}
				inputs[curslot].item(detached);
				curslot++;
				needed -= detached.quantity();
				if (detached == found.get(0)) {
					found.remove(0);
				}
			}
		}
		updateState();
	}
	
	@Override
	public void destroy() {
		synchronized ( inputs ) {
			restoreEquipments();
			clearSlots();
			for (int i = 0; i < inputs.length; i++) {
				inputs[i] = null;
			}
		}
		
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
			Journal.saveGlobal();
		} catch (IOException e) {
			CarbonizedPixelDungeon.reportException(e);
		}
		super.destroy();
	}
	
	public void clearSlots() {
		synchronized ( inputs ) {
			for (int i = 0; i < inputs.length; i++) {
				if (inputs[i] != null && inputs[i].item != null) {
					if (!(inputs[i].item instanceof AlchemistsToolkit) && !inputs[i].item.isEquipped( Dungeon.hero )) {
						if (!inputs[i].item.collect()) {
							Dungeon.level.drop(inputs[i].item, Dungeon.hero.pos);
						}
					}
					inputs[i].item(null);
					inputs[i].slot.item(new WndBag.Placeholder(ItemSpriteSheet.SOMETHING));
				}
			}
		}
	}
	
	public static class ItemButton extends Component {
		
		protected NinePatch bg;
		protected ItemSlot slot;
		
		public Item item = null;
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			bg = Chrome.get( Chrome.Type.RED_BUTTON);
			add( bg );
			
			slot = new ItemSlot() {
				@Override
				protected void onPointerDown() {
					bg.brightness( 1.2f );
					Sample.INSTANCE.play( Assets.Sounds.CLICK );
				}
				@Override
				protected void onPointerUp() {
					bg.resetColor();
				}
				@Override
				protected void onClick() {
					ItemButton.this.onClick();
				}

				@Override
				protected boolean onLongClick() {
					return ItemButton.this.onLongClick();
				}
			};
			slot.enable(true);
			add( slot );
		}
		
		protected void onClick() {}
		protected boolean onLongClick() {
			return false;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			bg.x = x;
			bg.y = y;
			bg.size( width, height );
			
			slot.setRect( x + 2, y + 2, width - 4, height - 4 );
		}
		
		public void item( Item item ) {
			slot.item( this.item = item );
		}
	}
	
	private static AlchemyProvider provider;
	
	public static void setProvider( AlchemyProvider p ){
		provider = p;
	}
	
	public static int availableEnergy(){
		return provider == null ? 0 : provider.getEnergy();
	}
	
	public static boolean providerIsToolkit(){
		return provider instanceof AlchemistsToolkit.kitEnergy;
	}
	
	public interface AlchemyProvider {
	
		int getEnergy();
		
		void spendEnergy(int reduction);
	
	}
}
