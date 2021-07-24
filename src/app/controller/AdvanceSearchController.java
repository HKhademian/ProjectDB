package app.controller;

import app.model.Language;
import app.model.Skill;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AdvanceSearchController {

    private User user;

    public AdvanceSearchController(User user) {
        this.user = user;
    }

    @FXML
    private Circle imagePlace;

    @FXML
    private TextField Location;

    @FXML
    private TextField name;

    @FXML
    private TextField family;

    @FXML
    private TextField searchBox;

    @FXML
    private ImageView searchIcon;

    @FXML
    private JFXButton logout;

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
    private JFXButton advanceSearch;

    @FXML
    private TextField nameSearch;

    @FXML
    private JFXComboBox<String> locationList;

    @FXML
    private JFXComboBox<String> skill;

    @FXML
    private JFXComboBox<String> language;

    @FXML
    private TextField background;

    @FXML
    private JFXButton searchButton;

    private ArrayList<String> skillList;
    private ArrayList<Integer> skillsId;
    private ArrayList<String> languageList;
    private ArrayList<String> languageCode;

    @FXML
    private Label errorLabel;

    public void initialize(){

        name.setText(user.getFirstname());
        family.setText(user.getLastname());
        Location.setText(user.getLocation());
        setImage();
        errorLabel.setVisible(false);

        //location
        ObservableList<String> locations = FXCollections.observableArrayList(Repository.suggestLocation());
        locationList.setItems(locations);
        new ComboBoxAutoComplete<>(locationList);

        //skill
        skillList = new ArrayList<>();
        skillsId = new ArrayList<>();

        for(Skill skill: Repository.listSkills()){
            skillList.add(skill.getTitle());
            skillsId.add(skill.getSkillId());
        }
        ObservableList<String> skillsList = FXCollections.observableArrayList(skillList);
        skill.setItems(skillsList);
        new ComboBoxAutoComplete(skill);
        skill.requestFocus();

        //language
        languageList = new ArrayList<>();
        languageCode = new ArrayList<>();
        for(Language language: Repository.listLanguages()){
            languageList.add(language.getTitle());
            languageCode.add(language.getCode());
        }
        ObservableList<String> languagesList = FXCollections.observableArrayList(languageList);
        language.setItems(languagesList);
        new ComboBoxAutoComplete(language);

        searchButton.setOnAction(event -> search());

        //Profile
        profile.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> profilePage());

        //Home
        home.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> homePage());

        //Notification
        notification.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> notificationPage());

        //MyNetwork
        network.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> myNetwork());

        logout.setOnAction(event -> logOut());
    }

    private void setImage() {
        if(user.getAvatar()!=null) {
            InputStream is = new ByteArrayInputStream(user.getAvatar());
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

    private void logOut(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Login.fxml", new LoginController(), "Login");
    }

    private void profilePage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Profile.fxml", new ProfileController(user, user),
                "Profile");
    }

    private void homePage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Home.fxml", new HomeController(user), "Home");
    }

    private void notificationPage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Notification.fxml", new NotificationController(user), "Notification");
    }

    private void myNetwork(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Network.fxml", new NetworkController(user), "MyNetwork");
    }

    private void search(){
        String n = nameSearch.getText().trim();
        String lc = locationList.getSelectionModel().getSelectedItem();
        String s = skill.getSelectionModel().getSelectedItem();
        String ln = language.getSelectionModel().getSelectedItem();
        String b = background.getText().trim();
        if(n.isEmpty() && lc == null && s==null && ln==null && b.isEmpty()){
            errorLabel.setVisible(true);
            return;
        }

        imagePlace.getScene().getWindow().hide();

        Integer idSkill=null;
        if(s!=null) idSkill = skillsId.get(skillList.indexOf(s));

        String langCode = null;
        if(ln!=null) langCode = languageCode.get(languageList.indexOf(ln));

        if(n.isEmpty()) n = null;
        if(b.isEmpty()) b = null;

        OpenWindow.openWindow("view/SearchResult.fxml", new SearchResultController(user,
                Repository.searchProfiles(n, lc, idSkill, langCode ,b)),"Search Result");

    }
}
