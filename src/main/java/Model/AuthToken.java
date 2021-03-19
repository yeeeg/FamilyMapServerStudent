package Model;
import java.lang.*;
import java.security.SecureRandom;

public class AuthToken {
    private String authtoken;
    private String username;
    private String password;

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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
