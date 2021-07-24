package app.controller.cells;

import app.controller.CommentController;
import app.controller.OpenWindow;
import app.controller.ProfileController;
import app.model.Article;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @FXML
    private Circle imagePlace;

    @FXML
    private Label time;


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
                fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/cells/ArticleCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            title.setText(article.getTitle());
            content.setText(article.getContent());
            time.setText(article.getTime().toString());
            User writer = Repository.getUserById(article.getWriterUserId(),article.getWriterUserId());
            setImage(writer);
            imagePlace.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> profile(writer));

            if(article.isLiked()){
                unlike.setVisible(false);
                like.setVisible(true);
            }else {
                unlike.setVisible(true);
                like.setVisible(false);
            }

            unlike.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> likeArticle(article));
            like.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> unLikeArticle(article));

            comment.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showComments(article));
            commentLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showComments(article));

            setText(null);
            setGraphic(rootAnchorPane);

        }
    }

    private void setImage(User user) {
        if(user.getAvatar()!=null) {
            InputStream is = new ByteArrayInputStream(user.getAvatar());
            BufferedImage bf=null;
            try {
                bf = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            WritableImage wr = null;
            if (bf != null) {
                wr = new WritableImage(bf.getWidth(), bf.getHeight());
                PixelWriter pw = wr.getPixelWriter();
                for (int x = 0; x < bf.getWidth(); x++) {
                    for (int y = 0; y < bf.getHeight(); y++) {
                        pw.setArgb(x, y, bf.getRGB(x, y));
                    }
                }
            }
            imagePlace.setFill(new ImagePattern(wr));
        }
    }

    private void showComments(Article article){
        imagePlace.getScene().getWindow();
        OpenWindow.openWindowWait("view/Comments.fxml", new CommentController(user, article), "Comments");
    }

    private void profile(User writer){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Profile.fxml", new ProfileController(writer, user), "Profile");
    }

    private void likeArticle(Article article){
        Repository.toggleUserLike(user.getUserId(), article.getArticleId(), -1);
        like.setVisible(true);
        unlike.setVisible(false);
    }

    private void unLikeArticle(Article article){
        Repository.toggleUserLike(user.getUserId(), article.getArticleId(), -1);
        like.setVisible(false);
        unlike.setVisible(true);
    }

}
