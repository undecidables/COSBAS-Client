package application.Controllers;

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
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
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

            saveImage();
            actiontarget.setText("Photo Taken");
            webcam.timer.stop();
            webcam.videoCapture.release();


            stage = (Stage) signInBtn.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/FXML/FacialRegisterConfirmScreen.fxml"));
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

        webcam = new VideoStream();
        webcam.start();
    }

    public void saveImage() throws Exception {


        Mat mat = new Mat();
        webcam.videoCapture.read(mat);
        javafx.scene.image.Image image = webcam.mat2Image(mat);
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

        croppedImage = grayscale(croppedImage);
        getRegistrationDO().setImage(extractBytes(croppedImage));
    }

    public byte[] extractBytes(WritableImage wim) throws IOException {
        // open image
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", byteOutput);
        return byteOutput.toByteArray();
    }


    private WritableImage grayscale(Image img) {
        WritableImage gray = new WritableImage((int) img.getWidth(), (int) img.getHeight());
        PixelReader imgReader = img.getPixelReader();
        PixelWriter imgWriter = gray.getPixelWriter();
        for (int y = 0; y < img.getHeight(); ++y) {
            for (int x = 0; x < img.getWidth(); ++x) {
                Color c = imgReader.getColor(x, y);
                int mx = (int) (0xFF * Math.max(c.getRed(), Math.max(c.getGreen(), c.getBlue())));
                int mn = (int) (0xFF * Math.min(c.getRed(), Math.min(c.getGreen(), c.getBlue())));
                int g = (mx + mn) / 2;
                Color nc = Color.rgb(g, g, g);
                imgWriter.setColor(x, y, nc);
            }
        }
        return gray;
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

                    canvas.setWidth(image.getWidth());
                    canvas.setHeight(image.getHeight());

                    stage.sizeToScene();

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
            faceDetector.detectMultiScale(mat, faceDetections);
            List<Rectangle2D> rectList = new ArrayList<>();
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
