package gamemodel;

public class Tile {
	Piece piece;
	boolean traversable;
	// might also have a type later
	
	public boolean hasPiece() {
		return piece != null;
	}
	
	public boolean hasCharacter() {
		return piece instanceof GameCharacter;
	}
	
	public GameCharacter getCharacter() {
		return (GameCharacter)piece;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public boolean isTraversable() {
		return traversable;
	}
}
