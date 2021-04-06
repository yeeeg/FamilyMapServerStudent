package Handler;

import DAO.DataAccessException;
import Service.GetPerson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

public class PersonHandler implements HttpHandler {
    GetPerson getPerson;
    /**
     * Handles calls to the /person/[personID] api and all related operations
     * @param exchange The http request object
     * @throws IOException Signals issues with I/O
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        try
        {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET"))
            {
                //get http request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                String url = exchange.getRequestURI().toString();

                //get personID from url
                String personID = url.substring(8);

                //check auth token and personID
                List<String> auth = reqHeaders.get("Authorization");
                String providedAuth = auth.get(0);
                getPerson = new GetPerson(personID);
                getPerson.checkAuth(providedAuth);

                //extract json string from http request
                //get request body input stream and read stream
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                //get response message
                OutputStream responseBody = exchange.getResponseBody();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write response
                String json = getPerson.gson.toJson(getPerson.result);
                writeString(json, responseBody);
                responseBody.close();
                exchange.close();

            }
            else
            {
                //got something else instead of POST
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        }
        catch (IOException | DataAccessException e)
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            OutputStream responseBody = exchange.getResponseBody();
            //Write response
            String json = getPerson.gson.toJson(getPerson.result);
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
