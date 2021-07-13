package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class AddBackgroundController {

    @FXML
    private TextArea titleBox;

    @FXML
    private Label titleError;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private TextField startDateBox;

    @FXML
    private Label startDateError;

    @FXML
    private TextField endDateBox;

    @FXML
    public void initialize(){
        titleError.setVisible(false);
        startDateError.setVisible(false);

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        saveButton.setOnAction(event -> saveBackground());
    }

    private void saveBackground(){
        boolean valid = true;
        if(titleBox.getText().trim().isEmpty()){
            titleError.setVisible(true);
            valid = false;
        }
        if(startDateBox.getText().trim().isEmpty()){
            startDateError.setVisible(true);
            valid = false;
        }
        if(valid){
            //save to db
            saveButton.getScene().getWindow().hide();
        }
    }

}
