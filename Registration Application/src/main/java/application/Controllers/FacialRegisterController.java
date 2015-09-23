package application.Controllers;

import HTTPPostBuilder.HTTPPostSender;
import javafx.animation.AnimationTimer;
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
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Canvas canvas;

    @FXML
    protected void handleFacialSubmitButtonAction(ActionEvent event) {


        try {
            Image originalImage = getScreenShot();
            BufferedImage bufferedImg = SwingFXUtils.fromFXImage(originalImage, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImg, "jpg", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            System.out.println(getRegistrationDO().getEmplid() + " " + getRegistrationDO().getEmail());
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
            actiontarget.setText("An Error has occurred - Please try again");
        }
    }

    @FXML
    protected void initialize() {

        webcam = new VideoStream();
        webcam.start();
    }

    public Image getScreenShot() {


        Mat mat = new Mat();
        webcam.videoCapture.read(mat);
        javafx.scene.image.Image image = webcam.mat2Image(mat);

        return image;
    }

    public void sendHTTPPostAsJSON(byte[] image, String emplid, String email) {
        HTTPPostSender sender = new HTTPPostSender();
        sender.sendPostRequest(image, emplid, email);
    }


    class VideoStream extends Thread {

        AnimationTimer timer;
        VideoCapture videoCapture;
        GraphicsContext g2d;
        CascadeClassifier faceDetector;

        public void run() {
            g2d = canvas.getGraphicsContext2D();
            g2d.setStroke(javafx.scene.paint.Color.GREEN);

            initOpenCv();

            timer = new AnimationTimer() {

                Mat mat = new Mat();

                @Override
                public void handle(long now) {

                    videoCapture.read(mat);

                    java.util.List<Rectangle2D> rectList = detectFaces(mat);

                    javafx.scene.image.Image image = mat2Image(mat);

                    g2d.drawImage(image, 0, 0);

                    for (Rectangle2D rect : rectList) {
                        g2d.strokeRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
                    }

                }
            };
            timer.start();
        }

        public synchronized Image mat2Image(Mat mat) {
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", mat, buffer);
            return new Image(new ByteArrayInputStream(buffer.toArray()));
        }

        public synchronized List<Rectangle2D> detectFaces(Mat mat) {

            MatOfRect faceDetections = new MatOfRect();
            //faceDetector.detectMultiScale( mat, faceDetections);
            //System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
            List<Rectangle2D> rectList = new ArrayList<>();
            for (Rect rect : faceDetections.toArray()) {

                int x = rect.x;
                int y = rect.y;
                int w = rect.width;
                int h = rect.height;
                rectList.add(new Rectangle2D(x, y, w, h));
            }

            return rectList;
        }

        public synchronized void initOpenCv() {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

            videoCapture = new VideoCapture();
            videoCapture.open(0);

            System.out.println("Camera open: " + videoCapture.isOpened());

            stage = (Stage) signInBtn.getScene().getWindow();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {

                    timer.stop();
                    videoCapture.release();

                    System.out.println("Camera released");

                }
            });
        }
    }

}
