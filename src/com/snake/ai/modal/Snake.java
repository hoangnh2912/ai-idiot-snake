package com.snake.ai.modal;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.GameState.*;

public class Snake extends Thread {
    public static final int HEAD_SNAKE_INDEX = 0;
    public int score = 0;
    public ArrayList<Point> coords;
    public Color color;
    public boolean alive = true;
    public Direction denyDir;

    public Snake(Point startPoint, Direction tailDirection, Color color) {
        coords = new ArrayList<>();
        coords.add(startPoint);
        coords.add(newPointAfterMove(startPoint, tailDirection));
        this.color = color;
        this.denyDir = tailDirection;
    }

    public Point newPointAfterMove(Point prePoint, Direction direction) {
        switch (direction) {
            case Up:
                return new Point(prePoint.x, prePoint.y - 1);
            case Down:
                return new Point(prePoint.x, prePoint.y + 1);
            case Left:
                return new Point(prePoint.x - 1, prePoint.y);
            case Right:
                return new Point(prePoint.x + 1, prePoint.y);
        }
        return prePoint;
    }

    public void eat(Direction direction) {
        Point point = coords.get(HEAD_SNAKE_INDEX);
        coords.add(HEAD_SNAKE_INDEX, newPointAfterMove(point, direction));
    }

    public boolean isEat(Direction direction) {
        Point point = coords.get(HEAD_SNAKE_INDEX);
        Point nextPoint = newPointAfterMove(point, direction);
        if (board[nextPoint.x][nextPoint.y] == Board.Item.Food) {
            foods.remove(nextPoint);
            return true;
        } else return false;
    }

    public boolean isSnakeAlive() {
        return alive;
    }

    public Direction getDenyDir() {
        return denyDir;
    }

    public void move(Direction direction) {
        if (direction != getDenyDir()) {
            switch (direction) {
                case Up:
                    denyDir = Direction.Down;
                    break;
                case Down:
                    denyDir = Direction.Up;
                    break;
                case Left:
                    denyDir = Direction.Right;
                    break;
                case Right:
                    denyDir = Direction.Left;
                    break;
            }
            Point point = coords.get(HEAD_SNAKE_INDEX);
            for (int i = coords.size() - 1; i > 0; i--)
                coords.set(i, coords.get(i - 1));
            coords.set(HEAD_SNAKE_INDEX, newPointAfterMove(point, direction));

        }
    }

    @Override
    public void run() {
        while (isSnakeAlive()) {
            timeEnd = System.currentTimeMillis();
            if (timeEnd - timeStart >= updateTime) {
                if (isEat(Direction.Down)) eat(Direction.Down);
                else move(Direction.Down);
                timeStart = timeEnd;
            }
        }
    }
}
