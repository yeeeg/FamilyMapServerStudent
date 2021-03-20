package Service.Result;

import Model.Event;
import Model.Person;

import java.util.ArrayList;

public class PersonsResult
{
    ArrayList<Person> data;
    String message;
    boolean success;

    public PersonsResult(ArrayList<Person> persons, boolean success)
    {
        this.data = new ArrayList<>();
        this.data.addAll(persons);
        this.success = true;
    }
    public PersonsResult(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }
}
