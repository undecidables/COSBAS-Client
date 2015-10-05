package application.Controllers;

import HTTPPostBuilder.HTTPPostSender;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.*;
//import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
//import org.opencv.videoio.VideoCapture;
import org.opencv.highgui.VideoCapture;

import modules.ConvertMatToImageByteArray;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tienie on 9/23/2015.
 */
public class FacialRegisterController extends BaseController {


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    Stage stage;
    Parent root;
    VideoStream webcam;
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


        try {
            Image originalImage = getScreenShot();
            BufferedImage bufferedImg = SwingFXUtils.fromFXImage(originalImage, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImg, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            sendHTTPPostAsJSON(imageInByte, getRegistrationDO().getEmplid(), getRegistrationDO().getEmail());
            baos.close();
            actiontarget.setText("Photo Taken");

            webcam.timer.stop();
            webcam.videoCapture.release();


            stage = (Stage) signInBtn.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/FXML/BiometricChoiceScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException exc) {
            actiontarget.setText("Server Unavailable");
            System.out.println(exc.getMessage());

        } catch (Exception ex) {
            ex.printStackTrace();
            actiontarget.setText("Server Unavailable");
        }
    }

    @FXML
    protected void initialize() {


        /*stage = (Stage) textfield.getScene().getWindow();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                capture.release();


            }
        });*/

        startCamera();



       // webcam = new VideoStream();
       // webcam.start();
    }

    @FXML
    private ImageView currentFrame;

    private VideoCapture capture = new VideoCapture();
    private Timer timer;

    private void startCamera()
    {
        capture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 1280); //camera resolution set here we could maybe have this in the config file???
        capture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 720);
        final ImageView frameView = currentFrame;
        if(!capture.isOpened())
        {
            capture.open(0);

            VideoCapture tempCap = new VideoCapture(0);
            System.out.println(tempCap.isOpened() + " THIS IS THE STATUS OF US");

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
        else
        {
            if(timer != null)
            {
                timer.cancel();
                timer = null;
            }

            capture.release();
            frameView.setImage(null);
        }
    }

    private Image grabFrame()
    {
        Image imageToShow = null;
        Mat frame = new Mat();

        if(capture.isOpened())
        {
            try
            {
                capture.read(frame);
                if(!frame.empty())
                {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
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


    public Image getScreenShot() throws Exception {
        ConvertMatToImageByteArray converter = new ConvertMatToImageByteArray();

        Mat tempMat = new Mat();
        Mat mat = new Mat();
        webcam.videoCapture.read(tempMat);
        Imgproc.cvtColor(tempMat, mat, Imgproc.COLOR_RGB2GRAY);
        javafx.scene.image.Image image = webcam.mat2Image(mat);
        /*byte[] byteImage = converter.convertToImageByteArray(mat);
        ByteArrayInputStream bais = new ByteArrayInputStream(byteImage);
        BufferedImage bImage = ImageIO.read(bais);
        WritableImage image = new WritableImage(bImage.getWidth(), bImage.getHeight());
        SwingFXUtils.toFXImage(bImage, image);*/
        java.util.List<Rectangle2D> rectList = webcam.detectFaces(mat);
        if (rectList.size() > 1) {
            throw new Exception("More than one face detected");
        }
        Rectangle2D faceRegion = rectList.get(0);

        Double X = faceRegion.getMinX();
        Double Y = faceRegion.getMinY();
        Double W = faceRegion.getWidth();
        Double H = faceRegion.getHeight();
        WritableImage croppedImage = new WritableImage(image.getPixelReader(), X.intValue(), Y.intValue(), W.intValue(), H.intValue());

        //croppedImage = grayscale(croppedImage);
        /* Uncomment this to save the image to disk so that you can test it.
        */
        File file = new File("test.png");
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(croppedImage, null);
        ImageIO.write(
                renderedImage,
                "png",
                file);



        return croppedImage;
    }

    public void sendHTTPPostAsJSON(byte[] image, String emplid, String email) throws Exception {
        HTTPPostSender sender = new HTTPPostSender();
        try {
            sender.sendPostRequest(image, emplid, email);
        } catch (Exception e) {
            throw e;
        }
    }

    class VideoStream extends Thread {

        AnimationTimer timer;
        VideoCapture videoCapture;
        GraphicsContext g2d;
        CascadeClassifier faceDetector;

        public void run() {

         /*   g2d = canvas.getGraphicsContext2D();
            g2d.setStroke(javafx.scene.paint.Color.GREEN);

            initOpenCv();

            timer = new AnimationTimer() {

                Mat mat = new Mat();

                @Override
                public void handle(long now) {

                    videoCapture.read(mat);


                    java.util.List<Rectangle2D> rectList = detectFaces(mat);

                    Image image = null;
                    try {
                        image = mat2Image(mat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    canvas.setWidth(image.getWidth());
                    canvas.setHeight(image.getHeight());

                    stage.sizeToScene();

                    g2d.drawImage(image, 0, 0);

                    for (Rectangle2D rect : rectList) {
                        g2d.strokeRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
                    }

                }
            };
            timer.start();*/
        }

        public synchronized Image mat2Image(Mat mat) throws IOException {
            ConvertMatToImageByteArray converter = new ConvertMatToImageByteArray();

            byte[] byteImage = converter.convertToImageByteArray(mat);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteImage);
            BufferedImage bImage = ImageIO.read(bais);
            WritableImage image = new WritableImage(bImage.getWidth(), bImage.getHeight());
            SwingFXUtils.toFXImage(bImage, image);
            return image;

   /*         MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", mat, buffer);
            return new Image(new ByteArrayInputStream(buffer.toArray()));*/
        }

        public synchronized List<Rectangle2D> detectFaces(Mat mat) {

            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(mat, faceDetections);
            List<Rectangle2D> rectList = new ArrayList<Rectangle2D>();
            for (Rect rect : faceDetections.toArray()) {
                rectList.add(new Rectangle2D(rect.x, rect.y, rect.width, rect.height));
            }

            return rectList;
        }

        public synchronized void initOpenCv() {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            videoCapture = new VideoCapture();
            videoCapture.open(0);


            videoCapture.set(3, 640);
            videoCapture.set(4, 480);

            System.out.println("Camera open: " + videoCapture.isOpened());

            stage = (Stage) signInBtn.getScene().getWindow();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {

                    timer.stop();
                    videoCapture.release();

                    System.out.println("Camera released");

                }
            });

            faceDetector = new CascadeClassifier(getOpenCvResource(getClass(), "/haarcascade_frontalface_alt.xml"));
        }

        public String getOpenCvResource(Class<?> clazz, String path) {
            try {
                return Paths.get(clazz.getResource(path).toURI()).toString();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
