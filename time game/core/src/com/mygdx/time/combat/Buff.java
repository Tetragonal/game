package com.mygdx.time.combat;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.time.Game;

public class Buff {
	
	public static final int NO_STACK_LIMIT = -1;
	
	public int startTick;
	public int endTick;
	public boolean isPermanent = false;
	
	public int flatArmor;
	public double percentArmor;
	
	public int flatFireResist;
	public double percentFireResist;
	
	public int flatIceResist;
	public double percentIceResist;
	
	public int flatLightningResist;
	public double percentLightningResist;
	
	public int flatMovementSpeed;
	public double percentMovementSpeed;
	
	public int flatAttack;
	public double percentAttack;
	
	public double damagePerTick;
	
	public int maxStacks = 1;
	public boolean isVisible = false;
	public int id;
	public Sprite sprite = null;
	
	public Buff(int duration){		
		startTick = Game.gameTick;
		endTick = startTick + duration;
	}
	
	public Buff(){		
		isPermanent = true;
	}
	
	public void setSprite(Sprite sprite){
		isVisible = true;
		this.sprite = sprite;
	}
}
