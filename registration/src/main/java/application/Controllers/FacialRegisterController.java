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
import modules.OPENCVCamera;
import modules.OPENCVFaceDetection;
import modules.BiometricData;
import org.opencv.core.*;
//import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;
//import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.VideoCapture;

import modules.ConvertMatToImageByteArray;
import org.springframework.context.ApplicationContext;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
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

/*
        if(capture.isOpened())
        {
            //timer.cancel();
            Mat frame = new Mat();
            capture.read(frame);
            OPENCVFaceDetection detection = new OPENCVFaceDetection();
            ConvertMatToImageByteArray convertor = new ConvertMatToImageByteArray();

            ArrayList<byte[]> frames = new ArrayList<byte[]>();
            frames.add(convertor.convertToImageByteArray(frame));

            frames = detection.detectFaces(frames);
            actiontarget.setText("Photo Taken");
            if(frames.get(0)!=null)
            {
                try {
                    sendHTTPPostAsJSON(frames.get(0), registrationDataObject.getEmplid(), registrationDataObject.getEmail(), registrationDataObject.getRegistratorID());
                } catch (Exception e) {

                    actiontarget.setText("Server Unavailable");
                }
            }
            else
            {
                //notify about more than one face in the picture must not show....
            }

            //currentFrame.setImage(mat2Image(Highgui.imdecode(new MatOfByte(frames.get(0)), Highgui.IMREAD_GRAYSCALE)));

        }
        else
        {

        }*/
    }

    RegistrationDataObject registrationDataObject;

    @FXML
    protected void initialize() {

        ApplicationContext app = ApplicationModel.app;
        registrationDataObject = (RegistrationDataObject) app.getBean("registerUserData");
        utility = (Utilities) app.getBean("utilities");
        camera = utility.getCameraObject();
        face = (Face) app.getBean("face");
        startCamera();

    }

    @FXML
    private ImageView currentFrame;

    Face face;
    Utilities utility;
    OPENCVCamera camera;
    private VideoCapture capture = new VideoCapture();
    private Timer timer;

    private void startCamera()
    {
        final ImageView frameView = currentFrame;
        System.out.println("yeah");
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
        timer.schedule(frameGrabber, 0, 33);

    }

    private Image grabFrame()
    {
        Image imageToShow = null;
        Mat frame = new Mat();

        if(camera.isOpened())
        {
            try
            {
                frame = camera.captureFrame();
                if(!frame.empty())
                {
                    //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                    frame = detectFaces(frame);
                    imageToShow = mat2Image(frame);
                }
            }
            catch (Exception e)
            {
                System.out.println("Whoops : " + e.toString());
            }
        }

        return imageToShow;
    }

    private Image mat2Image(Mat frame)
    {
        MatOfByte buffer = new MatOfByte();
        Highgui.imencode(".png", frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    private Mat detectFaces(Mat frame)
    {
        MatOfRect faceDetections = new MatOfRect();
        CascadeClassifier faceDetector = new CascadeClassifier("src/main/resources/haarcascade_frontalface_alt.xml");
        faceDetector.detectMultiScale(frame, faceDetections);

        for(Rect rect : faceDetections.toArray())
        {
            Core.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }

        return frame;
    }


    public void sendHTTPPostAsJSON(byte[] image, String emplid, String email, String registratorID) throws Exception {
        HTTPPostSender sender = new HTTPPostSender();
        try {
            sender.sendPostRequest(image, emplid, email, registratorID);
        } catch (Exception e) {
            throw e;
        }
    }


}
