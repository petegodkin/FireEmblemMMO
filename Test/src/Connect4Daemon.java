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
	
	public void start() {
		// TODO Auto-generated method stub
		
	}
	

}
