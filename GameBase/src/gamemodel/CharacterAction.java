package gamemodel;

import java.util.UUID;

public abstract class CharacterAction implements GameAction {
	protected UUID charId;
	
	protected CharacterAction(UUID charId) {
		this.charId = charId;
	}
	
	public abstract String getName();
	public abstract String getDescription();
	@Override
	public abstract boolean isValid(Board b);
	@Override
	public abstract void doAction(Board b); 
}
