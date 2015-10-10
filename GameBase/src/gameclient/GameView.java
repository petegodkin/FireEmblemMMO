package gameclient;

import java.util.List;

import gamemodel.Board;
import gamemodel.GameCharacter;

public class GameView {
	private Board board;
	private List<GameCharacter> characters;
	private String playerName;
	
	public GameView(String playerName, Board board) {
		this.playerName = playerName;
		this.board = board;
		characters = board.getCharactersForPlayer(playerName);
	}
	
	public List<GameCharacter> getCharacters() {
		return characters;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public String getPlayerName() {
		return playerName;
	}
}
