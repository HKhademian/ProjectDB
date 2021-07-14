package app;

import javafx.application.Application;
import javafx.stage.Stage;
import app.controller.OpenWindow;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    // OpenWindow.openWindow("../view/Profile.fxml", new ProfileController(), "Linkedin - Profile");
    OpenWindow.openWindow("./scene.fxml", new TestController(), "test javafx");
  }

  public static void main(String[] args) {
    launch(args);
  }
}
