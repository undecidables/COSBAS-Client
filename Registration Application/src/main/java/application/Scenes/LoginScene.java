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
    Label sceneTitle;
    GridPane guiGridPane;
    Scene thisScene;
    Button signIn;
    Stage mainStage;
    private Authenticator authenticator = new Authenticator();

    public LoginScene(Stage stage) {
        setupStage(stage);
    }

    public void setupStage(Stage stage) {
        mainStage = stage;
        sceneTitle = new Label("Scene 1");
        guiGridPane = new GridPane();
        guiGridPane.setAlignment(Pos.CENTER);
        guiGridPane.setVgap(10);
        guiGridPane.setHgap(10);
        guiGridPane.setPadding(new Insets(25, 25, 25, 25));
        guiGridPane.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        thisScene = new Scene(guiGridPane, 640, 480);
        Text sceneTitleText = new Text("Log In");
        sceneTitleText.setTextAlignment(TextAlignment.JUSTIFY);
        sceneTitleText.setId("welcome-text");
        guiGridPane.add(sceneTitleText, 1, 0);

        Label userName = new Label("EMPLID:");
        guiGridPane.add(userName, 1, 1);

        TextField userTextField = new TextField();
        guiGridPane.add(userTextField, 1, 2);

        Label pw = new Label("Password:");
        guiGridPane.add(pw, 1, 3);

        PasswordField pwBox = new PasswordField();
        guiGridPane.add(pwBox, 1, 4);

        signIn = new Button("Sign in");
        HBox box = new HBox(10);
        box.setAlignment(Pos.BOTTOM_RIGHT);
        box.getChildren().add(signIn);
        guiGridPane.add(box, 1, 5);

        final Text feedback = new Text();
        guiGridPane.add(feedback, 1, 7);
        thisScene.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());

        signIn.setOnAction(e -> {
            String EMPLID = userTextField.getText();
            String Password = pwBox.getText();
            feedback.setId("actiontarget");
            feedback.setText("Sign in button pressed");
            try {
                if (authenticator.doProcess(EMPLID, Password)) {
                    feedback.setText("Login Succeeded");
                    ToRegisterScene registerScene = new ToRegisterScene(mainStage);
                    mainStage.setScene(registerScene.getScene());
                } else {
                    feedback.setText("Login Failed");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public Scene getScene() {
        return thisScene;
    }
}
