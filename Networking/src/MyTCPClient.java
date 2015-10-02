import java.io.*;
import java.net.*;

class MyTCPClient {
	
	public static void main(String argv[]) throws Exception {
		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
		
		String hostname = argv[0];
		
		Socket clientSocket = new Socket(hostname, 33333);
		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		String sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + '\n');
		
		String modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
	}
}
