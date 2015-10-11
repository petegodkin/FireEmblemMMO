package gamemodel.impl;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.lang.reflect.Constructor;

import gameclient.ClientMessage;
import gameclient.ClientMessageType;
import gamemodel.Board;
import gamemodel.GameAction;
import gamemodel.GameModel;
import gameserver.Player;
import gameserver.ServerMessage;
import gameserver.ServerMessageType;
import javafx.scene.control.TextArea;

public class BasicGameModel extends GameModel {

	private TextArea textArea;
	
	public BasicGameModel(TextArea textArea) {
		this.textArea = textArea;
	}
	
	@Override
	public void start() {
		List<String> playerNames = players.stream().map(p -> p.getName()).collect(Collectors.toList());
		board = new Board(new File("Map1.txt"), playerNames);
		
		try {
			//TODO: fix way to many isGameover checks
			while (!isGameover()) {
				for (Player p : players) {
					takeTurn(p);
					if (isGameover()) {
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		handleGameover();
	}
	
	@Override
	public void log(String msg) {
		textArea.appendText(msg + "\n");
	}
	
	private void takeTurn(Player p) throws IOException {
		p.send(new ServerMessage(ServerMessageType.START_TURN));
		
		while (true) {
			try {
				ClientMessage clientMsg = p.getNextClientMessage();
				if (clientMsg.type == ClientMessageType.END_TURN) {
					break;
				} else if (clientMsg.type == ClientMessageType.GAME_ACTION) {
					GameAction action = createAction(clientMsg.data);
					if(board.handleAction(action)) {
						p.send(new ServerMessage(ServerMessageType.VALID_MOVE));
						broadcastAction(clientMsg.data, p.getName());
					} else {
						p.send(new ServerMessage(ServerMessageType.INVALID_MOVE));
					}
				} 
			} catch (ClassNotFoundException e) {
				p.send(new ServerMessage(ServerMessageType.INVALID_MOVE));
				e.printStackTrace();
			}
			if (isGameover()) {
				return;
			}
		}
	}
	
	private void handleGameover() {
		List<Player> winners = getWinners();
		for (Player p : players) {
			if (winners.contains(p)) {
				p.send(new ServerMessage(ServerMessageType.YOU_WIN));
			} else {
				p.send(new ServerMessage(ServerMessageType.YOU_LOSE));
			}
		}
	}
}
