package application.Scenes;

/**
 * Created by Tienie on 9/20/2015.
 */

import authentication.Authenticator;
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

/**
 * Created by Tienie on 9/20/2015.
 */
public class ToRegisterScene {
    Label lblscene2;
    GridPane pane2;
    Scene scene2;
    Button btnSignInScene2;
    String emplid;
    Stage thestage;
    private Authenticator authenticator = new Authenticator();

    public ToRegisterScene(Stage stage) {
        setupStage(stage);
    }

    public void setupStage(Stage stage) {
        thestage = stage;
        lblscene2 = new Label("Scene 2");
        pane2 = new GridPane();
        pane2.setAlignment(Pos.CENTER);
        pane2.setVgap(10);
        pane2.setHgap(10);
        pane2.setPadding(new Insets(25, 25, 25, 25));
        pane2.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        scene2 = new Scene(pane2, 640, 480);

        Text scenetitle2 = new Text("Register");
        scenetitle2.setTextAlignment(TextAlignment.JUSTIFY);
        scenetitle2.setId("welcome-text");
        pane2.add(scenetitle2, 1, 0);

        Label userName2 = new Label("Enter Student/Staff EMPLID:");
        pane2.add(userName2, 1, 1);

        TextField userTextField2 = new TextField();
        pane2.add(userTextField2, 1, 2);

        btnSignInScene2 = new Button("Start Registration");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(btnSignInScene2);
        pane2.add(hbBtn2, 1, 5);

        final Text actiontarget2 = new Text();
        pane2.add(actiontarget2, 1, 7);

        btnSignInScene2.setOnAction(e -> {
            String EMPLID = userTextField2.getText();
            actiontarget2.setId("actiontarget");
            actiontarget2.setText("Sign in button pressed");
            try {
                emplid = userTextField2.getText();
                LDAPTester ldapTest = new LDAPTester();
                if (LDAPTester.getDnForUser(EMPLID, null) != null) {
                    System.out.println("Found User!");

                    BiometricChoiceScene biometricChoiceScene = new BiometricChoiceScene(thestage, emplid);
                    thestage.setScene(biometricChoiceScene.getScene());

                } else {
                    System.out.println("Didn't Find User");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        scene2.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());
    }

    public Scene getScene() {
        return scene2;
    }
}

