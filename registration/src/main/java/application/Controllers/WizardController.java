package application.Controllers;

import application.Model.ApplicationModel;
import application.RegistrationDataObject;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * {@author Jason Richard Evans}
 */
public class WizardController {
    //Variables to be used by registration procedures...
    private String emplid;

    //Panels that will act as the steps we take the user through to be able to register.
    @FXML
    private Pane pnlStep1;
    @FXML
    private AnchorPane pnlStep2;
    @FXML
    private AnchorPane pnlStep3;
    @FXML
    private AnchorPane pnlStep4;

    //The step buttons the user can traverse back to if needed.
    @FXML
    private Button btnStep1;
    @FXML
    private Button btnStep2;
    @FXML
    private Button btnStep3;
    @FXML
    private Button btnStep4;

    //Buttons to traverse forward in the registration process.
    @FXML
    private Button btnNext1;
    @FXML
    private Button btnNext2;
    @FXML
    private Button btnNext3;
    @FXML
    private Button btnDone;

    RegistrationDataObject registrationDataObject;

    @FXML
    protected void initialize() {
        ApplicationContext app = ApplicationModel.app;
        registrationDataObject = (RegistrationDataObject)app.getBean("registerUserData");
        pnlStep1.setVisible(true);
        pnlStep2.setVisible(false);
        pnlStep3.setVisible(false);
        pnlStep4.setVisible(false);
        btnStep1.setDisable(false);
        btnStep2.setDisable(true);
        btnStep3.setDisable(true);
        btnStep4.setDisable(true);
    }

    public void logout(Event event) {
        try {
            Stage stage = (Stage) btnStep1.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Registration_Landing.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }

    public void nextStep(ActionEvent actionEvent) {
        Object selectedEvent = actionEvent.getSource();
        if (selectedEvent == btnNext1){
            //Choosen EMPLID to register.
            //TODO add functionality to add authorized personel to cmbBox
            //TODO check is a user has been selected...
            pnlStep1.setVisible(false);
            pnlStep2.setVisible(true);
            btnStep2.setDisable(false);
        }
        else if (selectedEvent == btnNext2){
            //Facial Recognition Data
            //TODO add functionality to display live feedback and display images captured.
            pnlStep2.setVisible(false);
            pnlStep3.setVisible(true);
            btnStep3.setDisable(false);
        }
        else if (selectedEvent == btnNext3){
            //Fingerprint Recognition Data
            //TODO add functionality to display which finger to capture and then display image captured.
            pnlStep3.setVisible(false);
            pnlStep4.setVisible(true);
            btnStep4.setDisable(false);

            //TODO save all data captured. This has to be done here as the user may cancel the registration process at any time.
            //because registration by this point is complete...we cannot let the user go back and change things...
            btnStep1.setDisable(true);
            btnStep2.setDisable(true);
            btnStep3.setDisable(true);
        }
        else if (selectedEvent == btnDone){
            try {
                Stage stage = (Stage) btnStep1.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/Registration_Landing.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            catch (IOException error){
                error.printStackTrace();
            }
        }
    }

    public void doTravel(ActionEvent actionEvent) {
        Object selectedElement = actionEvent.getSource();
        if (selectedElement == btnStep1 && !btnStep1.isDisabled()){
            pnlStep1.setVisible(true);
            pnlStep2.setVisible(false);
            pnlStep3.setVisible(false);
            pnlStep4.setVisible(false);
        }
        else if (selectedElement == btnStep2 && !btnStep2.isDisabled()){
            pnlStep1.setVisible(false);
            pnlStep2.setVisible(true);
            pnlStep3.setVisible(false);
            pnlStep4.setVisible(false);
        }
        else if (selectedElement == btnStep3 && !btnStep3.isDisabled()){
            pnlStep1.setVisible(false);
            pnlStep2.setVisible(false);
            pnlStep3.setVisible(true);
            pnlStep4.setVisible(false);
        }
    }
}
