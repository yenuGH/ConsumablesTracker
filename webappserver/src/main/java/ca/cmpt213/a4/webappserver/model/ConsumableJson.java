package ca.cmpt213.a4.webappserver.model;

import java.time.LocalDate;

/**
 * This allows for the webserver to properly post each consumable item as a JSON element
 * Allows the client to retrieve the information of a consumable item
 */
public class ConsumableJson {

    private int type;
    private String name;
    private String notes;
    private double price;
    private double measurement;
    private LocalDate expiryDate;

    public ConsumableJson(Consumable consumable){
        this.type = consumable.getType();
        this.name = consumable.getName();
        this.notes = consumable.getNotes();
        this.price = consumable.getPrice();
        this.measurement = consumable.getMeasurement();
        this.expiryDate = consumable.getExpiryDate();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }


}
