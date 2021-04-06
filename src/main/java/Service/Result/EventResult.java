package Service.Result;

import Model.*;

/**
 * Stores results from Event service
 */
public class EventResult
{
    String associatedUsername;
    String eventID;
    String personID;
    float latitude;
    float longitude;
    String country;
    String city;
    String eventType;
    Integer year;
    String message;
    boolean success;

    public EventResult(Event event, boolean success)
    {
        this.associatedUsername = event.getAssociatedUsername();
        this.eventID = event.getEventID();
        this.personID = event.getPersonID();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.country = event.getCountry();
        this.city = event.getCity();
        this.eventType = event.getEventType();
        this.year = event.getYear();
        this.success = success;
        message = null;
    }

    public EventResult(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }
}
