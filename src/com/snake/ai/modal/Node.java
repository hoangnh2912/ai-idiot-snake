package com.snake.ai.modal;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.GameState.*;

public class Node {
    public Point position;
    public Item value;
    ArrayList<Node> children;
    Direction denyDir;
    int dept = 0;
    public Node root = null;

    public Node(Point position, Direction denyDir) {
        this.position = position;
        this.denyDir = denyDir;
        this.value = board[position.x][position.y];
    }

    public Node(Point position, Direction denyDir, Node root) {
        this.position = position;
        this.denyDir = denyDir;
        this.value = board[position.x][position.y];
        this.root = root;
    }

    public ArrayList<Node> getChildren() {
        dept++;
        children = new ArrayList<>();
        Point posUp = new Point(position.x, position.y - 1),
                posDown = new Point(position.x, position.y + 1),
                posLeft = new Point(position.x - 1, position.y),
                posRight = new Point(position.x + 1, position.y);
        try {
            Item itemDown =
                    posDown.x < boardW ? posDown.y < boardH ?
                            board[posDown.x][posDown.y] : null : null,
                    itemLeft = posLeft.x < boardW ? posLeft.y < boardH ? board[posLeft.x][posLeft.y] : null : null,
                    itemRight = posRight.x < boardW ? posRight.y < boardH ? board[posRight.x][posRight.y] : null : null,

                    itemUp = posUp.x < boardW ? posUp.y < boardH ? board[posUp.x][posUp.y] : null : null;
            switch (denyDir) {
                case Up:
                    if (isNodeAvailable(itemDown)) children.add(new Node(posDown, denyDir, this));
                    if (isNodeAvailable(itemLeft))
                        children.add(new Node(posLeft, Direction.Right, this));
                    if (isNodeAvailable(itemRight))
                        children.add(new Node(posRight, Direction.Left, this));
                    break;
                case Down:
                    if (isNodeAvailable(itemUp)) children.add(new Node(posUp, denyDir, this));
                    if (isNodeAvailable(itemLeft))
                        children.add(new Node(posLeft, Direction.Right, this));
                    if (isNodeAvailable(itemRight))

                        children.add(new Node(posRight, Direction.Left, this));
                    break;
                case Right:
                    if (isNodeAvailable(itemUp)) children.add(new Node(posUp, Direction.Down, this));
                    if (isNodeAvailable(itemLeft))
                        children.add(new Node(posLeft, denyDir, this));
                    if (isNodeAvailable(itemDown))
                        children.add(new Node(posDown, Direction.Up, this));
                    break;
                case Left:
                    if (isNodeAvailable(itemUp)) children.add(new Node(posUp, Direction.Down, this));
                    if (isNodeAvailable(itemRight))
                        children.add(new Node(posRight, denyDir, this));
                    if (isNodeAvailable(itemDown))
                        children.add(new Node(posDown, Direction.Up, this));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return children;
    }


    private boolean isNodeAvailable(Item item) {
        return item == Item.Empty || item == Item.Food;
    }
}
