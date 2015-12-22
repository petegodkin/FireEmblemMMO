package gameclient;

import java.io.Serializable;
import java.util.UUID;

public class ClientMessage implements Serializable {
	private static final long serialVersionUID = 1l;
	
	public ClientMessageType type;
	/**
	 * Used to pass GameAction
	 * first Object should be the class you want to instantiate
	 * the class should only have one constructor
	 * rest should be arguments for the constructor
	 */
	public Object data[];
	
	public ClientMessage(ClientMessageType type) {
		this.type = type;
	}
	
	public ClientMessage(ClientMessageType type, Object...data) {
		this.type = type;
		this.data = data;
	}
}
