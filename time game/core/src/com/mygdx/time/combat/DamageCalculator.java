package com.mygdx.time.combat;

import com.mygdx.time.entities.Mob;

public class DamageCalculator {

	public static final int PHYSICAL = 0,
							FIRE = 1,
							ICE = 2,
							LIGHTNING = 3;
	
	
	public static float calculateDamage(Mob m, int type, double damage){
		switch(type){
			case PHYSICAL:
				//phys dmg formula
				return (float)Math.max(0, damage - m.modifiedArmor*0.5 - (1-m.modifiedArmor*0.005));
			case FIRE:
				//fire dmg formula
				return (float)damage;
			case ICE:
				//ice dmg formula
				return (float)damage;
			case LIGHTNING:
				//lightning dmg formula
				return (float)damage;
			default:
				System.out.println("Damage type error");
				return 0;
		}
	}
}
