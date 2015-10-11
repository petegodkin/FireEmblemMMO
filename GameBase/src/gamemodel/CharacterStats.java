package gamemodel;

/**
 * These are just sample stats and can be changed later
 */
public class CharacterStats {
	
	private int baseAttack;
	private int baseDefense;
	private int baseHp;
	
	public int attack;
	public int defense;
	private int hp;
	
	public CharacterStats(int h, int a, int d) {
		baseHp = h;
		baseAttack = a;
		baseDefense = d;
		
		hp = baseHp;
		attack = baseAttack;
		defense = baseDefense;
	}

	public void takeDamage(int damage) {
		hp = Math.max(0, hp - damage);
	}
	
	public void heal(int health) {
		hp = Math.min(baseHp, hp + health);
	}
	
}
