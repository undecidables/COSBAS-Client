package client;

import modules.BiometricData;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.HttpPost;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by simon on 2015-10-14.
 */
public class AuthenticationProcess extends Thread {

    private ApplicationContext context;
    private PropertiesConfiguration config;
    private Object object;

    private final String doorID;
    private final String action;



    public AuthenticationProcess()
    {
        context = Client.context;
        config = (PropertiesConfiguration) context.getBean("config");
        object = (Object) context.getBean("object");

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

                while (scan.hasNextLine())
                {
                    System.out.println("Welcome");
                    System.out.println("Please enter keycode or press enter for biometric auth.");
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

                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }


    }
}
