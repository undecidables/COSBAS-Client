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



       /* PropertiesConfiguration config = (PropertiesConfiguration) context.getBean("config");

        String doorID = config.getProperty("id").toString();
        String action = config.getProperty("action").toString();


        Scanner scan= new Scanner(System.in);

        String input = "";


        while(scan.hasNextLine()){
            AuthenticationDataObject authDO = new AuthenticationDataObject(doorID, action);
            input = scan.nextLine();

            ArrayList<BiometricData> data;
            if(input.toString().equals(""))
            {
                Factory factory = new Factory();
                data = factory.produce();
            }
            else
            {
                if(input.toString().equals("quity"))
                {
                    return;
                }
                data = new ArrayList<BiometricData>();
                BiometricData d = new BiometricData("keycode", input.getBytes());
                data.add(d);
            }

            authDO.setBiometricData(data);

            HttpPost httpPost = new HttpPostClientBuilder().buildPost(authDO);
            try
            {
                HttpClientPostSender sender = new HttpClientPostSender();
                AccessResponse accessResponse = (AccessResponse) sender.sendPostRequest(httpPost);
                System.out.println(accessResponse.getMessage() + " : " + accessResponse.getResult());
            }
            catch (Exception e)
            {
                System.out.println("Exception: " + e.toString());
            }

        }*/

    }

}
