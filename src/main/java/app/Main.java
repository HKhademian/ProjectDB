package app;

import javafx.application.Application;
import javafx.stage.Stage;
import app.controller.OpenWindow;
import app.controller.ProfileController;
import app.repository.Database;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    OpenWindow.openWindow("../view/Profile.fxml", new ProfileController(), "Linkedin - Profile");
  }

  public static void main(String[] args) {
    Database.connect();
    launch(args);
  }
}
