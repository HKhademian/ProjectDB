package app.controller;

import app.controller.cells.FeatureCellController;
import app.model.Article;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class AddFeatureController {

    private User user;

    public AddFeatureController(User user){
        this.user = user;
    }

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXListView<Article> articleList;

    @FXML
    private Label errorLabel;

    private ObservableList<Article> notFeatureArticles;


    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        setArticleList();
        cancelButton.setOnAction(event -> cancelButton.getScene().getWindow().hide());
        addButton.setOnAction(event -> addArticle());
    }

    private void setArticleList(){
        List<Article> articles = Repository.listUserFeaturedArticles(user.getUserId(),user.getUserId());
        notFeatureArticles = FXCollections.observableArrayList();
        for(Article article: articles){
            if(false /*!article.getFeatured() FIXME*/) notFeatureArticles.add(article);
        }
        articleList.setItems(notFeatureArticles);
        articleList.setCellFactory(FeatureCellController -> new FeatureCellController(user));
    }

    private void addArticle(){
        Article article = articleList.getSelectionModel().getSelectedItem();
        if(article==null){
            errorLabel.setVisible(true);
            return;
        }
        article.setFeatured(true);
        Repository.saveArticle(article);
        addButton.getScene().getWindow().hide();
    }
}
