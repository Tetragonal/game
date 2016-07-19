package com.mygdx.time.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.time.entities.Mob;
import com.mygdx.time.entities.Player;
import com.mygdx.time.entities.Projectile;
import com.mygdx.time.entities.WanderingEnemy;
import com.mygdx.time.screens.LevelScreen;

public class TestContactListener implements ContactListener{

	LevelScreen screen;
	public TestContactListener(LevelScreen screen){
		this.screen = screen;
	}
	
	@Override
	public void beginContact(Contact contact) {
		if(contact.getFixtureA().getBody().getUserData() instanceof WanderingEnemy || contact.getFixtureB().getBody().getUserData() instanceof WanderingEnemy){
			//System.out.println("bubly collision");
		}
		
		if(contact.getFixtureA().getBody().getUserData() instanceof Projectile){
			Projectile proj = (Projectile) contact.getFixtureA().getBody().getUserData();
			proj.flagForDelete();
			if(contact.getFixtureB().getBody().getUserData() instanceof Mob){
				Mob mob = (Mob) contact.getFixtureB().getBody().getUserData();
				mob.takeDamage(proj.getDamage());
			}
		}
		
		if(contact.getFixtureB().getBody().getUserData() instanceof Projectile){
			Projectile proj = (Projectile) contact.getFixtureB().getBody().getUserData();
			proj.flagForDelete();
			if(contact.getFixtureA().getBody().getUserData() instanceof Mob){
				Mob mob = (Mob) contact.getFixtureA().getBody().getUserData();
				mob.takeDamage(proj.getDamage());
			}
		}
		
		if(contact.getFixtureA().getBody().getUserData() instanceof MapObject){
			if(contact.getFixtureB().getBody().getUserData() instanceof Player){
				if(((MapObject)(contact.getFixtureA().getBody().getUserData())).getProperties().get("warp") != null){
					screen.setWarpDestination((String)((MapObject)(contact.getFixtureA().getBody().getUserData())).getProperties().get("warp"));
				}
			}
		}
		
		if(contact.getFixtureA().getBody().getUserData() instanceof Player){
			if(contact.getFixtureB().getBody().getUserData() instanceof MapObject){
				if(((MapObject)(contact.getFixtureB().getBody().getUserData())).getProperties().get("warp") != null){
					screen.setWarpDestination((String)((MapObject)(contact.getFixtureB().getBody().getUserData())).getProperties().get("warp"));
				}
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
}
