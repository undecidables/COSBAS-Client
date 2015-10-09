package authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * This class is a Spring LDAP Configuration file. It contains Beans which sets up LdapTemplate and ContextSource from the web.Application.Properties file.
 */

/**
 * @author Tienie
 */
@Configuration
@PropertySource("classpath:/application.properties")
public class LDAPSettings {


    @Autowired
    Environment env;

    /**
     * This method is a Bean for the application contextSource - sets up the contextSource with the provided ldap url and ldap base.
     */
    @Bean
    public LdapContextSource contextSource () {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.base"));
        return contextSource;
    }

    /**
     * This method is a Bean for the application ldapTemplate - sets up the ldapTemplate with the contextSource
     */
    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

}