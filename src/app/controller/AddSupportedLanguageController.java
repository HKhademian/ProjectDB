package app.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AddSupportedLanguageController {

    private int userId;

    public AddSupportedLanguageController(int userId) {
        this.userId = userId;
    }

    @FXML
    private JFXComboBox<?> languages;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label languageError;

    @FXML
    public void initialize(){
        languageError.setVisible(false);

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        addButton.setOnAction(event -> saveBackground());
    }

    private void saveBackground(){
        boolean valid = true;
        //check language an add it
        addButton.getScene().getWindow().hide();
    }
}
