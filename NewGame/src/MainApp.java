import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
	private ChatDisplay display;

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