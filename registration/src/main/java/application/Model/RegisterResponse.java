package application.Model;

import java.time.LocalDateTime;

/**
 * {@author Tienie}
 */
public class RegisterResponse {

    private LocalDateTime requestTime;
    private LocalDateTime responseTime;
    private Boolean result;
    private String message;


    public RegisterResponse(LocalDateTime requestTime, LocalDateTime responseTime, Boolean result, String message) {
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.message = message;
        this.result = result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRequestTime(LocalDateTime requestTime)
    {
        this.requestTime = requestTime;
    }

    public void setResponseTime(LocalDateTime responseTime)
    {
        this.responseTime = responseTime;
    }

    public Boolean getResult()
    {
        return result;
    }

    public String getMessage()
    {
        return message;
    }

    public LocalDateTime getRequestTime()
    {
        return requestTime;
    }

    public LocalDateTime getResponseTime()
    {
        return responseTime;
    }
}
