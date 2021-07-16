package app.controller.cells;

import app.model.Skill;
import app.model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SkillCellController extends JFXListCell<Skill> {

    private User user;

    public SkillCellController(User user) {
        this.user = user;
    }

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ImageView edit;

    @FXML
    private ImageView delete;

    @FXML
    private TextField skill;

    @FXML
    private Label endorseNumber;

    @FXML
    private JFXButton endorseButton;

    private FXMLLoader fxmlLoader;

    @FXML
    public void initialize() {

    }

    public void updateItem(Skill skill, boolean empty){
        super.updateItem(skill, empty);

        if(empty || skill == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../../view/cells/SkillCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
