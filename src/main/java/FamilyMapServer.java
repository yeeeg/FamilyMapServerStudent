import Handler.*;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;

//server related functions
public class FamilyMapServer
{
    HttpServer server;
    public void startServer(int port) throws IOException
    {
        try
        {
            InetSocketAddress serverAddress = new InetSocketAddress(port);
            server = HttpServer.create(serverAddress, 10);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        server.createContext("/", new FileHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/event/", new EventHandler());
        server.createContext("/event", new EventsHandler());
        server.createContext("/person/", new PersonHandler());
        server.createContext("/person", new PersonsHandler());
        server.createContext("/fill/", new FillHandler());

        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private void registerHandlers(HttpServer server)
    {
    }

    public static void main(String[] args) throws IOException
    {
        FamilyMapServer server = new FamilyMapServer();
        //convert cmd arg into port number
        int portNum;
        try
        {
            portNum = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e)
        {
            portNum = 0;
            throw new IOException("Invalid input");
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            throw new IOException("No input");
        }
        new FamilyMapServer().startServer(portNum);
    }
}
