package application;

import application.Model.ApplicationModel;
import modules.BiometricData;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.ConsoleAppender;
import org.springframework.context.ApplicationContext;

/**
 * Created by simon on 2015-10-07.
 */
public class HttpPostRegistrationBuilder {

    ApplicationContext app;
    PropertiesConfiguration config;

    public HttpPostRegistrationBuilder()
    {
        app = ApplicationModel.app;
        config = (PropertiesConfiguration) app.getBean("config");
    }

    public HttpPost buildPost(RegistrationDataObject reg)
    {
        HttpPostRequestBuilder httpBuilder = new HttpPostRequestBuilder(config.getProperty("url").toString(), config.getProperty("map").toString());
        httpBuilder.addUserAgentHeader("Registration");
        httpBuilder.addStringEntities("userID", reg.getEmplid());
        httpBuilder.addStringEntities("registratorID", reg.getRegistratorID());

        for(BiometricData data : reg.getBiometricData())
        {
            httpBuilder.addOtherEntities(data.getType(), data.getData());
        }

        for(ContactDetail data : reg.getContactDetails())
        {
            httpBuilder.addStringEntities(data.getType(), data.getDetails());
        }

        httpBuilder.setEntity();

        return httpBuilder.getHttpPost();
    }
}
