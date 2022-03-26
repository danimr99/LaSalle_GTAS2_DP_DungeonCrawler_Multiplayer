package client.controller;

import client.model.entities.player.Player;
import client.view.View;
import com.google.gson.Gson;
import common.ProtocolConstants;
import common.SocketData;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class RealTimeListener implements Runnable {
    private final View clientView;
    private final Socket server;
    private boolean exit;


    public RealTimeListener(View clientView, Socket server) {
        this.clientView = clientView;
        this.server = server;
        this.exit = false;
    }

    @Override
    public void run() {
        // Read messages until we want to stop / an exception is thrown
        while (!exit) {
            try {
                SocketData socketData = (SocketData) new ObjectInputStream(this.server.getInputStream()).readObject();
                Player connectedPlayer = new Gson().fromJson(socketData.getData(), Player.class);

                /* Manage every broadcast received from server */
                switch (socketData.getAction()) {
                    case ProtocolConstants.PLAYER_CONNECTED -> {
                        /* Add a new player to view */
                        SwingUtilities.invokeLater(() -> this.clientView.addConnectedPlayer(connectedPlayer));
                    }
                    case ProtocolConstants.PLAYER_MOVED -> {
                        /* Update player's position to view */
                        SwingUtilities.invokeLater(() -> this.clientView.updateConnectedPlayerPosition(connectedPlayer));
                    }
                    case ProtocolConstants.PLAYER_DISCONNECTED -> {
                        /* Remove player */
                        SwingUtilities.invokeLater(() -> this.clientView.removeConnectedPlayer(connectedPlayer));
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                exit = true;
            }
        }
    }
}
