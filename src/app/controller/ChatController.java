package app.controller;

import app.controller.cells.MessageCellController;
import app.controller.cells.NetworkCellController;
import app.model.Chat;
import app.model.Message;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {

    private User user;
    private Chat chat;

    public ChatController(User user, Chat chat) {
        this.user = user;
        this.chat = chat;
    }

    @FXML
    private JFXListView<Message> messageList;

    @FXML
    private TextArea messageText;

    @FXML
    private JFXButton sendButton;

    @FXML
    private JFXListView<User> memberList;

    @FXML
    private TextField searchChat;

    @FXML
    private JFXButton searchButton;

    private ObservableList<Message> messages;
    private ObservableList<User> users;

    @FXML
    public void initialize() {

        messages = FXCollections.observableArrayList(Repository.listUserMessages(user.getUserId(), chat.getChatId()));
        messageList.setItems(messages);
        messageList.setCellFactory(MessageCellController -> new MessageCellController(user));

        users = FXCollections.observableArrayList(Repository.listChatUsers(chat.getChatId()));
        memberList.setItems(users);
        memberList.setCellFactory(NetworkCellController -> new NetworkCellController(false, user));

        searchButton.setOnAction(event -> search());

        sendButton.setOnAction(event -> search());

        sendButton.setOnAction(event -> sendMessage());
    }

    private void sendMessage(){
        String text = messageText.getText().trim();
        if(!text.isEmpty()) {
            Message message = Repository.sendMessage(chat.getChatId(), user.getUserId(), -1, text);
            messages.add(message);
            messageText.setText("");
        }
    }

    private void search(){
        String s = searchChat.getText().trim();
        if(!s.isEmpty()){
            OpenWindow.openWindowWait("view/MessageSearchResult.fxml", new MessageSearchResultController(user, chat, s),
                    "Message Search Result");
        }
    }
}
