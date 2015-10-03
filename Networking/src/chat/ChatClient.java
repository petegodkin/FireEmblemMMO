package chat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import chat.Message.MessageType;

public class ChatClient {
	private static final String QUIT = "quit";
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String username;
	
	public static void main(String argv[]) throws Exception {
		new ChatClient().doStuff(argv[0]);
	}
	
	private void doStuff(String hostname) throws Exception {
		Scanner scanner = new Scanner(System.in);
		
		Socket socket = new Socket(hostname, 33333);
		
		output = new ObjectOutputStream(socket.getOutputStream());
		input  = new ObjectInputStream(socket.getInputStream());
		
		System.out.print("Type a username: ");
		username = scanner.nextLine();
		output.writeObject(username);
		
		System.out.print(">");
		String text = "";
		
		ReceiverThread receiver = new ReceiverThread();
		receiver.start();
		while (!text.equals(QUIT)) {
			if (scanner.hasNextLine()) {
				text = scanner.nextLine();
				output.writeObject(new Message(MessageType.MESSAGE, username, text));
				System.out.print(">");
			}
		}
		output.writeObject(new Message(MessageType.LOGOUT, username, ""));
		receiver.interrupt();
		socket.close();
		scanner.close();
	}
	
	class ReceiverThread extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					Message message = (Message)input.readObject();
					System.out.println(message.sender + ": " + message.body);
					System.out.print(">");
				} catch (Exception exc) {
					return;
				}
			}
		}
	}
}
