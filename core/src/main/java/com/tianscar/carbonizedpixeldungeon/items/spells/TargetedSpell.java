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

package com.tianscar.carbonizedpixeldungeon.items.spells;

import com.tianscar.carbonizedpixeldungeon.Assets;
import com.tianscar.carbonizedpixeldungeon.actors.Actor;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.Invisibility;
import com.tianscar.carbonizedpixeldungeon.actors.buffs.MagicImmune;
import com.tianscar.carbonizedpixeldungeon.actors.hero.Hero;
import com.tianscar.carbonizedpixeldungeon.effects.MagicMissile;
import com.tianscar.carbonizedpixeldungeon.items.wands.Wand;
import com.tianscar.carbonizedpixeldungeon.mechanics.Ballistica;
import com.tianscar.carbonizedpixeldungeon.messages.Messages;
import com.tianscar.carbonizedpixeldungeon.noosa.audio.Sample;
import com.tianscar.carbonizedpixeldungeon.scenes.CellSelector;
import com.tianscar.carbonizedpixeldungeon.scenes.GameScene;
import com.tianscar.carbonizedpixeldungeon.ui.QuickSlotButton;
import com.tianscar.carbonizedpixeldungeon.utils.Callback;
import com.tianscar.carbonizedpixeldungeon.utils.GLog;

public abstract class TargetedSpell extends Spell {
	
	protected int collisionProperties = Ballistica.PROJECTILE;

	protected int collisionProperties(int target) {
		return collisionProperties;
	}
	
	@Override
	protected void onCast(Hero hero) {
		GameScene.selectCell(targeter);
	}
	
	protected abstract void affectTarget( Ballistica bolt, Hero hero );
	
	protected void fx( Ballistica bolt, Callback callback ) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.MAGIC_MISSILE,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}
	
	private  static CellSelector.Listener targeter = new  CellSelector.Listener(){
		
		@Override
		public void onSelect( Integer target ) {
			
			if (target != null) {
				
				//FIXME this safety check shouldn't be necessary
				//it would be better to eliminate the curItem static variable.
				final TargetedSpell curSpell;
				if (curItem instanceof TargetedSpell) {
					curSpell = (TargetedSpell)curItem;
				} else {
					return;
				}
				
				final Ballistica shot = new Ballistica( curUser.pos, target, curSpell.collisionProperties(target) );
				int cell = shot.collisionPos;

				if (target == curUser.pos || cell == curUser.pos) {
					if (target == curUser.pos && curSpell instanceof ElementalHeart) {
						GLog.i( Messages.get(Wand.class, "self_target") );
						return;
					}
				}
				
				curUser.sprite.zap(cell);
				
				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));

				if (curUser.buff(MagicImmune.class) != null) {
					GLog.w( Messages.get(this, "no_magic") );
					return;
				}
				
				curUser.busy();
				
				curSpell.fx(shot, new Callback() {
					public void call() {
						curSpell.affectTarget(shot, curUser);
						curSpell.detach( curUser.belongings.backpack );
						Invisibility.dispel();
						curSpell.updateQuickslot();
						curUser.spendAndNext( 1f );
					}
				});
				
			}
				
		}
		
		@Override
		public String prompt() {
			return Messages.get(TargetedSpell.class, "prompt");
		}
	};
	
}
