package application.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Tienie on 9/23/2015.
 */

public class ApplicationView extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Biometric Registration");
        try {
            Pane myPane = FXMLLoader.load(getClass().getResource("/FXML/LoginScreen.fxml"));
            Scene myScene = new Scene(myPane);
            primaryStage.setScene(myScene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Here");
            e.printStackTrace();
        }
    }

}
