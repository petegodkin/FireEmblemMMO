package gamemodel;

import java.util.List;

/**
 * All characters should extend this class
 * Not sure if this should be abstract or not
 */
public class GameCharacter implements Piece {
	private String ownerName;
	protected CharacterStats stats;
	protected List<ActionType> actions;
	
	public GameCharacter(String ownerName, CharacterStats stats, List<ActionType> actions) {
		this.ownerName = ownerName;
		this.stats = stats;
		this.actions = actions;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public List<ActionType> getActions() {
		return actions;
	}
}
