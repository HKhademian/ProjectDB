package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class ProfileController {

    @FXML
    private Circle imagePlace;

    @FXML
    private TextField searchBox;

    @FXML
    private JFXButton changeImage;

    @FXML
    private ImageView introEdit;

    @FXML
    private TextArea intro;

    @FXML
    private ImageView aboutEdit;

    @FXML
    private TextArea about;

    @FXML
    private JFXListView<?> backgroundList;

    @FXML
    private ImageView backgroundAdd;

    @FXML
    private JFXListView<?> accomplishmentsList;

    @FXML
    private ImageView accomplishmentsAdd;

    @FXML
    private ImageView languageAdd;

    @FXML
    private JFXListView<?> languageList;

    @FXML
    private ImageView featuredAdd;

    @FXML
    private JFXListView<?> featuredList;


    @FXML
    private JFXButton saveIntro;

    @FXML
    private JFXButton saveAbout;


    @FXML
    private JFXButton cancelIntro;

    @FXML
    private JFXButton cancelAbout;


    @FXML
    private JFXButton logout;

    private String nowIntro;
    private String nowAbout;


    @FXML
    public void initialize(){

        saveIntro.setVisible(false);
        saveAbout.setVisible(false);
        cancelIntro.setVisible(false);
        cancelAbout.setVisible(false);

        nowIntro = "";
        nowAbout = "";

        if(!isOwner()){
            introEdit.setVisible(false);
            aboutEdit.setVisible(false);
            accomplishmentsAdd.setVisible(false);
            backgroundAdd.setVisible(false);
            languageAdd.setVisible(false);
            featuredAdd.setVisible(false);
        }

        introEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {editIntro();});
        aboutEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {editAbout();});

        saveIntro.setOnAction(event -> changeIntro());
        cancelIntro.setOnAction(event -> {
            intro.setText(nowIntro);
            saveIntro.setVisible(false);
            cancelIntro.setVisible(false);
            intro.setEditable(false);
        });

        saveAbout.setOnAction(event -> changeAbout());
        cancelAbout.setOnAction(event -> {
            about.setText(nowAbout);
            saveAbout.setVisible(false);
            cancelAbout.setVisible(false);
            about.setEditable(false);
        });

        logout.setOnAction(event -> logOut());
    }

    private boolean isOwner(){
        return true;
    }

    private void editIntro(){
        intro.setEditable(true);
        intro.requestFocus();
        saveIntro.setVisible(true);
        cancelIntro.setVisible(true);
    }

    private void editAbout(){
        about.setEditable(true);
        about.requestFocus();
        saveAbout.setVisible(true);
        cancelAbout.setVisible(true);
    }

    private void changeIntro(){
        saveIntro.setVisible(false);
        cancelIntro.setVisible(false);
        intro.setEditable(false);
        nowIntro = intro.getText();
        //save to database
    }

    private void changeAbout(){
        saveAbout.setVisible(false);
        cancelAbout.setVisible(false);
        about.setEditable(false);
        nowAbout = about.getText();
        //save to database
    }

    private void logOut(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("../view/Login.fxml", new LoginController(), "Linkedin - Login");
    }

}
