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

    private ArrayList<String> skillList;
    private ArrayList<Integer> id;

    @FXML
    public void initialize(){

        skillList = new ArrayList<>();
        id = new ArrayList<>();

        for(Skill skill: Repository.listSkills()){
            skillList.add(skill.getTitle());
            id.add(skill.getSkillId());
        }
        ObservableList<String> skillsList = FXCollections.observableArrayList(skillList);
        skills.setItems(skillsList);
        new ComboBoxAutoComplete(skills);
        skills.requestFocus();

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        addButton.setOnAction(event -> saveSkill());
    }

    private void saveSkill(){
        skillError.setText("");
        String skill = skills.getSelectionModel().getSelectedItem();
        if(skill==null){
            skillError.setText("Please choose a skill");
            return;
        }
        int index = skillList.indexOf(skill);
        boolean res = Repository.addUserSkill(user.getUserId(), id.get(index), 0);
        if(res) {
            addButton.getScene().getWindow().hide();
        }else{
            skillError.setText("You add this skill before");
            return;
        }
    }
}
