package client;

import modules.BiometricData;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.HttpPost;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * {@author Szymon}
 */
public class AuthenticationProcess extends Thread {

    private ApplicationContext context;
    private PropertiesConfiguration config;
    //@Autowire
    private Object object;
    private GPIOAccess access;

    private final String doorID;
    private final String action;



    public AuthenticationProcess()
    {
        context = Client.context;
        config = (PropertiesConfiguration) context.getBean("config");
        object = (Object) context.getBean("object");
        access = (GPIOAccess) context.getBean("access");

        doorID = config.getProperty("id").toString();
        action = config.getProperty("action").toString();

    }

    public void run()
    {
        Scanner scan= new Scanner(System.in);
        boolean spin = true;
        String input = "";

        while(spin)
        {

            try
            {
                synchronized (object)
                {
                    object.wait();
                }

               // while (scan.hasNextLine())
               // {
                LCDDisplay.write("WELCOME");
                LCDDisplay.write("ENTER ACCESS CODE OR NEW LINE");
                AuthenticationDataObject authDO = new AuthenticationDataObject(doorID, action);
                input = scan.nextLine();



                ArrayList<BiometricData> data = null;
                if (input.toString().equals("")) {
                    Factory factory = new Factory();
                    try {
                        data = factory.produce();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (input.toString().equals("+/-")) {
                        spin = false;
                        break;
                    }
                    data = new ArrayList<BiometricData>();
                    BiometricData d = new BiometricData("biometric-CODE", input.getBytes());
                    data.add(d);
                }

                if (data != null) {
                    authDO.setBiometricData(data);
                }

                LCDDisplay.write("PROCESSING REQUEST");

                HttpPost httpPost = new HttpPostClientBuilder().buildPost(authDO);
                try
                {
                    HttpClientPostSender sender = new HttpClientPostSender();
                    AccessResponse accessResponse = (AccessResponse) sender.sendPostRequest(httpPost);
                    System.out.println(accessResponse.getMessage() + " : " + accessResponse.getResult());

                    if(accessResponse.getResult() == true)
                    {
                        access.allowAccess();
                        LCDDisplay.write("ACCESS GRANTED");
                    }
                    else
                    {
                        LCDDisplay.write("ACCESS DENIED");
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Exception: " + e.toString());
                    LCDDisplay.write("ERROR COULD NOT PROCESS");
                }

              //  }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }


    }
}
