package gameclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gamemodel.Board;
import gameserver.ServerMessage;

public class GameClient {
	private Board board;
	
	private String name;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public GameClient(String name, ObjectInputStream input, ObjectOutputStream output) {
		this.name = name;
		this.input = input;
		this.output = output;
	}
	
	public ServerMessage makeMove(ClientMessage msg) throws ClassNotFoundException, IOException {
		sendAction(msg);
		return receiveMessage();
	}
	
	private void sendAction(ClientMessage msg) {
		try {
			output.writeObject(msg);
		} catch (IOException e) {
			System.err.println("Failed to send server an action!");
			e.printStackTrace();
		}
	}
	
	private ServerMessage receiveMessage() throws ClassNotFoundException, IOException {
		return (ServerMessage) input.readObject();
	}

	public String getName() {
		return name;
	}
}
