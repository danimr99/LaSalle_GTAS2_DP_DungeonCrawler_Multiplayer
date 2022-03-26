package client.model.dao;

import client.model.map.GameMap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MapDAO {
    private static final int FIRST_FILE_LINE = 0;
    private static final int SECOND_FILE_LINE = 1;
    private final int maxCellsX;
    private final int maxCellsY;

    public MapDAO() {
        this.maxCellsX = this.getFileContentForSpecifiedLine(FIRST_FILE_LINE);
        this.maxCellsY = this.getFileContentForSpecifiedLine(SECOND_FILE_LINE);
    }

    /**
     * Function to get the content from a specific line of an external file.
     * @param line Number of the line to read.
     * @return Content of the line read.
     */
    private int getFileContentForSpecifiedLine(int line) {
        int cells = 0;

        try {
            String lineContent = Files.readAllLines(Paths.get(FilePath.MAP_FILE_PATH)).get(line);
            cells = Integer.parseInt(lineContent);
        } catch (IOException exception) {
            System.out.println("ERROR: Cannot read the content of the map file!");
        } catch (NumberFormatException exception) {
            System.out.println("ERROR: Cannot retrieve the number of cells!");
        }

        return cells;
    }

    /**
     * Function to read the tiles of the {@link GameMap} on a 2D-array format.
     * @return 2D-array containing the {@link GameMap}'s terrain.
     */
    public char[][] getMapFromFile() {
        char[][] map = new char[this.maxCellsY][this.maxCellsX];

        try {
            File file = new File(FilePath.MAP_FILE_PATH);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int characterResult, indexX = 0, indexY = 0;

            while((characterResult = bufferedReader.read()) != -1) {
                char character = (char) characterResult;

                if(character == GameMap.START_PLAYER_CELL || character == GameMap.END_CELL ||
                        character == GameMap.EMPTY_CELL || character == GameMap.WALL_CELL ||
                        character == GameMap.SPIKES_CELL || character == GameMap.START_FLY_CELL ||
                        character == GameMap.START_SPIDER_CELL) {
                    map[indexY][indexX] = character;

                    indexX++;

                    if(indexX == this.maxCellsX) {
                        indexY++;
                        indexX = 0;
                    }
                }
            }
        } catch (FileNotFoundException exception) {
            System.out.println("ERROR: Map file not found!");
        } catch (IOException exception) {
            System.out.println("ERROR: Cannot read the content of the map file!");
        }

        return map;
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
}
