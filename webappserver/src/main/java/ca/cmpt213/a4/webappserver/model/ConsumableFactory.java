package ca.cmpt213.a4.webappserver.model;

import java.time.LocalDate;

/**
 * The class that is responsible for returning the proper instance of a consumable item to the calling object
 */
public class ConsumableFactory {

    // Citation: https://www.tutorialspoint.com/design_pattern/factory_pattern.htm

    /**
     * The method that returns the specific consumable instance
     * @param itemType the item's type tag
     * @param itemName the item's name
     * @param itemNotes the item's notes
     * @param itemPrice the item's price
     * @param itemMeasurement the item's measurement
     * @param itemExpiryDate the item's expiry date
     * @return the specific consumable item with the proper information
     */
    public static Consumable getInstance(int itemType, String itemName, String itemNotes, double itemPrice, double itemMeasurement, LocalDate itemExpiryDate){
        if (itemType == 1) {
            // Food item
            return new FoodItem(itemType, itemName, itemNotes, itemPrice, itemMeasurement, itemExpiryDate);
        }
        if (itemType == 2){
            // Drink item
            return new DrinkItem(itemType, itemName, itemNotes, itemPrice, itemMeasurement, itemExpiryDate);
        }
        else {
            return null; // If somehow itemType does not contain a 1 or 2, return a null item
        }
    }

}
