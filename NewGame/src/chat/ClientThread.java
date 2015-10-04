package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chat.Message.MessageType;

public class ClientThread extends Thread {

	private String username;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input  = new ObjectInputStream(socket.getInputStream());
		} catch (IOException exc) {
			System.out.println(exc.getMessage());
			return;
		}

		try {
			username = (String)input.readObject();
			System.out.println(username + " connected");
		} catch (Exception exc) {
			System.out.println("Not a good way to do this...");
			return;
		}

		while(true) {
			Message message;
			try {
				message = (Message)input.readObject();
				if (message.type == MessageType.LOGOUT) {
					break;
				} else if (message.type == MessageType.MESSAGE) {
					ChatServer.broadcastMessage(message);
				}
			} catch (Exception exc) {
				System.out.println("Not a good way to do this...");
				break;
			}


		}
		ChatServer.logout(this);
	}

	public void close() throws IOException {
		interrupt();
		socket.close();
	}

	public String getUsername() {
		return username;
	}

	public void sendMessage(Message message) throws IOException {
		output.writeObject(message);
	}
}
