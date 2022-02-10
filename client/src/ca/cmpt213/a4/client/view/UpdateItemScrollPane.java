package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.ConsumablesTrackerOptions;
import ca.cmpt213.a4.client.model.Consumable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Invokes many methods to fill the scrollpane with items for each listing option
 */
public class UpdateItemScrollPane {

    int width = 680;
    int height = 800;

    /**
     * The default constructor which allows for object creation
     */
    public UpdateItemScrollPane(){}

    /**
     * This class is responsible for creating new panels and adding them to listing scrollpanes
     * Also adds the remove button for each item in the scrollpane
     * @param programFrame the main program JFrame (needed for updating the GUI)
     * @param itemScrollPane the JScrollPane that holds the item panels for each listing option
     * @param itemPanel the child JPanel of the JScrollPane where each item panel is held
     * @param path a string that tells the method which listing option to show
     */
    public void updateScrollPane(JFrame programFrame, JScrollPane itemScrollPane, JPanel itemPanel, String path){
        itemPanel.removeAll();

        // Before the scrollpane is updated, sort the list beforehand
        ArrayList<Consumable> sortedList = ConsumablesTrackerOptions.getListingType(path);

        // Shows specified messages for when a certain listing option has no items to show
        if (sortedList.size() == 0){
            JPanel item = new JPanel();
            JLabel noItems = new JLabel();
            if (path.equalsIgnoreCase("listAll")){
                noItems = new JLabel("No items to show.");
            } else if (path.equalsIgnoreCase("listExpired")){
                noItems = new JLabel("No expired items to show.");
            } else if (path.equalsIgnoreCase("listUnexpired")){
                noItems = new JLabel("No unexpired items to show.");
            } else if (path.equalsIgnoreCase("listExpiring")){
                noItems = new JLabel("No items expiring in 7 days to show.");
            }

            item.add(noItems);

            itemPanel.add(item);
        }

        for (int i = 0; i < sortedList.size(); i++){
            // Depending on the type, it will display whether the item is food or drink
            String itemType;
            if (sortedList.get(i).getType() == 1){
                itemType = "(Food)";
            } else {
                itemType = "(Drink)";
            }

            // Each item will get its own panel to hold the item description and a remove button
            JPanel item = new JPanel();

            // Bordered title citation: https://docs.oracle.com/javase/tutorial/uiswing/components/border.html
            item.add(sortedList.get(i).getPanel(), BorderLayout.CENTER);
            item.setPreferredSize(new Dimension(width - 20, height - 650));
            item.setMaximumSize(new Dimension(width - 20, height - 650));
            TitledBorder itemTitle = BorderFactory.createTitledBorder("Item #" + Integer.toString(i + 1) + " " + itemType);
            item.setBorder(itemTitle);
            itemPanel.add(item);

            JButton removeButton = new JButton("Remove");
            // Each remove button will have an action listener that calls the removeItem method
            // The removeItem method required the main inputlist and the item's name to remove correct item
            int currentIndex = i;
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Consumable currentItem = sortedList.get(currentIndex);
                    boolean isRemoved = ConsumablesTrackerOptions.removeItem(currentItem);
                    if (isRemoved == true){
                        System.out.println(currentItem.getName() + " has been removed");
                        updateScrollPane(programFrame, itemScrollPane, itemPanel, path);
                    }
                }
            });
            item.add(removeButton, BorderLayout.EAST);
        }

        // Repaint and revalidate the frame to update the GUI during runtime
        itemPanel.validate();
        itemPanel.repaint();

        programFrame.validate();
        programFrame.repaint();
        programFrame.pack();
    }

}
