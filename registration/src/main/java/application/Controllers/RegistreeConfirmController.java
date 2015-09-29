package application.Controllers;

import authentication.LDAPTester;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Tienie on 9/23/2015.
 */
public class RegistreeConfirmController extends BaseController {

    Stage stage;
    Parent root;

    @FXML
    TextField registreeTextfield;

    @FXML
    private Button signInBtn;

    @FXML
    private Text registreeActionTarget;

    @FXML
    protected void handleRegistreeSubmitAction(ActionEvent event) {
        try {
            String emplid = registreeTextfield.getText();
            if (emplid.length() < 3) {
                registreeActionTarget.setText("Incorrect EMPLID/Password");
                return;
            }
            if (LDAPTester.getDnForUser(emplid, null) != null) {
                registreeActionTarget.setText("User Found");

                getRegistrationDO().setEmplid(emplid);

                stage = (Stage) signInBtn.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/FXML/BiometricChoiceScreen.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("User Does Not Exist");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

} //Controller