package app.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton signupButton;

    @FXML
    private Label incorrectLabel;

    @FXML
    private TextField passwordShowField;

    @FXML
    private ImageView showPassword;

    @FXML
    private ImageView hidePassword;

    public void initialize(){
        showPassword.setVisible(false);
        passwordShowField.setVisible(false);
        hidePassword.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showPassword());
        showPassword.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> hidePassword());

        loginButton.setOnAction(event -> {login();});

        signupButton.setOnAction(event -> {signup();});

    }

    private void showPassword(){
        hidePassword.setVisible(false);
        passwordShowField.setText(passwordField.getText());
        passwordShowField.setVisible(true);
        showPassword.setVisible(true);
        passwordField.setVisible(false);
    }

    private void hidePassword(){
        showPassword.setVisible(false);
        hidePassword.setVisible(true);
        passwordField.setText(passwordShowField.getText());
        passwordField.setVisible(true);
        passwordShowField.setVisible(false);
    }

    private void login(){
        incorrectLabel.setText("");
        String username = usernameField.getText().replaceFirst("\\s++$", "");
        String password;
        if(hidePassword.isVisible()){
            password = passwordField.getText().replaceFirst("\\s++$", "");
        }else{
            password = passwordShowField.getText().replaceFirst("\\s++$", "");
        }

        /*User user = User.login(username, password);
        if(user == null){
            incorrectLabel.setText("Incorrect username or password");
        }
        else{
            profilePage(user);
        }*/

    }

    private void signup(){
        signupButton.getScene().getWindow().hide();

        OpenWindow.openWindow("view/Signup.fxml",new SignupController(), "Linkedin - Signup Page");
    }


}
