package ca.cmpt213.a4.client.model;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A subclass that extends Consumable
 */
public class FoodItem extends Consumable{

    /**
     * The default constructor
     * @param itemType whether the item is a drink or food
     * @param itemName the name of the item
     * @param itemNotes extra notes for the item
     * @param itemPrice the price of the item
     * @param itemMeasurement the measurement of the item
     * @param itemExpiryDate the expiry date of the item
     */
    public FoodItem(int itemType, String itemName, String itemNotes, double itemPrice, double itemMeasurement, LocalDate itemExpiryDate) {
        super(itemType, itemName, itemNotes, itemPrice, itemMeasurement, itemExpiryDate);
    }

    /**
     * The specific consumable item's panel which shows each field per line
     * Later used to be added to the JScrollPane for listing options
     * @return JPanel is the item's information panel
     */
    @Override
    public JPanel getPanel(){
        JPanel foodPanel = new JPanel();
        foodPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridheight = 1;

        JLabel itemType = new JLabel("Name: " + name);
        gbc.gridy = 0;
        foodPanel.add(itemType, gbc);

        JLabel itemNotes = new JLabel("Notes: " + notes);
        gbc.gridy = 1;
        foodPanel.add(itemNotes, gbc);

        double roundedPrice = Math.round(price * 100.0)/100.0;
        JLabel itemPrice = new JLabel("Price: " + Double.toString(roundedPrice));
        gbc.gridy = 2;
        foodPanel.add(itemPrice, gbc);

        JLabel itemMeasurement = new JLabel("Weight: " + Double.toString(measurement));
        gbc.gridy = 3;
        foodPanel.add(itemMeasurement, gbc);

        // Citations: https://mkyong.com/java8/java-8-how-to-format-localdatetime/
        // Creating an object of DateTimeFormatter to format the time properly
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Formatting time with DateTimeFormatter object to only display year-month-day
        String formattedTime = expiryDate.format(timeFormatter);

        JLabel itemExpiry = new JLabel("Expiry Date: " + formattedTime);
        gbc.gridy = 4;
        foodPanel.add(itemExpiry, gbc);

        JLabel itemTimeToExpire = new JLabel(isExpired());
        gbc.gridy = 5;
        foodPanel.add(itemTimeToExpire, gbc);

        return foodPanel;
    }

    /**
     * Using @Override on toString(), we print out the item's information when passed into System.out.print(ln);
     * @return nothing (toString() is overridden)
     */
    @Override
    public String toString(){
        // Citations: https://mkyong.com/java8/java-8-how-to-format-localdatetime/

        // Creating an object of DateTimeFormatter to format the time properly
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Formatting time with DateTimeFormatter object to only display year-month-day
        String formattedTime = expiryDate.format(timeFormatter);

        double roundedPrice = Math.round(price * 100.0)/100.0;

        return "This is a food item." +
                "\nItem Name: " + name +
                "\nNotes: " + notes +
                "\nPrice: " + roundedPrice +
                "\nWeight: " + measurement +
                "\nExpiry Date: " + formattedTime +
                isExpired() + "\n";
    }

}
