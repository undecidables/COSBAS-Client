package client;

import modules.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by simon on 2015-10-13.
 */
public class HttpClientPostSender implements HttpPostSenderInterface {
    @Override
    public Object sendPostRequest(HttpPost httpPost) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        AccessResponse accessResponse = null;
        try
        {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            String json = modules.HttpResponseHandler.handleResponse(httpResponse);
            accessResponse = (AccessResponse) BuildObjectFromJSONResponse.buildOject(json, AccessResponse.class);
        }
        catch (HttpHostConnectException e)
        {
            throw new Exception("Server Unavailable");
        }
        finally
        {
            httpClient.close();
        }

        return accessResponse;

    }
}
