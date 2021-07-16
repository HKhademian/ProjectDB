package app;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import app.controller.OpenWindow;
import app.controller.SignupController;
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
  private Button test;

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
    String sqliteVersion = Database.scalarQuery("select sqlite_version();");
    String sqliteForeign = Database.scalarQuery("PRAGMA foreign_keys;");
    label.setText("Hello, JavaFX " + javafxVersion +
      "\nRunning on Java " + javaVersion
      + "\nCur Dir" + System.getProperty("user.dir")
      + "\n SQlite Version:" + sqliteVersion
      + "\n SQlite foreign:" + sqliteForeign
    );

    test.setOnMouseClicked(event -> {
      OpenWindow.openWindow("view/Signup.fxml", new SignupController(), "Signup");
    });


    btn.setText("Check Logged User");
    btn.setOnMouseClicked(event -> {
      text.setText("" + loggedUser);
    });

    btnLogin.setOnMouseClicked(event -> {
      Object res = loggedUser = Repository.loginUser(id.getText(), code.getText());
      text.setText("login res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnRegister.setOnMouseClicked(event -> {
      int index = (int) (Math.random() * 500 + 500);
      Object res = loggedUser = Repository.registerUser(new User(
        0, "user" + index,
        "fUser #" + index, "lUser #" + index,
        null, null, null, null
      ), "user");
      text.setText("register res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnToggleLike.setOnMouseClicked(event -> {
      Object res = Repository.toggleUserLike(loggedUser.getUserId(), Integer.parseInt(id.getText()), Integer.parseInt(code.getText()));
      text.setText("toggleLike res: " + res + "\nLastErr:" + Database.lastError);
    });

    btnAddSkill.setOnMouseClicked(event -> {
      Object res = Repository.addSkill(title.getText());
      text.setText("addSkill res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnAddLanguage.setOnMouseClicked(event -> {
      Object res = Repository.addLanguage(code.getText(), title.getText());
      text.setText("addLanguage res: " + res + "\nLastErr:" + Database.lastError);
    });


    btnGetArticle.setOnMouseClicked(event -> {
      Object res = Repository.getArticle(Integer.parseInt(id.getText()));
      text.setText("getArticle res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnSaveArticle1.setOnMouseClicked(event -> {
      final Article article = new Article(
        Integer.parseInt(id.getText()), loggedUser.getUserId(), title.getText(), text.getText(), new Date(), true, 0, 0
      );
      Object res = Repository.saveArticle(article);
      text.setText("saveArticle `edit` res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnSaveArticle2.setOnMouseClicked(event -> {
      final Article article = new Article(
        0, loggedUser.getUserId(), title.getText(), text.getText(), new Date(), true, 0, 0
      );
      Object res = Repository.saveArticle(article);
      text.setText("saveArticle `new` res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnDeleteArticle.setOnMouseClicked(event -> {
      Object res = Repository.deleteArticle(Integer.parseInt(id.getText()));
      text.setText("deleteArticle res: " + res + "\nLastErr:" + Database.lastError);
    });

    btnGetComment.setOnMouseClicked(event -> {
      Object res = Repository.getCommentById(Integer.parseInt(id.getText()));
      text.setText("getComment res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnListComment.setOnMouseClicked(event -> {
      Object res = Repository.listComments(Integer.parseInt(id.getText()));
      text.setText("listComments res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnDeleteComment.setOnMouseClicked(event -> {
      Object res = Repository.deleteComment(Integer.parseInt(id.getText()));
      text.setText("deleteComment res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnSendComment.setOnMouseClicked(event -> {
      Object res = Repository.sendCommentOn(
        loggedUser.getUserId(),
        Integer.parseInt(id.getText()),
        0,
        text.getText()
      );
      text.setText("commentOn res: " + res + "\nLastErr:" + Database.lastError);
    });
    btnSendReply.setOnMouseClicked(event -> {
      Object res = Repository.sendCommentOn(
        loggedUser.getUserId(),
        0,
        Integer.parseInt(id.getText()),
        text.getText()
      );
      text.setText("replyOn res: " + res + "\nLastErr:" + Database.lastError);
    });
  }
}
