package client;

import java.time.LocalDateTime;

/**
 * Created by Renette on 2015-06-27.
 * Response to AccessRequest.
 */
public class AccessResponse {
    public Boolean getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getDoorID() {
        return doorID;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResult(Boolean result)
    {
        this.result = result;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setDoorID(String doorID)
    {
        this.doorID = doorID;
    }

    public void setRequestTime(LocalDateTime requestTime)
    {
        this.requestTime = requestTime;
    }

    public void setResponseTime(LocalDateTime responseTime)
    {
        this.responseTime = responseTime;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    /**
     * Should the person be allowed in.
     * (True = allowed, false = rejected)
     */
    private Boolean result;

    /**
     * Reason for acceptance/rejection.
     */
    private String message;

    /**
     * Door ID as sent by client request
     */
    private String doorID;

    /**
     * If a user is identified their userID, otherwise null;
     */
    private String userID;

    /**
     * Time request was sent - for logging purposes.
     */
    private LocalDateTime requestTime;

    /**
     * Time response object created - for logging purposes.
     */
    private LocalDateTime responseTime;


    public AccessResponse(Boolean result, String message, String doorID, String userID, LocalDateTime requestTime, LocalDateTime responseTime)
    {
        this.result =result;
        this.message = message;
        this.doorID = doorID;
        this.userID = userID;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
    }


}
