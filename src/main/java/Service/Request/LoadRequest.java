package Service.Request;

import Model.Event;
import Model.LoadModel;
import Model.Person;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Object to store load request data
 */
public class LoadRequest
{
    String requestBody;
    public LoadModel loadModel;

    public LoadRequest(String requestBody)
    {
        this.requestBody = requestBody;
    }

    public void createObjects()
    {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        loadModel = gson.fromJson(requestBody, LoadModel.class);
    }
}
