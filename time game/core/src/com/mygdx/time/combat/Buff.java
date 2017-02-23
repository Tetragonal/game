package com.mygdx.time.combat;

import com.mygdx.time.map.Game;

public class Buff {
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
	
	public boolean isVisible = false;
	
	public Buff(int duration){		
		startTick = Game.gameTick;
		endTick = startTick + duration;
	}
	
	public Buff(){		
		isPermanent = true;
	}
}
