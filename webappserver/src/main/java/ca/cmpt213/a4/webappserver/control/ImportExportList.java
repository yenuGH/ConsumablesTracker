package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.ConsumableFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ImportExportList {

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
     * Reads in an ArrayList of type FoodItem from a json file
     * Distinguishes different types of Consumable items with the itemType tag
     * Uses other getters to create appropriate Consumable item and adds it to a new ArrayList that is returned
     * @return a filled ArrayList of FoodItem objects
     */
    public static ArrayList<Consumable> importJson(){
        // Citation: https://attacomsian.com/blog/gson-read-json-file
        ArrayList<Consumable> itemList;
        // Creating a reader object for importing
        try {
            Reader jsonReader = Files.newBufferedReader(Paths.get("itemsList.json"));

            // Converting the entries in the json file to FoodItem objects to input into ArrayList
            itemList = gsonObject.fromJson(jsonReader, new TypeToken<ArrayList<Consumable>>() {}.getType());

            jsonReader.close();

            ArrayList<Consumable> separatedList = new ArrayList<>();
            for (int i = 0; i < itemList.size(); i++){
                int itemType = itemList.get(i).getType();
                String itemName = itemList.get(i).getName();
                String itemNote = itemList.get(i).getNotes();
                double itemPrice = itemList.get(i).getPrice();
                double itemMeasurement = itemList.get(i).getMeasurement();
                LocalDate itemExpiryDate = itemList.get(i).getExpiryDate();

                Consumable newItem = ConsumableFactory.getInstance(itemType, itemName, itemNote, itemPrice, itemMeasurement, itemExpiryDate);
                separatedList.add(newItem);
            }

            itemList.clear();
            return separatedList;

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    /**
     * Exports all FoodItem objects into a json file
     * @param inputList food item list we are to export
     */
    public static void exportJson(ArrayList<Consumable> inputList){
        // Citation: https://attacomsian.com/blog/gson-write-json-file
        try {
            // Create a writer object to write to the json file
            Writer jsonWriter = new FileWriter("itemsList.json");

            // Converting ArrayList to json file and exporting
            gsonObject.toJson(inputList, jsonWriter);

            jsonWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
