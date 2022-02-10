package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.ConsumableFactory;
import ca.cmpt213.a4.webappserver.model.ConsumableJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class serves as a means of controlling how data is transferred between the model and view packages
 */
public class ConsumablesTrackerOptions {

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
     * Uses selection sort to compare times and sort the ArrayList by oldest expiry date first
     * @param inputList the unsorted item list
     * @return a new ArrayList with items sorted by oldest expiry date
     */
    public static ArrayList<Consumable> sortList(ArrayList<Consumable> inputList){
        // Using selection sort, return a sorted ArrayList (sorted by oldest item first)

        for (int i = 0; i < inputList.size(); i++){
            int minIndex = i;
            for (int j = i + 1; j < inputList.size(); j++){
                if (inputList.get(j).compareTo(inputList.get(minIndex)) == 0){
                    minIndex = j;
                }
            }
            Consumable tempItem = inputList.get(minIndex);
            inputList.set(minIndex, inputList.get(i));
            inputList.set(i, tempItem);
        }

        return inputList;
    }

    /**
     * Iterates the input list and returns an ArrayList of consumables according to the listing type string
     * @param inputList the original list of consumable items
     * @param listingType how the user wants the items to be listed
     * @return an array list with consumables according to the listing type
     */
    public static ArrayList<Consumable> iterateList(ArrayList<Consumable> inputList, String listingType){

        ArrayList<Consumable> sortedList = sortList(inputList); // Sort the list
        ArrayList<Consumable> specificList = new ArrayList<>(); // List to add consumable items to
        LocalDate currentTime = LocalDateTime.now().toLocalDate(); // Grabbing the machine's current time

        // Iterate through the list
        for (int i = 0; i < sortedList.size(); i++){
            if (listingType.equalsIgnoreCase("all")){ // If user wants to display all items
                specificList.add(sortedList.get(i));
            } else if (listingType.equalsIgnoreCase("expired")){ // If user wants to display expired items
                if (sortedList.get(i).getExpiryDate().isBefore(currentTime)){
                    specificList.add(sortedList.get(i));
                }
            } else if (listingType.equalsIgnoreCase("not expired")){ // If user wants to display unexpired items
                if (sortedList.get(i).getExpiryDate().isAfter(currentTime) || sortedList.get(i).getExpiryDate().equals(currentTime)){
                    specificList.add(sortedList.get(i));
                }
            } else if (listingType.equalsIgnoreCase("expiring")){ // If user wants to display items expiring in a week
                LocalDate expiryDate = sortedList.get(i).getExpiryDate();
                long daysToExpiry = currentTime.until(expiryDate, ChronoUnit.DAYS);
                int daysToExpiryInt = (int) daysToExpiry; // converting from long to int
                if (daysToExpiryInt < 8 && daysToExpiryInt >= 0){
                    specificList.add(sortedList.get(i));
                }
            }
        }

        return specificList;
    }

    /**
     * Takes each consumable item and turns it into an object that can be displayed in JSON format with GetMapping
     * @param inputList the arraylist containing consumable items
     * @param listingType the listing type the webserver receives
     * @return a sorted list of type ConsumableJson so the webserver can properly display it
     */
    public static ArrayList<ConsumableJson> consumableJsonList(ArrayList<Consumable> inputList, String listingType){
        ArrayList<Consumable> sortedList = iterateList(inputList, listingType);
        ArrayList<ConsumableJson> sortedListJson = new ArrayList<>();
        AtomicLong nextId = new AtomicLong();
        for (int i = 0; i < sortedList.size(); i++) {
            ConsumableJson consumableJson = new ConsumableJson(sortedList.get(i));
            sortedListJson.add(consumableJson);
        }
        return sortedListJson;
    }

    /**
     * Takes in the arraylist and a jsonobject of type string and uses GSON to deserialize it
     * Uses the custom object given from assignment2 to deal with LocalDateTime
     * Citation: https://attacomsian.com/blog/gson-read-json-file (for deserializing)
     * @param itemList the arraylist of items
     * @param consumableString
     */
    public static void addItem(ArrayList<Consumable> itemList, String consumableString){
        Type consumableType = new TypeToken<Consumable>() {}.getType();
        Consumable consumable = gsonObject.fromJson(consumableString, consumableType);

        int type = consumable.getType();
        String name = consumable.getName();
        String notes = consumable.getNotes();
        double price = consumable.getPrice();
        double measurement = consumable.getMeasurement();
        LocalDate expiryDate = consumable.getExpiryDate();

        Consumable typedConsumable = ConsumableFactory.getInstance(type, name, notes, price, measurement, expiryDate);

        itemList.add(typedConsumable);
        System.out.println(typedConsumable.getName() + " added.");
    }

    /**
     * Takes in a new consumable object and uses its getters to find the proper object within the itemList
     * Once found, takes that object and uses itself to delete itself from the itemList
     * @param itemList the arraylist of items
     * @param consumable the item to be deleted
     */
    public static void removeItem(ArrayList<Consumable> itemList, Consumable consumable){
        // As we are creating a new object and using that to remove
        // We must compare each parameter to find the correct item as we cannot use the object itself to remove
        // They are not the same object!
        for (int i = 0; i < itemList.size(); i++){
            if (itemList.get(i).getName().equals(consumable.getName())){
                if (itemList.get(i).getNotes().equals(consumable.getNotes()) &&
                    itemList.get(i).getPrice() == consumable.getPrice() &&
                    itemList.get(i).getMeasurement() == consumable.getMeasurement()){
                    // We parse the expiry dates with formatting to type Strings to compare them
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String targetItemDate = itemList.get(i).getExpiryDate().format(formatter);
                    String inputItemDate = consumable.getExpiryDate().format(formatter);
                    if (targetItemDate.equalsIgnoreCase(inputItemDate)){
                        // If all parameters match, remove the item using itself and return to exit loop
                        System.out.println("Removed: " + itemList.get(i).getName());
                        itemList.remove(itemList.get(i));
                    }
                }
            }
        }
    }

}
