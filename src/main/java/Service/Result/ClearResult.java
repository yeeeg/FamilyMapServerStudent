package Service.Result;
import java.sql.*;
import DAO.*;

/**
 * Object to store result of clear
 */
public class ClearResult
{
    String message;
    public boolean success;

    public String getResult()
    {
        return message;
    }

    public void setResult(String result)
    {
        this.message = result;
    }
}
