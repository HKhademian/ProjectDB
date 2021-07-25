package app.controller.cells;

import app.controller.ChatController;
import app.controller.OpenWindow;
import app.model.Chat;
import app.model.User;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ChatCellController extends JFXListCell<Chat> {

    private User user;

    public ChatCellController(User user) {
        this.user = user;
    }

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private Label chatName;

    @FXML
    private Label isArchive;

    @FXML
    private Label isMuted;

    private FXMLLoader fxmlLoader;

    @FXML
    public void initialize() {

    }

    public void updateItem(Chat chat, boolean empty){
        super.updateItem(chat, empty);

        if(empty || chat == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/cells/ChatCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            chatName.setText(chat.getTitle());

            if(chat.isMuted()) isMuted.setVisible(true);
            if(chat.isArchived()) isArchive.setVisible(true);

            setText(null);
            setGraphic(rootAnchorPane);

        }
    }

}
