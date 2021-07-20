package app.controller;

import app.model.Background;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddBackgroundController {

    private User user;
    private Background background;

    public AddBackgroundController(User user) {
        this.user = user;
        background = null;
    }

    public AddBackgroundController(User user, Background background){
        this.user = user;
        this.background = background;
    }

    @FXML
    private TextArea titleBox;

    @FXML
    private Label titleError;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton saveButton;

    @FXML
    private TextField startDateBox;

    @FXML
    private Label startDateError;

    @FXML
    private TextField endDateBox;

    @FXML
    private Label endDateError;

    @FXML
    private JFXComboBox<Background.BgType> type;

    private Date start;
    private Date end;

    @FXML
    public void initialize(){

        ObservableList<Background.BgType> bgTypes = FXCollections.observableArrayList(Background.BgType.values());
        type.setItems(bgTypes);
        type.setValue(Background.BgType.Work);

        if(background!=null){
            titleBox.setText(background.getTitle());
            type.setValue(background.getBgType());
            startDateBox.setText(parseDate(background.getFromTime()));
            endDateBox.setText(parseDate(background.getToTime()));
        }

        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        saveButton.setOnAction(event -> saveBackground());
    }

    private void saveBackground(){
        boolean valid = validation();
        if(valid){
            if(background==null) {
                Repository.saveUserBackground(new Background(-1, user.getUserId(), titleBox.getText().trim()
                        , type.getSelectionModel().getSelectedItem(), start, end));
            }else{
                background.setTitle(titleBox.getText().trim());
                background.setBgType(type.getSelectionModel().getSelectedItem());
                background.setFromTime(start);
                background.setToTime(end);
                Repository.saveUserBackground(background);
            }
            saveButton.getScene().getWindow().hide();
        }
    }

    private boolean validation(){
        startDateError.setText("");
        titleError.setText("");
        endDateError.setText("");
        boolean valid = true;
        if(titleBox.getText().trim().isEmpty()){
            titleError.setText("Title can not be empty");
            valid = false;
        }
        if(startDateBox.getText().trim().isEmpty()){
            startDateError.setText("start date can not be empty");
            valid = false;
        }
        else {
            try {
                start = getDate(startDateBox.getText().trim());
            } catch (ParseException e) {
                startDateError.setText("start date format is wrong. example : 20-02-2010");
                valid = false;
            }
            if(endDateBox.getText().trim().isEmpty()){
                end = null;
            }
            else {
                try {
                    end = getDate(endDateBox.getText().trim());
                } catch (ParseException e) {
                    endDateError.setText("end date format is wrong. example : 20-02-2010");
                    valid = false;
                }
            }
        }
        return valid;
    }

    private Date getDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = date;
        if(dateInString.isEmpty()){
            return null;
        }
        return formatter.parse(dateInString);
    }

    private String parseDate(Date date){
        if(date!=null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            return formatter.format(date);
        }
        return "";
    }

}
