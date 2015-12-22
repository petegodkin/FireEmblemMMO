package gamemodel;

import java.util.List;
import java.util.UUID;

/**
 * Not sure if this should be abstract or not
 */
public class GameCharacter implements Piece {
	private UUID id;
	private String ownerName;
	protected CharacterStats stats;
	protected List<ActionType> actions;
	protected boolean hasDoneAction;
	
	public GameCharacter(String ownerName, CharacterStats stats, List<ActionType> actions) {
		this.ownerName = ownerName;
		this.stats = stats;
		this.actions = actions;
		hasDoneAction = false;
		id = UUID.randomUUID();
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public List<ActionType> getActions() {
		return actions;
	}
	
	public void startTurn() {
		hasDoneAction = false;
	}
	
	// called when a turn is ended
	public void endTurn() {
	}
	
	public UUID getId() {
		return id;
	}
	
	public boolean hasDoneAction() {
		return hasDoneAction;
	}
	
	public void doAction() {
		hasDoneAction = true;
	}
}
