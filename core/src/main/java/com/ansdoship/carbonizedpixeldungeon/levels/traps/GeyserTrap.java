package com.ansdoship.carbonizedpixeldungeon.levels.traps;

import com.ansdoship.carbonizedpixeldungeon.Assets;
import com.ansdoship.carbonizedpixeldungeon.Dungeon;
import com.ansdoship.carbonizedpixeldungeon.actors.Actor;
import com.ansdoship.carbonizedpixeldungeon.actors.Char;
import com.ansdoship.carbonizedpixeldungeon.actors.blobs.Fire;
import com.ansdoship.carbonizedpixeldungeon.effects.Splash;
import com.ansdoship.carbonizedpixeldungeon.items.wands.WandOfBlastWave;
import com.ansdoship.carbonizedpixeldungeon.mechanics.Ballistica;
import com.ansdoship.carbonizedpixeldungeon.tiles.DungeonTilemap;
import com.ansdoship.carbonizedpixeldungeon.utils.BArray;
import com.ansdoship.pixeldungeonclasses.noosa.audio.Sample;
import com.ansdoship.pixeldungeonclasses.utils.PathFinder;
import com.ansdoship.pixeldungeonclasses.utils.PointF;
import com.ansdoship.pixeldungeonclasses.utils.Random;

import java.util.ArrayList;

public class GeyserTrap extends Trap {

	{
		color = TEAL;
		shape = DIAMOND;
	}

	public int centerKnockBackDirection = -1;

	@Override
	public void activate() {
		Splash.at( DungeonTilemap.tileCenterToWorld( pos ), -PointF.PI/2, PointF.PI/2, 0x5bc1e3, 100, 0.01f);
		Sample.INSTANCE.play(Assets.Sounds.GAS, 1f, 0.75f);

		Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] == 2 && Random.Int(3) > 0){
				Dungeon.level.setCellToWater(true, i);
				if (fire != null){
					fire.clear(i);
				}
			} else if (PathFinder.distance[i] < 2){
				Dungeon.level.setCellToWater(true, i);
				if (fire != null){
					fire.clear(i);
				}
			}
		}

		for (int i : PathFinder.NEIGHBOURS8){
			Char ch = Actor.findChar(pos + i);
			if (ch != null){
				//trace a ballistica to our target (which will also extend past them)
				Ballistica trajectory = new Ballistica(pos, ch.pos, Ballistica.STOP_TARGET);
				//trim it to just be the part that goes past them
				trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
				//knock them back along that ballistica
				WandOfBlastWave.throwChar(ch, trajectory, 2, true);
			}
		}

		Char ch = Actor.findChar(pos);
		if (ch != null){
			int targetpos = -1;
			if (centerKnockBackDirection != -1){
				targetpos = centerKnockBackDirection;
			} else if (ch == Dungeon.hero){
				//if it is the hero, random direction that isn't into a hazard
				ArrayList<Integer> candidates = new ArrayList<>();
				for (int i : PathFinder.NEIGHBOURS8){
					//add as a candidate if both cells on the trajectory are safe
					if (!Dungeon.level.avoid[pos + i] && !Dungeon.level.avoid[pos + i + i]){
						candidates.add(pos + i);
					}
				}
				if (!candidates.isEmpty()){
					targetpos = Random.element(candidates);
				}
			} else {
				//random direction if it isn't the hero
				targetpos = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			}
			if (targetpos != -1){
				//trace a ballistica in the direction of our target
				Ballistica trajectory = new Ballistica(pos, targetpos, Ballistica.MAGIC_BOLT);
				//knock them back along that ballistica
				WandOfBlastWave.throwChar(ch, trajectory, 2, true);
			}
		}
	}
}
