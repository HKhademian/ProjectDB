package app.controller;

import app.model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class ProfileController {

    private User owner;
    private User user;

    public ProfileController(User owner, User user) {
        this.user = user;
        this.owner = owner;
    }

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
    private ImageView skillAdd;

    @FXML
    private JFXListView<?> SkillList;

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

    @FXML
    private ImageView featureAdd;

    @FXML
    private JFXListView<?> featureList;

    @FXML
    private TextField locationProfile;

    @FXML
    private TextField name;

    @FXML
    private TextField family;

    @FXML
    private ImageView editNFL;

    @FXML
    private JFXButton saveChangeNFL;

    @FXML
    private JFXButton cancelChangeNFL;

    @FXML
    private JFXComboBox<?> changeLocation;

    @FXML
    private ImageView home;

    @FXML
    private ImageView network;

    @FXML
    private ImageView messaging;

    @FXML
    private ImageView notification;

    @FXML
    private ImageView profile;

    private String nowIntro;
    private String nowAbout;
    private String nowName;
    private String nowFamily;

    @FXML
    public void initialize(){

        name.setText(owner.getFirstname());
        family.setText(owner.getLastname());

        saveIntro.setVisible(false);
        saveAbout.setVisible(false);
        cancelIntro.setVisible(false);
        cancelAbout.setVisible(false);
        saveChangeNFL.setVisible(false);
        cancelChangeNFL.setVisible(false);
        changeLocation.setVisible(false);

        nowIntro = owner.getIntro();
        nowAbout = owner.getAbout();
        nowName = owner.getFirstname();
        nowFamily = owner.getLastname();

        if(!isOwner()){
            introEdit.setVisible(false);
            aboutEdit.setVisible(false);
            accomplishmentsAdd.setVisible(false);
            backgroundAdd.setVisible(false);
            languageAdd.setVisible(false);
            featureAdd.setVisible(false);
            editNFL.setVisible(false);
        }

        //Name, Family, Location
        editNFL.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> changeNFL());
        saveChangeNFL.setOnAction(event -> saveNFL());
        cancelChangeNFL.setOnAction(event -> cancelNFL());

        //Intro, About
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

        //Background
        backgroundAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addBackground());

        //Accomplishments
        accomplishmentsAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addAccomplishment());

        //Language
        languageAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addLanguage());

        //Skill
        skillAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addSkill());

        //Home icon
        home.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> homePage());

        logout.setOnAction(event -> logOut());
    }

    private boolean isOwner(){
        return owner.getUserId()==user.getUserId();
    }

    private void changeNFL(){
        saveChangeNFL.setVisible(true);
        cancelChangeNFL.setVisible(true);
        name.setEditable(true);
        name.requestFocus();
        family.setEditable(true);
        locationProfile.setVisible(false);
        changeLocation.setVisible(true);
    }

    private void saveNFL(){
        nowName = name.getText().trim();
        nowFamily = family.getText().trim();
        //get location
        //add to database
        saveChangeNFL.setVisible(false);
        cancelChangeNFL.setVisible(false);
        name.setEditable(false);
        family.setEditable(false);
        locationProfile.setVisible(true);
        changeLocation.setVisible(false);
    }

    private void cancelNFL(){
        name.setText(nowName);
        family.setText(nowFamily);
        saveChangeNFL.setVisible(false);
        cancelChangeNFL.setVisible(false);
        name.setEditable(false);
        family.setEditable(false);
        locationProfile.setVisible(true);
        changeLocation.setVisible(false);
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
        OpenWindow.openWindow("view/Login.fxml", new LoginController(), "Linkedin - Login");
    }

    private void addBackground(){
        imagePlace.getScene().getWindow();
        OpenWindow.openWindowWait("view/AddBackground.fxml", new AddBackgroundController(owner),
                "Background");
    }

    private void addAccomplishment(){
        imagePlace.getScene().getWindow();
        OpenWindow.openWindowWait("view/AddAccomplishments.fxml", new AddAccomplishmentsController(owner),
                "Accomplishment");
    }

    private void addLanguage(){
        imagePlace.getScene().getWindow();
        OpenWindow.openWindowWait("view/AddSupportedLanguage.fxml", new AddSupportedLanguageController(owner),
                "Add Language");
    }

    private void addSkill(){
        imagePlace.getScene().getWindow();
        OpenWindow.openWindowWait("view/AddSkill.fxml", new AddSkillController(owner),
                "Add Skill");
    }

    private void homePage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Home.fxml", new HomeController(owner),
                "Home");
    }

}
