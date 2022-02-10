package ca.cmpt213.a4.client;

import ca.cmpt213.a4.client.view.ConsumablesTracker;

import javax.swing.*;

/**
 * The main class which has the method serving as the entry/starting point of the program
 */
public class Main {

    /**
     * Serves as the entry point for the whole program by invoking the main JFrame in ConsumablesTracker
     * @param args the stuff it makes you have
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConsumablesTracker();
            }
        });
    }

}
