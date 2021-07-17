package app.controller.cells;

import app.controller.DeleteWarningController;
import app.controller.OpenWindow;
import app.model.Skill;
import app.model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

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
            if(owner == user){
                endorseButton.setVisible(false);
            }

            skillTitle.setText(skill.getTitle());

            setText(null);
            setGraphic(rootAnchorPane);
        }
    }


}
