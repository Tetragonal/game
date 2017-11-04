package collisionTest;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class Mob {
	public Polygon p;
	public Rectangle boundingRectangle;
	
	public Mob(Polygon p){
		this.p = p;
		boundingRectangle = p.getBoundingRectangle();
	}
	
	public void move(float x, float y){
		p.translate(x, y);
		
		boundingRectangle.height += y;
		boundingRectangle.width += x;
	}
}
