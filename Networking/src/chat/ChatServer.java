package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

class ChatServer {
	public static final int PORT_NUMBER = 33333;
	private static List<ClientThread> clients = new Vector<>();
	
	public static void main(String argv[]) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(PORT_NUMBER);
		System.out.println("Server using port number " + welcomeSocket.getLocalPort());
		
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			ClientThread user = new ClientThread(connectionSocket);
			clients.add(user);
			user.start();
		}
	}
	
	public static void broadcastMessage(Message message) throws IOException {
		System.out.println("Broadcasting: " + message.body);
		Iterator<ClientThread> it = clients.iterator();
		while (it.hasNext()) {
			ClientThread client = it.next();
			if (!client.getUsername().equals(message.sender)) {
				try {
					client.sendMessage(message);
				} catch (IOException exc) {
					System.out.println("Failed to send message to " + client.getUsername()
						+ ". Disconnecting...");
					client.close();
					it.remove();
				}
			}
		}
	}
	
	public static void logout(ClientThread user) {
		try {
			clients.remove(user);
			user.close();
		} catch (IOException exc) {
		}
	}
}