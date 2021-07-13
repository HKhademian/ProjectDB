package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SignupController {

    @FXML
    private JFXButton loginButton;

    @FXML
    private TextField usernameBox;

    @FXML
    private PasswordField passwordBox;

    @FXML
    private Label usernameError;

    @FXML
    private Label passwordError;

    @FXML
    private JFXCheckBox privacyBox;

    @FXML
    private JFXButton signupButton;

    @FXML
    private Label privacyError;

    @FXML
    private ImageView showPassword;

    @FXML
    private ImageView hidePassword;

    @FXML
    private TextField passwordShowField;

    @FXML
    private Label privacy;

    @FXML
    private TextField nameBox;

    @FXML
    private TextField familyBox;

    @FXML
    private TextField birthdayBox;

    @FXML
    private JFXComboBox<?> locationBox;

    @FXML
    public void initialize(){
        passwordShowField.setVisible(false);
        showPassword.setVisible(false);

        //ObservableList<?> locations = FXCollections.observableArrayList();
        //locationBox.setItems(locations);

        hidePassword.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showPassword());

        showPassword.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> hidePassword());

        signupButton.setOnAction(event -> { signUp(); });

        loginButton.setOnAction(event -> {login();});

    }

    private void showPassword(){
        hidePassword.setVisible(false);
        passwordShowField.setText(passwordBox.getText());
        passwordShowField.setVisible(true);
        showPassword.setVisible(true);
    }

    private void hidePassword(){
        showPassword.setVisible(false);
        hidePassword.setVisible(true);
        passwordBox.setText(passwordShowField.getText());
        passwordBox.setVisible(true);
        passwordShowField.setVisible(false);
    }

    private void signUp(){
        int valid = 1;
        usernameError.setText("");
        passwordError.setText("");
        privacyError.setText("");

        String username = usernameBox.getText().replaceFirst("\\s++$", "");
        String password;
        if(hidePassword.isVisible()){
            password = passwordBox.getText().replaceFirst("\\s++$", "");
        }
        else{
            password = passwordShowField.getText().replaceFirst("\\s++$", "");
        }

        /*int validUsername = Validation.usernameValidation(username);
        if(validUsername == 0){
            usernameError.setText("Invalid username");
            valid = 0;
        }else if(validUsername == 2){
            usernameError.setText("Length of username must be between 3 and 12");
            valid = 0;
        }

        int validPassword = Validation.passwordValidation(password);
        if(validPassword == 0){
            passwordError.setText("Invalid password");
            valid = 0;
        }else if(validPassword == 2){
            passwordError.setText("Length of your password must be between 6 and 16");
            valid = 0;
        }*/

        if(!privacyBox.isSelected()){
            privacyError.setText("You must agree with our privacy and policy");
            valid = 0;
        }

        if(valid == 1) {
            //create user
            //go to profile page
        }

    }

    private void login(){
        loginButton.getScene().getWindow().hide();
        OpenWindow.openWindow("../view/Login.fxml",new LoginController(), "Linkedin - Login Page");
    }


}
