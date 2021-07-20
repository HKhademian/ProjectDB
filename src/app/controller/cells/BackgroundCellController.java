package app.controller.cells;

import app.model.Background;
import app.model.User;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class BackgroundCellController extends JFXListCell<Background> {

    private User user;

    public BackgroundCellController(User user) {
        this.user = user;
    }

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextArea title;

    @FXML
    private Label startDate;

    @FXML
    private Label endDate;

    @FXML
    private Label type;

    private FXMLLoader fxmlLoader;

    @FXML
    public void initialize() {

    }

    public void updateItem(Background background, boolean empty){
        super.updateItem(background, empty);

        if(empty || background == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/cells/BackgroundCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            title.setText(background.getTitle());
            type.setText(background.getBgType().toString());
            startDate.setText(background.getFromTime().toString());
            if(background.getToTime()!=null) {
                endDate.setText(background.getToTime().toString());
            }else endDate.setText("");

            setText(null);
            setGraphic(rootAnchorPane);
        }
    }

}
