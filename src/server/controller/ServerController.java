package server.controller;

import common.NetworkConstants;
import server.model.DedicatedServer;
import server.view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerController {
    private final View serverView;
    private ServerSocket server;
    private ArrayList<DedicatedServer> dedicatedServers;

    public ServerController(View serverView) {
        this.serverView = serverView;
        this.dedicatedServers = new ArrayList<>();
    }

    public void runServer() {
        try {
            this.server = new ServerSocket(NetworkConstants.PORT);

            // Constantly wait for new clients
            while (true) {
                // Accept a new client (blocking)
                Socket client = server.accept();

                // Start a dedicated server (Thread) for that client, adding it to the list
                DedicatedServer dedicatedServer = new DedicatedServer(serverView, client, dedicatedServers);
                dedicatedServers.add(dedicatedServer);
                new Thread(dedicatedServer).start();
            }
        } catch (IOException e) {
            System.out.println("ERROR: Server failed to initialize properly");
        }
    }
}
