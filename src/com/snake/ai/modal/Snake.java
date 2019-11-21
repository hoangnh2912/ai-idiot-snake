package com.snake.ai.modal;

import com.snake.ai.utils.Utils;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.utils.Utils.getDenyDir;

public class Snake extends Thread {
    final int HEAD_SNAKE_INDEX = 0;
    int score = 0;
    ArrayList<Point> coords;
    Color color;
    boolean isBot = true;
    Direction denyDir, nextMove;
    private Node nextMoveToFood = null;
    private Node node;
    private ArrayList<Point> foods = new ArrayList<>();
    private Item[][] board;

    Snake(Point startPoint, Direction tailDirection, Color color, ArrayList<Point> foods, Item[][] board) {
        coords = new ArrayList<>();
        coords.add(startPoint);
        coords.add(newPointAfterMove(startPoint, tailDirection));
        this.color = color;
        this.denyDir = tailDirection;
        this.board = board;
        node = new Node(coords.get(HEAD_SNAKE_INDEX), denyDir, board);
        nextMove = getDenyDir(tailDirection);
        this.foods = foods;
    }

    Point newPointAfterMove(Point prePoint, Direction direction) {
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

    private double getDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
    }

    private Point getNearestFood(Point snakeHead, ArrayList<Point> foods) {
        double minDis = Double.MAX_VALUE;
        Point nearestFood = null;
        for (Point p : foods) {
            if (minDis > getDistance(p, snakeHead)) {
                minDis = getDistance(p, snakeHead);
                nearestFood = new Point(p.x, p.y);
            }
        }
        return nearestFood;
    }

    private synchronized Direction findNextMove(Node root) {
        Point nearestFood = getNearestFood(coords.get(HEAD_SNAKE_INDEX), foods);
        ArrayList<Node> list = root.getChildren();
        if (list.size() == 0) {
            System.out.println("empty list");
//            renewFindMove();
            node = new Node(coords.get(HEAD_SNAKE_INDEX), denyDir, board);
//            return findNextMove(node);
        } else {
            double minDis = Float.MAX_VALUE;
            for (Node n : list)
                if (minDis > getDistance(nearestFood, n.position)) {
                    minDis = getDistance(nearestFood, n.position);
                    nextMoveToFood = n;
                }
        }
        node = nextMoveToFood;
        return getDenyDir(nextMoveToFood.denyDir);
    }


    boolean isFindDone = false;

    @Override
    public void run() {
        isFindDone = false;
        nextMove = findNextMove(node);
        isFindDone = true;
    }
}
