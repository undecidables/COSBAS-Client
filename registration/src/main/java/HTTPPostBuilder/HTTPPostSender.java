package HTTPPostBuilder;

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
    private static final String CONFIG_FILE_NAME = "config.properties";

    public void sendPostRequest(byte[] data, String emplid, String email, String registratorID) throws Exception {
        PropertiesConfiguration config = new PropertiesConfiguration();
        try {

            config.load(CONFIG_FILE_NAME);
        } catch (Exception e) {

        }
        CloseableHttpClient httpClient = HttpClients.createDefault();

            HTTPRequestBuilder request = new HTTPRequestBuilder();
            if (config != null && data != null) {
                String url = config.getProperty("url").toString();
                String map = config.getProperty("map").toString();
                String id = emplid;
                String action = email;

                System.out.println(id + " ++++++++");
                System.out.println(email + " ++++++++");
                HttpPost httpPost = request.buildRequest(url, map, emplid, email, data, registratorID);
                try {
                    CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpResponseHandler response = new HttpResponseHandler();

                    String json = response.handleResponse(httpResponse);
                    System.out.println("The reply: ");
                    System.out.println(json);
                    //Gson gson = new Gson();
                    //AccessResponse aResponse = gson.fromJson(json, AccessResponse.class);

                    //System.out.println("This is our result: " + aResponse.getResult());
                    System.out.println("");
                } catch (HttpHostConnectException e) {
                    throw new Exception("Server Unavailable");
                }

                httpClient.close();
            } else {
                System.out.println("Config or Data is Null");
            }

    }
}
