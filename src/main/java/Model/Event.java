package Model;

import java.util.Objects;

public class Event
{
    String eventID;
    String associatedUsername;
    String personID;
    Float latitude;
    Float longitude;
    String country;
    String city;
    String eventType;
    Integer year;

    public Event(Event e)
    {
        this.latitude = e.getLatitude();
        this.longitude = e.getLongitude();
        this.country = e.getCountry();
        this.city = e.getCity();
    }

    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year)
    {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public Event()
    {

    }

    public void updateEvent(String eventID, String username, String personID, String eventType, int year)
    {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID()
    {
        return eventID;
    }

    public void setEventID(String eventID)
    {
        this.eventID = eventID;
    }

    public String getAssociatedUsername()
    {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername)
    {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID()
    {
        return personID;
    }

    public void setPersonID(String personID)
    {
        this.personID = personID;
    }

    public float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    public float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(float longitude)
    {
        this.longitude = longitude;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getEventType()
    {
        return eventType;
    }

    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    public int getYear()
    {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) && Objects.equals(associatedUsername, event.associatedUsername) && Objects.equals(personID, event.personID) && Objects.equals(latitude, event.latitude) && Objects.equals(longitude, event.longitude) && Objects.equals(country, event.country) && Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType) && Objects.equals(year, event.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }

    public void setYear(int year)
    {
        this.year = year;
    }
}
