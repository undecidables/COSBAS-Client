package client;

import org.apache.commons.configuration.ConfigurationException;
import org.opencv.core.Core;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.io.IOException;

/**
 * {@author Szymon}
 */
public class Client {

    public static ApplicationContext context;

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public static void main(String[] args) throws IOException, ConfigurationException
    {
        context = new ClassPathXmlApplicationContext("beans.xml");

        GPIOStarter n = new GPIOStarter();
        n.start();
    }

}
