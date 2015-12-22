package gameclient;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gamemodel.Board;

import gameserver.ServerMessage;
import gameserver.ServerMessageType;

public class GameClient implements Runnable {
	private GameView gameView;
	private GameDisplay gameDisplay;
	
	private String username;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Socket socket;
	private boolean gameover;
	
	public GameClient() {
		gameDisplay = new GameDisplay();
	}
	
	public void connect(String username, String hostname, int portNumber) throws IOException {
		socket = new Socket(hostname, portNumber);
		output = new ObjectOutputStream(socket.getOutputStream());
		input  = new ObjectInputStream(socket.getInputStream());
		output.writeObject(username);
		this.username = username;
		gameover = false;
	}
	
	public void disconect() throws IOException {
		socket.close();
	}
	
	@Override
	public void run() {
		try {
			ServerMessage msg = receiveMessage();
			assert(msg.type == ServerMessageType.START_GAME);
			List<String> playerNames = new ArrayList<>();
			for (Object o : msg.data) {
				playerNames.add((String)o);
			}
			
			gameView = new GameView(username, new Board(new File("Map1.txt"), playerNames));
			gameDisplay.setGameView(gameView);
			gameDisplay.update();
			
			while (true) {
				System.out.println("Listening for a msg");
				msg = receiveMessage();
				System.out.println("received a msg");
				switch (msg.type) {
					case START_TURN:
						takeTurn();
						break;
					case YOU_WIN:
						gameDisplay.youWon();
						break;
					case YOU_LOSE:
						gameDisplay.youLost();
						break;
					case UPDATE_BOARD:
						System.out.println("Received board update");
						gameView.update(msg);
						gameDisplay.update();
						break;
					default:
						System.err.println("Received wrong unexpected message type");
						break;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("Failed in recieving server message");
			gameover = true;
			e.printStackTrace();
		}
	}
	
	public void sendMessage(ClientMessage msg) {
		try {
			output.writeObject(msg);
		} catch (IOException e) {
			System.err.println("Failed to send server a message!");
			e.printStackTrace();
		}
	}
	
	public void sendAction(Object... data) {
		ClientMessage msg = new ClientMessage(ClientMessageType.GAME_ACTION, data);
		sendMessage(msg);
	}
	
	private ServerMessage receiveMessage() throws ClassNotFoundException, IOException {
		return (ServerMessage) input.readObject();
	}

	public String getUsername() {
		return username;
	}
	
	public GameView getGameView() {
		return gameView;
	}
	
	public GameDisplay getGameDisplay() {
		return gameDisplay;
	}
	
	public void takeTurn() throws ClassNotFoundException, IOException {
		gameView.startTurn();
		gameDisplay.startTurn();
		ServerMessage msg;
		boolean myTurn = true;
		
		while (myTurn && !gameover) {
			msg = receiveMessage(); //Player makes move and waits for server response
			if (msg.type == ServerMessageType.VALID_MOVE) {
				System.out.println("Move was valid.");
			} else if (msg.type == ServerMessageType.YOU_LOSE) {
				gameDisplay.youLost();
				gameover = true;
			} else if (msg.type == ServerMessageType.YOU_WIN) {
				gameDisplay.youWon();
				gameover = true;
			} else if (msg.type == ServerMessageType.INVALID_MOVE) {
				System.err.println("Invalid move!");
				gameover = true;
			} else {
				System.err.println("Should not be in this else!");
			}
			gameDisplay.update();
		}
		
		gameDisplay.endTurn();
		myTurn = false;
	}
	
	public boolean isMyTurn() {
		return !gameover && gameView.getState() != ViewState.OTHER_TURN;
	}
}
