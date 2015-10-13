package application.Controllers;

import application.*;
import application.Model.ApplicationModel;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modules.ConvertMatToImage;
import modules.OPENCVCamera;
import modules.OPENCVDetectAndBorderFaces;
import org.apache.http.client.methods.HttpPost;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.springframework.context.ApplicationContext;
import org.apache.commons.configuration.PropertiesConfiguration;
import modules.BiometricData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@author Jason Richard Evans}
 * {@author Tienie Pritchard}
 */
public class WizardController {
    public ImageView currentFrame;
    public ImageView imgFB1;
    public ImageView imgFB2;
    public ImageView imgFB3;
    public ImageView imgFB4;
    public ImageView imgFB5;
    public ImageView imgFB6;
    //Variables to be used by registration procedures...
    private String emplid;
    private List<BufferedImage> FacialRecData;
    @FXML
    private ImageView[] imageFeedback = {imgFB1, imgFB2, imgFB3, imgFB4, imgFB5, imgFB6};

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    //Image Capturing
    private PropertiesConfiguration config;
    private Face face;
    private Utilities utility;
    private OPENCVCamera camera;
    private ConvertMatToImage convertMatToImage;
    private OPENCVDetectAndBorderFaces detectAndBorderFaces;
    private Timer timer;

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

    //Buttons to traverse forward in the registration process.
    @FXML
    private Button btnNext1;
    @FXML
    private Button btnNext2;
    @FXML
    private Button btnNext3;
    @FXML
    private Button btnDone;

    RegistrationDataObject registrationDataObject;
    ApplicationContext app;

    @FXML
    protected void initialize() {
        app = ApplicationModel.app;
        registrationDataObject = (RegistrationDataObject) app.getBean("registerUserData");
        //TODO Remove hardcoded stuff
        registrationDataObject.setEmplid("BCrawley");
        //TODO Remove Hardcoded stuff^
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
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public void nextStep(ActionEvent actionEvent) {
        Object selectedEvent = actionEvent.getSource();
        if (selectedEvent == btnNext1) {
            //Choosen EMPLID to register.
            //TODO add functionality to add authorized personel to cmbBox
            //TODO check is a user has been selected...
            pnlStep1.setVisible(false);
            pnlStep2.setVisible(true);
            btnStep2.setDisable(false);

            startCamera();
        } else if (selectedEvent == btnNext2) {
            //Facial Recognition Data
            //TODO add functionality to display live feedback and display images captured.
            camera.releaseCamera();
            pnlStep2.setVisible(false);
            pnlStep3.setVisible(true);
            btnStep3.setDisable(false);
        } else if (selectedEvent == btnNext3) {
            //Fingerprint Recognition Data
            //TODO add functionality to display which finger to capture and then display image captured.
            pnlStep3.setVisible(false);
            pnlStep4.setVisible(true);
            btnStep4.setDisable(false);

            //TODO save all data captured. This has to be done here as the user may cancel the registration process at any time.
            //because registration by this point is complete...we cannot let the user go back and change things...
            btnStep1.setDisable(true);
            btnStep2.setDisable(true);
            btnStep3.setDisable(true);
        } else if (selectedEvent == btnDone) {
            try {
                Stage stage = (Stage) btnStep1.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/FXML/Registration_Landing.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }

    public void doTravel(ActionEvent actionEvent) {
        Object selectedElement = actionEvent.getSource();
        if (selectedElement == btnStep1 && !btnStep1.isDisabled()) {
            pnlStep1.setVisible(true);
            pnlStep2.setVisible(false);
            pnlStep3.setVisible(false);
            pnlStep4.setVisible(false);
            if (camera.isOpened())
                camera.releaseCamera();
        } else if (selectedElement == btnStep2 && !btnStep2.isDisabled()) {
            pnlStep1.setVisible(false);
            pnlStep2.setVisible(true);
            pnlStep3.setVisible(false);
            pnlStep4.setVisible(false);
            startCamera();
        } else if (selectedElement == btnStep3 && !btnStep3.isDisabled()) {
            pnlStep1.setVisible(false);
            pnlStep2.setVisible(false);
            pnlStep3.setVisible(true);
            pnlStep4.setVisible(false);
            if (camera.isOpened())
                camera.releaseCamera();
        }
    }

    private void startCamera() {
        utility = (Utilities) app.getBean("utilities");
        camera = utility.getCameraObject();
        face = (Face) app.getBean("face");
        convertMatToImage = utility.getConvertMatToImage();
        detectAndBorderFaces = utility.getDetectAndBorderFaces();
        config = (PropertiesConfiguration) app.getBean("config");

        final ImageView frameView = currentFrame;
        TimerTask frameGrabber = new TimerTask() {
            @Override
            public void run() {
                final Image tmp = grabFrame();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        frameView.setImage(tmp);
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(frameGrabber, 0, 70);

    }

    private Image grabFrame() {
        Image imageToShow = null;
        if (camera.isOpened()) {
            try {
                Mat frame = camera.captureFrame();
                if (!frame.empty()) {
                    frame = detectAndBorderFaces.detectAndBorderFaces(frame);
                    imageToShow = (Image) convertMatToImage.convert((Object) frame);
                }
            } catch (Exception e) {
            }
        }

        return imageToShow;
    }

    public void takeImages(ActionEvent actionEvent) {
        face.fillData(registrationDataObject.getBiometricData());
        HttpPost httpPost = new HttpPostRegistrationBuilder().buildPost(registrationDataObject);
        try {
            HttpRegisterPostSender postSender = new HttpRegisterPostSender();
            RegisterResponse registerResponse = (RegisterResponse) postSender.sendPostRequest(httpPost);
            System.out.println(registerResponse.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<BiometricData> faceImages = registrationDataObject.getBiometricData();
        for (BiometricData pic: faceImages){
            if (pic.getType() == "biometric-FACE"){
                try {
                    System.out.println("ATTENTION: Got an image of facial type...");
                    InputStream in = new ByteArrayInputStream(pic.getData());
                    BufferedImage bImageFromConvert = ImageIO.read(in);
                    FacialRecData.add(bImageFromConvert);
                }
                catch(IOException error){
                    error.printStackTrace();
                }
            }

        }

        /*for (int i = 0; (i < 5) && (FacialRecData.get(i) != null); i++){
            Image image = SwingFXUtils.toFXImage(FacialRecData.get(i), null);
            imageFeedback[i].setImage(image);
        }*/
    }
}
