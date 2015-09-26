package application.Controllers;

import HTTPPostBuilder.HTTPPostSender;
import application.RegistrationDataObject;

/**
 * Created by Tienie on 9/23/2015.
 */
public class BaseController {
    protected static RegistrationDataObject registrationDO = new RegistrationDataObject();

    public RegistrationDataObject getRegistrationDO() {
        return registrationDO;
    }

    public void sendHTTPPostAsJSON(byte[] image, String emplid, String email) throws Exception {
        HTTPPostSender sender = new HTTPPostSender();
        try {
            sender.sendPostRequest(image, emplid, email);
        } catch (Exception e) {
            throw e;
        }
    }
}
