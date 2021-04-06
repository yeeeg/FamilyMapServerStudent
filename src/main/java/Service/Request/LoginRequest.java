package Service.Request;

import Model.AuthToken;
import Model.User;
import Service.Login;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

/**
 * Object to store login request data
 */
public class LoginRequest
{
    String requestBody;
    public User user;

    public LoginRequest(String requestBody)
    {
        this.requestBody = requestBody;
    }
    public LoginRequest(User user)
    {
        this.user = new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getGender(), user.getPersonID());
    }

    /**
     * Creates user object based on requestBody. Only needed when using a requestBody
     */
    public void createUserObject()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        user = gson.fromJson(requestBody, User.class);
    }
}
