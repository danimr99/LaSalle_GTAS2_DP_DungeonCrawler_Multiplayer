package server.view;

import client.model.entities.player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JFrame {
    private ArrayList<JLabel> players;
    private JPanel log;
    private JPanel connectedPlayers;

    public View() {
        this.players = new ArrayList<>();

        this.configureWindow();
        this.renderLog();
        this.renderConnectedPlayers();
    }

    /**
     * Function to configure basic settings for the {@link server.view.View}.
     */
    private void configureWindow() {
        this.setLayout(new GridLayout(1, 2));
        this.setTitle("World of Crawler - Server");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(600, 400));
        this.setLocationRelativeTo(null);
    }

    /**
     * Function to render the log register side.
     */
    private void renderLog() {
        /* Configure the panel to display logs */
        this.log = new JPanel();
        BoxLayout boxLayout = new BoxLayout(this.log, BoxLayout.Y_AXIS);
        this.log.setLayout(boxLayout);

        /* Set a title decorated with a border */
        this.log.setBorder(BorderFactory.createTitledBorder("Log"));

        /* Add a JScrollPane for the logs' list to the view */
        this.add(new JScrollPane(this.log, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
    }

    /**
     * Function to render the list of connected players.
     */
    private void renderConnectedPlayers() {
        /* Configure the panel to display connected players */
        this.connectedPlayers = new JPanel();
        BoxLayout boxLayout = new BoxLayout(this.connectedPlayers, BoxLayout.Y_AXIS);
        this.connectedPlayers.setLayout(boxLayout);

        /* Set a title decorated with a border */
        this.connectedPlayers.setBorder(BorderFactory.createTitledBorder("Connected players"));

        /* Add a JScrollPane for the connected players' list to the view */
        this.add(new JScrollPane(this.connectedPlayers, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
    }

    /**
     * Function to add log messages.
     *
     * @param message Log message.
     */
    public void addLogMessage(String message) {
        JLabel logMessage = new JLabel(message);

        this.log.add(logMessage);
        this.log.revalidate();
    }

    /**
     * Function to add a player as connected.
     * @param playerData Data of the player that has just connected.
     */
    public void addConnectedPlayer(Player playerData) {
        /* Set up player label */
        JLabel player = new JLabel(playerData.getUsername());

        /* Add new player to list */
        this.players.add(player);

        /* Add label to view and update it */
        this.connectedPlayers.add(player);
        this.connectedPlayers.revalidate();

        /* Register a new log connection */
        this.addLogMessage(playerData.getUsername() + " has connected.");
    }

    public void removeConnectedPlayer(Player playerData) {
        /* Remove JLabel with the username of the disconnected player */
        this.players.removeIf(label -> label.getText().equals(playerData.getUsername()));

        /* Update list of connected players */
        this.connectedPlayers.removeAll();
        this.players.forEach(player -> this.connectedPlayers.add(player));
        this.connectedPlayers.revalidate();
        this.connectedPlayers.repaint();

        /* Register a new log disconnection */
        this.addLogMessage(playerData.getUsername() + " has disconnected.");
    }
}
