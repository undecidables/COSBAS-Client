package application.Controllers;

import application.Model.*;
import application.RegistrationApplication;
import authentication.Authenticator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modules.HttpPostSenderInterface;
import org.apache.http.client.methods.HttpPost;
import org.springframework.context.ApplicationContext;

import java.io.IOException;


/**
 * {@author Tienie}
 * {@author Jason Richard Evans}
 */
public class LoginController{
    @FXML
    private Button btnLogin;
    @FXML
    private Text lblErrorFeedback;
    @FXML
    private TextField edtUsername;
    @FXML
    private PasswordField edtPassword;
    Stage stage;
    Parent root;

    @FXML
    protected void handleLogin(ActionEvent event) {
        String registratorId = edtUsername.getText();
        String Password = edtPassword.getText();

        if (registratorId.length() < 3 || Password.length() < 3) {
            lblErrorFeedback.setVisible(true);
            return;
        }

        try {
            Authenticator authenticator = new Authenticator();
            if (authenticator.doProcess(registratorId, Password)) {

                boolean response = false;
                HttpPost httpPost = new HttpPostQueryBuilder().buildPost(registratorId);
                try {
                    HttpQueryPostSender postSender = new HttpQueryPostSender();
                    response = Boolean.parseBoolean(postSender.sendPostRequest(httpPost));
                } catch (Exception e) {
                    System.out.println("error");
                }

                if(response)
                {
                    lblErrorFeedback.setVisible(false);
                    registrationDataObject.setRegistratorID(registratorId);
                    stage = (Stage) btnLogin.getScene().getWindow();
                    root = FXMLLoader.load(getClass().getResource("/FXML/Registration_Landing.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                else
                {
                    lblErrorFeedback.setVisible(true);
                }


            } else {
                lblErrorFeedback.setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @FXML
    protected void initialize() {
        ApplicationContext app = RegistrationApplication.context;
        registrationDataObject = (RegistrationDataObject) app.getBean("registerUserData");

    }


    RegistrationDataObject registrationDataObject;

    public void doExit(Event event) {
        Platform.exit();
        System.exit(0);
    }

    public void goToHelp(ActionEvent actionEvent) {
        String resource = "https://github.com/undecidables/Documentation/wiki";
        try {
            Runtime.getRuntime().exec(
                    new String[]{
                            "/usr/bin/open", resource
                    });
        }
        catch(IOException error){
            try{
                Runtime.getRuntime().exec(
                        new String[] {
                                "rundll32", "url.dll,FileProtocolHandler", resource
                        });
            }
            catch(IOException error2){
                System.out.println("Tried to open in both Unix and Windows, but failed.");
                System.out.println(error2.getMessage());
            }
        }
    }

    public void goToAbout(ActionEvent actionEvent) {
        String resource = "https://github.com/undecidables/Documentation/wiki/The-Team";
        try {
            Runtime.getRuntime().exec(
                    new String[]{
                            "/usr/bin/open", resource
                    });
        }
        catch(IOException error){
            try{
                Runtime.getRuntime().exec(
                        new String[] {
                                "rundll32", "url.dll,FileProtocolHandler", resource
                        });
            }
            catch(IOException error2){
                System.out.println("Tried to open in both Unix and Windows, but failed.");
                System.out.println(error2.getMessage());
            }
        }
    }
} //Controller