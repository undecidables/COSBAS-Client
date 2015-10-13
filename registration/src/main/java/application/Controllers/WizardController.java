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

    public Button btnNext1;
    public Button btnNext2;
    public Button btnNext3;
    public Button btnDone;
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
            //TODO add functionality to add authorized personel to cmbBox
            //TODO check is a user has been selected...
            pnlStep1.setVisible(false);
            pnlStep2.setVisible(true);
            btnStep2.setDisable(false);
        }
        else if (selectedEvent == btnNext2){
            pnlStep2.setVisible(false);
            pnlStep3.setVisible(true);
            btnStep3.setDisable(false);
        }
        else if (selectedEvent == btnNext3){
            pnlStep3.setVisible(false);
            pnlStep4.setVisible(true);
            btnStep4.setDisable(false);
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
}
