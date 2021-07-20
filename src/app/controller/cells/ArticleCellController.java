package app.controller.cells;

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
            setImage(Repository.getUserById(user.getUserId(),article.getWriterUserId()));

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

}
