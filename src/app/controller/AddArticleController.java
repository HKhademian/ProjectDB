package app.controller;

import app.model.Article;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.Date;

public class AddArticleController {

    private User user;

    public AddArticleController(User user) {
        this.user = user;
    }
    @FXML
    private TextArea titleBox;

    @FXML
    private TextArea textBox;

    @FXML
    private Label titleError;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton postButton;

    @FXML
    private JFXCheckBox addFeature;

    @FXML
    public void initialize() {

        postButton.setOnAction(event -> {post();});

        cancelButton.setOnAction(event -> {cancel();});

    }

    private void cancel(){
        cancelButton.getScene().getWindow().hide();
    }

    private void post(){
        String title = titleBox.getText().replaceFirst("\\s++$", "");
        if(title.length() == 0 ){
            titleError.setText("Title must be write");
        }
        else{
            String text = textBox.getText().trim();
            Repository.saveArticle(new Article(-1, user.getUserId(), title, text, /*addFeature.isSelected() FIXME*/ new Date()));
            postButton.getScene().getWindow().hide();
        }
    }

}
