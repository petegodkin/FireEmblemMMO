package gameclient;


public class ClientMessage {
	public ClientMessageType type;
	/**
	 * meant to be the parameters for the GameAction
	 * first Object should be Constructor you want to use to instantiate the action
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
