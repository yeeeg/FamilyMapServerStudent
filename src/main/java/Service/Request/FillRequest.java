package Service.Request;

/**
 * Create the data to be filled in the db
 */
public class FillRequest
{
    int generations;
    String userName;

    public FillRequest(String userName, int generations)
    {
        this.userName = userName;
        this.generations = generations;
    }

    public String getUserName()
    {
        return userName;
    }
    public int getGenerations()
    {
        return generations;
    }
}
