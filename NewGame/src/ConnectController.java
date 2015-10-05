import java.io.IOException;

import chat.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ConnectController {

	ChatClient chatClient;
	ChatDisplay chatDisplay;

	public void setChatClient(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	public void setChatDisplay(ChatDisplay chatDisplay) {
		this.chatDisplay = chatDisplay;
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
    		chatClient.connect(username, hostname, portNumber);
    		chatDisplay.showChatClientPane();
    	} catch (IOException exc) {
    		System.err.println("Problem connecting: " + exc.getMessage());
    	}    	
    }
}