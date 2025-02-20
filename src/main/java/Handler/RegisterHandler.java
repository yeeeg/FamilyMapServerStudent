package Handler;

import DAO.DataAccessException;
import Service.Fill;
import Service.Login;
import Service.Register;
import Service.Request.FillRequest;
import Service.Request.LoginRequest;
import Service.Request.RegisterRequest;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler implements HttpHandler
{
    Register register;

    /**
     * Handles calls to the /user/register api and all related operations
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

                //USER ADD

                //register user in database
                RegisterRequest registerRequest = new RegisterRequest(reqData);
                register = new Register(registerRequest);
                registerRequest.createUser();
                register.addUser();

                //create fill data for user
                FillRequest fillRequest = new FillRequest(registerRequest.user.getUsername(),
                        FillHandler.DEFAULT_GENERATIONS);
                Fill fill = new Fill(fillRequest);
                fill.doFill();

                //log user in
                LoginRequest loginRequest = new LoginRequest(registerRequest.user);
                Login login = new Login(loginRequest);
                login.loginUser();
                //assign authtoken from login to request.result
                register.result.setAuthtoken(login.result.getAuthtoken());

                //END USER ADD

                //get response message

                OutputStream responseBody = exchange.getResponseBody();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                //Write response
                String json = register.gson.toJson(register.result);
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
            String json = register.gson.toJson(register.result);
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
