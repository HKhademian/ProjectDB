package app.controller;

import app.model.User;
import com.jfoenix.controls.JFXComboBox;
import app.repository.Repository;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private JFXComboBox<String> locationBox;

    @FXML
    private Label birthdayError;

    private Date birthday;

    @FXML
    public void initialize(){
        passwordShowField.setVisible(false);
        showPassword.setVisible(false);

        ObservableList<String> locations = FXCollections.observableArrayList(Repository.suggestLocation());
        locationBox.setItems(locations);
        new ComboBoxAutoComplete<>(locationBox);

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

        boolean valid = validation();

        if(valid) {
            String username = usernameBox.getText().trim();
            String password = getPassword();
            String name = nameBox.getText().trim();
            String family = familyBox.getText().trim();

            User user = Repository.registerUser(new User(0, username, name, family,
                    null, null, birthday, locationBox.getSelectionModel().getSelectedItem()), password);

            if(user==null){
                usernameError.setText("this username is taken");
                return;
            }

            signupButton.getScene().getWindow().hide();
            OpenWindow.openWindow("view/Profile.fxml", new ProfileController(user, user), "Profile");
        }

    }

    private Date getBirthday() throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = birthdayBox.getText().trim();
        if(dateInString.isEmpty()){
            return null;
        }
        Date date = formatter.parse(dateInString);
        return date;
    }

    private String getPassword(){
        String password;
        if(hidePassword.isVisible()){
            password = passwordBox.getText().replaceFirst("\\s++$", "");
        }
        else{
            password = passwordShowField.getText().replaceFirst("\\s++$", "");
        }
        return password;
    }

    private boolean validation(){
        boolean valid = true;
        usernameError.setText("");
        passwordError.setText("");
        privacyError.setText("");
        birthdayError.setText("");

        String username = usernameBox.getText().replaceFirst("\\s++$", "");
        String password = getPassword();

        if(username.isEmpty()){
            usernameError.setText("username can not be empty");
            valid = false;
        }
        if(password.isEmpty()){
            passwordError.setText("password can not be empty");
            valid = false;
        }

        if(!privacyBox.isSelected()){
            privacyError.setText("You must agree with our privacy and policy");
            valid = false;
        }
        try {
            birthday = getBirthday();
        } catch (ParseException e) {
            birthdayError.setText("birthday in not valid");
            valid = false;
        }
        return valid;
    }

    private void login(){
        loginButton.getScene().getWindow().hide();
        OpenWindow.openWindow("view/Login.fxml",new LoginController(), "Login");
    }


}
