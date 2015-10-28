package client;

import org.apache.commons.configuration.ConfigurationException;
import org.opencv.core.Core;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.io.IOException;

/**
 * {@author Szymon}
 */

@ComponentScan("client")
public class Client {

    public static ApplicationContext context;

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public static void main(String[] args) throws IOException, ConfigurationException
    {
        /*LCDDisplay d = new LCDDisplay();
        d.write("Hello");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        d.shutdown();*/
        context = new ClassPathXmlApplicationContext("beans.xml");

        GPIOStarter n = new GPIOStarter();
        n.start();
    }

}
