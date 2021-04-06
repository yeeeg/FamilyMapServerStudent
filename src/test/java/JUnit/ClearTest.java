package JUnit;

import DAO.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClearTest
{
    Database db;
    @BeforeEach
    public void setUp() throws DataAccessException
    {
        db = new Database();
    }

    @Test
    public void clearPass() throws DataAccessException
    {
        //open connection with db
        db = new Database();
        db.openConnection();
        //clear db, should have no errors thrown, I realize this is a dumb test
        assertDoesNotThrow(()->db.clearTables());
        db.closeConnection(false);
    }
    @Test
    public void clearFail() throws DataAccessException
    {
        //clear db should fail because a connection to the db has not been established
        assertThrows(NullPointerException.class, ()->db.clearTables());
    }
}
