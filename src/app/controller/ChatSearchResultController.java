package app.controller;

import app.controller.cells.ChatCellController;
import app.model.Chat;
import app.model.User;
import app.repository.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;

public class ChatSearchResultController {

    private User user;
    private String search;

    public ChatSearchResultController(User user, String search) {
        this.user = user;
        this.search = search;
    }

    @FXML
    private JFXListView<Chat> chatList;

    @FXML
    private JFXButton openChat;

    private ObservableList<Chat> chats;

    public void initialize(){

        chats = FXCollections.observableArrayList(Repository.searchChat(user.getUserId(), search));
        chatList.setItems(chats);
        chatList.setCellFactory(ChatCellController -> new ChatCellController(user));

        openChat.setOnAction(event -> chatOpen());

    }

    private void chatOpen(){
        Chat chat = chatList.getSelectionModel().getSelectedItem();
        if(chat!=null){
            OpenWindow.openWindowWait("view/Chat.fxml", new ChatController(user, chat), "Chat");
        }
    }

}
