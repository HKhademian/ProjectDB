package app.controller;

import app.controller.cells.EndorseCellController;
import app.model.Skill;
import app.model.SkillEndorse;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.util.List;

public class EndorseListController {

    private User user;
    private User owner;
    private Skill skill;

    public EndorseListController(User user, User owner, Skill skill) {
        this.user = user;
        this.owner = owner;
        this.skill = skill;
    }

    @FXML
    private JFXListView<User> endorseList;

    private ObservableList<User> users;

    @FXML
    public void initialize() {

        users = FXCollections.observableArrayList();
        List<SkillEndorse> skillEndorses = Repository.listEndorseToUserSkill(owner.getUserId(), skill.getSkillId());
        for(SkillEndorse skillEndorse: skillEndorses){
            users.add(Repository.getUserById(skillEndorse.getByUserId(), skillEndorse.getByUserId()));
            //System.out.println(skillEndorse.getUserId() + "   " + skillEndorse.getByUserId());
        }

        endorseList.setItems(users);
        endorseList.setCellFactory(CallFactory -> new EndorseCellController(user));
    }
}
