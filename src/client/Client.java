package client;

import client.controller.DirectionButtonsController;
import client.controller.RealTimeListener;
import client.model.dao.MapDAO;
import client.model.entities.player.Player;
import client.model.map.GameMap;
import client.view.UsernameDialog;
import client.view.View;
import common.NetworkConstants;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private final GameMap map;
    private Player player;
    private View clientView;

    public Client(MapDAO mapDAO) {
        this.map = new GameMap(mapDAO.getMaxCellsX(), mapDAO.getMaxCellsY(), mapDAO.getMapFromFile());
    }

    /**
     * Function to create a new {@link View} to play for the client.
     * @param server A {@link Socket} to connect with the server.
     */
    private void createGameForNewPlayer(Socket server) {
        SwingUtilities.invokeLater(() -> {
            /* Display a modal to ask for a username */
            UsernameDialog usernameDialog = new UsernameDialog();

            /* Create a new player with the introduced username */
            this.player = new Player(usernameDialog.getUsername(),
                    map.getPositionByCell(GameMap.START_PLAYER_CELL).get(0));

            /* Create a view for the client */
            this.clientView = new View(map, player, server);

            /* Create a controller for the game direction buttons and attach it to view */
            DirectionButtonsController directionButtonsController = new DirectionButtonsController(clientView);
            clientView.addActionListener(directionButtonsController);

            RealTimeListener realTimeListener = new RealTimeListener(clientView, server);
            new Thread(realTimeListener).start();

            /* Display the client view */
            clientView.setVisible(true);
        });
    }

    public static void main(String[] args) {
        try {
            // Open the connection with the server
            Socket server = new Socket(NetworkConstants.IP, NetworkConstants.PORT);

            Client client = new Client(new MapDAO());
            client.createGameForNewPlayer(server);
        } catch (IOException e) {
            System.out.println("ERROR: Client cannot play due to a failure while connecting to the server.");
        }
    }
}
