package Service.Result;

import Service.Clear;
import Service.Request.LoadRequest;

/**
 * Stores results of load service
 */
public class LoadResult
{
    String message;
    boolean success;

    public LoadResult(String message, boolean success)
    {
        this.message = message;
        this.success = success;
    }
}
