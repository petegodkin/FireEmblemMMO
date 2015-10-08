package gamemodel;

public class ServerMessage {
	public ServerMessageType type;
	public String details;
	
	public ServerMessage(ServerMessageType type) {
		this.type = type;
	}
	
	public ServerMessage(ServerMessageType type, String details) {
		this.type = type;
		this.details = details;
	}
}
