package app.controller.cells;

import app.model.Article;
import app.model.User;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ArticleCellController extends JFXListCell<Article> {

    private User user;

    public ArticleCellController(User user) {
        this.user = user;
    }

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextArea title;

    @FXML
    private TextArea content;

    @FXML
    private ImageView like;

    @FXML
    private ImageView unlike;

    @FXML
    private ImageView comment;

    @FXML
    private ImageView share;

    @FXML
    private Label commentLabel;

    @FXML
    private Label shareLabel;

    private FXMLLoader fxmlLoader;

    @FXML
    public void initialize() {

    }

    public void updateItem(Article article, boolean empty){
        super.updateItem(article, empty);

        if(empty || article == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../../view/cells/ArticleCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                title.setText(article.getTitle());
                content.setText(article.getContent());

            }

        }
    }

}
