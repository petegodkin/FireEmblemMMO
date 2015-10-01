import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connect4Daemon {

	ServerSocket port = null;
	
	public void run() {
		Socket clientSocket;
		while (true) {
		if (port == null) {
		System.out.println("Sorry, the port disappeared.");
		System.exit(1);
		}
		try {
		clientSocket = port.accept();
		new Connect4Player(this, clientSocket).start();
		}
		catch (IOException e) {
		System.out.println("Couldn’t connect player:" + e);
		System.exit(1);
		}
		}
	}
	
	public synchronized Game waitForGame(Connect4Player p) {
		Game retval = null;
		if (playerWaiting == null) {
		playerWaiting = p;
		thisGame = null;	// just in case!
		p.send("PLSWAIT");
		while (playerWaiting != null) {
		try {
		wait();
		}
		catch (InterruptedException e) {
		System.out.println("Error:" + e);
		}
		}
		return thisGame;
		}
		else {
		thisGame = new Game(playerWaiting, p);
		retval = thisGame;
		playerWaiting = null;
		notify();
		return retval;
		}
		}
	
	public void start() {
		// TODO Auto-generated method stub
		
	}
	

}
