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
    private List<Comment> replyComments;

    public CommentCellController(User user, Article article, List<Comment> replyComments) {
        this.user = user;
        this.article = article;
        this.replyComments = replyComments;
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
    private JFXListView<Comment> replyCommentList;

    @FXML
    private ImageView notLikeImage;

    @FXML
    private ImageView likeImage;

    @FXML
    private Label likeCountLabel;

    @FXML
    private Label familyLabel;


    private FXMLLoader fxmlLoader;
    private ObservableList<Comment> replyComment;

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
                fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/cells/CommentCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            replyComment = FXCollections.observableArrayList();
            replyCommentList.setItems(replyComment);
            for(Comment comment1: replyComments){
                if(comment1.getReplyCommentId() == comment.getCommentId()){
                    replyComment.add(comment);
                }
            }
            replyCommentList.setCellFactory(CommentCellController -> new CommentCellController(user, article, replyComments));

            User sender = Repository.getUserById(comment.getUserId(), comment.getUserId());
            setImage(sender);
            nameLabel.setText(sender.getFirstname());
            familyLabel.setText(sender.getLastname());
            dateLabel.setText(comment.getTime().toString());
            commentText.setText(comment.getContent());

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
