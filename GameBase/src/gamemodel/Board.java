package gamemodel;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private Tile tiles[][];
	
	public Board(int xSize, int ySize) {
		tiles = new Tile[xSize][ySize];
	}
	
	public boolean handleAction(GameAction action) {
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
	
	public Tile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	// Not really meant to be called over and over
	public List<GameCharacter> getCharactersForPlayer(String playerName) {
		List<GameCharacter> characters = new ArrayList<>();
		for (Tile col[] : tiles) {
			for (Tile t : col) {
				if (t.isTraversable() && t.hasCharacter() && 
						playerName.equals(t.getCharacter().getOwnerName()))
				characters.add(t.getCharacter());
			}
		}
		return characters;
	}
}
