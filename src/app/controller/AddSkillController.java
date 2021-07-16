package app.controller;

import app.model.Skill;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AddSkillController {

    private User user;

    public AddSkillController(User user) {
        this.user = user;
    }

    @FXML
    private JFXComboBox<Skill> skills;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label skillError;

    @FXML
    public void initialize(){

        ObservableList<Skill> skillList = FXCollections.observableArrayList(Repository.listSkills());
        skills.setItems(skillList);

        skillError.setVisible(false);

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        addButton.setOnAction(event -> saveBackground());
    }

    private void saveBackground(){
        boolean valid = true;
        //check skill an add it
        addButton.getScene().getWindow().hide();
    }
}
