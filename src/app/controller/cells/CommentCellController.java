package app.controller.cells;

import app.controller.AddCommentController;
import app.controller.OpenWindow;
import app.model.Article;
import app.model.Comment;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
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
import java.util.List;

public class CommentCellController extends JFXListCell<Comment> {

    private User user;
    private Article article;

    public CommentCellController(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private JFXButton replyButton;

    @FXML
    private JFXTextArea commentText;

    @FXML
    private Circle userImage;

    @FXML
    private Label nameLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView notLikeImage;

    @FXML
    private ImageView likeImage;

    @FXML
    private Label likeCountLabel;


    private FXMLLoader fxmlLoader;

    private int likeCount;

    @FXML
    public void initialize(){

    }

    public void updateItem(Comment comment, boolean empty) {
        super.updateItem(comment, empty);

        if (empty || comment == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/cells/CommentCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            User sender = Repository.getUserById(comment.getUserId(), comment.getUserId());
            setImage(sender);
            nameLabel.setText(sender.getFirstname() + " " + sender.getLastname());
            dateLabel.setText(comment.getTime().toString());
            String content = "";
            System.out.println(comment.getReplyCommentId());
            if(comment.getReplyCommentId()>0){
                Comment comment1 = Repository.getCommentById(user.getUserId(), comment.getReplyCommentId());
                if(comment1.getContent().length() > 10){
                    content = "..." + comment1.getContent().substring(0, 10) + '\n';
                }else content = "..." + comment1.getContent() + "\n";
                content += comment.getContent();
            }
            else content = comment.getContent();

            commentText.setText(content);

            if(comment.getHome_isLiked()){
                notLikeImage.setVisible(false);
                likeImage.setVisible(true);
            }else {
                notLikeImage.setVisible(true);
                likeImage.setVisible(false);
            }
            likeCount = comment.getLikeCount();
            likeCountLabel.setText(String.valueOf(likeCount));

            replyButton.setOnAction(event -> replyComment(comment));

            notLikeImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> likeComment(comment));
            likeImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> notLikeComment(comment));

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
            userImage.setFill(new ImagePattern(wr));
        }
    }

    private void likeComment(Comment comment){
        boolean d = Repository.toggleUserLike(user.getUserId(), article.getArticleId(), comment.getCommentId());
        notLikeImage.setVisible(false);
        likeImage.setVisible(true);
        likeCountLabel.setText(String.valueOf(likeCount+1));
    }

    private void notLikeComment(Comment comment){
        boolean d = Repository.toggleUserLike(user.getUserId(), article.getArticleId(), comment.getCommentId());
        likeImage.setVisible(false);
        notLikeImage.setVisible(true);
        likeCountLabel.setText(String.valueOf(likeCount-1));
    }

    private void replyComment(Comment comment){
        OpenWindow.openWindowWait("view/AddComment.fxml", new AddCommentController(user, article, comment),
                "Reply Comment");
    }
}
