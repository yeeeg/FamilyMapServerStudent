package Handler;

import DAO.DataAccessException;
import Service.Login;
import Service.Request.LoginRequest;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Locale;

public class LoginHandler implements HttpHandler {
    Login login;
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        try
        {
            if (exchange.getRequestMethod().toUpperCase().equals("POST"))
            {
                //get http request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                //normally would need to check for authtoken but not in this handler

                //extract json string from http request
                //get request body input stream and read stream
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                //USER LOGIN
                LoginRequest loginRequest = new LoginRequest(reqData);
                login = new Login(loginRequest);
                loginRequest.createUserObject();
                login.loginUser();
                //END USER LOGIN

                //get response message
                OutputStream responseBody = exchange.getResponseBody();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //write response
                String json = login.gson.toJson(login.result);
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
        catch (IOException | DataAccessException e)
        {
            //FIXME:Changed from HTTP_INTERNAL_ERROR to BAD_REQUEST
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            OutputStream responseBody = exchange.getResponseBody();
            //write response
            String json = login.gson.toJson(login.result);
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
