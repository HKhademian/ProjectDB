package app.controller;

import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CreateChatController {

    private User user;

    public CreateChatController(User user) {
        this.user = user;
    }

    @FXML
    private TextField title;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton createButton;

    @FXML
    private Label emptyError;

    @FXML
    public void initialize() {

        emptyError.setVisible(false);

        createButton.setOnAction(event -> {send();});

        cancelButton.setOnAction(event -> {cancelButton.getScene().getWindow().hide();});

    }

    private void send(){
        String t = title.getText().trim();
        if(t.isEmpty()){
            emptyError.setVisible(true);
            return;
        }
        Repository.createChat(user.getUserId(),t);
        createButton.getScene().getWindow().hide();
    }
}
