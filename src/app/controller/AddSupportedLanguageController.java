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

import java.util.ArrayList;

public class AddSupportedLanguageController {

    private User user;

    public AddSupportedLanguageController(User user) {
        this.user = user;
    }

    @FXML
    private JFXComboBox<String> languages;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label languageError;

    private ArrayList<String> languageList;
    private ArrayList<String> id;

    @FXML
    public void initialize(){

        languageList = new ArrayList<>();
        id = new ArrayList<>();

        for(Language language: Repository.listLanguages()){
            languageList.add(language.getTitle());
            id.add(language.getCode());
        }

        ObservableList<String> languagesList = FXCollections.observableArrayList(languageList);
        languages.setItems(languagesList);
        new ComboBoxAutoComplete(languages);

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        addButton.setOnAction(event -> saveLanguage());
    }

    private void saveLanguage(){
        languageError.setText("");
        String language = languages.getSelectionModel().getSelectedItem();
        if(language==null){
            languageError.setText("Please choose a language");
            return;
        }
        int index = languageList.indexOf(language);
        boolean res = Repository.addUserLanguage(user.getUserId(), id.get(index));
        if(res) {
            addButton.getScene().getWindow().hide();
        }else{
            languageError.setText("You add this language before");
            return;
        }
        addButton.getScene().getWindow().hide();
    }
}
