package client.view;

import client.controller.KeyboardController;
import client.model.dao.FilePath;
import client.model.entities.Entity;
import client.model.entities.EntityDirection;
import client.model.map.GameMap;
import client.model.map.MapPosition;
import client.model.entities.player.Player;
import com.google.gson.Gson;
import common.ProtocolConstants;
import common.SocketData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class View extends JFrame {
    private static final int ICON_WIDTH = 25;
    private static final int ICON_HEIGHT = 25;
    public static final String CONTROL_UP = "CONTROL_UP";
    public static final String CONTROL_DOWN = "CONTROL_DOWN";
    public static final String CONTROL_LEFT = "CONTROL_LEFT";
    public static final String CONTROL_RIGHT = "CONTROL_RIGHT";
    private final GameMap map;
    private final Player player;
    private final List<Player> connectedPlayers;
    private HealthBar healthBar;
    private GameBoard gameBoard;
    private JButton controlUpButton;
    private JButton controlDownButton;
    private JButton controlLeftButton;
    private JButton controlRightButton;
    private final Socket socket;

    public View(GameMap map, Player player, Socket server) {
        /* Store the socket */
        this.socket = server;

        /* Store the map and player information */
        this.map = map;
        this.player = player;

        /* Set up list of connected players */
        this.connectedPlayers = new LinkedList<>();

        /* Send player username to server */
        this.sendDataToServer(ProtocolConstants.PLAYER_CONNECTED);

        /* Configure and render all the client view */
        this.configureWindow();
        this.renderHealthBar();
        this.renderGame();
        this.renderDirectionButtons();
        this.configureKeyboardListener();
        this.addCloseWindowListener();
    }

    /**
     * Function to configure basic settings for the {@link View}.
     */
    private void configureWindow() {
        this.setTitle("World of Crawler - " + this.player.getUsername());
        this.setSize(new Dimension(1080, 720));
        this.setLocationRelativeTo(null);
    }

    /**
     * Function to render the {@link HealthBar}.
     */
    private void renderHealthBar() {
        this.healthBar = new HealthBar(Entity.MAX_PLAYER_HEALTH);
        this.add(this.healthBar, BorderLayout.NORTH);
    }

    /**
     * Function to render the {@link GameBoard}.
     */
    private void renderGame() {
        this.gameBoard = new GameBoard(this.map, this.player.getPosition());

        this.add(this.gameBoard, BorderLayout.CENTER);
    }

    /**
     * Function to get the direction buttons' icons of a specific size.
     *
     * @param path Path of the icon resource.
     * @return Instance of {@link ImageIcon} to set on a direction button.
     */
    private ImageIcon setImageIcon(String path) {
        return new ImageIcon(((new ImageIcon(path)).getImage()).getScaledInstance(View.ICON_WIDTH,
                View.ICON_HEIGHT, Image.SCALE_SMOOTH));
    }

    /**
     * Function to render the direction buttons.
     */
    private void renderDirectionButtons() {
        /* Configure icons for the control buttons */
        Icon upButtonIcon = this.setImageIcon(FilePath.ASSET_BUTTON_UP);
        Icon downButtonIcon = this.setImageIcon(FilePath.ASSET_BUTTON_DOWN);
        Icon leftButtonIcon = this.setImageIcon(FilePath.ASSET_BUTTON_LEFT);
        Icon rightButtonIcon = this.setImageIcon(FilePath.ASSET_BUTTON_RIGHT);

        /* Configure buttons and set them their corresponding icon */
        this.controlUpButton = new JButton(upButtonIcon);
        this.controlDownButton = new JButton(downButtonIcon);
        this.controlLeftButton = new JButton(leftButtonIcon);
        this.controlRightButton = new JButton(rightButtonIcon);

        /* Add buttons to a JPanel */
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        /* Set spacing between buttons */
        gbc.insets = new Insets(5, 10, 10, 5);

        /* Set position for each button */
        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonsPanel.add(controlUpButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        buttonsPanel.add(controlDownButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonsPanel.add(controlLeftButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        buttonsPanel.add(controlRightButton, gbc);

        /* Add JPanel containing buttons to MainView */
        this.add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Function to add an {@link ActionListener} to direction buttons.
     *
     * @param listener Instance of {@link ActionListener}.
     */
    public void addActionListener(ActionListener listener) {
        this.controlUpButton.setActionCommand(CONTROL_UP);
        this.controlUpButton.addActionListener(listener);

        this.controlDownButton.setActionCommand(CONTROL_DOWN);
        this.controlDownButton.addActionListener(listener);

        this.controlLeftButton.setActionCommand(CONTROL_LEFT);
        this.controlLeftButton.addActionListener(listener);

        this.controlRightButton.setActionCommand(CONTROL_RIGHT);
        this.controlRightButton.addActionListener(listener);

        /* Remove focus to buttons */
        this.controlUpButton.setFocusable(false);
        this.controlDownButton.setFocusable(false);
        this.controlLeftButton.setFocusable(false);
        this.controlRightButton.setFocusable(false);
    }

    /**
     * Function to add a {@link java.awt.event.KeyListener} to the {@link View}.
     */
    private void configureKeyboardListener() {
        this.addKeyListener(new KeyboardController(this));
        this.setFocusable(true);
    }

    /**
     * Function to add a {@link java.awt.event.WindowListener} to the {@link View}.
     */
    private void addCloseWindowListener() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                disconnectFromServer();
                dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Function to check whether an {@link client.model.entities.Entity} can move to a cell or not (is a wall).
     *
     * @param futureEntityPosition The {@link MapPosition} where an {@link client.model.entities.Entity} wants to move.
     * @return Result of the checkup.
     */
    private boolean isPlayableCell(MapPosition futureEntityPosition) {
        return this.map.getCellByPosition(futureEntityPosition) != GameMap.WALL_CELL;
    }

    /**
     * Function to check whether the {@link client.model.entities.player.Player} is on a spikes cell or not.
     *
     * @param playerPosition The {@link client.model.entities.player.Player}'s position.
     * @return Result of the checkup.
     */
    private boolean isSpikesCell(MapPosition playerPosition) {
        return this.map.getCellByPosition(playerPosition) == GameMap.SPIKES_CELL;
    }

    /**
     * Function to check whether an {@link client.model.entities.Entity} can move to a desired
     * {@link client.model.entities.EntityDirection}.
     *
     * @param entityDirection {@link client.model.entities.Entity}'s desired next move direction.
     * @return Result of the checkup.
     */
    public boolean canEntityMove(MapPosition entityPosition, EntityDirection entityDirection) {
        if (this.player.isAlive()) {
            switch (entityDirection) {
                case UP -> {
                    if (entityPosition.getY() > 0 &&
                            this.isPlayableCell(new MapPosition(entityPosition.getX(), entityPosition.getY() - 1))) {
                        return true;
                    }
                }
                case DOWN -> {
                    if (entityPosition.getY() < (this.map.getMaxCellsY() - 1) &&
                            this.isPlayableCell(new MapPosition(entityPosition.getX(), entityPosition.getY() + 1))) {
                        return true;
                    }
                }
                case LEFT -> {
                    if (entityPosition.getX() > 0 &&
                            this.isPlayableCell(new MapPosition(entityPosition.getX() - 1, entityPosition.getY()))) {
                        return true;
                    }
                }
                case RIGHT -> {
                    if (entityPosition.getX() < (this.map.getMaxCellsX() - 1) &&
                            this.isPlayableCell(new MapPosition(entityPosition.getX() + 1, entityPosition.getY()))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Function to cause damage to the {@link client.model.entities.player.Player}.
     *
     * @param damage Amount of damage to cause to the {@link client.model.entities.player.Player}.
     */
    public void damagePlayer(int damage) {
        this.player.setHealth(this.player.getHealth() - damage);
    }

    /**
     * Function to move the {@link client.model.entities.player.Player} to a desired
     * {@link client.model.entities.EntityDirection}.
     *
     * @param playerDirection {@link client.model.entities.player.Player}'s desired move direction.
     */
    public void movePlayer(EntityDirection playerDirection) {
        MapPosition playerPosition = this.player.getPosition();

        if (this.canEntityMove(this.player.getPosition(), playerDirection)) {
            switch (playerDirection) {
                case UP -> playerPosition.setY(playerPosition.getY() - 1);
                case DOWN -> playerPosition.setY(playerPosition.getY() + 1);
                case RIGHT -> playerPosition.setX(playerPosition.getX() + 1);
                case LEFT -> playerPosition.setX(playerPosition.getX() - 1);
            }

            /* Check if player has moved to a spikes cell */
            if (this.isSpikesCell(playerPosition)) {
                this.damagePlayer(GameMap.SPIKES_CELL_DAMAGE);
            }
        }

        this.sendDataToServer(ProtocolConstants.PLAYER_MOVED);
    }

    /**
     * Function to check whether the {@link client.model.entities.player.Player} is on the {@link GameMap#END_CELL}.
     *
     * @return Result of the checkup.
     */
    public boolean hasPlayerWon() {
        return this.map.getCellByPosition(this.player.getPosition()) == GameMap.END_CELL;
    }

    /**
     * Function to update the {@link HealthBar}.
     */
    private void updateHealthBar() {
        this.healthBar.updateHealthBar(this.player.getHealth());
    }

    /**
     * Function to update the {@link GameBoard}.
     */
    private void updatePlayerPosition() {
        this.gameBoard.updatePlayerPosition(this.player.getPosition());
    }

    /**
     * Function to update the whole {@link View} and check if a dialog has to be displayed (winning dialog or death
     * dialog).
     */
    public void updateView() {
        this.updateHealthBar();
        this.updatePlayerPosition();

        /* Check if player has dead */
        if (!this.player.isAlive()) {
            this.showDialog("YOU DIED", "Oh, no! You ran out of HP!", false);
        }

        /* Check if player has won */
        if (this.hasPlayerWon()) {
            this.showDialog("YOU WON", "Congratulations for reaching the end!", true);
        }
    }

    /**
     * Function to display a {@link JDialog} when the player wins or dies.
     *
     * @param title          Title of the dialog.
     * @param message        Message of the dialog.
     * @param isWinnerDialog Boolean whether is winner or dead dialog.
     */
    public void showDialog(String title, String message, boolean isWinnerDialog) {
        final JDialog dialog = new JDialog(this, title, false);
        JOptionPane optionPane = new JOptionPane(message, isWinnerDialog ?
                JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        optionPane.addPropertyChangeListener(e -> {
            String prop = e.getPropertyName();

            if (dialog.isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                /* Log whether the player has won or died */
                if (isWinnerDialog) {
                    this.sendDataToServer(ProtocolConstants.PLAYER_WON);
                } else {
                    this.sendDataToServer(ProtocolConstants.PLAYER_DIED);
                }

                /* Disconnect from server */
                this.disconnectFromServer();

                /* Close view on win or lose */
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

                dialog.setVisible(false);
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);
        dialog.requestFocus();

        /* Disable direction control buttons to prevent duplicating the dialog */
        this.controlUpButton.setEnabled(false);
        this.controlDownButton.setEnabled(false);
        this.controlLeftButton.setEnabled(false);
        this.controlRightButton.setEnabled(false);
    }

    public void addConnectedPlayer(Player connectedPlayer) {
        /* Add newly connected player to online players list */
        this.connectedPlayers.add(connectedPlayer);

        /* Update game board */
        this.gameBoard.updateConnectedPlayersPosition(this.connectedPlayers);
    }

    public void updateConnectedPlayerPosition(Player connectedPlayer) {
        boolean existsConnectedPlayer = false;

        /* Check if connected player is known by the client */
        for (Player knownPlayer : this.connectedPlayers) {
            if (knownPlayer.getUsername().equals(connectedPlayer.getUsername())) {
                knownPlayer.setPosition(connectedPlayer.getPosition());
                existsConnectedPlayer = true;
                break;
            }
        }

        /* If connected player is not known, add it to the connected players list */
        if (!existsConnectedPlayer) {
            this.connectedPlayers.add(connectedPlayer);
        }

        /* Update connected player position*/
        this.gameBoard.updateConnectedPlayersPosition(this.connectedPlayers);
    }

    public void removeConnectedPlayer(Player connectedPlayer) {
        /* Remove connected player from online players list */
        this.connectedPlayers.removeIf(player -> player.getUsername().equals(connectedPlayer.getUsername()));

        /* Update game board */
        this.gameBoard.updateConnectedPlayersPosition(this.connectedPlayers);
    }

    public void disconnectFromServer() {
        try {
            /* Broadcast to all players sending data to server */
            this.sendDataToServer(ProtocolConstants.PLAYER_DISCONNECTED);

            this.socket.close();
        } catch (IOException e) {
            System.out.println("ERROR: Player " + this.player.getUsername() + " cannot disconnect from server.");
        }
    }

    public void sendDataToServer(String action) {
        try {
            SocketData socketData = new SocketData(this.player.getUsername(), action, new Gson().toJson(this.player));

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectOutputStream.writeObject(socketData);
        } catch (IOException e) {
            System.out.println("ERROR: Player " + this.player.getUsername() +
                    " cannot send " + action + " to the server.");
        }
    }
}