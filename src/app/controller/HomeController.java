package app.controller;

import app.controller.cells.ArticleCellController;
import app.model.Article;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

public class HomeController {

    private User user;
    public HomeController(User user){
        this.user = user;
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
    private JFXButton addArticleButton;

    @FXML
    private JFXButton logout;

    @FXML
    private JFXListView<Article> articleList;

    @FXML
    private JFXComboBox<?> sortedByComboBox;

    @FXML
    private JFXButton sortedByButton;

    private ObservableList<Article> articles;

    @FXML
    public void initialize(){

        name.setText(user.getFirstname());
        family.setText(user.getLastname());
        Location.setText(user.getLocation());
        setImage();

        //System.out.println(user.getUserId());

        articles = FXCollections.observableArrayList(Repository.listHomeUserArticles(user.getUserId()));
        articleList.setItems(articles);
        articleList.setCellFactory(ArticleCellController -> new ArticleCellController(user));

        //Profile
        profile.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> profilePage());

        //createPost
        addArticleButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> createArticle());

        //MyNetwork
        network.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> myNetwork());

        //Notification
        notification.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> notificationPage());

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

    private void createArticle(){
        int len = articles.size()+1;
        imagePlace.getScene().getWindow();
        OpenWindow.openWindowWait("view/AddArticle.fxml", new AddArticleController(user),
                "Add Article");

        List<Article> listArticle = Repository.listHomeUserArticles(user.getUserId());
        if(len == listArticle.size()){
            for(Article article: listArticle){
                if(!articles.contains(article)){
                    articles.add(article);
                    break;
                }
            }
            articleList.setItems(articles);
        }
    }

    private void myNetwork(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Network.fxml", new NetworkController(user), "MyNetwork");
    }

    private void notificationPage(){
        imagePlace.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Notification.fxml", new NotificationController(user), "Notification");
    }

}
