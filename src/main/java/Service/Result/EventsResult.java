package Service.Result;

import Model.Event;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Stores results of Events service
 */
public class EventsResult
{
    public ArrayList<Event> data;
    String message;
    boolean success;

    public EventsResult(ArrayList<Event> events, boolean success)
    {
        this.data = new ArrayList<>();
        this.data.addAll(events);
        this.success = true;
    }
    public EventsResult(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }
}
