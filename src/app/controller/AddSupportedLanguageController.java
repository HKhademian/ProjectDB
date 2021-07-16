package app.controller;

import app.model.Language;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AddSupportedLanguageController {

    private User user;

    public AddSupportedLanguageController(User user) {
        this.user = user;
    }

    @FXML
    private JFXComboBox<Language> languages;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label languageError;

    @FXML
    public void initialize(){

        ObservableList<Language> languagesList = FXCollections.observableArrayList(Repository.listLanguages());
        languages.setItems(languagesList);

        languageError.setVisible(false);

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        addButton.setOnAction(event -> saveLanguage());
    }

    private void saveLanguage(){
        boolean valid = true;
        //check language an add it
        addButton.getScene().getWindow().hide();
    }
}
