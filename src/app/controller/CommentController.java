package app.controller;

import app.controller.cells.CommentCellController;
import app.model.Article;
import app.model.Comment;
import app.model.User;
import app.repository.Repository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class CommentController {

    private User user;
    private Article article;

    public CommentController(User user, Article article){
        this.user = user;
        this.article = article;
    }

    @FXML
    private JFXListView<Comment> commentList;

    @FXML
    private ImageView addCommentIcon;

    @FXML
    private JFXButton addCommentButton;

    private ObservableList<Comment> comments;


    @FXML
    public void initialize(){

        List<Comment> listComment = Repository.listComments(user.getUserId(),article.getArticleId());
        List<Comment> replyComments = new ArrayList<>();
        comments = FXCollections.observableArrayList();
        commentList.setItems(comments);
        for(Comment comment: listComment){
            String content = comment.getContent();
            int replyId = comment.getReplyCommentId();
            int articleId = comment.getArticleId();

            System.out.println(comment);
            System.out.println(content + " " +replyId+ " " + articleId);

            if(replyId<=0){
                comments.add(comment);
            }else{
              //FIXME
                //replyComments.add(comment);
            }
        }
        commentList.setCellFactory(CellFactory -> new CommentCellController(user, article, listComment, replyComments));


        addCommentButton.setOnAction(event -> addComment());
        addCommentIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> addComment());
    }

    private void addComment(){
        int len = comments.size()+1;
        OpenWindow.openWindowWait("view/AddComment.fxml", new AddCommentController(user, article),
                "Add Comment");
        List<Comment> listComment = Repository.listComments(user.getUserId(),article.getArticleId());
        if(len == listComment.size()){
            for(Comment comment: listComment){
                if(!comments.contains(comment)){
                    comments.add(comment);
                    break;
                }
            }
        }
    }
}
