package app.controller;

import app.controller.cells.ChatCellController;
import app.model.Chat;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class AddToChatController {

    private User owner;
    private User wantAdd;

    public AddToChatController(User owner, User wantAdd) {
        this.owner = owner;
        this.wantAdd = wantAdd;
    }

    @FXML
    private JFXListView<Chat> chatList;

    @FXML
    private JFXButton addToChatButton;

    @FXML
    private Label errorIsMember;

    private ObservableList<Chat> chats;

    public void initialize(){
        chats = FXCollections.observableArrayList(Repository.listUserChats(owner.getUserId()));
        chatList.setItems(chats);
        chatList.setCellFactory(ChatCellController -> new ChatCellController(owner));

        addToChatButton.setOnAction(event -> addToChat());
    }

    private void addToChat(){
        Chat chat = chatList.getSelectionModel().getSelectedItem();
        if(chat!=null){
            boolean res = Repository.addChatUser(chat.getChatId(), wantAdd.getUserId(), false);
            if(res) errorIsMember.setVisible(false);
            else errorIsMember.setVisible(true);
        }
    }
}
