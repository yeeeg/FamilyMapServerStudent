package Service;
import DAO.EventDAO;
import Model.Event;
import Service.Result.*;
import Model.*;
import java.util.ArrayList;

/**
 * Handles both /event and /event/[eventID], depending on construction params
 */
public class GetEvents
{
    EventDAO db;
    EventsResult eventsResult;
    EventResult eventResult;
    /**
     * URL Path: /event
     *
     * Description: Returns ALL events for ALL family members of the current user.
     * The current user is determined from the provided auth token.
     */
    public GetEvents(String Auth_Token)
    {
        //for each event under user
        //find event and pass to EventResult
        //add EventResult to EventsResult
        //get each event from user specified with auth token
    }

    /**
     * URL Path: /event/[eventID]
     *
     * Example: /event/251837d7
     *
     * Description: Returns the single Event object with the specified ID.
     *
     * Errors: Invalid auth token, Invalid eventID parameter, Requested event does not belong
     * to this user, Internal server error
     *
     * @param eventID The ID of the specified event.
     */
    public GetEvents(String Auth_Token, String eventID)
    {
        //find event and pass to EventResult
        //do something to get specified event from DB
    }


}
