package client;

import com.google.gson.Gson;
import modules.BiometricData;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opencv.core.Core;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * {@author Szymon}
 */


public class Client {

    public static ApplicationContext context;

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public static void main(String[] args) throws IOException, ConfigurationException
    {

        context = new ClassPathXmlApplicationContext("beans.xml");

        System.out.println("Starting up client");

        PropertiesConfiguration config = (PropertiesConfiguration) context.getBean("config");


        Scanner scan= new Scanner(System.in);

        String input = "";


        while(scan.hasNextLine()){
            input = scan.nextLine();
            //this means that no keycode was entered so take some pictures and scan finger print
            ArrayList<BiometricData> data;
            if(input.toString().equals(""))
            {
                Factory factory = new Factory();
                data = factory.produce();
            }
            else //keycode was entered, lets authenticate it. should we maybe take pictures of people that entered via keycode??
            {
                if(input.toString().equals("quity"))
                {
                    return;
                }
                data = new ArrayList<BiometricData>();
                BiometricData d = new BiometricData("keycode", input.getBytes());
                data.add(d);
            }


            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpRequestBuilder request = new HttpRequestBuilder();
            HttpPost httpPost = request.buildRequest(config.getProperty("url").toString(),config.getProperty("map").toString(),
                    config.getProperty("id").toString(), config.getProperty("action").toString(), data);
            try
            {
                CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
                HttpResponseHandler response = new HttpResponseHandler();

                String json = response.handleResponse(httpResponse);
                System.out.println("The reply: ");
                System.out.println(json);
                Gson gson = new Gson();
                AccessResponse aResponse = gson.fromJson(json, AccessResponse.class);

                System.out.println("This is our result: " + aResponse.getResult());
                System.out.println("");
            }
            catch(HttpHostConnectException e)
            {
                System.out.println("Could not connect to server.");
            }

            httpClient.close();

        }

       /* //PropertiesConfiguration prop;
        //have to spin, wait for something to activate me
        //for now activate on keypress??? can only do that if enter is pressed or something along those lines
        //need to set up a config file for server address + port and as well as where the images will be stored

        //do we agree on the emthod?? authenticateMe?????





       // System.out.println("POST Result Status:: "
       //         + httpResponse.getStatusLine().getStatusCode());


        // print result
       // System.out.println(response.toString());

*/


        System.out.println("Closing client");
    }

}
