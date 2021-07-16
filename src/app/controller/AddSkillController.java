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

import java.util.ArrayList;

public class AddSkillController {

    private User user;

    public AddSkillController(User user) {
        this.user = user;
    }

    @FXML
    private JFXComboBox<String> skills;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label skillError;

    @FXML
    public void initialize(){

        ArrayList<String> skill = new ArrayList<>();
        ArrayList<Integer> id = new ArrayList<>();
        for(Skill skill1: Repository.listSkills()){
            skill.add(skill1.getTitle());
            id.add(skill1.getSkillId());
        }
        ObservableList<String> skillList = FXCollections.observableArrayList(skill);
        skills.setItems(skillList);

        skillError.setVisible(false);

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        addButton.setOnAction(event -> saveSkill());
    }

    private void saveSkill(){
        boolean valid = true;
        //check skill an add it
        addButton.getScene().getWindow().hide();
    }
}
