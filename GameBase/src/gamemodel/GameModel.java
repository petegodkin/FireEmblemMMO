package gamemodel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import gameserver.Player;
import gameserver.ServerMessage;
import gameserver.ServerMessageType;

public abstract class GameModel {
	protected Board board;
	protected List<Player> players;
	
	public GameModel() {
		players = new ArrayList<>();
	}
	
	
	public void addPlayers(Player...players) {
		this.players.addAll(Arrays.asList(players));
	}
	
	public abstract void start();
	
	//meant to be overridden
	public void log(String msg) {
		System.out.println(msg);
	}
	
	protected List<Player> getWinners() {
		return players.stream().filter(p -> p.hasWon(this)).collect(Collectors.toList());
	}
	
	protected boolean isGameover() {
		return !getWinners().isEmpty();
	}
	
	public void broadcastAction(Object data[], String sender) {
		log("Broadcasting action...");
		for (Player p : players) {
			if (!p.getName().equals(sender)) {
				p.send(new ServerMessage(ServerMessageType.UPDATE_BOARD, data));
			}
		}
	}
	
	public static GameAction createAction(Object data[]) {
		try {
			Class<? extends GameAction> actionClass = (Class<? extends GameAction>)data[0]; 
			Constructor<? extends GameAction> constructor = (Constructor<? extends GameAction>)actionClass.getConstructors()[0];
			return constructor.newInstance(Arrays.copyOfRange(data, 1, data.length));
		} catch (Exception exc) {
			System.err.println("Unable to instantiate game action");
			exc.printStackTrace();
			// TODO: This isn't a great way to do this
			return new GameAction() {
				@Override
				public boolean isValid(Board b) {return false;}
				@Override
				public void doAction(Board b) {}
			};
		}
	}
}
