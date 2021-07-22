package app.controller;


import app.model.Accomplishment;
import app.model.Background;
import app.model.Language;
import app.model.Skill;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class DeleteWarningController<T> {
    private T object;
    private ObservableList<T> lists;
    private int userId;

    public DeleteWarningController(T object, ObservableList<T> lists, int userId){
        this.object = object;
        this.lists = lists;
        this.userId = userId;
    }

    @FXML
    private JFXButton deleteButton;

    @FXML
    private JFXButton cancelButton;

    @FXML
    public void initialize(){

        deleteButton.setOnAction(event -> {delete();});

        cancelButton.setOnAction(event -> {cancelButton.getScene().getWindow().hide();});

    }

    private void delete(){
        if(object instanceof Skill){
            Repository.removeUserSkill(userId, ((Skill) object).getSkillId());
        }
        else if(object instanceof Background){
            Repository.deleteUserBackground(((Background) object).getBgId());
        }
        else if(object instanceof Language){
            Repository.removeUserLanguage(userId, ((Language) object).getCode());
        }
        else if(object instanceof Accomplishment){
            Repository.deleteUserAccomplishment(((Accomplishment) object).getAccId());
        }
        lists.remove(object);
        deleteButton.getScene().getWindow().hide();
    }

}

