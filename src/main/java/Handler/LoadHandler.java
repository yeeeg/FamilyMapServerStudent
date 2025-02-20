package Handler;

import DAO.DataAccessException;
import Service.Clear;
import Service.Load;
import Service.Request.LoadRequest;
import Service.Request.LoginRequest;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Locale;

public class LoadHandler implements HttpHandler {
    Load load;

    /**
     * Handles calls to the /load api and all related operations
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
                //normally would need to check for authorization key but not in this case

                //extract json string from http request
                //get request body input stream and read stream
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                //LOAD
                //clear database
                Clear clear = new Clear();
                clear.clear();
                //load data
                LoadRequest loadRequest = new LoadRequest(reqData);
                loadRequest.createObjects();
                load = new Load(loadRequest);
                load.doLoad();
                //END LOAD

                //get response message

                OutputStream responseBody = exchange.getResponseBody();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write response
                String json = load.gson.toJson(load.result);
                writeString(json, responseBody);
                responseBody.close();
                exchange.close();

            }
            else
            {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        }
        catch (IOException | DataAccessException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            OutputStream responseBody = exchange.getResponseBody();
            //Write response
            String json = load.gson.toJson(load.result);
            writeString(json, responseBody);
            responseBody.close();
            exchange.close();

            System.out.println(e.toString());
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
