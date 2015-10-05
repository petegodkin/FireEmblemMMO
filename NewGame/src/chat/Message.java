package chat;

import java.io.Serializable;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1l;
	
	public enum MessageType {
		MESSAGE,
		LOGOUT,
		LOGIN,
		DISCONNECT;
	}
	
	MessageType type;
	String body;
	String sender;
	
	public Message(MessageType type, String sender, String body) {
		this.type = type;
		this.sender = sender;
		this.body = body;
	}
}
