import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import chat.ChatClient;

public class MainApp extends Application {
	private ChatDisplay display;
	private ChatController controller;

    @Override
    public void start(Stage primaryStage) {
    	display = new ChatDisplay();
		display.start(primaryStage);

    }

    @Override
    public void stop() {
    	display.stop();
    }


    public static void main(String[] args) {
        launch(args);
    }
}