package app.controller;

import app.controller.cells.NotificationCellController;
import app.controller.cells.SearchResultCellController;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import java.util.List;

public class SearchResultController {

    private User user;
    private List<User> users;

    public SearchResultController(User user, List<User> users) {
        this.user = user;
        this.users = users;
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
    private ImageView iconSearch;

    @FXML
    private JFXButton advanceSearch;

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
    private JFXListView<User> resultList;

    private ObservableList<User> userList;

    public void initialize(){

        name.setText(user.getFirstname());
        family.setText(user.getLastname());
        Location.setText(user.getLocation());
        setImage();

        //System.out.println(users.size());

        userList = FXCollections.observableArrayList(users);
        resultList.setItems(userList);
        resultList.setCellFactory(CellController -> new SearchResultCellController(user));

        //Profile
        profile.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> profilePage());

        //Home
        home.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> homePage());

        //MyNetwork
        network.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> myNetwork());

        //Notification
        notification.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> notificationPage());

        advanceSearch.setOnAction(event -> searchAdvance());

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

    private void myNetwork(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Network.fxml", new NetworkController(user), "MyNetwork");
    }

    private void notificationPage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Notification.fxml", new NotificationController(user), "Notification");
    }

    private void searchAdvance(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/AdvanceSearch.fxml", new AdvanceSearchController(user), "Advance Search");
    }

}
