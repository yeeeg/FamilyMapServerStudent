package JUnit;

import Service.Fill;
import Service.Request.FillRequest;
import Service.Request.RegisterRequest;
import Service.Result.RegisterResult;
import client.Client;
import client.ServerConnectionException;
import logs.InitLogs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.logging.Logger;

public class FillTest
{
    private static final Logger logger;
    private static boolean displayCurrentTest = true;

    static {
        InitLogs.init();
        logger = Logger.getLogger(Client.class.getName());
    }

    @Test
    @DisplayName("Death Date Test")
    public void testDeathDate(TestInfo testInfo)
    {
        printTestName(testInfo);
        FillRequest fillRequest = new FillRequest("username", 4);
        Fill fill = new Fill(fillRequest);
    }

    /**
     * Prints the test name
     *
     * @param testInfo The name of the test
     */
    private void printTestName(TestInfo testInfo) {
        if (displayCurrentTest) System.out.println("Running " + testInfo.getDisplayName() + "...");
        logger.info("Running " + testInfo.getDisplayName() + "...");
    }

}
