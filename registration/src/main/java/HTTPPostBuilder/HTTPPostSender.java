package HTTPPostBuilder;

import application.RegisterResponse;
import com.google.gson.Gson;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created by Tienie on 09/09/2015.
 */
public class HTTPPostSender {

    public static void sendPostRequest(HttpPost httpPost) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

                try {
                    CloseableHttpResponse httpResponse = httpClient.execute(httpPost);


                    String json = HttpResponseHandler.handleResponse(httpResponse);
                    System.out.println("The reply: ");
                    System.out.println(json);
                    Gson gson = new Gson();
                    RegisterResponse aResponse = gson.fromJson(json, RegisterResponse.class);

                    System.out.println("This is our result: " + aResponse.getResult());
                    System.out.println("");
                } catch (HttpHostConnectException e) {
                    throw new Exception("Server Unavailable");
                }

                httpClient.close();
    }
}
