package client.model.map;

public class MapPosition {
    private int x;
    private int y;

    public MapPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter of the X-axis position.
     * @return X-axis position.
     */
    public int getX() {
        return x;
    }

    /**
     * Setter of the X-axis position.
     * @param x X-axis position.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter of the Y-axis position.
     * @return Y-axis position.
     */
    public int getY() {
        return y;
    }

    /**
     * Setter of the Y-axis position.
     * @param y Y-axis position.
     */
    public void setY(int y) {
        this.y = y;
    }
}
