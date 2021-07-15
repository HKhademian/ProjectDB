package app;

import java.net.URL;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

import app.model.Article;
import app.model.User;
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
  private Button btnLogin;
  @FXML
  private Button btnRegister;
  @FXML
  private Button btnToggleLike;


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

  @FXML
  public Button btnGetComment;
  @FXML
  public Button btnListComment;
  @FXML
  public Button btnDeleteComment;
  @FXML
  public Button btnSendComment;
  @FXML
  public Button btnSendReply;

  User loggedUser = null;

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    String sqliteVersion = _BaseRepository.connect(conn -> {
      ResultSet res = conn.createStatement().executeQuery("select sqlite_version();");
      res.next();
      return res.getString(1);
    });
    String sqliteForeign = _BaseRepository.connect(conn -> {
      ResultSet res = conn.createStatement().executeQuery("PRAGMA foreign_keys;");
      res.next();
      return res.getString(1);
    });
    label.setText("Hello, JavaFX " + javafxVersion +
      "\nRunning on Java " + javaVersion
      + "\nCur Dir" + System.getProperty("user.dir")
      + "\n SQlite Version:" + sqliteVersion
      + "\n SQlite foreign:" + sqliteForeign
    );


    btn.setText("Check Logged User");
    btn.setOnMouseClicked(event -> {
      text.setText("" + loggedUser);
    });

    btnLogin.setOnMouseClicked(event -> {
      Object res = loggedUser = UserRepository.login(id.getText(), code.getText());
      text.setText("login res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
    btnRegister.setOnMouseClicked(event -> {
      int index = (int) (Math.random() * 500 + 500);
      Object res = loggedUser = UserRepository.register(
        "user" + index, "user",
        "User #" + index, "u" + index + "@u.u",
        "900-" + index, null, null, null, null, null
      );
      text.setText("register res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
    btnToggleLike.setOnMouseClicked(event -> {
      Object res = UserRepository.toggleLike(loggedUser.getUserId(), Integer.parseInt(id.getText()), Integer.parseInt(code.getText()));
      text.setText("toggleLike res: " + res + "\nLastErr:" + _BaseRepository.lastError);
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
        Integer.parseInt(id.getText()), loggedUser.getUserId(), title.getText(), text.getText(), new Date(), true, 0, 0
      );
      Object res = ArticleRepository.saveArticle(article);
      text.setText("saveArticle `edit` res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
    btnSaveArticle2.setOnMouseClicked(event -> {
      final Article article = new Article(
        0, loggedUser.getUserId(), title.getText(), text.getText(), new Date(), true, 0, 0
      );
      Object res = ArticleRepository.saveArticle(article);
      text.setText("saveArticle `new` res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
    btnDeleteArticle.setOnMouseClicked(event -> {
      Object res = ArticleRepository.deleteArticle(Integer.parseInt(id.getText()));
      text.setText("deleteArticle res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });

    btnGetComment.setOnMouseClicked(event -> {
      Object res = CommentRepository.getComment(Integer.parseInt(id.getText()));
      text.setText("getComment res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
    btnListComment.setOnMouseClicked(event -> {
      Object res = CommentRepository.listComments(Integer.parseInt(id.getText()));
      text.setText("listComments res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
    btnDeleteComment.setOnMouseClicked(event -> {
      Object res = CommentRepository.deleteComment(Integer.parseInt(id.getText()));
      text.setText("deleteComment res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
    btnSendComment.setOnMouseClicked(event -> {
      Object res = CommentRepository.commentOn(
        loggedUser.getUserId(),
        Integer.parseInt(id.getText()),
        0,
        text.getText()
      );
      text.setText("commentOn res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
    btnSendReply.setOnMouseClicked(event -> {
      Object res = CommentRepository.commentOn(
        loggedUser.getUserId(),
        0,
        Integer.parseInt(id.getText()),
        text.getText()
      );
      text.setText("replyOn res: " + res + "\nLastErr:" + _BaseRepository.lastError);
    });
  }
}
