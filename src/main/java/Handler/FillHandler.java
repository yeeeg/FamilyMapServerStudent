package Handler;

import DAO.DataAccessException;
import Service.Fill;
import Service.Request.FillRequest;
import Service.Result.FillResult;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Locale;

public class FillHandler implements HttpHandler {
    public static final int DEFAULT_GENERATIONS = 4;
    Fill fill;

    /**
     * Handles calls to the /fill or /fill/[generations] api and all related operations
     * @param exchange The http request object
     * @throws IOException Signals issues with I/O
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        try
        {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST"))
            {
                //Get http request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                //no need for auth key here

                String url = exchange.getRequestURI().toString();
                //regex determines method based on url
                if (url.matches("/fill/.*/[0-9]*"))
                {
                    //parse url to strings
                    int i = url.lastIndexOf("/");
                    String username = url.substring(6, i);
                    String gen = url.substring(i+1);

                    //FILL
                    FillRequest fillRequest = new FillRequest(username, Integer.parseInt(gen));
                    fill = new Fill(fillRequest);
                    fill.doFill();
                    //END FILL

                    //get response message
                    OutputStream responseBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    //write response
                    String json = fill.gson.toJson(fill.result);
                    writeString(json, responseBody);
                    responseBody.close();
                    exchange.close();
                }
                //default generations
                else if (url.matches("/fill/.*"))
                {
                    String username = url.substring(6);
                    //FILL
                    FillRequest fillRequest = new FillRequest(username, DEFAULT_GENERATIONS);
                    fill = new Fill(fillRequest);
                    fill.doFill();
                    //END FILL

                    //get response message
                    OutputStream responseBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    //write response
                    String json = fill.gson.toJson(fill.result);
                    writeString(json, responseBody);
                    responseBody.close();
                    exchange.close();
                }
                else
                {
                    throw new IOException("Incorrect url structure provided");
                }
            }
        }
        catch (IOException | DataAccessException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            OutputStream responseBody = exchange.getResponseBody();
            //Write response
            String json = fill.gson.toJson(fill.result);
            writeString(json, responseBody);
            responseBody.close();
            exchange.close();
            e.printStackTrace();
        }
    }

    /**
     * Reads in the character stream from the http request body and
     * converts to string
     * @param is Input stream
     * @return Request body in string form
     * @throws IOException Issues with I/O
     */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /**
     * Writes string to output stream to be sent in response body
     * @param str String to write
     * @param os Output stream
     * @throws IOException Issues with I/O
     */
    private void writeString(String str, OutputStream os) throws IOException
    {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
