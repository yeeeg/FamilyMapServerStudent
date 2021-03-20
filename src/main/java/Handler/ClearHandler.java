package Handler;

import DAO.DataAccessException;
import Service.Clear;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Locale;

public class ClearHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        Clear c = new Clear();
        try
        {
            if (exchange.getRequestMethod().toUpperCase().equals("POST"))
            {
                //Get http request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                //extract json string from http request
                //get request body input stream and read stream
                InputStream requestBody = exchange.getRequestBody();
                String requestData = readString(requestBody);

                //Clear database
                c.clear();
                //end clear

                OutputStream responseBody = exchange.getResponseBody();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //write response
                String json = c.gson.toJson(c.getResult());
                writeString(json, responseBody);
                responseBody.close();
                exchange.close();
            }
            else
            {
                //got something else instead of post
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        }
        catch (IOException |DataAccessException e)
        {
            //FIXME
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            OutputStream responseBody = exchange.getResponseBody();
            //write response
            String json = c.gson.toJson(c.getResult());
            writeString(json, responseBody);
            responseBody.close();
            exchange.close();

            System.out.println(e.toString());
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
