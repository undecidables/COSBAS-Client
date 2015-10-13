package client;

import modules.HttpPostBuilderInterface;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.HttpPost;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

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




        return null;
    }
}
