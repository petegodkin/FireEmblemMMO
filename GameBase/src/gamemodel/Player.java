package gamemodel;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public interface Player {
	/**
	 * Called at start of a player's turn to notify the player.
	 */
	void startTurn();
	
	/**
	 * Listens to client for GameActions that will get validated by the server and the run
	 */
	ObjectInputStream getInputStream();
	/**
	 * Used to send messages to the client. TODO: Not sure what yet...
	 */
	ObjectOutputStream getOutputStream();
	/**
	 * Called at end of a player's turn to notify the player.
	 */
	void finishTurn();
}
