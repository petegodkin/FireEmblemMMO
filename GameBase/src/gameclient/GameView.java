package gameclient;

import java.awt.Point;
import java.util.List;
import java.util.UUID;

import gamemodel.Board;
import gamemodel.GameAction;
import gamemodel.GameCharacter;
import gamemodel.GameModel;
import gamemodel.Tile;
import gamemodel.impl.MoveAction;
import gameserver.ServerMessage;
import gameserver.ServerMessageType;

public class GameView {
	private Board board;
	private List<GameCharacter> characters;
	private String playerName;
	private Point selected;
	public Point cursor;
	private Point destination;
	private Point target;
	private ViewState state;
	
	public GameView(String playerName, Board board) {
		this.playerName = playerName;
		this.board = board;
		characters = board.getCharactersForPlayer(playerName);
		cursor = new Point(0,0);
		state = ViewState.OTHER_TURN;
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
	
	public ViewState getState() {
		return state;
	}
	
	public void startTurn() {
		state = ViewState.SELECTING;
	}
	
	public void select() {
		
		if (state == ViewState.OTHER_TURN) {
			//do nothing for now
		} else if (state == ViewState.SELECTING) {
			Tile t = board.getTile(cursor);
			if (t.hasCharacter(playerName) && !t.getCharacter().hasDoneAction()) {
				selected = cursor;
				state = ViewState.MOVING;
			} else {
				state = ViewState.MENU;
			}
		} else if (state == ViewState.MOVING) {
			Tile t = board.getTile(cursor);
			if (t.hasCharacter(playerName) && !t.getCharacter().hasDoneAction()) {
				selected = cursor;
			//need to add move range
			} else if (t.hasPiece() || !t.isTraversable()) {
				state = ViewState.SELECTING;
			} else {
				destination = cursor;
				state = ViewState.CHARACTER_MENU;
			}
		} else if (state == ViewState.TARGETING) {
			//TODO: if valid target then set it as target
		} else {	
			state = ViewState.SELECTING;
		}
	}

	public void deselect() {
		selected = null;
	}
	
	public Point getSelected() {
		return selected;
	}
	
	public boolean hasSelected() {
		return selected != null;
	}
	
	public Point getDestination() {
		return destination;
	}
}
