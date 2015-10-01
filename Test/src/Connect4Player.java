import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;


public class Connect4Player {
	
	private DataInputStream inStream = null;
	protected PrintStream outStream = null;
	private Socket socket = null;

	public Connect4Player(Connect4Daemon connect4Daemon, Socket clientSocket) {
		// TODO Auto-generated constructor stub
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	public void send(String s) {
		outStream.println(s);
	}
	public String receive() throws IOException {
		return inStream.readLine();
	}

	public void closeConnections() {
		// TODO Auto-generated method stub
		
	}

}
