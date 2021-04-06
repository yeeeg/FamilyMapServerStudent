package Service.Result;

import DAO.Database;
import Service.Request.*;

/**
 * Stores results of fill service
 */
public class FillResult
{
    String message;
    boolean success;
    public FillResult(String message, boolean success)
    {
        this.success = success;
        this.message = message;
    }
}
