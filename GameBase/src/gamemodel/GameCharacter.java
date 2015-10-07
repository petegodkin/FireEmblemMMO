package gamemodel;

import java.util.List;

public abstract class GameCharacter implements Piece {
	public abstract List<CharacterAction> getActions();
}
