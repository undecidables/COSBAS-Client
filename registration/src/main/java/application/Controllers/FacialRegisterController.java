package application.Controllers;

import application.HttpRegisterPostSender;
import application.*;
import application.Model.ApplicationModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import modules.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.HttpPost;
import org.opencv.core.*;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.videoio.VideoCapture;

import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Tienie
 */
public class FacialRegisterController {



    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    Stage stage;
    Parent root;
    @FXML
    private Text actiontarget;

    @FXML
    private TextField textfield;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signInBtn;

    @FXML
    private ImageView canvas;

    @FXML
    protected void handleFacialSubmitButtonAction(ActionEvent event) throws IOException {

        face.fillData(registrationDataObject.getBiometricData());
        HttpPost httpPost = new HttpPostRegistrationBuilder().buildPost(registrationDataObject);
        try {
            HttpRegisterPostSender postSender = new HttpRegisterPostSender();
            RegisterResponse registerResponse = (RegisterResponse) postSender.sendPostRequest(httpPost);
            //do something with response, this will be moved with the new UI...
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeAndClean();

        //code duplication??? make a class that hndles this maybe???
        stage = (Stage) currentFrame.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("/FXML/BiometricChoiceScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    protected void initialize() {
        ApplicationContext app = ApplicationModel.app;
        registrationDataObject = (RegistrationDataObject) app.getBean("registerUserData");
        utility = (Utilities) app.getBean("utilities");
        camera = utility.getCameraObject();
        face = (Face) app.getBean("face");
        convertMatToImage = utility.getConvertMatToImage();
        detectAndBorderFaces = utility.getDetectAndBorderFaces();
        config = (PropertiesConfiguration) app.getBean("config");
        startCamera();

    }

    @FXML
    private ImageView currentFrame;

    private PropertiesConfiguration config;
    private Face face;
    private Utilities utility;
    private OPENCVCamera camera;
    private RegistrationDataObject registrationDataObject;
    private ConvertMatToImage convertMatToImage;
    private OPENCVDetectAndBorderFaces detectAndBorderFaces;
    private Timer timer;

    private void startCamera()
    {
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

    private Image grabFrame()
    {
        Image imageToShow = null;
        if(camera.isOpened())
        {
            try
            {
                Mat frame = camera.captureFrame();
                if(!frame.empty())
                {
                    frame = detectAndBorderFaces.detectAndBorderFaces(frame);
                    imageToShow = (Image) convertMatToImage.convert((Object) frame);
                }
            }
            catch (Exception e)
            {
            }
        }

        //this calls the function to set up onclose, need to find a more elegant solution.
        setOnClose();

        return imageToShow;
    }



    private boolean setClose = false;
    private void setOnClose()
    {
        if(!setClose) {
            ((Stage) currentFrame.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    closeAndClean();
                }
            });
            setClose = true;
        }
    }

    private void clearOnCloseEvent()
    {
        ((Stage) currentFrame.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            }
        });
    }

    private void closeAndClean()
    {
        clearOnCloseEvent();
        timer.cancel();
        timer = null;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        camera.releaseCamera();
    }




}
