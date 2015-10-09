package authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for authenticating a user. It does this by getting the username and password from the HttpServletRequest object and then sending those through the LDAP
 * authenticator.
 */
/**
 * @author Tienie
 */
public class Authenticator {

    /**
     * This method is responsible for doing the actual authentication - when a user is authenticated using LDAP, they are logged in to the system.
     * It is done by getting the username and password from the request object, then constructing a user object from those variables.
     * The username and password are then validated against ldap - if authentication succeeds, it logs the user in by creating and returning an HttpSession
     * Otherwise it returns null
     * @param username This is the username which will be used to log in
     * @param password This is the password which will be used to log in
     * @return HttpSession This is the session that the user will be using after they've been logged in
     */
    public Boolean doProcess(String username, String password) throws Exception {
        try {
            String usr = username;
            String pwd = password;
            LDAPTester a = new LDAPTester();
            if (a.authenticateLDAP(usr, pwd)) {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }

    /**
     * This method is responsible for logging a user in by creating a session from the user's details.
     * @return HttpSession This is the session that the user will be using after they've been logged in
     */
}

