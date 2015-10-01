import java.io.*;
import java.net.*;

class MyTCPServer {
	
	public static void main(String argv[]) throws Exception {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(0);
		System.out.println("Server using port number " + welcomeSocket.getLocalPort());
		System.out.println("Server address " + welcomeSocket.getInetAddress());
		
		while(true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient =
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capitalizedSentence);
		}
	}
}