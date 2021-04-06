package Service.Result;

import java.util.Objects;

/**
 * Stores results of register service
 */
public class RegisterResult
{
    String authtoken;
    String username;
    String personID;
    String message;
    boolean success;


    public String getAuthtoken()
    {
        return authtoken;
    }

    public void setAuthtoken(String authtoken)
    {
        this.authtoken = authtoken;
    }

    public void success(String username, String personID)
    {
        this.username = username;
        this.personID = personID;
        this.success = true;
    }

    public void failure(String message)
    {
        this.success = false;
        this.message = message;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterResult that = (RegisterResult) o;
        return success == that.success && authtoken.equals(that.authtoken) && username.equals(that.username) && personID.equals(that.personID);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(authtoken, username, personID, success);
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
}
