package application.Scenes;

import authentication.Authenticator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Created by Tienie on 9/20/2015.
 */
public class LoginScene {
    Label lblscene1;
    GridPane pane1;
    Scene scene1;
    Button btnSignInScene1;
    Stage thestage;
    private Authenticator authenticator = new Authenticator();

    public LoginScene(Stage stage) {
        setupStage(stage);
    }

    public void setupStage(Stage stage) {
        thestage = stage;
        lblscene1 = new Label("Scene 1");
        pane1 = new GridPane();
        pane1.setAlignment(Pos.CENTER);
        pane1.setVgap(10);
        pane1.setHgap(10);
        pane1.setPadding(new Insets(25, 25, 25, 25));
        pane1.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        scene1 = new Scene(pane1, 640, 480);
        Text scenetitle = new Text("Log In");
        scenetitle.setTextAlignment(TextAlignment.JUSTIFY);
        scenetitle.setId("welcome-text");
        pane1.add(scenetitle, 1, 0);

        Label userName = new Label("EMPLID:");
        pane1.add(userName, 1, 1);

        TextField userTextField = new TextField();
        pane1.add(userTextField, 1, 2);

        Label pw = new Label("Password:");
        pane1.add(pw, 1, 3);

        PasswordField pwBox = new PasswordField();
        pane1.add(pwBox, 1, 4);

        btnSignInScene1 = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btnSignInScene1);
        pane1.add(hbBtn, 1, 5);

        final Text actiontarget = new Text();
        pane1.add(actiontarget, 1, 7);
        scene1.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());

        btnSignInScene1.setOnAction(e -> {
            String EMPLID = userTextField.getText();
            String Password = pwBox.getText();
            actiontarget.setId("actiontarget");
            actiontarget.setText("Sign in button pressed");
            try {
                if (authenticator.doProcess(EMPLID, Password)) {
                    actiontarget.setText("Login Succeeded");
                    ToRegisterScene registerScene = new ToRegisterScene(thestage);
                    thestage.setScene(registerScene.getScene());
                } else {
                    actiontarget.setText("Login Failed");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public Scene getScene() {
        return scene1;
    }
}
