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
    boolean isBot = true, isAlive = true;
    Direction denyDir, nextMove;
    private Node nextMoveToFood;
    private Node node;
    private ArrayList<Point> foods;
    private Item[][] board;

    Snake(Point startPoint, Direction tailDirection, Color color, ArrayList<Point> foods, Item[][] board) {
        coords = new ArrayList<>();
        coords.add(startPoint);
        coords.add(newPointAfterMove(startPoint, tailDirection));
        this.color = color;
        this.denyDir = tailDirection;
        this.board = board;
        node = new Node(coords.get(HEAD_SNAKE_INDEX), denyDir, board);
        nextMove = getDenyDir(denyDir);
        this.foods = foods;
    }

    synchronized Point newPointAfterMove(Point prePoint, Direction direction) {
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
        System.out.println("pre");
        return prePoint;
    }

    private synchronized double getDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2));
    }

    private synchronized Point getNearestFood(Point snakeHead, ArrayList<Point> foods) {
        double minDis = Double.MAX_VALUE;
        Point nearestFood = null;
        for (Point p : foods) {
            if (minDis > getDistance(p, snakeHead)) {
                minDis = getDistance(p, snakeHead);
                nearestFood = p;
            }
        }
        board[nearestFood.x][nearestFood.y] = Item.NearestFood;
        return nearestFood;
    }

    int i = 0;

    private synchronized Direction findNextMove(Node root) {
        i++;
        if (!node.position.equals(coords.get(0))) node.position = coords.get(0);
        Point nearestFood = getNearestFood(coords.get(HEAD_SNAKE_INDEX), foods);
        ArrayList<Node> list = root.getChildren();
        if (list.isEmpty()) {
            System.out.println("empty list");
            isAlive = false;
//            renewFindMove();
//            return findNextMove(node);
        } else {
            double minDis = Float.MAX_VALUE;
            for (Node n : list) {
                board[n.position.x][n.position.y] = Item.NearMove;
                if (minDis > getDistance(nearestFood, n.position)) {
                    minDis = getDistance(nearestFood, n.position);
                    nextMoveToFood = n;
//                    System.out.println(board[n.position.x][n.position.y]);
                }
            }
//            if (!root.position.equals(this.coords.get(0))) System.out.println("leak");
//            findNextMove(nextMoveToFood);
        }
        node = nextMoveToFood;
//        System.out.println(getDenyDir(nextMoveToFood.denyDir));
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
