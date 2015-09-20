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
public class BiometricChoiceScene {
    Label sceneLabel;
    GridPane guiGridPane;
    Scene thisScene;
    Button completeButton;
    String email, emplid;
    Stage mainStage;


    public BiometricChoiceScene(Stage stage, String emplid) {
        this.emplid = emplid;

        setupStage(stage);
    }

    public void setupStage(Stage stage) {
        mainStage = stage;
        sceneLabel = new Label("Scene 3");
        guiGridPane = new GridPane();
        guiGridPane.setAlignment(Pos.CENTER);
        guiGridPane.setVgap(10);
        guiGridPane.setHgap(10);
        guiGridPane.setPadding(new Insets(35, 35, 35, 35));
        guiGridPane.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        thisScene = new Scene(guiGridPane, 640, 480);
        mainStage.setTitle("Biometric Registration");
        Text scenetitle3 = new Text("Choose Type Of Biometric System");
        scenetitle3.setTextAlignment(TextAlignment.JUSTIFY);
        scenetitle3.setId("welcome-text");
        guiGridPane.add(scenetitle3, 1, 0);

        final ComboBox accessSystemComboBox = new ComboBox();
        accessSystemComboBox.getItems().addAll(
                "Facial Recognition",
                "Fingerprint Scanner"
        );

        accessSystemComboBox.setValue("Facial Recognition");
        guiGridPane.add(accessSystemComboBox, 1, 1);

        Label username = new Label("Enter Student/Staff preferred e-mail:");
        guiGridPane.add(username, 1, 2);

        TextField userInput = new TextField();
        guiGridPane.add(userInput, 1, 3);


        completeButton = new Button("Continue");
        HBox box = new HBox(10);
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(completeButton);
        guiGridPane.add(box, 1, 6);

        final Text feedback = new Text();
        guiGridPane.add(feedback, 1, 8);


        completeButton.setOnAction(e -> {
            email = userInput.getText();
            EmailValidator validator = new EmailValidator();
            if (validator.validate(email)) {
                String BiometricSystem = accessSystemComboBox.getValue().toString();
                feedback.setId("actiontarget");
                feedback.setText("Sign in button pressed");
                try {
                    if (BiometricSystem.equals("Facial Recognition")) {
                        FacialRecognitionRegisterScene facialRecognitionRegisterScene = new FacialRecognitionRegisterScene(mainStage, emplid, email);
                        mainStage.setScene(facialRecognitionRegisterScene.getScene());

                    } else if (BiometricSystem.equals("Fingerprint Scanner")) {
                        System.out.println("Finger");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                feedback.setText("Invalid E-mail entered.");
            }
        });
        thisScene.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());


    }

    public Scene getScene() {
        return thisScene;
    }
}

