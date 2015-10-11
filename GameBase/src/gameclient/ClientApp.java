package gameclient;

import java.io.IOException;

import gameclient.control.ConnectController;
import gameclient.control.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientApp extends Application {
	
	Thread clientThread;
	GameClient client;
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
		
		client = new GameClient();
		clientThread = new Thread(client);
		loadConnectionPane();
	}
	
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadConnectionPane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/ConnectionPane.fxml"));
            AnchorPane connectionPane = (AnchorPane) loader.load();
            rootLayout.setCenter(connectionPane);
            
            ConnectController cont = (ConnectController)loader.getController();
            cont.setGameClient(client);
            cont.setClientApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void loadCanvasPane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("view/CanvasPane.fxml"));
            AnchorPane canvasPane = (AnchorPane) loader.load();
            rootLayout.setCenter(canvasPane);
            
            GameController cont = (GameController)loader.getController();
            cont.init(client);
            clientThread.start();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
