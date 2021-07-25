package app.controller;

import app.controller.cells.MessageCellController;
import app.model.Chat;
import app.model.Message;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

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

    private ObservableList<Message> messages;

    @FXML
    public void initialize() {

        messages = FXCollections.observableArrayList(Repository.listUserMessages(user.getUserId(), chat.getChatId()));
        messageList.setItems(messages);
        messageList.setCellFactory(MessageCellController -> new MessageCellController(user));

        //sendButton.setOnAction(event -> sendMessage());
    }

    private void sendMessage(){
        String text = messageText.getText().trim();
        if(!text.isEmpty()) {
            Message message = Repository.sendMessage(chat.getChatId(), user.getUserId(), -1, text);
            //System.out.println(message.getSenderUserId());
            messages.add(message);
        }
    }
}
