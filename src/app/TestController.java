package app;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import app.model._BaseModel;
import app.repository.SkillRepository;
import app.repository.UserRepository;
import app.repository._BaseRepository;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TestController implements Initializable {

  @FXML
  private Label label;

  @FXML
  private Button btn;

  @FXML
  private TextArea text;

  @FXML
  public TextField title;

  @FXML
  public Button btnAdd;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    String sqliteVersion = _BaseRepository.connect(conn -> {
      ResultSet res = conn.createStatement().executeQuery("select sqlite_version();");
      res.next();
      return res.getString(1);
    });
    label.setText("Hello, JavaFX " + javafxVersion +
      "\nRunning on Java " + javaVersion
      + "\nCur Dir" + System.getProperty("user.dir")
      + "\n SQlite Version:" + sqliteVersion);


    btn.setText("Hello");
    btn.setOnMouseClicked(event -> {
      text.setText("" + UserRepository.login("admin", "admin"));
    });

    btnAdd.setOnMouseClicked(event -> {
      Object res = SkillRepository.addSkill(title.getText());
      text.setText("res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
  }
}
