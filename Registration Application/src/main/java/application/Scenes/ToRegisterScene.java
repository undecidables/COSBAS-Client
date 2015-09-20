package application.Scenes;

/**
 * Created by Tienie on 9/20/2015.
 */

import authentication.LDAPTester;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
public class ToRegisterScene {
    Label sceneTitle;
    GridPane guiGridPane;
    Scene thisScene;
    Button startRegister;
    String emplid;
    Stage mainStage;

    public ToRegisterScene(Stage stage) {
        setupStage(stage);
    }

    public void setupStage(Stage stage) {
        mainStage = stage;
        sceneTitle = new Label("Scene 2");
        guiGridPane = new GridPane();
        guiGridPane.setAlignment(Pos.CENTER);
        guiGridPane.setVgap(10);
        guiGridPane.setHgap(10);
        guiGridPane.setPadding(new Insets(25, 25, 25, 25));
        guiGridPane.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        thisScene = new Scene(guiGridPane, 640, 480);

        Text sceneTitleText = new Text("Register");
        sceneTitleText.setTextAlignment(TextAlignment.JUSTIFY);
        sceneTitleText.setId("welcome-text");
        guiGridPane.add(sceneTitleText, 1, 0);

        Label usernameEnter = new Label("Enter Student/Staff EMPLID:");
        guiGridPane.add(usernameEnter, 1, 1);

        TextField usernameInput = new TextField();
        guiGridPane.add(usernameInput, 1, 2);

        startRegister = new Button("Start Registration");
        HBox box = new HBox(10);
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(startRegister);
        guiGridPane.add(box, 1, 5);

        final Text feedback = new Text();
        guiGridPane.add(feedback, 1, 7);

        startRegister.setOnAction(e -> {
            String EMPLID = usernameInput.getText();
            feedback.setId("actiontarget");
            feedback.setText("Sign in button pressed");
            try {
                emplid = usernameInput.getText();
                if (LDAPTester.getDnForUser(EMPLID, null) != null) {
                    System.out.println("Found User!");
                    BiometricChoiceScene biometricChoiceScene = new BiometricChoiceScene(mainStage, emplid);
                    mainStage.setScene(biometricChoiceScene.getScene());

                } else {
                    System.out.println("Didn't Find User");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thisScene.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());
    }

    public Scene getScene() {
        return thisScene;
    }
}

