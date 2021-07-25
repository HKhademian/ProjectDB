package app.controller;

import app.model.Article;
import app.model.Comment;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class AddCommentController {

    private User user;
    private Article article;
    private Comment comment;

    public AddCommentController(User user, Article article){
        this.user = user;
        this.article = article;
        comment = null;
    }

    public AddCommentController(User user, Article article, Comment comment) {
        this.user = user;
        this.article = article;
        this.comment = comment;
    }

    @FXML
    private TextArea text;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton sendButton;

    @FXML
    private Label emptyError;

    @FXML
    public void initialize() {

        emptyError.setVisible(false);

        sendButton.setOnAction(event -> {send();});

        cancelButton.setOnAction(event -> {cancelButton.getScene().getWindow().hide();});

    }

    private void send(){
        String t = text.getText().trim();
        if(t.isEmpty()){
            emptyError.setVisible(true);
            return;
        }
        int replyId = comment==null ? -1 : comment.getCommentId();
        Comment a = Repository.sendCommentOn(user.getUserId(), article.getArticleId(), replyId, t);
        if(a!=null) {
          System.out.println(a.getContent());
          System.out.println(a.getReplyCommentId());
          System.out.println("c = " + a.getCommentId());
          sendButton.getScene().getWindow().hide();
        } else {
          System.out.println("no comment");
        }
    }
}
