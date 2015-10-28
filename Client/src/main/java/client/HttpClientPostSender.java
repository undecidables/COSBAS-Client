package client;

import modules.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.ApplicationContext;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * {@author Szymon}
 */
public class HttpClientPostSender implements HttpPostSenderInterface {

    private ApplicationContext context;
    private PropertiesConfiguration config;

    public HttpClientPostSender()
    {
        context = Client.context;
        config = (PropertiesConfiguration) context.getBean("config");
    }

    @Override
    public Object sendPostRequest(HttpPost httpPost) throws Exception {
        KeyStore k = KeyStore.getInstance(config.getProperty("server.ssl.key-store-type").toString().toLowerCase());

        k.load(new FileInputStream(config.getProperty("server.ssl.key-store").toString()), config.getProperty("server.ssl.key-store-password").toString().toCharArray());


        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(k, new TrustSelfSignedStrategy()).build();

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1"}, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

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
