package gamemodel.impl;

import java.util.UUID;

import gamemodel.Board;
import gamemodel.CharacterAction;
import gamemodel.Piece;


/**
 * lets a character move to another tile
 * This is just a temporary implementation
 */
public class MoveAction extends CharacterAction {
	int startX;
	int startY;
	int endX;
	int endY;

	public MoveAction(UUID charId, int startX, int startY, int endX, int endY) {
		super(charId);
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

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
