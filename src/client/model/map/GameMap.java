package client.model.map;

import java.util.ArrayList;

public class GameMap {
    public static final int SPIKES_CELL_DAMAGE = 1;
    public static final char START_PLAYER_CELL = 'S';
    public static final char END_CELL = 'W';
    public static final char EMPTY_CELL = ' ';
    public static final char WALL_CELL = '#';
    public static final char SPIKES_CELL = 'X';
    public static final char START_FLY_CELL = '|';
    public static final char START_SPIDER_CELL = '-';
    private final int maxCellsX;
    private final int maxCellsY;
    private final char[][] map;

    public GameMap(int maxCellsX, int maxCellsY, char[][] map) {
        this.maxCellsX = maxCellsX;
        this.maxCellsY = maxCellsY;
        this.map = map;
    }

    /**
     * Getter for the max number of cells of the {@link GameMap} on the X-axis.
     * @return Max number of cells of the {@link GameMap} on the X-axis.
     */
    public int getMaxCellsX() {
        return maxCellsX;
    }

    /**
     * Getter for the max number of cells of the {@link GameMap} on the Y-axis.
     * @return Max number of cells of the {@link GameMap} on the Y-axis.
     */
    public int getMaxCellsY() {
        return maxCellsY;
    }

    /**
     * Function to get all the positions for a cell.
     * @return A list of {@link MapPosition}s to locate a cell type.
     */
    public ArrayList<MapPosition> getPositionByCell(char cell) {
        ArrayList<MapPosition> positions = new ArrayList<>();

        for(int i = 0; i < this.maxCellsY; i++) {
            for(int j = 0; j < this.maxCellsX; j++) {
                if(this.map[i][j] == cell) {
                    positions.add(new MapPosition(j, i));
                }
            }
        }

        return positions;
    }

    /**
     * Function to get a specific cell given a {@link MapPosition}.
     * @param position A {@link MapPosition} of the cell.
     * @return The character (type of cell) of the specified cell.
     */
    public char getCellByPosition(MapPosition position) {
        return this.map[position.getY()][position.getX()];
    }

    /**
     * Getter of the map's terrain.
     * @return A 2D-array containing the {@link GameMap}'s terrain.
     */
    public char[][] getMap() {
        return map;
    }
}