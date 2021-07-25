package app.controller;

import app.controller.cells.ChatCellController;
import app.controller.cells.NetworkCellController;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class MutualConnectionController {

    private User visitor;
    private User user;

    public MutualConnectionController(User visitor, User user) {
        this.visitor = visitor;
        this.user = user;
    }

    @FXML
    private JFXListView<User> userList;

    public void initialize(){

        ObservableList<User> users = FXCollections.observableArrayList(Repository.listMutual(visitor.getUserId(), user.getUserId()));
        userList.setItems(users);
        userList.setCellFactory(NetworkCellController -> new NetworkCellController(false, visitor));

    }
}
