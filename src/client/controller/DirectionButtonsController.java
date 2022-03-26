package client.controller;

import client.model.entities.EntityDirection;
import client.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DirectionButtonsController implements ActionListener {
    private final View view;

    public DirectionButtonsController(View view) {
        this.view = view;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case View.CONTROL_UP -> this.view.movePlayer(EntityDirection.UP);
            case View.CONTROL_DOWN -> this.view.movePlayer(EntityDirection.DOWN);
            case View.CONTROL_LEFT -> this.view.movePlayer(EntityDirection.LEFT);
            case View.CONTROL_RIGHT -> this.view.movePlayer(EntityDirection.RIGHT);
            default -> System.out.println("ERROR: Invalid button pressed!");
        }

        /* Update view */
        this.view.updateView();
    }
}