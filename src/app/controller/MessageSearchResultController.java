package app.controller;

import app.controller.cells.ChatCellController;
import app.controller.cells.MessageCellController;
import app.model.Chat;
import app.model.Message;
import app.model.User;
import app.repository.Repository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;


public class MessageSearchResultController {

    private User user;
    private String search;
    private Chat chat;

    public MessageSearchResultController(User user, Chat chat, String search) {
        this.user = user;
        this.chat = chat;
        this.search = search;
    }

    @FXML
    private JFXListView<Message> messageList;

    @FXML
    private JFXButton openChat;

    private ObservableList<Message> messages;

    public void initialize(){

        messages = FXCollections.observableArrayList(Repository.searchChat(user.getUserId(), chat.getChatId(), search));
        messageList.setItems(messages);
        messageList.setCellFactory(MessageCellController -> new MessageCellController(user));

    }

}
