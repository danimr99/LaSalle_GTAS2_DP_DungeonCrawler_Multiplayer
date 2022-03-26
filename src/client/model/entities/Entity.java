package client.model.entities;

import client.model.map.MapPosition;

public abstract class Entity {
    public static final int MAX_PLAYER_HEALTH = 10;
    protected MapPosition position;

    public Entity(MapPosition position) {
        this.position = position;
    }

    /**
     * Getter of the {@link Entity}'s position on the {@link client.model.map.GameMap} in {@link MapPosition} format.
     * @return {@link Entity}'s position on the {@link client.model.map.GameMap}.
     */
    public MapPosition getPosition() {
        return position;
    }

    /**
     * Setter of the {@link Entity}'s position on the {@link client.model.map.GameMap} in {@link MapPosition} format.
     * @param position {@link Entity}'s position on the {@link client.model.map.GameMap}.
     */
    public void setPosition(MapPosition position) {
        this.position = position;
    }
}