package gameserver;

import java.io.IOException;

import gameserver.control.SetUpController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * This class will setup all the connections with GameClients and kickoff the game
 */
public class ServerApp extends Application {
	
	private Stage primaryStage;
    private BorderPane rootLayout;
    
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("FireEmblemMMO");
		initRootLayout();
		TextArea textArea = loadSetUpPane();
		GameServer server = new GameServer(textArea);
		server.start();
	}
	
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ServerApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public TextArea loadSetUpPane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ServerApp.class.getResource("view/SetUpPane.fxml"));
            AnchorPane setUpPane = (AnchorPane) loader.load();
            rootLayout.setCenter(setUpPane);
            
            SetUpController cont = (SetUpController)loader.getController();
            return cont.getTextArea();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}