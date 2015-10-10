package gameserver.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SetUpController {

	@FXML
	Button startGameButton;
	
	@FXML
	TextArea textArea;
	
	@FXML
	protected void handleStartGameButton() {
		textArea.appendText("Button Press\n");
	}
	
	public TextArea getTextArea() {
		return textArea;
	}
}
