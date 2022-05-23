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

package com.ansdoship.carbonizedpixeldungeon;

import com.badlogic.gdx.Input;
import com.ansdoship.pixeldungeonclasses.input.GameAction;
import com.ansdoship.pixeldungeonclasses.input.KeyBindings;
import com.ansdoship.pixeldungeonclasses.utils.Bundle;
import com.ansdoship.pixeldungeonclasses.utils.FileUtils;

import java.io.IOException;
import java.util.LinkedHashMap;

public class PDAction extends GameAction {

	protected PDAction(String name ){
		super( name );
	}

	//--New references to existing actions from GameAction
	public static final GameAction NONE  = GameAction.NONE;
	public static final GameAction BACK  = GameAction.BACK;
	//--

	public static final GameAction HERO_INFO   = new PDAction("hero_info");
	public static final GameAction JOURNAL     = new PDAction("journal");

	public static final GameAction WAIT        = new PDAction("wait");
	public static final GameAction SEARCH      = new PDAction("search");
	public static final GameAction REST        = new PDAction("rest");

	public static final GameAction INVENTORY   = new PDAction("inventory");
	public static final GameAction QUICKSLOT_1 = new PDAction("quickslot_1");
	public static final GameAction QUICKSLOT_2 = new PDAction("quickslot_2");
	public static final GameAction QUICKSLOT_3 = new PDAction("quickslot_3");
	public static final GameAction QUICKSLOT_4 = new PDAction("quickslot_4");
	public static final GameAction QUICKSLOT_5 = new PDAction("quickslot_5");
	public static final GameAction QUICKSLOT_6 = new PDAction("quickslot_6");

	public static final GameAction TAG_ATTACK  = new PDAction("tag_attack");
	public static final GameAction TAG_DANGER  = new PDAction("tag_danger");
	public static final GameAction TAG_ACTION  = new PDAction("tag_action");
	public static final GameAction TAG_LOOT    = new PDAction("tag_loot");
	public static final GameAction TAG_RESUME  = new PDAction("tag_resume");

	public static final GameAction ZOOM_IN     = new PDAction("zoom_in");
	public static final GameAction ZOOM_OUT    = new PDAction("zoom_out");

	public static final GameAction N           = new PDAction("n");
	public static final GameAction E           = new PDAction("e");
	public static final GameAction S           = new PDAction("s");
	public static final GameAction W           = new PDAction("w");
	public static final GameAction NE          = new PDAction("ne");
	public static final GameAction SE          = new PDAction("se");
	public static final GameAction SW          = new PDAction("sw");
	public static final GameAction NW          = new PDAction("nw");

	private static final LinkedHashMap<Integer, GameAction> defaultBindings = new LinkedHashMap<>();
	static {
		defaultBindings.put( Input.Keys.ESCAPE,      PDAction.BACK );
		defaultBindings.put( Input.Keys.BACKSPACE,   PDAction.BACK );

		defaultBindings.put( Input.Keys.H,           PDAction.HERO_INFO );
		defaultBindings.put( Input.Keys.J,           PDAction.JOURNAL );

		defaultBindings.put( Input.Keys.SPACE,       PDAction.WAIT );
		defaultBindings.put( Input.Keys.S,           PDAction.SEARCH );
		defaultBindings.put( Input.Keys.Z,           PDAction.REST );

		defaultBindings.put( Input.Keys.I,           PDAction.INVENTORY );
		defaultBindings.put( Input.Keys.NUM_1,       PDAction.QUICKSLOT_1 );
		defaultBindings.put( Input.Keys.NUM_2,       PDAction.QUICKSLOT_2 );
		defaultBindings.put( Input.Keys.NUM_3,       PDAction.QUICKSLOT_3 );
		defaultBindings.put( Input.Keys.NUM_4,       PDAction.QUICKSLOT_4 );
		defaultBindings.put( Input.Keys.NUM_5,       PDAction.QUICKSLOT_5 );
		defaultBindings.put( Input.Keys.NUM_6,       PDAction.QUICKSLOT_6 );

		defaultBindings.put( Input.Keys.A,           PDAction.TAG_ATTACK );
		defaultBindings.put( Input.Keys.TAB,         PDAction.TAG_DANGER );
		defaultBindings.put( Input.Keys.D,           PDAction.TAG_ACTION );
		defaultBindings.put( Input.Keys.ENTER,       PDAction.TAG_LOOT );
		defaultBindings.put( Input.Keys.T,           PDAction.TAG_RESUME );

		defaultBindings.put( Input.Keys.PLUS,        PDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.EQUALS,      PDAction.ZOOM_IN );
		defaultBindings.put( Input.Keys.MINUS,       PDAction.ZOOM_OUT );

		defaultBindings.put( Input.Keys.UP,          PDAction.N );
		defaultBindings.put( Input.Keys.RIGHT,       PDAction.E );
		defaultBindings.put( Input.Keys.DOWN,        PDAction.S );
		defaultBindings.put( Input.Keys.LEFT,        PDAction.W );

		defaultBindings.put( Input.Keys.NUMPAD_5,    PDAction.WAIT );
		defaultBindings.put( Input.Keys.NUMPAD_8,    PDAction.N );
		defaultBindings.put( Input.Keys.NUMPAD_9,    PDAction.NE );
		defaultBindings.put( Input.Keys.NUMPAD_6,    PDAction.E );
		defaultBindings.put( Input.Keys.NUMPAD_3,    PDAction.SE );
		defaultBindings.put( Input.Keys.NUMPAD_2,    PDAction.S );
		defaultBindings.put( Input.Keys.NUMPAD_1,    PDAction.SW );
		defaultBindings.put( Input.Keys.NUMPAD_4,    PDAction.W );
		defaultBindings.put( Input.Keys.NUMPAD_7,    PDAction.NW );
	}

