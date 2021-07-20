package app.controller.cells;

import app.controller.EndorseListController;
import app.controller.OpenWindow;
import app.model.Skill;
import app.model.SkillEndorse;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class SkillCellController extends JFXListCell<Skill> {

    private User owner;
    private User user;

    public SkillCellController(User owner, User user) {
        this.owner = owner;
        this.user = user;
    }

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField skillTitle;

    @FXML
    private Label endorseNumber;

    @FXML
    private JFXButton endorseButton;

    private FXMLLoader fxmlLoader;

    private List<SkillEndorse> skillEndorses;

    @FXML
    public void initialize() {

    }

    @Override
    public void updateItem(Skill skill, boolean empty){
        super.updateItem(skill, empty);

        if(empty || skill == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/cells/SkillCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            skillEndorses = Repository.listEndorseToUserSkill(owner.getUserId(), skill.getSkillId());

            if(owner == user){
                endorseButton.setVisible(false);
            }
            for(SkillEndorse skillEndorse : skillEndorses){
                if(skillEndorse.getByUserId() == user.getUserId()){
                    endorseButton.setVisible(false);
                    break;
                }
            }

            int endorseNum = Repository.listEndorseToUserSkill(owner.getUserId(), skill.getSkillId()).size();
            if(endorseNum == 0){
                endorseNumber.setVisible(false);
            }else endorseNumber.setText(String.valueOf(endorseNum));

            endorseButton.setOnAction(event -> endorse(skill, endorseNum));

            endorseNumber.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> listEndorse(skill));

            skillTitle.setText(skill.getTitle());

            setText(null);
            setGraphic(rootAnchorPane);
        }
    }

    private void endorse(Skill skill, int endorseNum){
        Repository.addSkillEndorse(user.getUserId(), owner.getUserId(), skill.getSkillId());
        endorseButton.setVisible(false);
        endorseNumber.setText(String.valueOf(endorseNum+1));
    }

    private void listEndorse(Skill skill){
        OpenWindow.openWindowWait("view/EndorseList.fxml", new EndorseListController(user, owner, skill),"Endorse List");
    }

}
