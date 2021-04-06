package JUnit;

import DAO.DataAccessException;
import DAO.Database;
import Model.LoadModel;
import Service.Load;
import Service.Login;
import Service.Request.LoadRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoadTest
{
    Gson gson;
    Reader reader;
    @BeforeEach
    public void setUp() throws DataAccessException, IOException {
        gson = new Gson();
        reader = Files.newBufferedReader(Paths.get("passoffFiles/LoadData.json"));
    }

    @Test
    public void doLoadPass() throws DataAccessException
    {
        //clear the database (would usually happen in LoadHandler)
        Database db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);

        //create new load request
        LoadRequest request = new LoadRequest("");
        request.loadModel = gson.fromJson(reader, LoadModel.class);
        Load load = new Load(request);

        //do the load, should not fail because database was cleared beforehand
        assertDoesNotThrow(()->load.doLoad());
    }
    @Test
    public void doLoadFail() throws DataAccessException
    {
        //don't clear the database this time
        LoadRequest request = new LoadRequest("");
        request.createObjects();
        Load load = new Load(request);

        //loading should throw error because request body was empty
        assertThrows(NullPointerException.class, ()->load.doLoad());
    }
}
