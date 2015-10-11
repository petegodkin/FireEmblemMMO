package gameserver;

import java.io.Serializable;

public class ServerMessage implements Serializable {
	private static final long serialVersionUID = 1l;
	
	public ServerMessageType type;
	public Object[] data;
	
	public ServerMessage(ServerMessageType type) {
		this.type = type;
	}
	
	public ServerMessage(ServerMessageType type, Object...data) {
		this.type = type;
		this.data = data;
	}
}
