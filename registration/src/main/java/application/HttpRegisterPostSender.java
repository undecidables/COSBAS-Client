package application;

import modules.BuildObjectFromJSONResponse;
import modules.HttpResponseHandler;
import modules.HttpPostSenderInterface;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * @author Tienie
 */
public class HttpRegisterPostSender implements HttpPostSenderInterface {

    public Object sendPostRequest(HttpPost httpPost) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        RegisterResponse regResponse = null;
        try
        {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            String json = HttpResponseHandler.handleResponse(httpResponse);
            regResponse = (RegisterResponse) BuildObjectFromJSONResponse.buildOject(json, RegisterResponse.class);
        }
        catch (HttpHostConnectException e)
        {
                    throw new Exception("Server Unavailable");
        }
        finally
        {
            httpClient.close();
        }

        return regResponse;

    }
}
