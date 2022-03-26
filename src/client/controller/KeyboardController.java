package client.controller;

import client.model.entities.EntityDirection;
import client.view.View;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {
    private final static int W = 87;
    private final static int A = 65;
    private final static int S = 83;
    private final static int D = 68;
    private final View view;

    public KeyboardController(View view) {
        this.view = view;
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param event the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent event) {
        switch(event.getKeyCode()) {
            case W -> this.view.movePlayer(EntityDirection.UP);
            case A -> this.view.movePlayer(EntityDirection.LEFT);
            case S -> this.view.movePlayer(EntityDirection.DOWN);
            case D -> this.view.movePlayer(EntityDirection.RIGHT);
            default -> System.out.println("ERROR: Invalid key from the keyboard pressed!");
        }

        /* Update view */
        this.view.updateView();
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}