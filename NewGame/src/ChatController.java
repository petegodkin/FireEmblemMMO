import java.io.IOException;

import chat.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ChatController {

	private ChatClient chatClient;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Button sendButton;
    @FXML
    private TextArea messageDisplay;

    public void setChatClient(ChatClient chatClient) {
		this.chatClient = chatClient;
		chatClient.startReceiving(messageDisplay);
	}

    @FXML
    protected void handleSendButtonAction(ActionEvent event) {
    	sendMessage();
    }
    
    @FXML
    protected void handleInputKeyPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		event.consume();
    		sendMessage();
    	}
    }
    
    private void sendMessage() {
    	String message = messageTextArea.getText();
    	try {
    		chatClient.sendMessage(message);
    	} catch (IOException exc) {
			System.err.println("Problem connecting: " + exc.getMessage());
		}
    	messageTextArea.clear();
    }

}
