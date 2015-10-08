package gamemodel.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Constructor;

import gameclient.ClientMessage;
import gameclient.ClientMessageType;
import gamemodel.Board;
import gamemodel.GameAction;
import gamemodel.GameModel;
import gameserver.Player;
import gameserver.ServerMessage;
import gameserver.ServerMessageType;

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
				ClientMessage clientMsg = p.getNextClientMessage();
				if (clientMsg.type == ClientMessageType.END_TURN) {
					break;
				} else { //type GameAction
					GameAction action = createAction(clientMsg);
					if(board.handleAction(action)) {
						p.send(new ServerMessage(ServerMessageType.VALID_MOVE));
					} else {
						p.send(new ServerMessage(ServerMessageType.INVALID_MOVE));
					}
				} 
			} catch (ClassNotFoundException | IOException e) {
				// This shouldn't really have to happen unless the client is messed with
				// to make invalid requests. If that's the case there are plenty of other
				// security flaws we need to worry about.
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
	
	private GameAction createAction(ClientMessage msg) {
		try {
			Object data[] = msg.data;
			Constructor<? extends GameAction> constructor = (Constructor<? extends GameAction>)data[0];
			return constructor.newInstance(Arrays.copyOfRange(data, 1, data.length));
		} catch (Exception exc) {
			System.err.println("Unable to instantiate game action");
			exc.printStackTrace();
			// TODO: This isn't a great way to do this but returning null seems worse
			return new GameAction() {
				@Override
				public boolean isValid(Board b) {return false;}
				@Override
				public void doAction(Board b) {}
			};
		}
	}
}
