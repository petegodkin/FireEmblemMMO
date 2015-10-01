import java.awt.Canvas;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {
	private DataInputStream inStream = null;
	protected PrintStream outStream = null;
	private Socket socket = null;
	
	public SocketAction(Socket sock) {
		super("SocketAction");
		try {
			inStream = new DataInputStream(new BufferedInputStream(sock.getInputStream(), 1024));
			outStream = new PrintStream(new BufferedOutputStream(sock.getOutputStream(), 1024), true);
		socket = sock;
		}
		catch (IOException e) {
			System.out.println("Couldn’t initialize SocketAction:" + e);
			System.exit(1);
		}
	}
	
	public void send(String s) {
		outStream.println(s);
	}
	
	public String receive() throws IOException {
		return inStream.readLine();
	}
	
	public void closeConnections() {
		try {
			socket.close();
			socket = null;
		}
		catch (IOException e) {
			System.out.println("Couldn’t close socket:" + e);
		}
	}
	
	public boolean isConnected() {
		return ((inStream != null) && (outStream != null) && (socket != null));
	}
	
	protected void finalize () {
		if (socket != null) {
			try {
				socket.close();
			}
			catch (IOException e) {
				System.out.println("Couldn’t close socket:" + e);
			}
			socket = null;
		}
	}
	
	class Connect4Server {
		public void main(String args[ ]) {
			System.out.println("NetConnect4 server up and running...");
			new Connect4Daemon().start();
		}
	}

}
