package application;

import HTTPPostBuilder.HTTPPostSender;
import authentication.Authenticator;
import authentication.LDAPTester;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;



public class RegistrationApplication extends Application {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final String USER_AGENT = "Mozilla/5.0";
    Button btnscene1, btnscene2, btnSignInScene1, btnSignInScene3, btnSignInScene4;
    Label lblscene1, lblscene2, lblscene3, lblscene4;
    GridPane pane1, pane2, pane3, pane4;
    Scene scene1, scene2, scene3, scene4;
    Stage thestage;
    JLabel imageLabel = new JLabel();
    SwingNode swingContent = new SwingNode();
    private Authenticator authenticator = new Authenticator();

    public static void main(String[] args) {
        launch(args);
    }

    public static BufferedImage mat2Img(Mat in) {
        BufferedImage out;
        byte[] data = new byte[320 * 240 * (int) in.elemSize()];
        int type;
        in.get(0, 0, data);

        if (in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(320, 240, type);

        out.getRaster().setDataElements(0, 0, 320, 240, data);
        return out;
    }

    public static BufferedImage getScreenShot(Component component) {

        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        // paints into image's Graphics
        component.paint(image.getGraphics());
        return image;
    }

    public static void getSaveSnapShot(Component component, String fileName) throws Exception {
        BufferedImage img = getScreenShot(component);
        // write the captured image as a PNG
        ImageIO.write(img, "jpg", new File(fileName));
    }

    @Override
    public void start(Stage primaryStage) {

        thestage = primaryStage;
        setupScene1();
        setupScene2();
        setupScene3();
        setupScene4();
    }

    public void setupScene1() {
        lblscene1 = new Label("Scene 1");
        pane1 = new GridPane();
        pane1.setAlignment(Pos.CENTER);
        pane1.setVgap(10);
        pane1.setHgap(10);
        pane1.setPadding(new Insets(25, 25, 25, 25));
        pane1.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        scene1 = new Scene(pane1, 640, 480);
        thestage.setTitle("Biometric Registration");
        Text scenetitle = new Text("Log In");
        scenetitle.setTextAlignment(TextAlignment.JUSTIFY);
        scenetitle.setId("welcome-text");
        pane1.add(scenetitle, 1, 0);

        Label userName = new Label("EMPLID:");
        pane1.add(userName, 1, 1);

        TextField userTextField = new TextField();
        pane1.add(userTextField, 1, 2);

        Label pw = new Label("Password:");
        pane1.add(pw, 1, 3);

        PasswordField pwBox = new PasswordField();
        pane1.add(pwBox, 1, 4);

        btnSignInScene1 = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btnSignInScene1);
        pane1.add(hbBtn, 1, 5);

        final Text actiontarget = new Text();
        pane1.add(actiontarget, 1, 7);

        btnSignInScene1.setOnAction(e -> {
            String EMPLID = userTextField.getText();
            String Password = pwBox.getText();
            actiontarget.setId("actiontarget");
            actiontarget.setText("Sign in button pressed");
            try {
                if (authenticator.doProcess(EMPLID, Password)) {
                    actiontarget.setText("Login Succeeded");
                    thestage.setScene(scene2);
                } else {
                    actiontarget.setText("Login Failed");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thestage.setScene(scene1);
        scene1.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());

        thestage.show();

    }

    public void setupScene2() {
        lblscene2 = new Label("Scene 2");
        pane2 = new GridPane();
        pane2.setAlignment(Pos.CENTER);
        pane2.setVgap(10);
        pane2.setHgap(10);
        pane2.setPadding(new Insets(25, 25, 25, 25));
        pane2.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        scene2 = new Scene(pane2, 640, 480);

        thestage.setTitle("Biometric Registration");
        Text scenetitle2 = new Text("Register");
        scenetitle2.setTextAlignment(TextAlignment.JUSTIFY);
        scenetitle2.setId("welcome-text");
        pane2.add(scenetitle2, 1, 0);

        Label userName2 = new Label("Enter Student/Staff EMPLID:");
        pane2.add(userName2, 1, 1);

        TextField userTextField2 = new TextField();
        pane2.add(userTextField2, 1, 2);

        btnSignInScene3 = new Button("Start Registration");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(btnSignInScene3);
        pane2.add(hbBtn2, 1, 5);

        final Text actiontarget2 = new Text();
        pane2.add(actiontarget2, 1, 7);

        btnSignInScene3.setOnAction(e -> {
            String EMPLID = userTextField2.getText();
            actiontarget2.setId("actiontarget");
            actiontarget2.setText("Sign in button pressed");
            try {
                LDAPTester ldapTest = new LDAPTester();
                if (LDAPTester.getDnForUser(EMPLID, null) != null) {
                    System.out.println("Found User!");
                    thestage.setScene(scene3);

                } else {
                    System.out.println("Didn't Find User");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        scene2.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());

    }

    public void setupScene3() {
        lblscene3 = new Label("Scene 3");
        pane3 = new GridPane();
        pane3.setAlignment(Pos.CENTER);
        pane3.setVgap(10);
        pane3.setHgap(10);
        pane3.setPadding(new Insets(35, 35, 35, 35));
        pane3.setStyle("-fx-background-color: tan;-fx-padding: 10px;");
        scene3 = new Scene(pane3, 640, 480);

        thestage.setTitle("Biometric Registration");
        Text scenetitle3 = new Text("Choose Type Of Biometric System");
        scenetitle3.setTextAlignment(TextAlignment.JUSTIFY);
        scenetitle3.setId("welcome-text");
        pane3.add(scenetitle3, 1, 0);

        final ComboBox accessSystemComboBox = new ComboBox();
        accessSystemComboBox.getItems().addAll(
                "Facial Recognition",
                "Fingerprint Scanner"
        );
        accessSystemComboBox.setValue("Facial Recognition");

        pane3.add(accessSystemComboBox, 1, 1);

        btnSignInScene3 = new Button("Continue");
        HBox hbBtn3 = new HBox(10);
        hbBtn3.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn3.getChildren().add(btnSignInScene3);
        pane3.add(hbBtn3, 1, 6);

        final Text actiontarget3 = new Text();
        pane3.add(actiontarget3, 1, 8);


        btnSignInScene3.setOnAction(e -> {
            String BiometricSystem = accessSystemComboBox.getValue().toString();
            actiontarget3.setId("actiontarget");
            actiontarget3.setText("Sign in button pressed");
            try {
                if (BiometricSystem.equals("Facial Recognition")) {
                    System.out.println("Facial");

                    Task<Integer> task = new Task<Integer>() {
                        @Override
                        protected Integer call() throws Exception {

                            Mat webcamMatImage = new Mat();
                            java.awt.Image tempImage;
                            VideoCapture capture = new VideoCapture(0);
                            capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 320);
                            capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 240);

                            if (capture.isOpened()) {
                                ImageIcon imageIcon;
                                while (true) {
                                    capture.read(webcamMatImage);
                                    if (!webcamMatImage.empty()) {
                                        tempImage = mat2Img(webcamMatImage);
                                        imageIcon = new ImageIcon(tempImage, "Captured Video");
                                        imageLabel.setIcon(imageIcon);
                                        swingContent.setContent(imageLabel);
                                    } else {
                                        System.out.println("Frame not captured");
                                        break;
                                    }
                                    imageIcon = null;
                                }
                            } else {
                                System.out.println("Couldn't open capture!");
                            }

                            return 0;
                        }
                    };
                    Thread th = new Thread(task);
                    th.setDaemon(true);
                    th.start();
                    thestage.setScene(scene4);


                } else if (BiometricSystem.equals("Fingerprint Scanner")) {
                    System.out.println("Finger");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        scene3.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());


    }

    public void setupScene4() {
        /*Webcam Stream Screen*/
        lblscene4 = new Label("Scene 4");
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


        btnSignInScene3 = new Button("Take Photo");
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
                BufferedImage originalImage = getScreenShot(imageLabel);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(originalImage, "jpg", baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                sendHTTPPostAsJSON(imageInByte);


                baos.close();

            } catch (IOException exc) {
                System.out.println(exc.getMessage());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        scene4.getStylesheets().add
                (application.RegistrationApplication.class.getResource("/Login.css").toExternalForm());


        imageLabel = new JLabel();
        swingContent.setContent(imageLabel);
        pane4.add(swingContent, 1, 5);
        thestage.sizeToScene();

    }

    public void sendHTTPPostAsJSON(byte[] image) {
        HTTPPostSender sender = new HTTPPostSender();
        sender.sendPostRequest(image);
    }

}
