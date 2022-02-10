package ca.cmpt213.a4.client.control;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used to send a GET request to the server and receive a welcome message
 * This class is also used to send a GET request to the server to make it save the arraylist to a json file and exit
 */
public class PingWebServer {

    /**
     * Requests a welcome message from the server
     */
    public static void startServer() {
        try {
            // Citation: https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
            // Citation: https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html
            // Citation: https://www.baeldung.com/httpurlconnection-post
            URL url = new URL("http://localhost:8080/ping");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String inputStream;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((inputStream = bufferedReader.readLine()) != null){
                System.out.println(inputStream);
            }
        } catch (Exception e) {
            System.out.println("Could not ping server.");
            e.printStackTrace();
        }
    }

    /**
     * Requests an exit message from the server.
     * Tells the server to save the ArrayList to a json file
     */
    public static void exitServer() {
        try {
            // Citation: https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
            // Citation: https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html
            // Citation: https://www.baeldung.com/httpurlconnection-post
            URL url = new URL("http://localhost:8080/exit");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String inputStream;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((inputStream = bufferedReader.readLine()) != null){
                System.out.println(inputStream);
            }
        } catch (Exception e) {
            System.out.println("Could not ping server.");
            e.printStackTrace();
        }
    }
}
