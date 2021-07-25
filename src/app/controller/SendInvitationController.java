package app.controller;

import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class SendInvitationController {

    private User sender;
    private User receiver;

    public SendInvitationController(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @FXML
    private TextArea text;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton sendButton;

    @FXML
    private Label emptyError;

    @FXML
    public void initialize() {

        emptyError.setVisible(false);

        sendButton.setOnAction(event -> {send();});

        cancelButton.setOnAction(event -> {cancelButton.getScene().getWindow().hide();});

    }

    private void send(){
        String t = text.getText().trim();
        if(t.isEmpty()){
            emptyError.setVisible(true);
            return;
        }
        Repository.sendInvitation(sender.getUserId(), receiver.getUserId(), t);
        sendButton.getScene().getWindow().hide();
    }
}
