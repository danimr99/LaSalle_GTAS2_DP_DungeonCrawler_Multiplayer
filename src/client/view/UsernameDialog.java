package client.view;

import javax.swing.*;

public class UsernameDialog extends JDialog {
    private String username;

    public UsernameDialog() {
        this.username = JOptionPane.showInputDialog(null, "Enter a username");
    }

    /**
     * Getter of the username introduced by the user.
     * @return Username introduced by the user of type {@link String}.
     */
    public String getUsername() {
        return username;
    }
}
