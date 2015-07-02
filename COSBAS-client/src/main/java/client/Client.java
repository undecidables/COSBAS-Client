package client;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by POLSKA on 24/06/2015.
 */


public class Client {

    private static final String CONFIG_FILE_NAME = "config.properties";


    public static void main(String[] args) throws IOException, ConfigurationException
    {


        System.out.println("Starting up client");

        PropertiesConfiguration config = new PropertiesConfiguration();
        config.load(CONFIG_FILE_NAME);


        Scanner scan= new Scanner(System.in);


        String input = "";

        while(scan.hasNextLine()){
            input = scan.nextLine();
            //this means that no keycode was entered so take some pictures and scan finger print
            ArrayList<BiometricData> data = new ArrayList<BiometricData>();
            if(input.toString().equals(""))
            {
                System.out.println("got nothing this time boys");
                Path p = Paths.get("images/faces/face1.png");
                BiometricData g = new BiometricData("face", Files.readAllBytes(p));

                Path q = Paths.get("images/faces/face2.jpg");
                BiometricData f = new BiometricData("face", Files.readAllBytes(q));

                Path r = Paths.get("images/finger/finger.jpg");
                BiometricData h = new BiometricData("finger", Files.readAllBytes(r));
                data.add(g);
                data.add(f);
                data.add(h);
            }
            else //keycode was entered, lets authenticate it. should we maybe take pictures of people that entered via keycode??
            {
                if(input.toString().equals("quity"))
                {
                    return;
                }
                BiometricData d = new BiometricData("keycode", input.getBytes());
                data.add(d);
            }


            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpRequestBuilder request = new HttpRequestBuilder();
            HttpPost httpPost = request.buildRequest(config.getProperty("url").toString(),
                    config.getProperty("id").toString(), config.getProperty("action").toString(), data);
            try
            {
                CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
                HttpResponseHandler response = new HttpResponseHandler();
                response.handleResponse(httpResponse);
            }
            catch(HttpHostConnectException e)
            {
                System.out.println("Could not connect to server.");
            }

            httpClient.close();

        }

        //PropertiesConfiguration prop;
        //have to spin, wait for something to activate me
        //for now activate on keypress??? can only do that if enter is pressed or something along those lines
        //need to set up a config file for server address + port and as well as where the images will be stored

        //do we agree on the emthod?? authenticateMe?????





       // System.out.println("POST Result Status:: "
       //         + httpResponse.getStatusLine().getStatusCode());


        // print result
       // System.out.println(response.toString());




        System.out.println("Closing client");
    }

}
