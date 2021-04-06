package Service.Request;

import Model.Person;
import Model.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.UUID;

/**
 * Object to store register request data
 */
public class RegisterRequest
{
    String requestBody;
    public User user;
    public Person person;

    public RegisterRequest(String requestBody)
    {
        this.requestBody = requestBody;
    }
    public void createUser()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        user = gson.fromJson(requestBody, User.class);
        person = gson.fromJson(requestBody, Person.class);

        person.setAssociatedUsername(user.getUsername());
        person.setPersonID(UUID.randomUUID().toString());
        user.setPersonID(person.getPersonID());
        //need to figure out how to handle bad json data
    }
    private boolean validBody()
    {
        return false;
    }
}
