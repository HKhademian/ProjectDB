package app.controller;

import app.controller.cells.ChatCellController;
import app.controller.cells.SearchResultCellController;
import app.model.Chat;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
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
import java.util.List;

public class MessagingController {

    private User user;

    public MessagingController(User user) {
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
    private JFXListView<Chat> chatList;

    @FXML
    private Label createChatLabel;

    @FXML
    private ImageView createChatIcon;

    @FXML
    private JFXButton openChat;


    @FXML
    private JFXButton addToArchive;

    @FXML
    private JFXButton mute;

    @FXML
    private JFXButton unArchive;

    @FXML
    private JFXButton unMute;

    @FXML
    private Label errorLabel;

    private ObservableList<Chat> chats;

    public void initialize(){

        name.setText(user.getFirstname());
        family.setText(user.getLastname());
        Location.setText(user.getLocation());
        setImage();

        chats = FXCollections.observableArrayList(Repository.listUserChats(user.getUserId()));
        chatList.setItems(chats);
        chatList.setCellFactory(ChatCellController -> new ChatCellController(user));

        createChatIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> createChat());
        createChatLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> createChat());

        openChat.setOnAction(event -> chatOpen());

        addToArchive.setOnAction(event -> archiveChat(true));
        unArchive.setOnAction(event -> archiveChat(false));

        mute.setOnAction(event -> muteChat(true));
        unMute.setOnAction(event -> muteChat(false));

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

    private void createChat(){
        int len = chats.size()+1;
        OpenWindow.openWindowWait("view/CreateChat.fxml", new CreateChatController(user), "Create Chat");
        List<Chat> listChat = Repository.listUserChats(user.getUserId());
        if(listChat.size() == len){
            for(Chat chat: listChat){
                if(!chats.contains(chat)){
                    chats.add(chat);
                    break;
                }
            }
        }
    }

    private void chatOpen(){
        Chat chat = chatList.getSelectionModel().getSelectedItem();
        if(chat!=null){
            OpenWindow.openWindowWait("view/Chat.fxml", new ChatController(user, chat), "Chat");
        }
    }

    private void archiveChat(boolean b){
        Chat chat = chatList.getSelectionModel().getSelectedItem();
        if(chat!=null){
            boolean res = Repository.updateChat(chat.getChatId(), user.getUserId(), b, chat.isMuted());
            if(!res){
                if(b)errorLabel.setText("You before archive this chat");
                else errorLabel.setText("this chat is not archived");
                errorLabel.setVisible(true);
            }
            else errorLabel.setVisible(false);
            chats.remove(chat);
            chats.add(chat);
        }
    }

    private void muteChat(boolean b){
        Chat chat = chatList.getSelectionModel().getSelectedItem();
        if(chat!=null){
            boolean res = Repository.updateChat(chat.getChatId(), user.getUserId(), chat.isArchived(), b);
            if(!res){
                if(b) errorLabel.setText("You before mute this chat");
                else errorLabel.setText("this chat is not muted");
                errorLabel.setVisible(true);
            }
            else errorLabel.setVisible(false);
            chats.remove(chat);
            chats.add(chat);
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
