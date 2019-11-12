package com.snake.ai.utils;

import com.snake.ai.modal.Direction;

import java.awt.*;

import static com.snake.ai.modal.GameState.foods;

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

    public static Point getNearestFood(Point snakeHead) {
        double minDis = Double.MAX_VALUE;
        Point nearestFood = null;
        for (Point p : foods) {
            if (minDis > getDistance(p, snakeHead)) {
                minDis = getDistance(p, snakeHead);
                nearestFood = new Point(p.x,p.y);
            }
        }
        return nearestFood;
    }

    public static double getDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
    }
}
