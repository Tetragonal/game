package com.mygdx.time.map;

import static com.mygdx.time.screens.LevelScreen.PPM;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class MapLoader {
	TiledMap map;
	Array<Body> mapObjects;
	public MapLoader(TiledMap map, int layer, World world){
		this.map = map;
		int j=0;
		
		Shape shape = null;
		PolygonShape polygonShape = new PolygonShape();
		BodyDef bd = new BodyDef();
		bd.type = BodyDef.BodyType.StaticBody;
		for(MapObject obj : map.getLayers().get(layer).getObjects()){
			if(obj instanceof RectangleMapObject){
				RectangleMapObject rectangleObject = (RectangleMapObject)obj;
			      Rectangle rectangle = ((RectangleMapObject) rectangleObject).getRectangle();
			      PolygonShape polygon = new PolygonShape();
			      Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
			            (rectangle.y + rectangle.height * 0.5f ) / PPM);
			      polygon.setAsBox(rectangle.width * 0.5f / PPM,
			            rectangle.height * 0.5f / PPM,
			            size,
			            0.0f);
			      shape = polygon;
			}else if(obj instanceof PolygonMapObject){
				PolygonMapObject polygonObject = (PolygonMapObject)obj;
			      float[] vertices = polygonObject.getPolygon().getTransformedVertices();

			      float[] worldVertices = new float[vertices.length];

			      for (int i = 0; i < vertices.length; ++i) {
			         worldVertices[i] = vertices[i] / PPM;
			      }
			      polygonShape.set(worldVertices);
			      shape = polygonShape;
			}else if(obj instanceof EllipseMapObject){ //can only be circles
			      Ellipse ellipse = ((EllipseMapObject) obj).getEllipse();
			      CircleShape circleShape = new CircleShape();
			      circleShape.setRadius(-1f);
			      if(ellipse.width == ellipse.height){     
				      circleShape.setRadius(ellipse.width / PPM / 2);
				      circleShape.setPosition(new Vector2(ellipse.x / PPM, ellipse.y / PPM));
			      }
			      shape = circleShape;
			}
		      FixtureDef fixtureDef = new FixtureDef();
		      if(shape != null){
		         fixtureDef.shape = shape;
		      }
		     Body body =  world.createBody(bd);
		     if(shape.getRadius() != -1f){
		         body.createFixture(fixtureDef);
			      j++;
		     }
		}
		System.out.println("number of objects: " + j);
	}
}
