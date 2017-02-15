package Testing;

import java.util.ArrayList;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class CollisionTest {
	public static final int MAP_SIZE = 1000;
	
	ArrayList<Mob> mobList = new ArrayList<Mob>();
	
	public CollisionTest(int size){
		for(int i=0; i<size; i++){
			float x = (float)Math.random()*MAP_SIZE;
			float y = (float)Math.random()*MAP_SIZE;
			Mob m = new Mob(new Polygon(new float[]{x,y, x+10,y, x+10,y+10, x+5,y+15, x,y+10}));
			mobList.add(m);
		}
	}
	
	public static void main(String args[]){
		CollisionTest ct = new CollisionTest((int)Math.pow(10, 4));
		
		ct.timeCollisionAll();
		ct.timeCollisionPlayer();
	}
	
	public void timeCollisionAll(){
		int overlaps = 0;
		int nonOverlaps = 0;
		
		final long startTime = System.nanoTime();
		for(int i=0; i<mobList.size(); i++){
			Mob m = mobList.get(i);
			Polygon p1 = m.p;
			Rectangle r1 = m.boundingRectangle;
			for(int j=i; j<mobList.size(); j++){
				Rectangle r2 = mobList.get(j).boundingRectangle;
				if(r1.overlaps(r2)){
					Polygon p2 = mobList.get(j).p;
					if(Intersector.overlapConvexPolygons(p1, p2)){
						overlaps++;
					}else{
						nonOverlaps++;
					}
				}else{
					nonOverlaps++;
				}
			}
		}
		final long duration = System.nanoTime() - startTime;
		System.out.println("Checked collision against all entities\nMap size: " + MAP_SIZE + "\nDuration: " + duration/Math.pow(10,6) + " ms\n" + mobList.size() + " Entities, " + (overlaps+nonOverlaps) + " collisions done, " + overlaps + " overlaps, " + nonOverlaps + " non-overlaps\n");
	}
	
	public void timeCollisionPlayer(){
		float x = (float)Math.random()*MAP_SIZE;
		float y = (float)Math.random()*MAP_SIZE;
		Mob player = new Mob(new Polygon(new float[]{x,y, x+10,y, x+10,y+10, x+5,y+15, x,y+10}));
		
		int overlaps = 0;
		int nonOverlaps = 0;
		
		final long startTime = System.nanoTime();
		for(int i=0; i<mobList.size(); i++){
			Mob m = mobList.get(i);
			if(m.boundingRectangle.overlaps(player.boundingRectangle)){
				if(Intersector.overlapConvexPolygons(m.p, player.p)){
					overlaps++;
				}else{
					nonOverlaps++;
				}
			}else{
				nonOverlaps++;
			}

		}
		final long duration = System.nanoTime() - startTime;
		System.out.println("Checked collision against player\nMap size: " + MAP_SIZE + "\nDuration: " + duration/Math.pow(10,6) + " ms\n" + mobList.size() + " Entities, " + (overlaps+nonOverlaps) + " collisions done, " + overlaps + " overlaps, " + nonOverlaps + " non-overlaps\n");
	}
}
