package application.Model;

import application.RegistrationApplication;
import modules.BiometricData;
import modules.HttpPostRequestBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.HttpPost;
import org.springframework.context.ApplicationContext;

/**
 * Created by simon on 2015-10-29.
 */
public class HttpPostQueryBuilder {

    ApplicationContext app;
    PropertiesConfiguration config;

    public HttpPostQueryBuilder()
    {
        app = RegistrationApplication.context;
        config = (PropertiesConfiguration) app.getBean("config");
    }


    public HttpPost buildPost(String staffID)
    {
        HttpPostRequestBuilder httpBuilder = new HttpPostRequestBuilder(config.getProperty("url").toString(), "/canRegister");
        httpBuilder.addUserAgentHeader("Registration");
        httpBuilder.addStringEntities("staffID", staffID);

        httpBuilder.setEntity();

        return httpBuilder.getHttpPost();
    }

}
