import javafx.application.Application;
import javafx.stage.Stage;
import controller.OpenWindow;
import controller.ProfileController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        OpenWindow.openWindow("../view/Profile.fxml", new ProfileController(), "Linkedin - Profile");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
