package application.Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * {@author Tienie}
 */
public class ApplicationModel extends Application {

    public static ApplicationContext app;

    public static void main(String[] args) {
        launch(args);
    }

    /*public static void oL(String[] args)
    {
        launch(args);
    }*/

    @Override
    public void start(Stage primaryStage) {
        app = new ClassPathXmlApplicationContext("beans.xml");

        primaryStage.setTitle("Biometric Registration");
        try {
            Pane myPane = FXMLLoader.load(getClass().getResource("/FXML/Registration_Login.fxml"));
            Scene myScene = new Scene(myPane);

            primaryStage.setScene(myScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
