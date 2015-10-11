package gamemodel.impl;

import gamemodel.Board;
import gamemodel.GameAction;
import gamemodel.Piece;


/**
 * lets a character move to another tile
 * This is just a temporary implementation
 */
public class MoveAction implements GameAction {
	int startX;
	int startY;
	int endX;
	int endY;

	public MoveAction(int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}
	
	@Override
	public boolean isValid(Board b) {
		return b.getTile(startX, startY).hasCharacter() && b.onBoard(endX, endY)
			&& !b.isOccupied(endX, endY) && b.isTraversable(endX, endY);
	}

	@Override
	public void doAction(Board b) {
		Piece piece = b.getTile(startX, startY).removePiece();
		b.getTile(endX, endY).placePiece(piece);
	}
	

}
