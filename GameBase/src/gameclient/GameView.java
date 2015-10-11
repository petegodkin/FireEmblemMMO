package gameclient;

import java.awt.Point;
import java.util.List;

import gamemodel.Board;
import gamemodel.GameAction;
import gamemodel.GameCharacter;
import gamemodel.GameModel;
import gameserver.ServerMessage;
import gameserver.ServerMessageType;

public class GameView {
	private Board board;
	private List<GameCharacter> characters;
	private String playerName;
	public Point selected;
	
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
	
	public void update(ServerMessage msg) {
		assert(msg.type == ServerMessageType.UPDATE_BOARD);
		GameAction action = GameModel.createAction(msg.data);
		if (action.isValid(board)) {
			action.doAction(board);
		} else {
			System.err.println("Received invalid action!");
		}
	}
}
