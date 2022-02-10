package ca.cmpt213.a4.client.control;

import ca.cmpt213.a4.client.model.Consumable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This class serves as a means of controlling how data is transferred between the model and view packages
 */
public class ConsumablesTrackerOptions {

    /**
     * Iterates the input list and returns an ArrayList of consumables according to the listing type string
     * @param path the path the client needs to complete the http url request
     * @return an array list with consumables according to the listing type
     */
    public static ArrayList<Consumable> getListingType(String path){
        ArrayList<Consumable> specificList = ImportJsonArray.importFromServer(path);
        return specificList;
    }

    /**
     * Takes in the main ArrayList of consumables and the consumable item to be deleted
     * Uses the object to locate and delete itself within the ArrayList
     * @param item the item to be deleted
     * @return a boolean (true if removal is successful, false if error has occurred)
     */
    public static boolean removeItem(Consumable item){

        boolean isRemoved = false;

        // Convert all parameters to type string
        String type = Integer.toString(item.getType());
        String name = item.getName();
        String notes = item.getNotes();
        String price = Double.toString(item.getPrice());
        String measurement = Double.toString(item.getMeasurement());

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = item.getExpiryDate().format(timeFormatter);

        // Citation: https://www.baeldung.com/java-9-http-client (HttpUrlConnection or cUrl in Java would not handle spaces)
        // Citation: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html
        String jsonElement = "{ \"type\": "+type+", \"name\": \""+name+"\", \"notes\": \""+notes+"\", \"price\": "+price+", \"measurement\": "+measurement+", \"expiryDate\": \""+date+"\" }";
        System.out.println("Sent to remove: " + jsonElement);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/removeItem")).POST(HttpRequest.BodyPublishers.ofString(jsonElement)).header("Content-Type","application/json").build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
            isRemoved = true;
        } catch (Exception e){
            e.printStackTrace();
            isRemoved = false;
        }

        return isRemoved;

    }

    /**
     * Responsible for taking in the fields for a consumable item
     * Once the consumable's parameters are taken in as a string value, it is sent to the server as a String JSON object
     * Server will deserialize and add it to the ArrayList of consumables
     * @param type the item's type tag
     * @param name the item's name
     * @param notes the item's notes
     * @param price the item's price
     * @param measurement the item's measurement
     * @param date the item's expiry date
     */
    public static void addItem(int type,
                               String name,
                               String notes,
                               Double price,
                               Double measurement,
                               LocalDate date ){

        // Convert all parameters to type string
        String typeString = Integer.toString(type);
        String priceString = Double.toString(price);
        String measurementString = Double.toString(measurement);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(timeFormatter);

        // Citation: https://www.baeldung.com/java-9-http-client (HttpUrlConnection or cUrl in Java would not handle spaces)
        // Citation: https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html
        String jsonElement = "{ \"type\": "+typeString+", \"name\": \""+name+"\", \"notes\": \""+notes+"\", \"price\": "+priceString+", \"measurement\": "+measurementString+", \"expiryDate\": \""+dateString+"\" }";
        System.out.println("Sent to add: " + jsonElement);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/addItem")).POST(HttpRequest.BodyPublishers.ofString(jsonElement)).header("Content-Type","application/json").build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
