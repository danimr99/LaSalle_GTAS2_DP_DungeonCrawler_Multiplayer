package client.view;

import client.model.entities.Entity;
import client.model.entities.player.Player;

import javax.swing.*;
import java.awt.*;

public class HealthBar extends JPanel {
    private final int maxHealth;
    private int playerCurrentHealth;
    private JLabel playerCurrentHealthLabel;

    public HealthBar(int playerCurrentHealth) {
        this.maxHealth = Entity.MAX_PLAYER_HEALTH;
        this.playerCurrentHealth = playerCurrentHealth;

        this.configureHealthBar();
        this.renderHealthLabels();
    }

    /**
     * Function to configure basic settings for the {@link HealthBar}.
     */
    private void configureHealthBar() {
        this.setPreferredSize(new Dimension(this.getWidth(), 40));
    }

    /**
     * Function to render the {@link JLabel}s that display the current and max {@link Player}'s health.
     */
    private void renderHealthLabels() {
        this.playerCurrentHealthLabel = new JLabel(String.valueOf(this.playerCurrentHealth));
        this.add(this.playerCurrentHealthLabel, BorderLayout.EAST);
        this.add(new JLabel("/"), BorderLayout.CENTER);
        this.add(new JLabel(String.valueOf(this.maxHealth)), BorderLayout.WEST);
    }

    /**
     * Function to update the status of the {@link HealthBar}.
     * @param playerCurrentHealth Current {@link Player}'s health.
     */
    public void updateHealthBar(int playerCurrentHealth) {
        this.playerCurrentHealth = playerCurrentHealth;

        /* Update the label of the available health */
        this.playerCurrentHealthLabel.setText(String.valueOf(this.playerCurrentHealth));

        /* Repaint the health bar */
        this.repaint();
    }

    /**
     * Function to paint the {@link HealthBar} on the {@link View}.
     * @param g Instance of {@link Graphics}.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        /* Paint as red the entire background of the bar */
        g.setColor(Color.RED);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        /* Paint as green the corresponding area depending on the player's health */
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, this.getWidth() * this.playerCurrentHealth / this.maxHealth, this.getHeight());
    }
}
