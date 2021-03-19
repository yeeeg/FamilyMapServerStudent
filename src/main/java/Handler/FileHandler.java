package Handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class FileHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        try
        {
            if (exchange.getRequestMethod().toUpperCase(Locale.ROOT).equals("GET"))
            {
                //need to come back and change a bit
                String url = exchange.getRequestURI().toString();
                if (url == null || url.equals("/"))
                {
                    url = "/index.html";
                }
                String filePath = "web" + url;
                File file = new File(filePath);

                if (file.exists())
                {
                    OutputStream response = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(file.toPath(), response);
                    response.flush();
                    response.close();
                }
                else
                {
                    String newFilePath = "web" + "//HTML" + "//404.html";
                    File badPath = new File(newFilePath);
                    OutputStream response = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    Files.copy(badPath.toPath(), response);
                    response.flush();
                    response.close();
                }
            }
            else
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }
        }
        catch (IOException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR,0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
