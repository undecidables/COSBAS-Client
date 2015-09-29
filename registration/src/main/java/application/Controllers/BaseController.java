package application.Controllers;

import application.RegistrationDataObject;

/**
 * Created by Tienie on 9/23/2015.
 */
public class BaseController {
    protected static RegistrationDataObject registrationDO = new RegistrationDataObject();

    public RegistrationDataObject getRegistrationDO() {
        return registrationDO;
    }
}
