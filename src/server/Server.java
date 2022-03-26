package server;

import server.controller.ServerController;
import server.view.View;

public class Server {

    public static void main(String[] args) {
        // Create a server view
        View serverView = new View();
        serverView.setVisible(true);

        // Create a server controller
        ServerController serverController = new ServerController(serverView);
        serverController.runServer();
    }
}
