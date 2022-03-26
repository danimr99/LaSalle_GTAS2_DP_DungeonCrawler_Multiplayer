package client.model.entities.player;

import client.model.entities.Entity;
import client.model.map.MapPosition;

public class Player extends Entity {
    private final String username;
    private int health;

    public Player(String username, MapPosition position) {
        super(position);
        this.username = username;
        this.health = Entity.MAX_PLAYER_HEALTH;
    }

    /**
     * Function to check whether the {@link Player} is alive or not.
     * @return Result of the checkup.
     */
    public boolean isAlive() {
        return this.health > 0;
    }

    /**
     * Getter of the {@link Player}'s health.
     * @return {@link Player}'s health.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Setter of the {@link Player}'s health.
     * @param health {@link Player}'s health.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Getter of the username.
     * @return Username of type {@link String}.
     */
    public String getUsername() {
        return this.username;
    }
}