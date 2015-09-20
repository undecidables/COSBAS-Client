package application.Scenes;

/**
 * Created by Tienie on 9/20/2015.
 */

import application.EmailValidator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Created by Tienie on 9/20/2015.
 */
public class BiometricChoiceScene {
    Label lblscene3;
    GridPane pane3;
    Scene scene3;
    Button btnSignInScene3;
    String email, emplid;
    Stage thestage;


    public BiometricChoiceScene(Stage stage, String emplid) {
        this.emplid = emplid;
        setupStage(stage);
    }

    public void setupStage(Stage stage) {
        thestage = stage;
        lblscene3 = new Label("Scene 3");
        pane3 = new GridPane();
        pane3.setAlignment(Pos.CENTER);
        pane3.setVgap(10);
        pane3.setHgap(10);
        pane3.setPadding(new Insets(35, 35, 35, 35));
        pane3.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        scene3 = new Scene(pane3, 640, 480);

        thestage.setTitle("Biometric Registration");
        Text scenetitle3 = new Text("Choose Type Of Biometric System");
        scenetitle3.setTextAlignment(TextAlignment.JUSTIFY);
        scenetitle3.setId("welcome-text");
        pane3.add(scenetitle3, 1, 0);


        final ComboBox accessSystemComboBox = new ComboBox();
        accessSystemComboBox.getItems().addAll(
                "Facial Recognition",
                "Fingerprint Scanner"
        );
        accessSystemComboBox.setValue("Facial Recognition");

        pane3.add(accessSystemComboBox, 1, 1);

        Label userName3 = new Label("Enter Student/Staff preferred e-mail:");
        pane3.add(userName3, 1, 2);

        TextField userTextField3 = new TextField();
        pane3.add(userTextField3, 1, 3);


        btnSignInScene3 = new Button("Continue");
        HBox hbBtn3 = new HBox(10);
        hbBtn3.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn3.getChildren().add(btnSignInScene3);
        pane3.add(hbBtn3, 1, 6);

        final Text actiontarget3 = new Text();
        pane3.add(actiontarget3, 1, 8);


        btnSignInScene3.setOnAction(e -> {
            email = userTextField3.getText();
            EmailValidator validator = new EmailValidator();
            if (validator.validate(email)) {
                String BiometricSystem = accessSystemComboBox.getValue().toString();
                actiontarget3.setId("actiontarget");
                actiontarget3.setText("Sign in button pressed");
                try {
                    if (BiometricSystem.equals("Facial Recognition")) {

                        FacialRecognitionRegisterScene facialRecognitionRegisterScene = new FacialRecognitionRegisterScene(thestage, emplid, email);
                        thestage.setScene(facialRecognitionRegisterScene.getScene());


                    } else if (BiometricSystem.equals("Fingerprint Scanner")) {
                        System.out.println("Finger");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                actiontarget3.setText("Invalid E-mail entered.");
            }
        });
        scene3.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());


    }

    public Scene getScene() {
        return scene3;
    }
}

