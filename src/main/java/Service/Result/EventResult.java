package Service.Result;

import Model.*;

/**
 * Used for both /event and /event/eventID
 * EventsResult stores values from EventResult into an array
 */
public class EventResult
{
    String associatedUsername;
    String eventID;
    String personID;
    String lat;
    String lon;
    String country;
    String city;
    String eventType;
    String year;
    String message;
    boolean success;

    /**
     * Constructor to get parameters from event that will be used to create json body in handler
     * @param event The event that will be used to create the parameters
     */
    public EventResult(Event event)
    {
        //generate body of json object for event
    }
}
