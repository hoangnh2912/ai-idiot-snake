package com.snake.ai.modal;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.snake.ai.modal.GameState.movePlayer;

public class PlayerKey extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        movePlayer = e.getKeyCode();
    }
}
