package app.controller;

import app.controller.cells.*;
import app.model.*;
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
import java.util.List;

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
    private ImageView iconSearch;

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
    private JFXListView<Accomplishment> accomplishmentsList;

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
    private JFXListView<Article> featureList;

    @FXML
    private TextField locationField;

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
    private JFXComboBox<String> changeLocation;

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
    private ImageView deleteSelectedSkill;

    @FXML
    private ImageView deleteSelectedBackground;

    @FXML
    private ImageView editSelectedBackground;

    @FXML
    private ImageView deleteSelectedLanguage;

    @FXML
    private JFXButton unFeature;


    @FXML
    private ImageView deleteSelectedAccomplishment;

    @FXML
    private ImageView editSelectedAccomplishment;


    @FXML
    private JFXButton advanceSearch;

    private String nowIntro;
    private String nowAbout;
    private String nowName;
    private String nowFamily;
    private String nowLocation;
    private ObservableList<Background> backgrounds;
    private ObservableList<Skill> skills;
    private ObservableList<Language> languages;
    private ObservableList<Article> featureArticles;
    private ObservableList<Accomplishment> accomplishments;

    @FXML
    public void initialize(){

        init();

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
        backgroundList.setCellFactory(BackgroundCellController -> new BackgroundCellController(owner));

        deleteSelectedBackground.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteBackground());
        editSelectedBackground.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> editBackground());

        //Accomplishments
        accomplishmentsAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addAccomplishment());
        accomplishments = FXCollections.observableArrayList(Repository.listUserAccomplishment(owner.getUserId()));
        accomplishmentsList.setItems(accomplishments);
        accomplishmentsList.setCellFactory(AccomplishmentCellController -> new AccomplishmentCellController(owner));

        deleteSelectedAccomplishment.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteAccomplishment());
        editSelectedAccomplishment.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> editAccomplishment());

        //Language
        languageAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addLanguage());

        languages = FXCollections.observableArrayList(Repository.listUserLanguage(owner.getUserId()));
        languageList.setItems(languages);
        languageList.setCellFactory(LanguageCellController -> new LanguageCellController(owner));

        deleteSelectedLanguage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteLanguage());

        //Skill
        skillAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addSkill());

        skills = FXCollections.observableArrayList(Repository.listUserSkills(owner.getUserId()));
        skillList.setItems(skills);
        skillList.setCellFactory(SkillCellController -> new SkillCellController(owner, user));

        deleteSelectedSkill.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> deleteSkill());

        //feature
        setFeatureList();
        featureAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addFeature());
        unFeature.setOnAction(event -> unFeatureArticle());

        //Home icon
        home.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> homePage());

        //MyNetwork icon
        network.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> myNetwork());

        //profile
        profile.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> userProfile());

        //Notification
        notification.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> notificationPage());

        advanceSearch.setOnAction(event -> searchAdvance());
        iconSearch.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> search());

        logout.setOnAction(event -> logOut());

    }

    private void init(){
        name.setText(owner.getFirstname());
        family.setText(owner.getLastname());
        locationField.setText(owner.getLocation());
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
        nowLocation = owner.getLocation();

        if(!isOwner()){
            Repository.getUserById(user.getUserId(), owner.getUserId());
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
            deleteSelectedLanguage.setVisible(false);
            unFeature.setVisible(false);
            skillAdd.setVisible(false);
            changeImage.setVisible(false);
            editSelectedAccomplishment.setVisible(false);
            deleteSelectedAccomplishment.setVisible(false);
        }
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

    private void deleteAccomplishment(){
        Accomplishment accomplishment = accomplishmentsList.getSelectionModel().getSelectedItem();
        if(accomplishment!=null){
            OpenWindow.openWindowWait("view/DeleteWarning.fxml", new DeleteWarningController<Accomplishment>(
                    accomplishment, accomplishments, owner.getUserId()), "Warning");
        }
    }

    private void deleteLanguage(){
        if(languageList.getSelectionModel().getSelectedItem()!=null){
            OpenWindow.openWindowWait("view/DeleteWarning.fxml", new DeleteWarningController<Language>(
                    languageList.getSelectionModel().getSelectedItem(), languages, owner.getUserId()),"Warning"
            );
        }
    }

    private void editBackground(){
        Background background = backgroundList.getSelectionModel().getSelectedItem();
        if(background!=null){
            OpenWindow.openWindowWait("view/AddBackground.fxml", new AddBackgroundController(owner, background), "Edit Background");
        }
        backgrounds.remove(background);
        backgrounds.add(background);
    }

    private void editAccomplishment(){
        Accomplishment accomplishment = accomplishmentsList.getSelectionModel().getSelectedItem();
        if(accomplishment!=null){
            OpenWindow.openWindowWait("view/AddAccomplishment.fxml", new AddAccomplishmentsController(owner, accomplishment),
                    "Edit Accomplishment");
        }
        accomplishments.remove(accomplishment);
        accomplishments.add(accomplishment);
    }

    private void unFeatureArticle(){
        Article article = featureList.getSelectionModel().getSelectedItem();
        if(article != null){
            featureArticles.remove(article);
            article.setFeatured(false);
            Repository.removeUserFeaturedArticle(owner.getUserId(), article.getArticleId());
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
        ObservableList<String> locations = FXCollections.observableArrayList(Repository.suggestLocation());
        changeLocation.setItems(locations);
        new ComboBoxAutoComplete<>(changeLocation);

        saveChangeNFL.setVisible(true);
        cancelChangeNFL.setVisible(true);
        name.setEditable(true);
        name.requestFocus();
        family.setEditable(true);
        locationField.setVisible(false);
        changeLocation.setVisible(true);
    }

    private void saveNFL(){
        nowName = name.getText().trim();
        nowFamily = family.getText().trim();
        nowLocation = changeLocation.getSelectionModel().getSelectedItem();
        Repository.updateAvatar(owner.getUserId(), nowName, nowFamily, nowLocation);
        owner.setFirstname(nowName);
        owner.setLastname(nowFamily);
        owner.setLocation(nowLocation);
        locationField.setText(nowLocation);
        saveChangeNFL.setVisible(false);
        cancelChangeNFL.setVisible(false);
        name.setEditable(false);
        family.setEditable(false);
        locationField.setVisible(true);
        changeLocation.setVisible(false);
    }

    private void cancelNFL(){
        name.setText(nowName);
        family.setText(nowFamily);
        saveChangeNFL.setVisible(false);
        cancelChangeNFL.setVisible(false);
        name.setEditable(false);
        family.setEditable(false);
        locationField.setVisible(true);
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
                "Add Background");
        List<Background> backgroundList = Repository.listUserBackground(owner.getUserId());
        if(len == backgroundList.size()){
            for(Background background: backgroundList){
                if(!backgrounds.contains(background)){
                    backgrounds.add(background);
                    break;
                }
            }
        }

    }

    private void addAccomplishment(){
        int len = accomplishments.size()+1;
        OpenWindow.openWindowWait("AddAccomplishment.fxml", new AddAccomplishmentsController(owner),
                "Add Accomplishment");
        List<Accomplishment> listAccomplishment = Repository.listUserAccomplishment(owner.getUserId());
        if(len == listAccomplishment.size()){
            for(Accomplishment accomplishment: listAccomplishment){
                if(!accomplishments.contains(accomplishment)){
                    accomplishments.add(accomplishment);
                    break;
                }
            }
        }
    }

    private void addLanguage(){
        int len = languages.size() + 1;
        OpenWindow.openWindowWait("view/AddSupportedLanguage.fxml", new AddSupportedLanguageController(owner),
                "Add Language");
        List<Language> languageList = Repository.listUserLanguage(owner.getUserId());
        if(len==languageList.size()){
            for(Language language: languageList){
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
        List<Skill> skillList = Repository.listUserSkills(owner.getUserId());
        if(len == skillList.size()){
            for(Skill skill:skillList){
                if(!skills.contains(skill)){
                    skills.add(skill);
                    break;
                }
            }
        }
    }

    private void setFeatureList(){
        List<Article> articles = Repository.listUserArticles(owner.getUserId());
        featureArticles = FXCollections.observableArrayList();
        for(Article article: articles){
           if(article.isFeatured()) featureArticles.add(article);
        }
        featureList.setItems(featureArticles);
        featureList.setCellFactory(FeatureCellController -> new FeatureCellController(owner));
    }

    private void addFeature(){
        int len = featureArticles.size() + 1;
        OpenWindow.openWindowWait("view/AddFeature.fxml", new AddFeatureController(owner),
                "Add Feature");
        List<Article> articles = Repository.listUserArticles(owner.getUserId());
        for(Article article: articles){
            if(article.isFeatured() && !featureArticles.contains(article)){
                featureArticles.add(article);
                break;
            }
        }

    }

    private void homePage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Home.fxml", new HomeController(user),
                "Home");
    }

    private void myNetwork(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Network.fxml", new NetworkController(user), "MyNetwork");
    }

    private void userProfile(){
        if(!isOwner()){
            imagePlace.getScene().getWindow().hide();
            OpenWindow.openWindow("view/Profile.fxml", new ProfileController(user, user), "Profile");
        }
    }

    private void notificationPage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Notification.fxml", new NotificationController(user), "Notification");
    }

    private void searchAdvance(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/AdvanceSearch.fxml", new AdvanceSearchController(user), "Advance Search");
    }

    private void search(){
        String s = searchBox.getText().trim();
        if(!s.isEmpty()){
            imagePlace.getScene().getWindow().hide();
            OpenWindow.openWindow("view/SearchResult.fxml", new SearchResultController(user,
                    Repository.searchProfiles(s, null, null, null, null)),
                    "Search Result");
        }
    }

}
