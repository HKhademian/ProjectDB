package app.controller;

import app.controller.cells.ChatCellController;
import app.model.Chat;
import app.model.User;
import app.repository.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;

import java.util.List;

public class ArchiveChatController {

    private User user;

    public ArchiveChatController(User user) {
        this.user = user;
    }

    @FXML
    private JFXListView<Chat> chatList;

    @FXML
    private JFXButton openChat;

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

        List<Chat> listChat = Repository.listUserChats(user.getUserId());
        chats = FXCollections.observableArrayList();
        for(Chat chat: listChat){
            if(chat.isArchived()){
                chats.add(chat);
            }
        }
        chatList.setItems(chats);
        chatList.setCellFactory(ChatCellController -> new ChatCellController(user));

        openChat.setOnAction(event -> chatOpen());

        unArchive.setOnAction(event -> unArchiveChat());

        mute.setOnAction(event -> muteChat(true));
        unMute.setOnAction(event -> muteChat(false));

    }

    private void chatOpen(){
        Chat chat = chatList.getSelectionModel().getSelectedItem();
        if(chat!=null){
            mute.getScene().getWindow().hide();
            OpenWindow.openWindowWait("view/Chat.fxml", new ChatController(user, chat), "Chat");
        }
    }

    private void unArchiveChat() {
        Chat chat = chatList.getSelectionModel().getSelectedItem();
        if (chat != null) {
            boolean res = Repository.updateChat(chat.getChatId(), user.getUserId(), false, chat.isMuted());
            if (!res) {
                chats.remove(chat);
            }
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
            Chat chat1 = Repository.getUserChat(chat.getChatId(), user.getUserId());
            chats.add(chat1);
        }
    }

}
