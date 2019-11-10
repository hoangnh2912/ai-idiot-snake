package com.snake.ai.modal;

import java.awt.*;
import java.util.ArrayList;

import static com.snake.ai.modal.GameState.board;

public class Node {
    Point position;
    Item value;
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
            Item itemDown = board[posDown.x][posDown.y], itemLeft = board[posLeft.x][posLeft.y], ItemRight = board[posRight.x][posRight.y], ItemUp = board[posUp.x][posUp.y];
            switch (denyDir) {
                case Up:
                    if (itemDown != null && itemDown != Item.NEAR_NODE) children.add(new Node(posDown, denyDir, this));
                    if (itemLeft != null && itemLeft != Item.NEAR_NODE)
                        children.add(new Node(posLeft, Direction.Right, this));
                    if (ItemRight != null && ItemRight != Item.NEAR_NODE)
                        children.add(new Node(posRight, Direction.Left, this));
                    break;
                case Down:
                    if (ItemUp != null && ItemUp != Item.NEAR_NODE) children.add(new Node(posUp, denyDir, this));
                    if (itemLeft != null && itemLeft != Item.NEAR_NODE)
                        children.add(new Node(posLeft, Direction.Right, this));
                    if (ItemRight != null && ItemRight != Item.NEAR_NODE)
                        children.add(new Node(posRight, Direction.Left, this));
                    break;
                case Right:
                    if (ItemUp != null && ItemUp != Item.NEAR_NODE) children.add(new Node(posUp, Direction.Down, this));
                    if (itemLeft != null && itemLeft != Item.NEAR_NODE) children.add(new Node(posLeft, denyDir, this));
                    if (itemDown != null && itemDown != Item.NEAR_NODE)
                        children.add(new Node(posDown, Direction.Up, this));
                    break;
                case Left:
                    if (ItemUp != null && ItemUp != Item.NEAR_NODE) children.add(new Node(posUp, Direction.Down, this));
                    if (ItemRight != null && ItemRight != Item.NEAR_NODE)
                        children.add(new Node(posRight, denyDir, this));
                    if (itemDown != null && itemDown != Item.NEAR_NODE)
                        children.add(new Node(posDown, Direction.Up, this));
                    break;
            }
        } catch (Exception e) {
//            System.out.println(e.getMessage());
        }
//        System.out.println(children.size());
        return children;
    }
}
