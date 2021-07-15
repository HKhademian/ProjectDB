package app;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import app.model.Article;
import app.repository.*;
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
  public TextField id;

  @FXML
  public TextField code;

  @FXML
  public TextField title;

  @FXML
  public Button btnAddSkill;

  @FXML
  public Button btnAddLanguage;

  @FXML
  public Button btnGetArticle;
  @FXML
  public Button btnSaveArticle1;
  @FXML
  public Button btnSaveArticle2;
  @FXML
  public Button btnDeleteArticle;

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

    btnAddSkill.setOnMouseClicked(event -> {
      Object res = SkillRepository.addSkill(title.getText());
      text.setText("addSkill res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });

    btnAddLanguage.setOnMouseClicked(event -> {
      Object res = LanguageRepository.addLanguage(code.getText(), title.getText());
      text.setText("addLanguage res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });


    btnGetArticle.setOnMouseClicked(event -> {
      Object res = ArticleRepository.getArticle(Integer.parseInt(id.getText()));
      text.setText("getArticle res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });

    btnSaveArticle1.setOnMouseClicked(event -> {
      final Article article = new Article(
        Integer.parseInt(id.getText()), 1, "test article", "this is a content", null, true, 0, 0
      );
      Object res = ArticleRepository.saveArticle(article);
      text.setText("saveArticle `edit` res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });

    btnSaveArticle2.setOnMouseClicked(event -> {
      final Article article = new Article(
        0, 1, "test article", "this is a content", null, true, 0, 0
      );
      Object res = ArticleRepository.saveArticle(article);
      text.setText("saveArticle `new` res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });

    btnDeleteArticle.setOnMouseClicked(event -> {
      Object res = ArticleRepository.deleteArticle(Integer.parseInt(id.getText()));
      text.setText("deleteArticle res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
  }
}
