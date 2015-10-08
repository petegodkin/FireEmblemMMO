package gamemodel.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Optional;

import gamemodel.EndTurnAction;
import gamemodel.GameAction;
import gamemodel.GameModel;
import gamemodel.ServerMessage;
import gamemodel.ServerMessageType;
import gameserver.Player;

public class BasicGameModel extends GameModel {
	public BasicGameModel() {
		super(10, 10);
	}

	@Override
	public void start() {
		for (Player p : players) {
			takeTurn(p);
			if (isGameover()) {
				break;
			}
		}
		handleGameover();
	}
	
	private void takeTurn(Player p) {
		p.send(new ServerMessage(ServerMessageType.START_TURN));
		
		while (true) {
			try {
				GameAction action = p.getNextAction();
				if (action instanceof EndTurnAction) {
					break;
				} else {
					if(board.handleAction(action)) {
						p.send(new ServerMessage(ServerMessageType.VALID_MOVE));
					} else {
						p.send(new ServerMessage(ServerMessageType.INVALID_MOVE));
					}
				} 
			} catch (ClassNotFoundException | IOException e) {
				// TODO: notify player their move was invalid
				// This shouldn't really have to happen unless the client is messed with
				// to make invalid requests. If that's the case there are plenty of other
				// security flaws we need to worry about.
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
