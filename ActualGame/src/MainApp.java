import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	
    private Stage primaryStage;
    private BorderPane rootLayout;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("FireEmblemMMO");

        initRootLayout();

        //showMenuPane();
        showSinglePlayer();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("menu/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMenuPane() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("menu/view/MenuPane.fxml"));
            AnchorPane connectionPane = (AnchorPane) loader.load();
            rootLayout.setCenter(connectionPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showSinglePlayer() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("menu/view/SinglePlayer.fxml"));
            AnchorPane connectionPane = (AnchorPane) loader.load();
            rootLayout.setCenter(connectionPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}