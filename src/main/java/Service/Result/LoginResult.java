package Service.Result;

/**
 * Stores results of login service
 */
public class LoginResult
{
    String authtoken;
    String username;
    String personID;
    String message;
    boolean success;

    public void success(String authtoken, String username, String personID)
    {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        success = true;
    }
    public void failure(String message)
    {
        success = false;
        this.message = message;
    }

    public String getAuthtoken()
    {
        return authtoken;
    }

    public void setAuthtoken(String authtoken)
    {
        this.authtoken = authtoken;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPersonID()
    {
        return personID;
    }

    public void setPersonID(String personID)
    {
        this.personID = personID;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }
}
