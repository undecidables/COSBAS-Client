package application.Model;

import application.RegistrationApplication;
import modules.BuildObjectFromJSONResponse;
import modules.HttpResponseHandler;
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
 * Created by simon on 2015-10-29.
 */
public class HttpQueryPostSender {

    private ApplicationContext context;
    private PropertiesConfiguration config;

    public HttpQueryPostSender()
    {
        context = RegistrationApplication.context;
        config = (PropertiesConfiguration) context.getBean("config");
    }



    public String sendPostRequest(HttpPost httpPost) throws Exception {

        KeyStore k = KeyStore.getInstance(config.getProperty("server.ssl.key-store-type").toString().toLowerCase());

        k.load(new FileInputStream(config.getProperty("server.ssl.key-store").toString()), config.getProperty("server.ssl.key-store-password").toString().toCharArray());


        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(k, new TrustSelfSignedStrategy()).build();

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, new String[] {"TLSv1"}, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


        String json = "";
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        try
        {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            json = HttpResponseHandler.handleResponse(httpResponse);
        }
        catch (HttpHostConnectException e)
        {
            throw new Exception("The COSBAS Server is currently unavailable");
        }
        finally
        {
            httpClient.close();
        }

        return json;

    }
}
