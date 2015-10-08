package gameserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gamemodel.GameAction;
import gamemodel.GameModel;
import gamemodel.ServerMessage;
import gamemodel.WinCondition;

public class Player {	
	private String name;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private WinCondition winCondition;
	
	public Player(String name, ObjectInputStream input, ObjectOutputStream output, 
			WinCondition winCondition) {
		this.name = name;
		this.input = input;
		this.output = output;
		this.winCondition = winCondition;
	}

	public boolean hasWon(GameModel game) {
		return winCondition.hasWon(game);
	}
	
	public void send(ServerMessage message) {
		try {
			output.writeObject(message);
		} catch (IOException e) {
			System.err.println("Failed to send " + name + " a message!");
			e.printStackTrace();
		}
	}

	public GameAction getNextAction() throws ClassNotFoundException, IOException {
		return (GameAction)input.readObject();
	}

	public String getName() {
		return name;
	}
}
