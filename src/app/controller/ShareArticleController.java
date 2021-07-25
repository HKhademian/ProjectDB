package app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShareArticleController {

    private int articleId;

    public ShareArticleController(int articleId) {
        this.articleId = articleId;
    }

    @FXML
    private Label id;

    public void initialize(){
        id.setText(String.valueOf(articleId));
    }
}
