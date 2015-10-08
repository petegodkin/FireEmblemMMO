package gameclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gamemodel.Board;
import gamemodel.GameCharacter;

public class GameView {
	Board board;
	List<GameCharacter> characters;
	String playerName;
	
	public GameView(String playerName, Board board) {
		this.playerName = playerName;
		this.board = board;
		characters = board.getCharactersForPlayer(playerName);
	}
	
	public List<GameCharacter> getCharacters() {
		return characters;
	}
}
