package application.Controllers;

import authentication.Authenticator;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by Tienie on 9/26/2015.
 */
public class FacialRegisterConfirmController extends BaseController {
    /**
     * Created by Tienie on 9/23/2015.
     */
    Stage stage;
    Parent root;

    @FXML
    Canvas canvas;
    @FXML
    Text EMail;
    @FXML
    private Text actiontarget;
    @FXML
    private TextField textfield;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInBtn;

    public static BufferedImage convert(byte[] b) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(b));
            System.out.println(image);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    protected void handleConfirmSubmitButtonAction(ActionEvent event) {

        try {
            sendHTTPPostAsJSON(getRegistrationDO().getImage(), getRegistrationDO().getEmplid(), getRegistrationDO().getEmail());
        } catch (Exception e) {
            actiontarget.setText("Server Unavailable");
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleRetakeSubmitButtonAction(ActionEvent event) {

        String EMPLID = textfield.getText();
        String Password = passwordField.getText();

        if (EMPLID.length() < 3 || Password.length() < 3) {
            actiontarget.setText("Incorrect EMPLID/Password");
            return;
        }
        actiontarget.setId("actiontarget");
        actiontarget.setText("Sign in button pressed");
        try {
            Authenticator authenticator = new Authenticator();
            if (authenticator.doProcess(EMPLID, Password)) {
                actiontarget.setText("Login Succeeded " + EMPLID + " " + Password);
                stage = (Stage) signInBtn.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/FXML/BiometricChoiceScreen.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                actiontarget.setText("Login Failed");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    protected void initialize() {

        try {
            byte[] image = getRegistrationDO().getImage();
            BufferedImage toDisplay = convert(image);
            if (toDisplay == null) {
                System.out.println("Null Image");
                return;
            }
            GraphicsContext g2d;
            g2d = canvas.getGraphicsContext2D();
            g2d.setStroke(javafx.scene.paint.Color.GREEN);
            Image draw = SwingFXUtils.toFXImage(toDisplay, null);
            g2d.drawImage(draw, 0, 0);
            EMail.setText("Confirm Details:" + "\n\n" + "EMPLID: " + getRegistrationDO().getEmplid() + "\nE-Mail: " + getRegistrationDO().getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


} //Controller



