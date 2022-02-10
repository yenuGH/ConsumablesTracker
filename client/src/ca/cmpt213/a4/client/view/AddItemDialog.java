package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.ConsumablesTrackerOptions;
import ca.cmpt213.a4.client.model.Consumable;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class that extends JDialog for adding items
 */
public class AddItemDialog extends JDialog {

    static int width = 680;
    static int height = 800;

    /**
     * The default constructor
     */
    public AddItemDialog(){}

    /**
     * This class creates a JDialog and fills it up with the fields required for adding a new item.
     * If required fields are left empty and/or numbers are negative, a warning message will pop up prompting for correct input
     * @param programFrame the main program JFrame (needed for updating the GUI)
     * @param itemScrollPane the JScrollPane that holds the item panels for each listing option
     * @param itemPanel the child JPanel of the JScrollPane where each item panel is held
     * @param path a string that tells the method which listing option to show
     */
    public void addItem(JFrame programFrame, JScrollPane itemScrollPane, JPanel itemPanel, String path){
        setTitle("Add an item");
        setSize(width, height);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        // Create a main panel using GridBagLayout that will hold components
        // Citation: https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
        JPanel addItemPanel = new JPanel();
        addItemPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridheight = 1;
        add(addItemPanel);

        // Create a combo box to select consumable type
        gbc.gridx = 0;
        gbc.gridy = 0;
        addItemPanel.add(new JLabel("Select Consumable Type: "), gbc);

        // Label for the volume/weight created now for action listener
        JLabel consumableMeasurementText = new JLabel("Weight: ");

        gbc.gridx = 1;
        gbc.gridy = 0;
        String itemTypes[] = {"Food", "Drink"};
        JComboBox consumableType = new JComboBox(itemTypes);

        //Set the default option for consumableType to Food so null isn't returned in Consumable
        consumableType.setSelectedItem("Food");

        consumableType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (consumableType.getSelectedItem() == "Food"){
                    consumableMeasurementText.setText("Weight: ");
                }
                if (consumableType.getSelectedItem() == "Drink"){
                    consumableMeasurementText.setText("Volume: ");
                }
            }
        });
        addItemPanel.add(consumableType, gbc);

        // Textfield for consumable name
        gbc.gridx = 0;
        gbc.gridy = 1;
        addItemPanel.add(new JLabel("Name: "), gbc);

        JTextField consumableName = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        addItemPanel.add(consumableName, gbc);

        // Textfield for consumable notes
        gbc.gridx = 0;
        gbc.gridy = 2;
        addItemPanel.add(new JLabel("Notes: "), gbc);

        JTextField consumableNotes = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        addItemPanel.add(consumableNotes, gbc);

        // Textfield for consumable price
        gbc.gridx = 0;
        gbc.gridy = 3;
        addItemPanel.add(new JLabel("Price: "), gbc);

        JTextField consumablePrice = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        addItemPanel.add(consumablePrice, gbc);

        // Textfield for consumable volume/weight
        // The JLabel for this is created with the consumable type JComboBox, allowing the proper usage of ActionListener
        gbc.gridx = 0;
        gbc.gridy = 4;
        addItemPanel.add(consumableMeasurementText, gbc);

        JTextField consumableMeasurement = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        addItemPanel.add(consumableMeasurement, gbc);

        // Date picker using LGoodDatePicker
        JLabel consumableDate = new JLabel("Expiry Date: ");
        gbc.gridx = 0;
        gbc.gridy = 5;
        addItemPanel.add(consumableDate, gbc);

        DatePicker datePicker = new DatePicker();
        gbc.gridx = 1;
        gbc.gridy = 5;
        addItemPanel.add(datePicker, gbc);
        datePicker.getDate();

        // Buttons for adding / cancelling
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    // Extract Consumable type from JComboBox
                    int type = 0;
                    if (consumableType.getSelectedItem().equals("Food")){
                        type = 1;
                    } else if (consumableType.getSelectedItem().equals("Drink")) {
                        type = 2;
                    }

                    // Extract information from components
                    String name = consumableName.getText();
                    String notes = consumableNotes.getText();
                    Double price = Double.parseDouble(consumablePrice.getText());
                    Double measurement = Double.parseDouble(consumableMeasurement.getText());
                    LocalDate date = datePicker.getDate();

                    // Pass into addItem in control package for data extraction and item adding
                    ConsumablesTrackerOptions.addItem(type,
                            name,
                            notes,
                            price,
                            measurement,
                            date);

                    UpdateItemScrollPane updateItemScrollPane = new UpdateItemScrollPane();
                    updateItemScrollPane.updateScrollPane(programFrame, itemScrollPane, itemPanel, path);

                    dispose();
                } catch (Exception error){
                    // If all else fails and somehow you manage to get this portion to run shrug
                    // This will occur if the user enters in incorrect values, presses add and is warned, but then proceeds to delete input in all fields and presses add
                    JOptionPane.showMessageDialog(null, "All text fields, except notes, and the expiry date cannot be left empty. Please also check that you have entered valid input.");
                    //JOptionPane.showMessageDialog(new JFrame(), "Information about the program's creator.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
        gbc.gridx = 1;
        gbc.gridy = 6;
        addItemPanel.add(addButton, gbc);

        // Basically what it is, disposes the JDialog window without adding a new item.
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 6;
        addItemPanel.add(cancelButton, gbc);

        // Repaint and validate to show the dialog and not a white canvas c:
        repaint();
        revalidate();

    }

}
