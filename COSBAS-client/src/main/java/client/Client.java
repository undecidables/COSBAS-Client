package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by POLSKA on 24/06/2015.
 */

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting up client");

        //have to spin, wait for something to activate me
        //for now activate on keypress??? java is retarded can only do it with enter :/ this is why c# is better guys :p
        //need to set up a config file for server address + port and as well as where the images will be stored

        //do we agree on the emthod?? authenticateMe?????
        String url = "http://localhost:2222/authenticateMe";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestBuilder request = new RequestBuilder();
        HttpPost httpPost = request.buildPostRequest(url);
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        System.out.println("POST Response Status:: "
                + httpResponse.getStatusLine().getStatusCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        // print result
        System.out.println(response.toString());

        httpClient.close();


        System.out.println("Closing client");
    }
}
