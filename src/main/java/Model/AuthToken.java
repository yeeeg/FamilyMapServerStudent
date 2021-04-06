package Model;
import java.lang.*;
import java.security.SecureRandom;
import java.util.Objects;

public class AuthToken {
    private String authtoken;
    private String username;
    private String password;
    private String personID;

    /**
     * Default constructor for AuthToken
     */
    public AuthToken() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthToken)) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(authtoken, authToken.authtoken) && Objects.equals(username, authToken.username) && Objects.equals(password, authToken.password) && Objects.equals(personID, authToken.personID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authtoken, username, password, personID);
    }

    /**
     * Alternate constructor for AuthToken
     * @param authtoken AuthToken string
     * @param username Username string
     * @param password Password string
     */
    public AuthToken(String authtoken, String username, String password)
    {
        this.authtoken = authtoken;
        this.username = username;
        this.password = password;
    }

    /**
     * Getter for PersonID
     * @return personID
     */
    public String getPersonID()
    {
        return personID;
    }

    /**
     * Setter for personID
     * @param personID personID value
     */
    public void setPersonID(String personID)
    {
        this.personID = personID;
    }

    /**
     * Getter for authtoken
     * @return authtoken
     */
    public String getAuthtoken()
    {
        return authtoken;
    }

    /**
     * Setter for authtoken
     * @param authtoken authtoken value
     */
    public void setAuthtoken(String authtoken)
    {
        this.authtoken = authtoken;
    }

    /**
     * Getter for username
     * @return username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Setter for username
     * @param username username value
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Getter for password
     * @return password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Setter for password
     * @param password password value
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
}
