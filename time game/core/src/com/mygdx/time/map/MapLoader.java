package com.mygdx.time.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
import com.mygdx.time.Game;

public class MapLoader {
	
	TiledMap map;
	Array<Body> mapObjects;
	World world;
	public MapLoader(TiledMap map, World world){
		this.map = map;
		this.world = world;
	}
	
	public void loadLayer(int layer, short bitCategory){
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
			      Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / Game.PPM,
			            (rectangle.y + rectangle.height * 0.5f ) / Game.PPM);
			      polygon.setAsBox(rectangle.width * 0.5f / Game.PPM,
			            rectangle.height * 0.5f / Game.PPM,
			            size,
			            0.0f);
			      shape = polygon;
			}else if(obj instanceof PolygonMapObject){
				PolygonMapObject polygonObject = (PolygonMapObject)obj;
			      float[] vertices = polygonObject.getPolygon().getTransformedVertices();
			      float[] worldVertices = new float[vertices.length];
			      for (int i = 0; i < vertices.length; ++i) {
			         worldVertices[i] = vertices[i] / Game.PPM;
			      }
			      polygonShape.set(worldVertices);
			      shape = polygonShape;
			}else if(obj instanceof EllipseMapObject){ //can only be circles
			      Ellipse ellipse = ((EllipseMapObject) obj).getEllipse();
			      CircleShape circleShape = new CircleShape();
			      circleShape.setRadius(-1f);
			      if(ellipse.width == ellipse.height){     
				      circleShape.setRadius(ellipse.width / Game.PPM / 2);
				      circleShape.setPosition(new Vector2((ellipse.x+ellipse.width/2) / Game.PPM, (ellipse.y+ellipse.height/2) / Game.PPM));
			      }
			      shape = circleShape;
			}
		      FixtureDef fixtureDef = new FixtureDef();
		      fixtureDef.filter.categoryBits = bitCategory;
    		  fixtureDef.filter.maskBits = -1;
		      if(layer == Game.WARP_LAYER){
	    		  bd.type = BodyDef.BodyType.StaticBody;
	    		  fixtureDef.isSensor = true;
		      }
		      if(shape != null){
		         fixtureDef.shape = shape;
		      }
		     Body body =  world.createBody(bd);
		     body.setUserData(obj);
		     if(shape.getRadius() != -1f){
		         body.createFixture(fixtureDef);
			      j++;
		     }
		}
		System.out.println("number of objects created: " + j);
	}
	
}
