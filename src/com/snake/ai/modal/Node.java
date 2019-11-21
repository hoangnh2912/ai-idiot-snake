package com.snake.ai.modal;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.GameState.*;

public class Node {
    Point position;
    private ArrayList<Node> children;
    Direction denyDir;
    private int dept = 0;
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

    ArrayList<Node> getChildren() {
        dept++;
        children = new ArrayList<>();
        Point posUp = new Point(position.x, position.y - 1),
                posDown = new Point(position.x, position.y + 1),
                posLeft = new Point(position.x - 1, position.y),
                posRight = new Point(position.x + 1, position.y);
        try {
            Item itemDown =
                    posDown.x < boardW ? posDown.y < boardH ?
                            board[posDown.x][posDown.y] : Item.DEAD : Item.DEAD,
                    itemLeft = posLeft.x < boardW ? posLeft.y < boardH ? board[posLeft.x][posLeft.y] : Item.DEAD : Item.DEAD,
                    itemRight = posRight.x < boardW ? posRight.y < boardH ? board[posRight.x][posRight.y] : Item.DEAD : Item.DEAD,

                    itemUp = posUp.x < boardW ? posUp.y < boardH ? board[posUp.x][posUp.y] : Item.DEAD : Item.DEAD;
            switch (denyDir) {
                case Up:
                    if (isNodeAvailable(itemDown)) children.add(new Node(posDown, denyDir, this, board));
                    if (isNodeAvailable(itemLeft))
                        children.add(new Node(posLeft, Direction.Right, this, board));
                    if (isNodeAvailable(itemRight))
                        children.add(new Node(posRight, Direction.Left, this, board));
                    break;
                case Down:
                    if (isNodeAvailable(itemUp)) children.add(new Node(posUp, denyDir, this, board));
                    if (isNodeAvailable(itemLeft))
                        children.add(new Node(posLeft, Direction.Right, this, board));
                    if (isNodeAvailable(itemRight))

                        children.add(new Node(posRight, Direction.Left, this, board));
                    break;
                case Right:
                    if (isNodeAvailable(itemUp)) children.add(new Node(posUp, Direction.Down, this, board));
                    if (isNodeAvailable(itemLeft))
                        children.add(new Node(posLeft, denyDir, this, board));
                    if (isNodeAvailable(itemDown))
                        children.add(new Node(posDown, Direction.Up, this, board));
                    break;
                case Left:
                    if (isNodeAvailable(itemUp)) children.add(new Node(posUp, Direction.Down, this, board));
                    if (isNodeAvailable(itemRight))
                        children.add(new Node(posRight, denyDir, this, board));
                    if (isNodeAvailable(itemDown))
                        children.add(new Node(posDown, Direction.Up, this, board));
                    break;
            }
        } catch (Exception e) {
            System.out.println("exp " + e.getMessage());
        }
        return children;
    }


    private boolean isNodeAvailable(Item item) {
        return item == Item.Empty || item == Item.Food || item == Item.DEAD;
    }
}
