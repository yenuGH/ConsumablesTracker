package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.control.PingWebServer;
import ca.cmpt213.a4.client.model.Consumable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * The class that holds the main method creating the GUI
 */
public class ConsumablesTracker implements ActionListener {

    ArrayList<Consumable> itemList = new ArrayList<>();

    private JFrame programFrame;
    private JPanel mainPanel;
    private int width = 680;
    private int height = 800;

    private JPanel topButtonBar;
    private JPanel bottomButtonBar;

    private JScrollPane itemScrollPane;
    private JPanel itemPanel;

    private UpdateItemScrollPane updateItemScrollPane = new UpdateItemScrollPane();
    private String currentView;

    /**
     * Sets and creates the application's main JFrame
     */
    private void setProgramFrame(){
        // Create the frame of the program
        programFrame = new JFrame("Consumables Tracker");
        programFrame.setSize(width, height);
        programFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener windowListener = new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("Program has started.");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Program is closing.");
                PingWebServer.exitServer();
                programFrame.dispose();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        };
        programFrame.addWindowListener(windowListener);
        programFrame.setVisible(true);

        // Then configure the main panel that holds everything else
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        programFrame.add(mainPanel);

        // Panel to hold the button at the top
        topButtonBar = new JPanel();
        topButtonBar.setPreferredSize(new Dimension(width, height - 700));

        JButton allButton = new JButton("All");
        allButton.addActionListener(this);
        topButtonBar.add(allButton);

        JButton expiredButton = new JButton("Expired");
        expiredButton.addActionListener(this);
        topButtonBar.add(expiredButton);

        JButton unexpiredButton = new JButton("Not Expired");
        unexpiredButton.addActionListener(this);
        topButtonBar.add(unexpiredButton);

        JButton expiringButton = new JButton("Expiring in 7 Days");
        expiringButton.addActionListener(this);
        topButtonBar.add(expiringButton);

        mainPanel.add(topButtonBar, BorderLayout.NORTH);

        // Panel to hold the item list
        itemPanel = new JPanel();
        itemPanel.setVisible(true);
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.PAGE_AXIS));

        // Scroll pane for the panels
        itemScrollPane = new JScrollPane(itemPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        itemScrollPane.setPreferredSize(new Dimension(width, height - 200));
        mainPanel.add(itemScrollPane, BorderLayout.CENTER);

        // Adding an Add Item button to the bottom of the window
        bottomButtonBar = new JPanel();
        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(this);
        bottomButtonBar.add(addButton);
        bottomButtonBar.setPreferredSize(new Dimension(width, height - 700));
        mainPanel.add(bottomButtonBar, BorderLayout.SOUTH);
    }

    /**
     * Default constructor that creates the main JFrame window and buttons
     */
    public ConsumablesTracker() {
        // Ping the webserver to check that it is on
        PingWebServer.startServer();

        // Sets up the main GUI of the application
        setProgramFrame();
        currentView = "listAll";
        updateItemScrollPane.updateScrollPane(programFrame, itemScrollPane, itemPanel, currentView);

        // Sets all the panels to their preferred/maximum size
        programFrame.pack();
    }

    /**
     * As the class implements ActionListener, the following methods must be overridden for the specific buttons
     * @param e the object containing the ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("All")){
            currentView = "listAll";
            updateItemScrollPane.updateScrollPane(programFrame, itemScrollPane, itemPanel, "listAll");
        } else if (e.getActionCommand().equals("Expired")){
            currentView = "listExpired";
            updateItemScrollPane.updateScrollPane(programFrame, itemScrollPane, itemPanel, "listExpired");
        } else if (e.getActionCommand().equals("Not Expired")){
            currentView = "listNonExpired";
            updateItemScrollPane.updateScrollPane(programFrame, itemScrollPane, itemPanel, "listNonExpired");
        } else if (e.getActionCommand().equals("Expiring in 7 Days")){
            currentView = "listExpiringIn7Days";
            updateItemScrollPane.updateScrollPane(programFrame, itemScrollPane, itemPanel, "listExpiringIn7Days");
        } else if (e.getActionCommand().equals("Add Item")){
            AddItemDialog addItemDialog = new AddItemDialog();
            addItemDialog.addItem(programFrame, itemScrollPane, itemPanel, currentView);
        } else {
            System.out.println("Assignment4 was so pain, never again.");
        }
    }
}
