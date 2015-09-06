package authentication;
/**
 * This is the User class. It doesn't do much right now, but it's used to extend the variables being saved in the session easily.
 */
public class User
{
    private String userName = "";
    private String password = "";

    /**
     * This constructor initializes the User's details
     * @param usr This is the username of the user (u+"' or Staff Name)
     * @param pw The user's LDAP password
     */
    public User(String usr, String pw)
    {
        userName = usr;
        password = pw;
    }

    /**
     * Returns the user's username
     * @return The user's username
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Returns the user's password
     * @return The user's password
     */
    public String getPassword()
    {
        return password;
    }
}