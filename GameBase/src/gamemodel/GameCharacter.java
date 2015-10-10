package gamemodel;

import java.util.List;

/**
 * All characters should extend this class
 * Or maybe this shouldn't be abstract... idk
 */
public abstract class GameCharacter implements Piece {
	private String ownerName;
	protected CharacterStats stats;
	protected List<CharacterAction> actions;
	
	public GameCharacter(String ownerName, CharacterStats stats, List<CharacterAction> actions) {
		this.ownerName = ownerName;
		this.stats = stats;
		this.actions = actions;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public List<CharacterAction> getActions() {
		return actions;
	}
}
