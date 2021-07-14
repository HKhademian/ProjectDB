package app;

import java.net.URL;
import java.util.ResourceBundle;

import app.repository.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class TestController implements Initializable {

  @FXML
  private Label label;

  @FXML
  private Button btn;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".\n" + System.getProperty("user.dir"));

    btn.setText("Hello");
    btn.setOnMouseClicked(event -> {
      label.setText("" + UserRepository.login("admin", "admin"));
    });
  }
}
