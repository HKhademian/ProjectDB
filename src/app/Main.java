package app;

import app.controller.ProfileController;
import javafx.application.Application;
import javafx.stage.Stage;
import app.controller.OpenWindow;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    if ("backend".equals(System.getenv().getOrDefault("dev.mode", null))) {
      OpenWindow.openWindow("scene.fxml", null, "test javafx");
    } else {
      OpenWindow.openWindow("view/Profile.fxml", new ProfileController(), "Linkedin - Profile");
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
