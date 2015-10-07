package gamemodel;

public class Board {
	private Tile[][] tiles;
	
	public boolean makeMove(GameAction action) {
		if (!action.isValid(this)) {
			return false;
		} else {
			action.doAction(this);
			return true;
		}
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
}
