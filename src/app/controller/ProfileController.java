package app.controller;

import app.controller.cells.BackgroundCellController;
import app.controller.cells.LanguageCellController;
import app.controller.cells.SkillCellController;
import app.model.Background;
import app.model.Language;
import app.model.Skill;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

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
    private JFXListView<Background> backgroundList;

    @FXML
    private ImageView backgroundAdd;

    @FXML
    private JFXListView<?> accomplishmentsList;

    @FXML
    private ImageView accomplishmentsAdd;

    @FXML
    private ImageView languageAdd;

    @FXML
    private JFXListView<Language> languageList;

    @FXML
    private ImageView skillAdd;

    @FXML
    private JFXListView<Skill> skillList;

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

    @FXML
    private ImageView editSelectedSkill;

    @FXML
    private ImageView deleteSelectedSkill;

    @FXML
    private ImageView deleteSelectedBackground;

    @FXML
    private ImageView editSelectedBackground;

    @FXML
    private ImageView editSelectedLanguage;

    @FXML
    private ImageView deleteSelectedLanguage;


    private String nowIntro;
    private String nowAbout;
    private String nowName;
    private String nowFamily;
    private ObservableList<Background> backgrounds;
    private ObservableList<Skill> skills;
    private ObservableList<Language> languages;

    @FXML
    public void initialize(){

        name.setText(owner.getFirstname());
        family.setText(owner.getLastname());
        setImage();
        intro.setText(owner.getIntro());
        about.setText(owner.getAbout());

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
            deleteSelectedBackground.setVisible(false);
            editSelectedBackground.setVisible(false);
            deleteSelectedSkill.setVisible(false);
            editSelectedSkill.setVisible(false);
        }

        //Image
        changeImage.setOnAction(event -> imageChange());

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

        backgrounds = FXCollections.observableArrayList(Repository.listUserBackground(owner.getUserId()));
        backgroundList.setItems(backgrounds);
        backgroundList.setCellFactory(BackgroundCellController -> new BackgroundCellController(owner, user));

        deleteSelectedBackground.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteBackground());

        //Accomplishments
        accomplishmentsAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addAccomplishment());

        //Language
        languageAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addLanguage());

        languages = FXCollections.observableArrayList(Repository.listUserLanguage(owner.getUserId()));
        languageList.setItems(languages);
        languageList.setCellFactory(LanguageCellController -> new LanguageCellController(owner));

        deleteSelectedLanguage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteLanguage());

        //Skill
        skillAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addSkill());

        skills = FXCollections.observableArrayList();
        skills.addAll(Repository.listUserSkill(owner.getUserId()));
        skillList.setItems(skills);
        skillList.setCellFactory(SkillCellController -> new SkillCellController(owner, user));

        deleteSelectedSkill.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteSkill());

        //Home icon
        home.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> homePage());

        //MyNetwork icon
        network.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> myNetwork());

        logout.setOnAction(event -> logOut());

    }

    private void deleteSkill(){
        if(skillList.getSelectionModel().getSelectedItem()!=null) {
            OpenWindow.openWindowWait("view/DeleteWarning.fxml", new DeleteWarningController<Skill>(
                    skillList.getSelectionModel().getSelectedItem(), skills, user.getUserId()), "Warning"
            );
        }
    }

    private void deleteBackground(){
        if(backgroundList.getSelectionModel().getSelectedItem()!=null) {
            OpenWindow.openWindowWait("view/DeleteWarning.fxml", new DeleteWarningController<Background>(
                    backgroundList.getSelectionModel().getSelectedItem(), backgrounds, user.getUserId()), "Warning"
            );
        }
    }

    private void deleteLanguage(){
        if(languageList.getSelectionModel().getSelectedItem()!=null){
            OpenWindow.openWindowWait("view/DeleteWarning.fxml", new DeleteWarningController<Language>(
                    languageList.getSelectionModel().getSelectedItem(), languages, owner.getUserId()),"Warning"
            );
        }
    }

    private boolean isOwner(){
        return owner.getUserId()==user.getUserId();
    }

    private void setImage() {
        if(owner.getAvatar()!=null) {
            InputStream is = new ByteArrayInputStream(owner.getAvatar());
            BufferedImage bf=null;
            try {
                bf = ImageIO.read(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            WritableImage wr = null;
            if (bf != null) {
                wr = new WritableImage(bf.getWidth(), bf.getHeight());
                PixelWriter pw = wr.getPixelWriter();
                for (int x = 0; x < bf.getWidth(); x++) {
                    for (int y = 0; y < bf.getHeight(); y++) {
                        pw.setArgb(x, y, bf.getRGB(x, y));
                    }
                }
            }
            imagePlace.setFill(new ImagePattern(wr));
        }
    }

    private void imageChange(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image File","*.png","*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null) {
            try {
                BufferedImage bi = ImageIO.read(new File(selectedFile.getAbsolutePath()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bi, "jpg", baos);
                byte[] bytes = baos.toByteArray();
                Repository.updateAvatar(owner.getUserId(), bytes);
                owner.setAvatar(bytes);
                setImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        Repository.updateAvatar(owner.getUserId(), nowName, nowFamily, "");
        owner.setFirstname(nowName);
        owner.setLastname(nowFamily);
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
        Repository.updateInfo(owner.getUserId(), nowIntro);
        owner.setIntro(nowIntro);
    }

    private void changeAbout(){
        saveAbout.setVisible(false);
        cancelAbout.setVisible(false);
        about.setEditable(false);
        nowAbout = about.getText();
        Repository.updateAbout(owner.getUserId(), nowAbout);
        owner.setAbout(nowAbout);
    }

    private void logOut(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Login.fxml", new LoginController(), "Login");
    }

    private void addBackground(){
        int len = backgrounds.size() + 1;
        OpenWindow.openWindowWait("view/AddBackground.fxml", new AddBackgroundController(owner),
                "Background");
        int lenBackground = Repository.listUserBackground(owner.getUserId()).size();
        if(len == lenBackground){
            for(Background background:Repository.listUserBackground(owner.getUserId())){
                if(!backgrounds.contains(background)){
                    backgrounds.add(background);
                    break;
                }
            }
        }
    }

    private void addAccomplishment(){
        OpenWindow.openWindowWait("view/AddAccomplishments.fxml", new AddAccomplishmentsController(owner),
                "Accomplishment");
    }

    private void addLanguage(){
        int len = languages.size() + 1;
        OpenWindow.openWindowWait("view/AddSupportedLanguage.fxml", new AddSupportedLanguageController(owner),
                "Add Language");
        int lenLanguage = Repository.listUserLanguage(owner.getUserId()).size();
        if(len==lenLanguage){
            for(Language language: Repository.listUserLanguage(owner.getUserId())){
                if(!languages.contains(language)){
                    languages.add(language);
                    break;
                }
            }
        }
    }

    private void addSkill(){
        int len = skills.size()+1;
        OpenWindow.openWindowWait("view/AddSkill.fxml", new AddSkillController(owner),
                "Add Skill");
        int lenSkill = Repository.listUserSkill(owner.getUserId()).size();
        if(len == lenSkill){
            for(Skill skill:Repository.listUserSkill(owner.getUserId())){
                if(!skills.contains(skill)){
                    skills.add(skill);
                    break;
                }
            }
            //skills.add(Repository.getLastUserSkill(user.getUserId()));
        }
    }

    private void homePage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Home.fxml", new HomeController(owner),
                "Home");
    }

    private void myNetwork(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Network.fxml", new NetworkController(owner), "MyNetwork");
    }

}
