package authentication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.support.LdapUtils;
import javax.naming.directory.DirContext;
import java.util.List;
/**
 * This class is a Spring LDAP Integrated LDAP Authenticator. It authenticates a user with LDAP by doing a simple binding with the user's DN (Domain Name) and password.
 * NOTE: By doing the binding, it's only necessary to send in the DN and cleartext password - the server automatically compares the cleartext password with the hashed+salted password.
 */
public class LDAPTester {

    /**
     * This method is responsible for setting up the authentication. May see changes - the userDN is what I could see from the provided LDIF file.
     * The application context is what's used to get the configuration file's beans - this is the Spring integration
     * @param username This is the user's LDAP username
     * @param passwd This is the user's LDAP password
     * @return True if the user was authenticated, False if not
     */
    public static boolean authenticateLDAP(String username, String passwd) {
        try {

            ApplicationContext ctx = new AnnotationConfigApplicationContext(LDAPSettings.class);
            LdapContextSource contextSource = ctx.getBean(LdapContextSource.class);
            LdapTemplate ldapTemplate = ctx.getBean(LdapTemplate.class);
            String userDN = getDnForUser(username, ldapTemplate);
            contextSource.setUserDn(userDN);
            contextSource.setPassword(passwd);
            contextSource.afterPropertiesSet();
            return authenticate(userDN, passwd, contextSource);
        }
        catch(Exception e)
        {
            return false;
        }

    }

    /**
     * This method is responsible for getting the DN of the user by using the username as the uid.
     * @param uid This is the user's LDAP username
     * @param ldapTemplate This is the ldapTemplate created in authenticateLDAP.
     * @return The user's DN on the LDAP server
     */
    public static String getDnForUser(String uid, LdapTemplate ldapTemplate) {
        if(ldapTemplate == null)
        {
            ApplicationContext ctx = new AnnotationConfigApplicationContext(LDAPSettings.class);
            LdapContextSource contextSource = ctx.getBean(LdapContextSource.class);
            ldapTemplate = ctx.getBean(LdapTemplate.class);
        }
        Filter f = new EqualsFilter("uid", uid);
        List result = ldapTemplate.search("", f.toString(),
                new AbstractContextMapper() {
                    protected Object doMapFromContext(DirContextOperations ctx)
                    {
                        return ctx.getNameInNamespace();
                    }
                });

        if(result.size() != 1) {
            return null;
        }
        return (String)result.get(0);
    }

    /**
     * This method is responsible for doing the actual authentication against the LDAP server
     * @param userDn This is the user's LDAP DN
     * @param passwd This is the user's LDAP password
     * @param contextSource This is the contextSource (essentially the LDAP Server settings) created in authenticateLDAP
     * @return True if authenticated, False if not
     */
    public static boolean authenticate(String userDn, String passwd, ContextSource contextSource) {
        DirContext ctx = null;
        try {
            ctx = contextSource.getContext(userDn, passwd);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            LdapUtils.closeContext(ctx);
        }
    }


}