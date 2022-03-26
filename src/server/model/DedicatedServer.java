package server.model;

import client.model.entities.player.Player;
import com.google.gson.Gson;
import common.ProtocolConstants;
import common.SocketData;
import server.view.View;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class DedicatedServer implements Runnable {
    private final View serverView;
    private final Socket socket;
    private final ArrayList<DedicatedServer> servers;

    public DedicatedServer(View serverView, Socket socket, ArrayList<DedicatedServer> dedicatedServers) {
        this.serverView = serverView;
        this.socket = socket;
        this.servers = dedicatedServers;
    }

    @Override
    public void run() {
        try {
            // Permanently read the next message the user sends
            SocketData socketData = null;
            do {
                socketData = (SocketData) new ObjectInputStream(this.socket.getInputStream()).readObject();
                Player playerData = new Gson().fromJson(socketData.getData(), Player.class);

                // Check for the action received
                switch (socketData.getAction()) {
                    case ProtocolConstants.PLAYER_CONNECTED ->
                            this.serverView.addConnectedPlayer(playerData);
                    case ProtocolConstants.PLAYER_MOVED -> {
                        this.serverView.addLogMessage(playerData.getUsername() + " has moved to [" +
                                playerData.getPosition().getX() + "," + playerData.getPosition().getY() + "]");
                    }
                    case ProtocolConstants.PLAYER_WON ->
                            this.serverView.addLogMessage(playerData.getUsername() + " has won.");
                    case ProtocolConstants.PLAYER_DIED ->
                            this.serverView.addLogMessage(playerData.getUsername() + " has died.");
                    case ProtocolConstants.PLAYER_DISCONNECTED ->
                            this.serverView.removeConnectedPlayer(playerData);
                }

                // Broadcast every message
                broadcastToOthers(socketData);
            } while (!socketData.getAction().equals(ProtocolConstants.PLAYER_DISCONNECTED));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Remove the dedicated server from the list and close the socket
        this.servers.remove(this);
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("ERROR: A player could not close socket communication properly");
        }
    }

    /**
     * Auxiliary method to send a message from this dedicated server to all others
     * @param socketData Data to be sent.
     */
    private void broadcastToOthers(SocketData socketData) {
        for (DedicatedServer dedicatedServer : servers) {
            if (dedicatedServer != this) {
                dedicatedServer.sendMessage(socketData);
            }
        }
    }

    /**
     * Method to send a message to this dedicated server from another
     * @param socketData Data to be sent.
     */
    private void sendMessage(SocketData socketData) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectOutputStream.writeObject(socketData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
