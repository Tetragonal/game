package attacks;

import org.apache.xmlbeans.impl.xb.xsdschema.DocumentationDocument;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.time.Game;
import com.mygdx.time.TimeGame;
import com.mygdx.time.combat.Attack;
import com.mygdx.time.entities.CollidableEntity;
import com.mygdx.time.entities.Entity;
import com.mygdx.time.entities.Mob;
import com.mygdx.time.manager.EntityLoader;

public class SlashAttack extends Attack{
	float offsetX, offsetY, initialAngle;
	int startTick;
	float rotationRate; //deg/sec
	float totalAngle;
	
	public SlashAttack(String args[], Mob parentMob){
		this(Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), args[3], parentMob, Boolean.parseBoolean(args[4]));
	}
	
	public SlashAttack(float centerAngle, float rotationRate, float totalAngle, String entityName, Mob parentMob, boolean isAlly) {
		super(parentMob.getX()+parentMob.getWidth()/2, parentMob.getY(), entityName, parentMob, isAlly);
		this.damage = parentMob.modifiedAttack*Float.parseFloat(EntityLoader.getValue(entityName, "damage multi", Game.ATTACK));
		initialAngle = centerAngle-totalAngle/2;
		this.rotationRate = rotationRate;
		this.totalAngle = totalAngle;
		this.offsetX = parentMob.sprite.getWidth()/2;
		this.offsetY = 0;
		modifiedMovementSpeed = 0;
		body.setBullet(true);
		worldDestination.set(body.getPosition().x, body.getPosition().y);
		body.setFixedRotation(false);
		startTick = Game.gameTick;

		body.setTransform(0, 0, (float) Math.toRadians(initialAngle));
		float cosX = offsetX*(float)Math.cos(body.getAngle());
		float sinX = offsetX*(float)Math.sin(body.getAngle());
		float sinY = offsetY*(float)Math.sin(body.getAngle());
		float cosY = offsetY*(float)Math.cos(body.getAngle());
		float centerX = parentEntity.getX()+parentEntity.getWidth()/2;
		float centerY = parentEntity.getY()+parentEntity.getHeight()/2;
		int deltaTick = Game.gameTick-startTick;
		//(float)Math.cos(body.getAngle())*sprite.getHeight()/2 is to make the body center (0,sprite.getHeight()/2)
		body.setTransform(centerX+cosX-sinY+(float)Math.sin(body.getAngle())*sprite.getHeight()/2, centerY+sinX+cosY-(float)Math.cos(body.getAngle())*sprite.getHeight()/2, (float) Math.toRadians(initialAngle+deltaTick*rotationRate/Game.ENGINE_FPS));
		float degrees = (float) Math.toDegrees(body.getAngle());
		this.setPosition(centerX+cosX-sinY, centerY+sinX+cosY);
		this.setRotation(degrees);
	    sprite.setPosition(centerX+cosX-sinY, centerY+sinX+cosY);
	    sprite.setRotation(degrees);
	}
	
	@Override
	public void act(float delta){
		float cosX = offsetX*(float)Math.cos(body.getAngle());
		float sinX = offsetX*(float)Math.sin(body.getAngle());
		float sinY = offsetY*(float)Math.sin(body.getAngle());
		float cosY = offsetY*(float)Math.cos(body.getAngle());
		float centerX = parentEntity.getX()+parentEntity.getWidth()/2;
		float centerY = parentEntity.getY()+parentEntity.getHeight()/2;
		int deltaTick = Game.gameTick-startTick;
		body.setTransform(centerX+cosX-sinY+(float)Math.sin(body.getAngle())*sprite.getHeight()/2, centerY+sinX+cosY-(float)Math.cos(body.getAngle())*sprite.getHeight()/2, (float) Math.toRadians(initialAngle+deltaTick*rotationRate/Game.ENGINE_FPS));
		float degrees = (float) Math.toDegrees(body.getAngle());
		this.setPosition(body.getPosition().x, body.getPosition().y);
		this.setRotation(degrees);
	    sprite.setPosition(body.getPosition().x, body.getPosition().y);
	    sprite.setRotation(degrees);
	    if(Math.toDegrees(body.getAngle())-initialAngle>totalAngle){
	    	this.isFlaggedForDelete = true;
	    }
	}
	
	@Override
	public void collideWith(CollidableEntity e){
		if(e instanceof Mob && !collidedEntities.contains(e)){
			((Sound) TimeGame.assets.get("sound/warp2.ogg")).play();
		}
		super.collideWith(e);
	}
}
