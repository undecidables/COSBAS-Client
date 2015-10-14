package application.Controllers;

import application.*;
import application.Model.ApplicationModel;
import authentication.LDAPTester;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

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
    private List<ImageView> imageFeedback = new ArrayList<ImageView>(){{
       add(imgFB1); add(imgFB2); add(imgFB3);
       add(imgFB4); add(imgFB5); add(imgFB6);
    }};
    private List<Image> FacialRecData = new ArrayList<Image>();

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    //Registree data
    @FXML
    private TextField edtEmplid;
    @FXML
    private Label lblEmplidFB;

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

    //Finger highlighters for fingerprint data collection.
    @FXML
    private Circle shpLeftIndex;
    @FXML
    private Circle shpLeftThumb;
    @FXML
    private Circle shpRightThumb;
    @FXML
    private Circle shpRightIndex;

    RegistrationDataObject registrationDataObject;
    ApplicationContext app;

    @FXML
    protected void initialize() {
        app = ApplicationModel.app;
        registrationDataObject = (RegistrationDataObject) app.getBean("registerUserData");
        pnlStep1.setVisible(true);
        pnlStep2.setVisible(false);
        pnlStep3.setVisible(false);
        pnlStep4.setVisible(false);
        btnStep1.setDisable(false);
        btnStep2.setDisable(true);
        btnStep3.setDisable(true);
        btnStep4.setDisable(true);
        shpRightThumb.setVisible(false);
        shpRightIndex.setVisible(false);
        shpLeftThumb.setVisible(false);
        shpLeftIndex.setVisible(false);
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
            String emplid = edtEmplid.getText();
            if (emplid.length() < 3) {
                lblEmplidFB.setVisible(true);
                return;
            }
            if (LDAPTester.getDnForUser(emplid, null) != null) {
                lblEmplidFB.setVisible(false);
                registrationDataObject.setEmplid(emplid);

                pnlStep1.setVisible(false);
                pnlStep2.setVisible(true);
                btnStep2.setDisable(false);

                startCamera();
            }
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
                Image temp = new Image(new ByteArrayInputStream(pic.getData()));
                if (temp != null)
                    FacialRecData.add(temp);
            }

        }

        /*for(int i = 0; i < FacialRecData.size(); i++){
            (imageFeedback.get(i)).setImage(FacialRecData.get(i));
        }*/
        imgFB1.setImage(FacialRecData.get(0));
        imgFB2.setImage(FacialRecData.get(1));
        imgFB3.setImage(FacialRecData.get(2));
        imgFB4.setImage(FacialRecData.get(3));
        imgFB5.setImage(FacialRecData.get(4));
    }

    public void takeFingerprintImage(ActionEvent actionEvent) {
        for(int i = 0; i < 4; i++) {
            switch (i) {
                case 0: {
                    shpRightThumb.setVisible(false);
                    shpRightIndex.setVisible(false);
                    shpLeftThumb.setVisible(false);
                    shpLeftIndex.setVisible(true);
                    break;
                }
                case 1: {
                    shpRightThumb.setVisible(false);
                    shpRightIndex.setVisible(false);
                    shpLeftThumb.setVisible(true);
                    shpLeftIndex.setVisible(false);
                    break;
                }
                case 2: {
                    shpRightThumb.setVisible(false);
                    shpRightIndex.setVisible(true);
                    shpLeftThumb.setVisible(false);
                    shpLeftIndex.setVisible(false);
                    break;
                }
                case 3: {
                    shpRightThumb.setVisible(true);
                    shpRightIndex.setVisible(false);
                    shpLeftThumb.setVisible(false);
                    shpLeftIndex.setVisible(false);
                    break;
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setX(0);
            alert.setY(0);
            alert.setTitle("COSBAS Information");
            alert.setHeaderText("Fingerprint Recognition Data");
            alert.setContentText("Please place finger on scanner as shown in image...");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                //then take picture from the fingerprint scanner and place it in an image on the UI.
            }
        }
        shpRightThumb.setVisible(false);
    }
}
