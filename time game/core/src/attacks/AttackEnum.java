package attacks;

import com.mygdx.time.combat.Attack;
import com.mygdx.time.entities.Mob;

public enum AttackEnum {
	SLASH{public Attack createAttack(String[] args, Mob parentMob){return new SlashAttack(args, parentMob);}};
	public abstract Attack createAttack(String[] args, Mob parentMob);
}
