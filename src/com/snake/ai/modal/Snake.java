package com.snake.ai.modal;

import com.snake.ai.utils.Utils;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.GameState.*;
import static com.snake.ai.utils.Utils.getDirection;

public class Snake extends Thread {
    public static final int HEAD_SNAKE_INDEX = 0;
    public int score = 0;
    public ArrayList<Point> coords;
    Color color;
    boolean isBot = true;
    private boolean alive = true;
    public Direction denyDir;
    private Point nearestFood = null;
    private Node nextMoveToFood = null;
    private Node node;

    public Snake(Point startPoint, Direction tailDirection, Color color) {
        coords = new ArrayList<>();
        coords.add(startPoint);
        coords.add(newPointAfterMove(startPoint, tailDirection));
        this.color = color;
        this.denyDir = tailDirection;
        node = new Node(coords.get(HEAD_SNAKE_INDEX), denyDir);
    }

    public Snake(Snake snake, Direction denyDir) {
        this.score = snake.score;
        this.coords = snake.coords;
        this.color = snake.color;
        this.alive = snake.alive;
        this.denyDir = denyDir;
        this.isBot = snake.isBot;
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
        Point nextPoint = newPointAfterMove(point, direction);
        score++;
        coords.add(HEAD_SNAKE_INDEX, nextPoint);
        foods.remove(nextPoint);
    }

    public boolean isEat(Direction direction) {
        makeDenyDir(direction);
        Point point = coords.get(HEAD_SNAKE_INDEX);
        Point nextPoint = newPointAfterMove(point, direction);
        Item check = nextPoint.x < boardW ? nextPoint.y < boardH ? board[nextPoint.x][nextPoint.y] : null : null;
        return (check == Item.Food);
    }

    private void makeDenyDir(Direction direction) {
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
    }

    public boolean isSnakeAlive() {
        return alive;
    }

    public Direction getDenyDir() {
        return denyDir;
    }

    public void moveForward() {
        move(getDirection(denyDir));
    }

    public void move(Direction direction) {
        if (direction != getDenyDir()) {
            makeDenyDir(direction);
            Point point = coords.get(HEAD_SNAKE_INDEX);
            Point newPoint = newPointAfterMove(point, direction);
            if (board[newPoint.x][newPoint.y] == Item.Snake || board[newPoint.x][newPoint.y] == Item.SnakeHead) {
                snakes.remove(this);
            } else {
                for (int i = coords.size() - 1; i > 0; i--)
                    coords.set(i, coords.get(i - 1));
                coords.set(HEAD_SNAKE_INDEX, newPoint);
            }
        }
    }

    void findFood(Node root) {
        nearestFood = Utils.getNearestFood(coords.get(HEAD_SNAKE_INDEX));
        ArrayList<Node> list = root.getChildren();
        if (list.size() == 0) {
//            System.out.println("empty list");
            nextMoveToFood = root;
        }
        double minDis = Float.MAX_VALUE;
        for (Node n : list)
            if (minDis > Utils.getDistance(nearestFood, n.position)) {
                minDis = Utils.getDistance(nearestFood, n.position);
                nextMoveToFood = n;
            }
        node = nextMoveToFood;
    }

    private void renewSnake() {
        nextMoveToFood = null;
        for (int i = 0; i < boardW; i++)
            for (int j = 0; j < boardH; j++)
                if (board[i][j] == Item.NEAR_NODE)
                    board[i][j] = Item.Empty;
        node = new Node(coords.get(HEAD_SNAKE_INDEX), denyDir);
    }

    @Override
    public void run() {
        while (isSnakeAlive()) {
            timeEnd = System.currentTimeMillis();
            if (timeEnd - timeStart >= updateTime) {
                if (node != null) {
                    findFood(node);
                    Direction move = Utils.getDirection(nextMoveToFood.denyDir);
                    if (isEat(move)) {
                        eat(move);
                    } else
                        move(move);
                } else System.out.println("null");
                timeStart = timeEnd;
            }
        }
    }
}
