package application.Controllers;

import application.Model.ApplicationModel;
import application.RegistrationDataObject;
import authentication.Authenticator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.springframework.context.ApplicationContext;


/**
 * Created by Tienie on 9/23/2015.
 */

public class LoginController{


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


        String registratorId = textfield.getText();
        String Password = passwordField.getText();

        if (registratorId.length() < 3 || Password.length() < 3) {
            actiontarget.setText("Incorrect EMPLID/Password");
            return;
        }
        actiontarget.setId("actiontarget");
        actiontarget.setText("Sign in button pressed");
        try {
            Authenticator authenticator = new Authenticator();
            if (authenticator.doProcess(registratorId, Password)) {
                registrationDataObject.setRegistratorID(registratorId);
                actiontarget.setText("Login Succeeded " + registratorId + " " + Password);
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

    @FXML
    private void closeAction(ActionEvent event)
    {
        System.out.println("PLease work");
    }


    @FXML
    protected void initialize() {




        ApplicationContext app = ApplicationModel.app;
        registrationDataObject = (RegistrationDataObject) app.getBean("registerUserData");

    }

    RegistrationDataObject registrationDataObject;
} //Controller