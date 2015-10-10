package gameserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import gamemodel.GameModel;
import gamemodel.WinCondition;
import gamemodel.impl.BasicGameModel;
import javafx.scene.control.TextArea;

/**
 * A simple 2 player server
 * TODO: change this to handle number of players depending on the game and lots more
 */
public class GameServer extends Thread {
	private static final int PORT_NUMBER = 33334;
	
	TextArea textArea;
	
	public GameServer(TextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	public void run() {
		try {
			ServerSocket welcomeSocket = new ServerSocket(PORT_NUMBER);
			textArea.appendText("Server using port number " + welcomeSocket.getLocalPort() + "\n");
	
			Socket p1Socket = welcomeSocket.accept();
			textArea.appendText("Player 1 has connected\n");
			Socket p2Socket = welcomeSocket.accept();
			textArea.appendText("Player 2 has connected\n");
			welcomeSocket.close();
			
			GameModel game = new BasicGameModel();
			//TODO: replace placeholder player names and win condition
			WinCondition cond = new WinCondition() {
				@Override
				public boolean hasWon(GameModel game) {return false;}
			};
			ObjectOutputStream output = new ObjectOutputStream(p1Socket.getOutputStream());
			ObjectInputStream input  = new ObjectInputStream(p1Socket.getInputStream());
			Player p1 = new Player("Player1", input, output, cond);
			
			output = new ObjectOutputStream(p2Socket.getOutputStream());
			input  = new ObjectInputStream(p2Socket.getInputStream());
			Player p2 = new Player("Player2", input, output, cond);
			
			game.addPlayers(p1, p2);
			textArea.appendText("Starting game...\n");
			game.start();
		} catch (IOException exc) {
			textArea.appendText("Setup error: game failed to start\n");
		}
	}
}
