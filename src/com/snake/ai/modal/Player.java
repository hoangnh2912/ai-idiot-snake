package com.snake.ai.modal;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.GameState.*;
import static com.snake.ai.utils.Utils.getDenyDir;


public class Player extends Snake {

    Player(Point startPoint, Direction tailDirection, Color color, ArrayList<Point> foods, Item[][] board) {
        super(startPoint, tailDirection, color, foods, board);
        this.isBot = false;
    }

    private Direction getDirectionPlayer() {
        switch (movePlayer) {
            case PRESS_DOWN:
                return Direction.Down;
            case PRESS_UP:
                return Direction.Up;
            case PRESS_RIGHT:
                return Direction.Right;
            case PRESS_LEFT:
                return Direction.Left;
        }
        return Direction.Down;
    }

    @Override
    public void run() {
        isFindDone = false;
        nextMove = getDirectionPlayer() == getDenyDir(nextMove) ? nextMove : getDirectionPlayer();
        isFindDone = true;
    }
}
