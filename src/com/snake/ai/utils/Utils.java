package com.snake.ai.utils;

import com.snake.ai.modal.Direction;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.Direction.*;
import static com.snake.ai.modal.GameState.*;

public class Utils {


    public static Direction getDenyDir(Direction direction) {
        switch (direction) {
            case Up:
                return Down;
            case Down:
                return Up;
            case Left:
                return Right;
            case Right:
                return Left;
        }
        return direction;
    }


}
