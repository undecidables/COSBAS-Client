package application.Controllers;

import authentication.Authenticator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Tienie on 9/23/2015.
 */
public class LoginController extends BaseController {

    Stage stage;
    Parent root;

    @FXML
    private Text actiontarget;

    @FXML
    private TextField textfield;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInBtn;

    @FXML
    protected void handleLoginSubmitButtonAction(ActionEvent event) {

        String EMPLID = textfield.getText();
        String Password = passwordField.getText();
        actiontarget.setId("actiontarget");
        actiontarget.setText("Sign in button pressed");
        try {
            Authenticator authenticator = new Authenticator();
            if (authenticator.doProcess(EMPLID, Password)) {
                actiontarget.setText("Login Succeeded " + EMPLID + " " + Password);
                stage = (Stage) signInBtn.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/FXML/SelectUserScreen.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                actiontarget.setText("Login Failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
} //Controller