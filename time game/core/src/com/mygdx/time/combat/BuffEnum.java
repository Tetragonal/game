package com.mygdx.time.combat;

import com.mygdx.time.Game;

public enum BuffEnum {
	
	POTION_REGEN{
		public Buff getBuff(String[] params){
			Buff b = new Buff((int)Float.parseFloat(params[0]));
			b.damagePerTick=-Double.parseDouble(params[1])/Game.ENGINE_FPS;
			b.id = 1;
			return b;
		}
	},
	
	BLIZZARD_CHILL{
		public Buff getBuff(String[] params){
			Buff b = new Buff();
			b.percentMovementSpeed = -60;
			return b;
		}
	},
	
	CHILL{
		public Buff getBuff(String[] params){
			Buff b = new Buff();
			b.percentMovementSpeed = -Integer.parseInt(params[0]);
			return b;
		}
	},
	
	ARMOR_BREAK{
		public Buff getBuff(String[] params){
			Buff b = new Buff((int) (Game.ENGINE_FPS*10));
			b.flatArmor = -Integer.parseInt(params[0]);
			b.maxStacks = 10;
			return b;
		}
	};
	
	Buff buff;
	
	public abstract Buff getBuff(String[] params);
}
