package app.controller;

import app.model.User;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class AddAccomplishmentsController {

    private User user;

    public AddAccomplishmentsController(User user){
        this.user = user;
    }

    @FXML
    private TextArea titleBox;


    @FXML
    private Label titleError;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    public void initialize(){
        titleError.setVisible(false);

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        saveButton.setOnAction(event -> saveBackground());
    }

    private void saveBackground(){
        boolean valid = true;
        if(titleBox.getText().trim().isEmpty()){
            titleError.setVisible(true);
            valid = false;
        }
        if(valid){
            //save to db
            saveButton.getScene().getWindow().hide();
        }
    }

}
