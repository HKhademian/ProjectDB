package app.controller.cells;

import app.model.Language;
import app.model.User;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LanguageCellController extends JFXListCell<Language> {

    private User user;

    public LanguageCellController(User user) {
        this.user = user;
    }

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private TextField languageTitle;

    private FXMLLoader fxmlLoader;

    @FXML
    public void initialize() {

    }

    public void updateItem(Language language, boolean empty){
        super.updateItem(language, empty);

        if(empty || language == null){
            setText(null);
            setGraphic(null);
        }else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("../../../view/cells/LanguageCell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            languageTitle.setText(language.getTitle());

            setText(null);
            setGraphic(rootAnchorPane);

        }
    }

}
