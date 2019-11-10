package com.snake.ai.utils;

import com.snake.ai.modal.Direction;

public class Utils {
    public static Direction getDirection(Direction denyDir) {
        switch (denyDir) {
            case Up:
                return Direction.Down;
            case Down:
                return Direction.Up;
            case Left:
                return Direction.Right;
            case Right:
                return Direction.Left;
        }
        return Direction.DEAD_LOOK;
    }
}
