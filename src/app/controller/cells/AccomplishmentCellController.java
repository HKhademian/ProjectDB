package app.controller.cells;

import app.model.Accomplishment;
import app.model.User;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AccomplishmentCellController extends JFXListCell<Accomplishment> {

    private User user;

    public AccomplishmentCellController(User user){this.user = user;}

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextArea title;

    @FXML
    private TextArea about;

    private FXMLLoader fxmlLoader;

    @FXML
    public void initialize() {

    }

    public void updateItem(Accomplishment accomplishment, boolean empty){
        super.updateItem(accomplishment, empty);

        if(empty || accomplishment == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/cells/AccomplishmentsCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            title.setText(accomplishment.getTitle());
            about.setText(accomplishment.getContent());

            setText(null);
            setGraphic(rootAnchorPane);
        }
    }
}
