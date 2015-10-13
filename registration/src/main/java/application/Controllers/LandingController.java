package application.Controllers;

import application.Model.ApplicationModel;
import application.RegistrationDataObject;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * {@author Jason Richard Evans}
 */
public class LandingController {
    @FXML
    private Button btnStartReg;
    @FXML
    private Button Logout;
    RegistrationDataObject registrationDataObject;

    @FXML
    protected void initialize() {
        ApplicationContext app = ApplicationModel.app;
        registrationDataObject = (RegistrationDataObject) app.getBean("registerUserData");
    }

    public void startRegistering(Event event) {
        try {
            Stage stage = (Stage) btnStartReg.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Registration_Wizard.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }

    public void doLogout(Event event) {
        try {
            Stage stage = (Stage) btnStartReg.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Registration_Login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }
}
