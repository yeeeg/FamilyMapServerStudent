package Handler;

import DAO.DataAccessException;
import Service.GetEvents;
import Service.GetPersons;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;

public class PersonsHandler implements HttpHandler {
    GetPersons  getPersons;
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        try
        {
            if (exchange.getRequestMethod().toUpperCase().equals("GET"))
            {
                Headers reqHeaders = exchange.getRequestHeaders();

                //check auth token and personID
                List<String> auth = reqHeaders.get("Authorization");
                String providedAuth = auth.get(0);
                getPersons = new GetPersons();
                getPersons.getAssociatedUN(providedAuth);
                getPersons.doGet();

                //extract json string from http request
                //get request body input stream and read stream
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                //get response message
                OutputStream responseBody = exchange.getResponseBody();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write response
                String json = getPersons.gson.toJson(getPersons.result);
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
        catch (DataAccessException e)
        {
            //fixme
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            OutputStream responseBody = exchange.getResponseBody();
            //Write response
            String json = getPersons.gson.toJson(getPersons.result);
            writeString(json, responseBody);
            responseBody.close();
            exchange.close();

            e.printStackTrace();
        }
    }
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
    private void writeString(String str, OutputStream os) throws IOException
    {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }
}
