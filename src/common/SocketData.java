package common;

import java.io.Serializable;

public class SocketData implements Serializable {
    private final String username;
    private final String action;
    private final String data;

    public SocketData(String username, String action, String data) {
        this.username = username;
        this.action = action;
        this.data = data;
    }

    /**
     * Getter of the username to identify the {@link client.model.entities.player.Player} who has sent/received
     * data to/from the server.
     * @return Username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter of the type of action done by the {@link client.model.entities.player.Player}.
     * @return Code of the action done by the {@link client.model.entities.player.Player}.
     * @see ProtocolConstants
     */
    public String getAction() {
        return action;
    }

    /**
     * Getter of the data sent/received through a {@link java.net.Socket}. The majority of times, the data sent is an
     * instance of {@link client.model.entities.player.Player} converted to JSON string using
     * {@link com.google.gson.Gson}.
     * @return Data sent through a {@link java.net.Socket}.
     */
    public String getData() {
        return data;
    }
}
