package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import chat.Message.MessageType;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChatClient {
	private static final String QUIT = "quit";
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String username;
	private Socket socket;
	private ReceiverThread receiver;
	TextArea messageDisplay;

	public void connect(String username, String hostname, int portNumber) throws IOException {
		socket = new Socket(hostname, portNumber);
		output = new ObjectOutputStream(socket.getOutputStream());
		input  = new ObjectInputStream(socket.getInputStream());
		output.writeObject(username);
		this.username = username;
	}

	public void sendMessage(String text) throws IOException {
		output.writeObject(new Message(MessageType.MESSAGE, username, text));
		messageDisplay.appendText("Me: " + text + "\n\n");
	}

	public void startReceiving(TextArea messageDisplay) {
		this.messageDisplay = messageDisplay;
		receiver = new ReceiverThread();
		receiver.start();
	}

	public void disconnect() throws IOException {
		output.writeObject(new Message(MessageType.LOGOUT, username, "So long motherfucker!"));
		receiver.interrupt();
		socket.close();
	}

	class ReceiverThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Message message = (Message)input.readObject();
					messageDisplay.appendText(message.sender + ": " + message.body + "\n\n");
				} catch (Exception exc) {
					return;
				}
			}
		}
	}
}
