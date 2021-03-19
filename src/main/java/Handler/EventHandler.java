package Handler;

import DAO.DataAccessException;
import Service.GetEvent;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;

public class EventHandler implements HttpHandler {
    GetEvent getEvent;
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        try
        {
            if (exchange.getRequestMethod().toUpperCase().equals("GET"))
            {
                //Get http request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                String url = exchange.getRequestURI().toString();

                //get eventID from url
                int i = url.lastIndexOf("/");
                String eventID = url.substring(7);

                List<String> auth = reqHeaders.get("Authorization");
                String providedAuth = auth.get(0);
                getEvent = new GetEvent(eventID);


                //extract json string from http request
                //get request body input stream and read stream
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

            }
        }
        catch (IOException e)
        {

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
