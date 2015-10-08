package gamemodel;

import java.util.List;

public abstract class GameCharacter implements Piece {
	private String ownerName;
	
	public GameCharacter(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getOwnerName() {
		return ownerName;
	}
	
	public abstract List<CharacterAction> getActions();
}
