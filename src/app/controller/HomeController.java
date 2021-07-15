package app.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class HomeController {

    private int userId;
    public HomeController(int userId){
        this.userId = userId;
    }

    @FXML
    private Circle imagePlace;

    @FXML
    private TextField searchBox;

    @FXML
    private TextField Location;

    @FXML
    private TextField name;

    @FXML
    private TextField family;

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
    private JFXButton addPostButton;

    @FXML
    private JFXButton logout;

    @FXML
    private JFXListView<?> postList;

    @FXML
    private JFXComboBox<?> sortedByComboBox;

    @FXML
    private JFXButton sortedByButton;

    @FXML
    public void initialize(){

        //Profile
        profile.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> profilePage());

        //createPost
        addPostButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> createPost());

        logout.setOnAction(event -> logOut());
    }

    private void logOut(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Login.fxml", new LoginController(), "Linkedin - Login");
    }

    private void profilePage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Profile.fxml", new ProfileController(userId, userId),
                "Profile");
    }

    private void createPost(){
        imagePlace.getScene().getWindow();
        OpenWindow.openWindowWait("view/AddArticle.fxml", new AddArticleController(userId),
                "Create Article");
    }

}
