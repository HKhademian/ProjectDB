package app.controller;

import app.model.Accomplishment;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.Date;

public class AddAccomplishmentsController {

    private User user;
    private Accomplishment accomplishment;

    public AddAccomplishmentsController(User user){
        this.user = user;
        accomplishment = null;
    }

    public AddAccomplishmentsController(User user, Accomplishment accomplishment) {
        this.user = user;
        this.accomplishment = accomplishment;
    }

    @FXML
    private TextArea titleBox;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private TextArea aboutBox;

    @FXML
    private Label titleError;

    @FXML
    public void initialize(){
        titleError.setVisible(false);

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        saveButton.setOnAction(event -> saveAccomplishment());
    }

    private void saveAccomplishment(){
        boolean valid = true;
        if(titleBox.getText().trim().isEmpty()){
            titleError.setVisible(true);
            valid = false;
        }
        if(valid){
            if(accomplishment == null) {
                Repository.saveUserAccomplishment(new Accomplishment(-1, user.getUserId(), titleBox.getText().trim(),
                        aboutBox.getText().trim(), new Date()));
            }
            else{
                accomplishment.setTitle(titleBox.getText().trim());
                accomplishment.setContent(aboutBox.getText().trim());
                Repository.saveUserAccomplishment(accomplishment);
            }
            saveButton.getScene().getWindow().hide();
        }
    }

}
