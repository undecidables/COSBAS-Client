package client;

import modules.BiometricData;
import modules.HttpPostBuilderInterface;
import modules.HttpPostRequestBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.HttpPost;
import org.springframework.context.ApplicationContext;

/**
 * Created by simon on 2015-10-12.
 */
public class HttpPostClientBuilder implements HttpPostBuilderInterface {

    ApplicationContext context;
    PropertiesConfiguration config;

    public HttpPostClientBuilder()
    {
        context = Client.context;
        config = (PropertiesConfiguration) context.getBean("config");
    }

    @Override
    public HttpPost buildPost(Object data) {
        AuthenticationDataObject object = (AuthenticationDataObject) data;

        HttpPostRequestBuilder httpBuilder = new HttpPostRequestBuilder(config.getProperty("url").toString(), config.getProperty("map").toString());
        httpBuilder.addUserAgentHeader("Authentication");
        httpBuilder.addStringEntities("ID", object.getDoorID());
        httpBuilder.addStringEntities("Action", object.getAction());

        for(BiometricData bData : object.getBiometricData())
        {
            httpBuilder.addOtherEntities(bData.getType(), bData.getData());
        }

        httpBuilder.setEntity();

        return httpBuilder.getHttpPost();
    }
}
