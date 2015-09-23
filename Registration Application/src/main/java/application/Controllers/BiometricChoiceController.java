package application.Controllers;

import application.EmailValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Tienie on 9/23/2015.
 */
public class BiometricChoiceController extends BaseController {

    Stage stage;
    Parent root;

    @FXML
    private Button signInBtn;

    @FXML
    private Text biometricAccessTarget;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox biometricCombo;

    private String email;


    @FXML
    protected void initialize() {

        if (registrationDO.getEmail() != null) {
            emailField.setText(registrationDO.getEmail());
            emailField.setDisable(true);
        }
    }

    @FXML
    protected void handleBiometricSubmitAction(ActionEvent event) {
        email = emailField.getText();
        EmailValidator validator = new EmailValidator();
        if (validator.validate(email)) {
            String BiometricSystem = biometricCombo.getValue().toString();
            biometricAccessTarget.setText("Chose " + BiometricSystem);
            try {
                if (BiometricSystem.equals("Facial")) {

                    getRegistrationDO().setEmail(email);
                    stage = (Stage) signInBtn.getScene().getWindow();
                    root = FXMLLoader.load(getClass().getResource("/FXML/FacialRegisterScreen.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else if (BiometricSystem.equals("Fingerprint")) {
                    System.out.println("Finger");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            biometricAccessTarget.setText("Invalid E-mail entered.");
        }
    }

} //Controller