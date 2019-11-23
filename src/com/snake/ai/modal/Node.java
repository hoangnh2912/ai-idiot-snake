package com.snake.ai.modal;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.Direction.*;
import static com.snake.ai.modal.GameState.*;

class Node {
    Point position;
    private ArrayList<Node> children;
    Direction denyDir;
    private Node root = null;
    private Item[][] board;

    Node(Point position, Direction denyDir, Item[][] board) {
        this.position = position;
        this.denyDir = denyDir;
        this.board = board;
    }

    private Node(Point position, Direction denyDir, Node root, Item[][] board) {
        this.position = position;
        this.denyDir = denyDir;
        this.board = board;
        this.root = root;
    }

    synchronized ArrayList<Node> getChildren() {
        children = new ArrayList<>();
        Point posUp = new Point(position.x, position.y - 1),
                posDown = new Point(position.x, position.y + 1),
                posLeft = new Point(position.x - 1, position.y),
                posRight = new Point(position.x + 1, position.y);
        try {
            Item itemDown =
                    posDown.x < boardW ? posDown.y < boardH ?
                            board[posDown.x][posDown.y] : Item.WALL : Item.WALL,
                    itemLeft = posLeft.x < boardW ? posLeft.y < boardH ? board[posLeft.x][posLeft.y] : Item.WALL : Item.WALL,
                    itemRight = posRight.x < boardW ? posRight.y < boardH ? board[posRight.x][posRight.y] : Item.WALL : Item.WALL,
                    itemUp = posUp.x < boardW ? posUp.y < boardH ? board[posUp.x][posUp.y] : Item.WALL : Item.WALL;
            Node nodeDown = new Node(posDown, Up, this, board),
                    nodeUp = new Node(posUp, Down, this, board),
                    nodeRight = new Node(posRight, Left, this, board),
                    nodeLeft = new Node(posLeft, Right, this, board);
            if (isNodeAvailable(itemUp)) children.add(nodeUp);
            if (isNodeAvailable(itemDown)) children.add(nodeDown);
            if (isNodeAvailable(itemRight)) children.add(nodeRight);
            if (isNodeAvailable(itemLeft)) children.add(nodeLeft);
            switch (denyDir) {
                case Up:
                    children.remove(nodeUp);
                    break;
                case Down:
                    children.remove(nodeDown);
                    break;
                case Right:
                    children.remove(nodeRight);
                    break;
                case Left:
                    children.remove(nodeLeft);
                    break;
            }
        } catch (Exception e) {
            System.out.println("exp " + e.getMessage());
        }
        return children;
    }


    private synchronized boolean isNodeAvailable(Item item) {
        return item == Item.Empty || item == Item.Food || item == Item.NearestFood || item == Item.NearMove;
    }
}
