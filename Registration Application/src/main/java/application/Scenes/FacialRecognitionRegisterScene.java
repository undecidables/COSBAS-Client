package application.Scenes;

import HTTPPostBuilder.HTTPPostSender;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
 * Created by Tienie on 9/20/2015.
 */
public class FacialRecognitionRegisterScene {

    private static final int SCENE_W = 320;
    private static final int SCENE_H = 240;
    Scene scene4;
    Stage thestage;
    CascadeClassifier faceDetector;
    VideoCapture videoCapture;

    Canvas canvas;
    GraphicsContext g2d;
    AnimationTimer timer;
    GridPane pane4;
    String emplid, email;

    public FacialRecognitionRegisterScene(Stage stage, String emp, String mail) {
        email = mail;
        emplid = emp;
        setupStage(stage);
    }

    public static Image mat2Image(Mat mat) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", mat, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

    public void setupStage(Stage stage) {

        thestage = stage;


        System.out.println("Here");


        //scene4 = new Scene(group, SCENE_W, SCENE_H);
        canvas = new javafx.scene.canvas.Canvas(SCENE_W, SCENE_H);
        g2d = canvas.getGraphicsContext2D();
        g2d.setStroke(javafx.scene.paint.Color.GREEN);

        Group group = new Group(canvas);

        pane4 = new GridPane();
        pane4.setAlignment(Pos.CENTER);
        pane4.setVgap(10);
        pane4.setHgap(10);
        pane4.setPadding(new Insets(45, 45, 45, 45));
        pane4.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        scene4 = new Scene(pane4, 640, 480);

        thestage.setTitle("Biometric Registration");
        Text scenetitle4 = new Text("Take Photo");
        scenetitle4.setTextAlignment(TextAlignment.JUSTIFY);
        scenetitle4.setId("welcome-text");
        pane4.add(scenetitle4, 1, 0);


        Button btnSignInScene3 = new Button("Take Photo");
        HBox hbBtn4 = new HBox(10);
        hbBtn4.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn4.getChildren().add(btnSignInScene3);
        pane4.add(hbBtn4, 1, 1);

        final Text actiontarget4 = new Text();
        pane4.add(actiontarget4, 1, 3);

        btnSignInScene3.setOnAction(e -> {
            actiontarget4.setId("actiontarget");
            actiontarget4.setText("Sign in button pressed");
            try {
                Image originalImage = getScreenShot();
                BufferedImage bufferedImg = SwingFXUtils.fromFXImage(originalImage, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImg, "jpg", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                sendHTTPPostAsJSON(imageInByte, emplid, email);
                baos.close();

            } catch (IOException exc) {
                System.out.println(exc.getMessage());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        scene4.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());


        pane4.add(group, 1, 5);
        thestage.setScene(scene4);
        thestage.setResizable(false);
        thestage.show();
        VideoStream webcam = new VideoStream();
        webcam.start();

    }

    public Scene getScene() {
        return scene4;
    }

    public Image getScreenShot() {


        Mat mat = new Mat();
        videoCapture.read(mat);
        javafx.scene.image.Image image = mat2Image(mat);

        return image;
    }

    public void sendHTTPPostAsJSON(byte[] image, String emplid, String email) {
        HTTPPostSender sender = new HTTPPostSender();
        sender.sendPostRequest(image, emplid, email);
    }

    public void initOpenCv() {

        //setLibraryPath();

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        videoCapture = new VideoCapture();
        videoCapture.open(0);

        System.out.println("Camera open: " + videoCapture.isOpened());

        thestage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {

                timer.stop();
                videoCapture.release();

                System.out.println("Camera released");

            }
        });
    }

    public List<Rectangle2D> detectFaces(Mat mat) {

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

    class VideoStream extends Thread {

        public void run() {
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

    }
}
