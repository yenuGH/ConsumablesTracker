package ca.cmpt213.a4.client.control;

import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.ConsumableFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
/**
 * This class is responsible for the importing and exporting of the Consumable ArrayList to a .json using GSON
 */
public class ImportJsonArray {

    private static final Gson gsonObject = new GsonBuilder().registerTypeAdapter(LocalDate.class,
            new TypeAdapter<LocalDate>() {
                @Override
                public void write(JsonWriter jsonWriter,
                                  LocalDate localDate) throws IOException {
                    jsonWriter.value(localDate.toString());
                }
                @Override
                public LocalDate read(JsonReader jsonReader) throws IOException {
                    return LocalDate.parse(jsonReader.nextString());
                }
            }).setPrettyPrinting().create();

    /**
     * Responsible for taking an ArrayList of Consumables and turning them into Food/Drink items
     * @param inputList the arraylist of raw consumable items
     * @return a list with the consumables items separated into food/drink items
     */
    public static ArrayList<Consumable> separateList(ArrayList<Consumable> inputList){
        // Take each consumable and separate them into each food type category
        ArrayList<Consumable> separatedList = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++){
            int itemType = inputList.get(i).getType();
            String itemName = inputList.get(i).getName();
            String itemNote = inputList.get(i).getNotes();
            double itemPrice = inputList.get(i).getPrice();
            double itemMeasurement = inputList.get(i).getMeasurement();
            LocalDate itemExpiryDate = inputList.get(i).getExpiryDate();

            Consumable newItem = ConsumableFactory.getInstance(itemType, itemName, itemNote, itemPrice, itemMeasurement, itemExpiryDate);
            separatedList.add(newItem);
        }
        return separatedList;
    }

    /**
     * Requests GET the webserver for a JSON array object
     * Uses Google's GSON to parse the JSON array into consumable items
     * @return a filled ArrayList of FoodItem objects
     */
    public static ArrayList<Consumable> importFromServer(String path){
        try {
            URL url = new URL("http://localhost:8080/" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Citation: https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
            // Citation: https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html
            StringBuilder stringBuilder = new StringBuilder();
            String inputStream;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((inputStream = bufferedReader.readLine()) != null){
                stringBuilder.append(inputStream);
            }

            // Uses substring to cut the string up until the beginning of the JSON array
            String stringBuilderString = stringBuilder.toString();
            String jsonArray = stringBuilderString.substring(stringBuilderString.indexOf("["));

            Type typeConsumable = new TypeToken<ArrayList<Consumable>>() {}.getType();
            ArrayList<Consumable> itemList = gsonObject.fromJson(jsonArray, typeConsumable);

            // Take each consumable and separate them into each food type category
            ArrayList<Consumable> separatedList = separateList(itemList);
            return separatedList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
