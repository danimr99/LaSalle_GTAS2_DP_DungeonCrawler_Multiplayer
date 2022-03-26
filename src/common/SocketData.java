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

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getData() {
        return data;
    }
}
