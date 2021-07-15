package app.controller;

import app.model.User;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class AddArticleController {

    private User user;

    public AddArticleController(User user) {
        this.user = user;
    }

    @FXML
    private TextArea titleBox;

    @FXML
    private TextArea textBox;

    @FXML
    private Label chooseTypeLabel;

    @FXML
    private Label address;

    @FXML
    private Label titleError;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton postButton;

    @FXML
    public void initialize() {

        postButton.setOnAction(event -> {post();});

        cancelButton.setOnAction(event -> {cancel();});

    }

    private void cancel(){
        cancelButton.getScene().getWindow().hide();
    }

    private void post(){
        String title = titleBox.getText().replaceFirst("\\s++$", "");
        if(title.length() == 0 ){
            titleError.setText("Title must be write");
        }
        else{
            //add to database
            postButton.getScene().getWindow().hide();
        }
    }

}
