package Service.Result;

import Model.Event;
import java.util.ArrayList;

/**
 * Only used when /event is being used instead of /event/eventID
 */
public class EventsResult
{
    ArrayList<Event> events;

    /**
     * Add object to array of objects
     * @param event
     */
    public void addObject(Event event)
    {
        events.add(event);
    }
}
