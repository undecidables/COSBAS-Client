package application.Model;

/**
 * @author Szymon
 */
public class ContactDetail {
    private String type;
    private String details;

    public String getType()
    {
        return type;
    }

    public String getDetails()
    {
        return details;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public ContactDetail(String type, String details) {
        this.type = type;
        this.details = details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }
}
