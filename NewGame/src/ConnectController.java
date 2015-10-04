import java.io.IOException;

import chat.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
    	String username = usernameField.getCharacters().toString();
    	String hostname = hostnameField.getCharacters().toString();
    	int portNumber = Integer.parseInt(portNumberField.getCharacters().toString());
    	try {
    		chatClient.connect(username, hostname, portNumber);
    	} catch (IOException exc) {
    		System.err.println("Problem connecting: " + exc.getMessage());
    	}

    	chatDisplay.showChatClientPane();
    }
}
