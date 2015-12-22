package gameserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import gamemodel.GameModel;
import gamemodel.WinCondition;
import gamemodel.impl.BasicGameModel;
import javafx.scene.control.TextArea;

/**
 * A simple 2 player server
 * TODO: change this to handle number of players depending on the game and lots more
 */
public class GameServer extends Thread { //TODO: should prob implement runnable instead of extending thread
	private static final int PORT_NUMBER = 33334;
	
	TextArea textArea;
	List<Socket> sockets;
	ServerSocket welcomeSocket;
	
	public GameServer(TextArea textArea) {
		this.textArea = textArea;
		sockets = new ArrayList<>();
	}

	@Override
	public void run() {
		try {
			welcomeSocket = new ServerSocket(PORT_NUMBER);
			textArea.appendText("Server using port number " + welcomeSocket.getLocalPort() + "\n");
			
			sockets.add(welcomeSocket.accept());
			textArea.appendText("Player 1 has connected\n");
			sockets.add(welcomeSocket.accept());
			textArea.appendText("Player 2 has connected\n");
			welcomeSocket.close();
			
			BasicGameModel game = new BasicGameModel(textArea);
			//TODO: replace placeholder win condition
			WinCondition cond = new WinCondition() {
				@Override
				public boolean hasWon(GameModel game) {return false;}
			};
			
			Socket socket = sockets.get(0);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input  = new ObjectInputStream(socket.getInputStream());
			String name = (String)input.readObject();
			Player p1 = new Player(name, input, output, cond);
			
			socket = sockets.get(1);
			output = new ObjectOutputStream(socket.getOutputStream());
			input  = new ObjectInputStream(socket.getInputStream());
			name = (String)input.readObject();
			Player p2 = new Player(name, input, output, cond);
			
			game.addPlayers(p1, p2);
			ServerMessage msg = new ServerMessage(ServerMessageType.START_GAME, p1.getName(), p2.getName());
			p1.send(msg);
			p2.send(msg);
			textArea.appendText("Starting game...\n");
			game.start();
		} catch (IOException | ClassNotFoundException exc) {
			textArea.appendText("Setup error: game failed to start\n");
			exc.printStackTrace();
		}
	}
	
	public void shutdown() throws IOException {
		interrupt(); //not sure if necessary
		welcomeSocket.close();
		for (Socket s : sockets) {
			s.close();
		}
	}
}
