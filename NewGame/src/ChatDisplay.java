import java.io.IOException;

import chat.ChatClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatDisplay {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ChatClient chatClient;

    public void start(Stage primaryStage) {
    	chatClient = new ChatClient();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("FireEmblemMMO");

        initRootLayout();

        showConnectionPane();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConnectionPane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ConnectionPane.fxml"));
            AnchorPane connectionPane = (AnchorPane) loader.load();
            rootLayout.setCenter(connectionPane);

            ConnectController cont = (ConnectController)loader.getController();
            cont.setChatDisplay(this);
            cont.setChatClient(chatClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChatClientPane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ChatClient.fxml"));
            AnchorPane chatClientPane = (AnchorPane) loader.load();
            rootLayout.setCenter(chatClientPane);

            ChatController cont = (ChatController)loader.getController();
            cont.setChatClient(chatClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
    	try {
			chatClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
