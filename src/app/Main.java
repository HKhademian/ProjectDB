package app;

import app.controller.LoginController;
import app.controller.SignupController;
import javafx.application.Application;
import javafx.stage.Stage;
import app.controller.OpenWindow;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        if ("backend".equals(System.getenv().getOrDefault("dev.mode", null))) {
            OpenWindow.openWindow("dev/scene.fxml", null, "test javafx");
        } else {
            OpenWindow.openWindow("view/Login.fxml", new LoginController(), "Linkedin - Login");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
