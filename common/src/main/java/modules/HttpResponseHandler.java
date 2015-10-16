package modules;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpResponseHandler
{
    public static String handleResponse(CloseableHttpResponse httpResponse) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuilder responseBuilder = new StringBuilder();

        while ((inputLine = reader.readLine()) != null) {
            responseBuilder.append(inputLine);
        }
        reader.close();

        return responseBuilder.toString();
    }


}
