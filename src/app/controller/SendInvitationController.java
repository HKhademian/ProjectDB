package app.controller;

import app.model.Invitation;
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
    private Label errorLabel;

    @FXML
    public void initialize() {

        sendButton.setOnAction(event -> {send();});

        cancelButton.setOnAction(event -> {cancelButton.getScene().getWindow().hide();});

    }

    private void send(){
        String t = text.getText().trim();
        if(t.isEmpty()){
            errorLabel.setText("Please first write something");
            return;
        }
        Invitation res = Repository.sendInvitation(sender.getUserId(), receiver.getUserId(), t);
        if(res==null){
            errorLabel.setText("You send invitation before");
        }
        sendButton.getScene().getWindow().hide();
    }
}
