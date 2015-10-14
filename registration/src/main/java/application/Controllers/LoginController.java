package application.Controllers;

import application.Model.ApplicationModel;
import application.RegistrationDataObject;
import authentication.Authenticator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;


/**
 * {@author Tienie}
 * {@author Jason Richard Evans}
 */
public class LoginController{
    @FXML
    private Button btnLogin;
    @FXML
    private Text lblErrorFeedback;
    @FXML
    private TextField edtUsername;
    @FXML
    private PasswordField edtPassword;
    Stage stage;
    Parent root;

    @FXML
    protected void handleLogin(ActionEvent event) {
        String registratorId = edtUsername.getText();
        String Password = edtPassword.getText();

        if (registratorId.length() < 3 || Password.length() < 3) {
            lblErrorFeedback.setVisible(true);
            return;
        }

        try {
            Authenticator authenticator = new Authenticator();
            if (authenticator.doProcess(registratorId, Password)) {
                lblErrorFeedback.setVisible(false);
                registrationDataObject.setRegistratorID(registratorId);
                stage = (Stage) btnLogin.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/FXML/Registration_Landing.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                lblErrorFeedback.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @FXML
    protected void initialize() {
        ApplicationContext app = ApplicationModel.app;
        registrationDataObject = (RegistrationDataObject)app.getBean("registerUserData");
    }

    RegistrationDataObject registrationDataObject;

    public void doExit(Event event) {
        Platform.exit();
        System.exit(0);
    }
} //Controller