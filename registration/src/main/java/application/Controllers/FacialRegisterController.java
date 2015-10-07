package application.Controllers;

import HTTPPostBuilder.HTTPPostSender;
import application.Face;
import application.Model.ApplicationModel;
import application.RegistrationDataObject;
import application.Utilities;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;
import modules.*;
import org.opencv.core.*;
//import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
//import org.opencv.videoio.VideoCapture;

import org.springframework.context.ApplicationContext;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tienie on 9/23/2015.
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
    protected void handleFacialSubmitButtonAction(ActionEvent event) {

        face.fillData(registrationDataObject.getBiometricData());

        //sending of httppost to server????

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
        startCamera();

    }

    @FXML
    private ImageView currentFrame;

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

    //this shouldnt sit here, move it somewhere
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
                System.out.println("Whoops : " + e.toString());
            }
        }

        //this calls the function to set up onclose, need to find a more elegant solution.
        setOnClose();

        return imageToShow;
    }

    //this shouldnt sit here, move it somewhere


    public void sendHTTPPostAsJSON(byte[] image, String emplid, String email, String registratorID) throws Exception {
        HTTPPostSender sender = new HTTPPostSender();
        try {
            sender.sendPostRequest(image, emplid, email, registratorID);
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean setClose = false;
    private void setOnClose()
    {
        if(!setClose) {
            ((Stage) currentFrame.getScene().getWindow()).setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    timer.cancel();
                    camera.releaseCamera();
                }
            });
            setClose = true;
        }
    }




}
