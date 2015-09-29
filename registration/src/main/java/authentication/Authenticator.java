package authentication;
import com.google.api.client.http.HttpResponse;
import org.springframework.http.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for authenticating a user. It does this by getting the username and password from the HttpServletRequest object and then sending those through the LDAP
 * authenticator.
 */
public class Authenticator {

    private HttpServletRequest request;
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
            User user = new User(usr, pwd);
            LDAPTester a = new LDAPTester();
            if (a.authenticateLDAP(usr, pwd)) {
                return true;
            }
            else
            {
                System.out.println("Failed to log in user: " + usr + " " + pwd);
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
     * @param user This is the User object which contains the user details
     * @return HttpSession This is the session that the user will be using after they've been logged in
     */
    public Boolean login(User user) throws ServletException, IOException {

        String Username = user.getUserName();
        String Password = user.getPassword();
        System.out.println(Username + " " + Password + " " + "authenticated");
        return true;
    }
}

