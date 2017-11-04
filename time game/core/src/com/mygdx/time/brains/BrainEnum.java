package com.mygdx.time.brains;

public enum BrainEnum {
	NONE{public Brain createBrain(String[] args){return null;}},
	STATIONARY{public Brain createBrain(String[] args){return new Brain();}},
	WANDERING{public Brain createBrain(String[] args){return new WanderingBrain(args);}},
	AGGRO_ENEMY_TEST{public Brain createBrain(String[] args){return new AggroEnemyTestBrain(args);}},
	SIMPLE_AGGRO{public Brain createBrain(String[] args){return new SimpleAggroBrain(args);}};
	public abstract Brain createBrain(String[] args);
}
