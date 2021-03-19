package Service.Result;

import Service.Clear;
import Service.Request.LoadRequest;

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