	public static LinkedHashMap<Integer, GameAction> getDefaults() {
		return new LinkedHashMap<>(defaultBindings);
	}

	//hard bindings for android devices
	static {
		KeyBindings.addHardBinding( Input.Keys.BACK, PDAction.BACK );
		KeyBindings.addHardBinding( Input.Keys.MENU, PDAction.INVENTORY );
	}

	//we only save/loads keys which differ from the default configuration.
	private static final String BINDINGS_FILE = "keybinds.dat";

	public static void loadBindings(){

		if (!KeyBindings.getAllBindings().isEmpty()){
			return;
		}

		try {
			Bundle b = FileUtils.bundleFromFile(BINDINGS_FILE);

			Bundle firstKeys = b.getBundle("first_keys");
			Bundle secondKeys = b.getBundle("second_keys");

			LinkedHashMap<Integer, GameAction> defaults = getDefaults();
			LinkedHashMap<Integer, GameAction> custom = new LinkedHashMap<>();

			for (GameAction a : allActions()) {
				if (firstKeys.contains(a.name())) {
					if (firstKeys.getInt(a.name()) == 0){
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								defaults.remove(i);
								break;
							}
						}
					} else {
						custom.put(firstKeys.getInt(a.name()), a);
						defaults.remove(firstKeys.getInt(a.name()));
					}
				}

				//we store any custom second keys in defaults for the moment to preserve order
				//incase the 2nd key is custom but the first one isn't
				if (secondKeys.contains(a.name())) {
					if (secondKeys.getInt(a.name()) == 0){
						int last = 0;
						for (int i : defaults.keySet()){
							if (defaults.get(i) == a){
								last = i;
							}
						}
						defaults.remove(last);
					} else {
						defaults.remove(secondKeys.getInt(a.name()));
						defaults.put(secondKeys.getInt(a.name()), a);
					}
				}

			}

			//now merge them and store
			for( int i : defaults.keySet()){
				if (i != 0) {
					custom.put(i, defaults.get(i));
				}
			}

			KeyBindings.setAllBindings(custom);

		} catch (Exception e){
			KeyBindings.setAllBindings(getDefaults());
		}

	}

	public static void saveBindings(){

		Bundle b = new Bundle();

		Bundle firstKeys = new Bundle();
		Bundle secondKeys = new Bundle();

		for (GameAction a : allActions()){
			int firstCur = 0;
			int secondCur = 0;
			int firstDef = 0;
			int secondDef = 0;

			for (int i : defaultBindings.keySet()){
				if (defaultBindings.get(i) == a){
					if(firstDef == 0){
						firstDef = i;
					} else {
						secondDef = i;
					}
				}
			}

			LinkedHashMap<Integer, GameAction> curBindings = KeyBindings.getAllBindings();
			for (int i : curBindings.keySet()){
				if (curBindings.get(i) == a){
					if(firstCur == 0){
						firstCur = i;
					} else {
						secondCur = i;
					}
				}
			}

			if (firstCur != firstDef){
				firstKeys.put(a.name(), firstCur);
			}
			if (secondCur != secondDef){
				secondKeys.put(a.name(), secondCur);
			}

		}

		b.put("first_keys", firstKeys);
		b.put("second_keys", secondKeys);

		try {
			FileUtils.bundleToFile(BINDINGS_FILE, b);
		} catch (IOException e) {
			CarbonizedPixelDungeon.reportException(e);
		}

	}

}
