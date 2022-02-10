package ca.cmpt213.a4.webappserver.model;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Is the superclass for food/drink consumable items
 * Implements the Comparable interface to allow comparison of expiry dates
 */
public class Consumable implements Comparable<Consumable>{

    // These variables are protected as subclasses (Food/Drink) need them for their implementation of toString()
    protected int type;
    protected String name;
    protected String notes;
    protected double price;
    protected double measurement;
    protected LocalDate expiryDate;

    /**
     * Just a constructor c:
     */
    public Consumable(){}

    /**
     * The default constructor
     * @param itemType whether the item is a drink or food
     * @param itemName the name of the item
     * @param itemNotes extra notes for the item
     * @param itemPrice the price of the item
     * @param itemMeasurement the measurement of the item
     * @param itemExpiryDate the expiry date of the item
     */
    public Consumable(int itemType, String itemName, String itemNotes, double itemPrice, double itemMeasurement, LocalDate itemExpiryDate){
        this.type = itemType;
        this.name = itemName;
        this.notes = itemNotes;
        this.price = itemPrice;
        this.measurement = itemMeasurement;
        this.expiryDate = itemExpiryDate;
    }

    public JPanel getPanel() { return null; }

    /**
     * Checks if it is expired or not, checks how many days until expiry or how many days it has been expired
     * Protected as the subclasses need access to this method
     * @return a string literal with the formatted expiration information
     */
    protected String isExpired(){
        // Citations: https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html

        int whichCase;

        LocalDate currentTime = LocalDateTime.now().toLocalDate();

        if (currentTime.isBefore(expiryDate)){
            whichCase = 1; // Not expired
        }
        else {
            whichCase = 2; // Is expired
        }

        if (currentTime.equals(expiryDate)){
            return ("\nThis item will expire today.");
        }
        switch (whichCase){
            case 1:
                long daysToExpiry = currentTime.until(expiryDate, ChronoUnit.DAYS);
                int daysToExpiryInt = (int) daysToExpiry; // Converting from long to int
                return("This item will expire in " + daysToExpiryInt + " day(s).");
            case 2:
                long daysExpired = expiryDate.until(currentTime, ChronoUnit.DAYS);
                int daysExpiredInt = (int) daysExpired; // Converting from long to int
                return("This item is expired for " + daysExpiredInt + " day(s).");
        }
        return "";
    }

    /**
     * getter for the item's type
     * @return the item's type
     */
    public int getType() { return type; }

    /**
     * getter for the item's name
     * @return the item's name
     */
    public String getName(){ return name; }

    /**
     * getter for item's notes
     * @return the item's notes
     */
    public String getNotes() { return notes; }

    /**
     * getter for item's price
     * @return the item's price
     */
    public double getPrice() { return price; }

    /**
     * getter for item's measurement
     * @return the item's measurement
     */
    public double getMeasurement() { return measurement; }

    /**
     * getter for the item's expiry date
     * @return the item's expiry date
     */
    public LocalDate getExpiryDate(){ return expiryDate; }

    /**
     * setter for the item's type
     * @param type the inputted value
     */
    public void setType(int type) { this.type = type; }

    /**
     * setter for the item's name
     * @param name the inputted name
     */
    public void setName(String name) { this.name = name; }

    /**
     * setter for the item's notes
     * @param notes the inputted notes
     */
    public void setNotes(String notes) { this.notes = notes; }

    /**
     * setter for the item's price
     * @param price the inputted price
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * setter for the item's measurement
     * @param measurement the inputted measurement
     */
    public void setMeasurement(double measurement) { this.measurement = measurement; }

    /**
     * setter for the item's expiry date
     * @param expiryDate
     */
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    /**
     * Overridden compareTo method from Compareable interface
     * Compares the Consumable object's expiry date with the parameter Consumable object
     * @param consumable the Consumable object to compare expiry dates with
     * @return 0 if the current object expires first, 1 if otherwise
     */
    @Override
    public int compareTo(Consumable consumable){
        // We compare the current object's expiry date to the parameter's expiry date
        if (expiryDate.isBefore(consumable.getExpiryDate())){ // If the parameter's expiry date is after the current consumable item's expiry date
            return 0;
        } else { // If the parameter's expiry date is before the current consumable item's expiry date
            return 1;
        }
    }

}
