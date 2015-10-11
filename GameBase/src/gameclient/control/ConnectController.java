package gameclient.control;

import java.io.IOException;

import gameclient.ClientApp;
import gameclient.GameClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ConnectController {

	private GameClient gameClient;
	private ClientApp clientApp;
	
	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
	}
	
	public void setClientApp(ClientApp clientApp) {
		this.clientApp = clientApp;	
	}

    @FXML
    private TextField usernameField;
    @FXML
    private TextField hostnameField;
    @FXML
    private TextField portNumberField;
    @FXML
    private Button connectButton;

    @FXML
    protected void handleConnectButtonAction(ActionEvent event) {
    	connect();
    }
    

    @FXML
    protected void handleConnectButtonKeyPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		connect();
    	}
    }
    
    private void connect() {
    	String username = usernameField.getCharacters().toString();
    	String hostname = hostnameField.getCharacters().toString();
    	int portNumber = Integer.parseInt(portNumberField.getCharacters().toString());
    	try {
    		gameClient.connect(username, hostname, portNumber);
    		clientApp.loadCanvasPane();
    	} catch (IOException exc) {
    		System.err.println("Problem connecting: " + exc.getMessage());
    	}    	
    }
}
