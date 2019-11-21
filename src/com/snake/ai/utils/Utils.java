package com.snake.ai.utils;

import com.snake.ai.modal.Direction;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.Direction.Up;
import static com.snake.ai.modal.GameState.*;

public class Utils {




    public static Direction getDenyDir(Direction direction) {
        switch (direction) {
            case Up:
                return Direction.Down;
            case Down:
                return Up;
            case Left:
                return Direction.Right;
            case Right:
                return Direction.Left;
        }
        return direction;
    }


}
