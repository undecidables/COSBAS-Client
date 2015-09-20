package application;

import application.Scenes.LoginScene;
import javafx.application.Application;
import javafx.stage.Stage;
import org.opencv.core.Core;



public class RegistrationApplication extends Application {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    Stage thestage;


    public static void main(String[] args) {
        launch(args);
    }




    @Override
    public void start(Stage primaryStage) {

        thestage = primaryStage;
        setupApplication();
    }

    public void setupApplication() {

        //Setup Scenes
        LoginScene loginScene = new LoginScene(thestage);
        thestage.setScene(loginScene.getScene());
        thestage.show();

    }
}
