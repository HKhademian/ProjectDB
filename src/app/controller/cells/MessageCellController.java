package app.controller.cells;

import app.model.Message;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

public class MessageCellController extends JFXListCell<Message> {

    private User user;

    public MessageCellController(User user) {
        this.user = user;
    }

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Circle imagePlace;

    @FXML
    private Label name;

    @FXML
    private TextArea messageText;

    @FXML
    private Label time;

    private FXMLLoader fxmlLoader;

    @FXML
    public void initialize() {

    }

    public void updateItem(Message message, boolean empty){
        super.updateItem(message, empty);

        if(empty || message == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/cells/MessageCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            User user = Repository.getUserById(message.getSenderUserId(), message.getSenderUserId());
            setImage(user);

            messageText.setText(message.getContent());
            name.setText(user.getFirstname() + " " + user.getLastname());

            time.setText(message.getSendTime().toString());

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
