package application.Model;

import application.RegistrationApplication;
import modules.BiometricData;
import modules.HttpPostBuilderInterface;
import modules.HttpPostRequestBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.client.methods.HttpPost;
import org.springframework.context.ApplicationContext;

/**
 * @author Szymon
 */
public class HttpPostRegistrationBuilder implements HttpPostBuilderInterface
{
    ApplicationContext app;
    PropertiesConfiguration config;

    public HttpPostRegistrationBuilder()
    {
        app = RegistrationApplication.context;
        config = (PropertiesConfiguration) app.getBean("config");
    }

    @Override
    public HttpPost buildPost(Object data)
    {
        HttpPostRequestBuilder httpBuilder = new HttpPostRequestBuilder(config.getProperty("url").toString(), config.getProperty("map").toString());
        RegistrationDataObject reg = (RegistrationDataObject) data;
        httpBuilder.addUserAgentHeader("Registration");
        httpBuilder.addStringEntities("userID", reg.getEmplid());
        httpBuilder.addStringEntities("registratorID", reg.getRegistratorID());

        for(BiometricData bData : reg.getBiometricData())
        {
            httpBuilder.addOtherEntities(bData.getType(), bData.getData());
        }

        for(ContactDetail cData : reg.getContactDetails())
        {
            httpBuilder.addStringEntities(cData.getType(), cData.getDetails());
        }

        httpBuilder.setEntity();

        return httpBuilder.getHttpPost();
    }
}
