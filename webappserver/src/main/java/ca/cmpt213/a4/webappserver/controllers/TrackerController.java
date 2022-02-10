package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumablesTrackerOptions;
import ca.cmpt213.a4.webappserver.control.ImportExportList;
import ca.cmpt213.a4.webappserver.model.Consumable;

import ca.cmpt213.a4.webappserver.model.ConsumableJson;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class TrackerController {

    private ArrayList<Consumable> itemList = ImportExportList.importJson();
    private ArrayList<ConsumableJson> jsonList = new ArrayList<>();

    /**
     * For the client to check if the server is running
     * @return welcome message
     */
    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public String startMessage() {
        return "Server is up!\n";
    }

    /**
     * For the client to save the itemsList.json with the updates items
     * @return goodbye message
     */
    @GetMapping("/exit")
    @ResponseStatus(HttpStatus.OK)
    public String endMessage() {
        ImportExportList.exportJson(itemList);
        return "Goodbye, SpringBoot WebServer terminated. Items list has been saved.\n";
    }

    /**
     * For the client to request all consumable items
     * @return array of JSON objects
     */
    @GetMapping("/listAll")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ConsumableJson> getAllList(){
        return ConsumablesTrackerOptions.consumableJsonList(itemList, "all");
    }

    /**
     * For the client to request expired items
     * @return array of Json objects
     */
    @GetMapping("/listExpired")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ConsumableJson> getExpiredList(){
        return ConsumablesTrackerOptions.consumableJsonList(itemList, "expired");
    }
    /**
     * For the client to request unexpired items
     * @return array of Json objects
     */
    @GetMapping("/listNonExpired")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ConsumableJson> getUnexpiredList(){
        return ConsumablesTrackerOptions.consumableJsonList(itemList, "not expired");
    }

    /**
     * For the client to request expiring items
     * @return array of Json objects
     */
    @GetMapping("/listExpiringIn7Days")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ConsumableJson> getExpiringList(){
        return ConsumablesTrackerOptions.consumableJsonList(itemList, "expiring");
    }

    @GetMapping("/listAllFoodItems")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<ConsumableJson> getFoodsList(){
        return ConsumablesTrackerOptions.consumableJsonList(itemList, "foodItems");
    }

    /**
     * For the client to add a consumable item
     * @param consumable JSON object to be parsed and added
     * @return the consumable item added as a JSON object string to display
     */
    @PostMapping("/addItem")
    @ResponseStatus(HttpStatus.CREATED)
    public String createConsumableS(@RequestBody String consumable){
        System.out.println(consumable);
        ConsumablesTrackerOptions.addItem(itemList, consumable);
        return consumable;
    }

    /**
     * For the client to remove a consumable item
     * @param consumable JSON object to be parsed and removed
     */
    @PostMapping("/removeItem")
    @ResponseStatus(HttpStatus.CREATED)
    public void removeConsumable (@RequestBody Consumable consumable){
        ConsumablesTrackerOptions.removeItem(itemList, consumable);
    }

}
