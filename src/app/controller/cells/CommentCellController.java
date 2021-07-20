package app.controller.cells;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CommentCellController extends JFXListCell<Comment> {

    private User user;

    public CommentCellController(User user){
        this.user = user;
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
    private JFXListView<?> replyCommentList;

    @FXML
    private ImageView notLikeImage;

    @FXML
    private ImageView likeImage;

    @FXML
    private Label likeCountLabel;

    @FXML
    private Label familyLabel;


    private FXMLLoader fxmlLoader;
    private ObservableList<Comment> replyComments;

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

            User sender = Repository.getUserById(comment.getUserId(), comment.getUserId());
            setImage(sender);
            nameLabel.setText(sender.getFirstname());
            familyLabel.setText(sender.getLastname());
            dateLabel.setText(comment.getTime().toString());
            commentText.setText(comment.getContent());

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

}
