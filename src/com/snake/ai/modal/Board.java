package com.snake.ai.modal;

import com.snake.ai.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static com.snake.ai.modal.GameState.*;
import static com.snake.ai.modal.GameState.node;
import static com.snake.ai.modal.Snake.HEAD_SNAKE_INDEX;

public class Board extends JPanel implements Runnable {
    private static final int EMPTY_HEAD_SNAKE = -2;
    private static final int EMPTY_BODY_SNAKE = -3;
    private final int num_food = 15; // how many pieces of food there should be
    private final int boardW = 60; // how many cells wide the board will be
    private final int boardH = 60; // how many cells tall the board will be
    private final int blockSize = 10; // size of each cell in the GUI
    // must give enough time for each snake's AI to make a decision
    Thread thread;
    // don't modify these
    private boolean gameOn = true, paused, playable = true;
    //    private LinkedList<GameState> previous_turns;

    public Board() {
        setPreferredSize(new Dimension(boardW * blockSize, boardH * blockSize));
        setFocusable(true);
        board = new Item[boardW][boardH];
//        previous_turns = new LinkedList<>();
        initBoard();
        timeStart = System.currentTimeMillis();
        thread = new Thread(this);

    }

    private void initBoard() {
        for (int i = 0; i < boardW; i++)
            for (int j = 0; j < boardH; j++)
                board[i][j] = Item.Empty;
        initData();
    }

    private void renewSnake() {
        moves = null;
        isFound = false;
        foodFound = null;
        node.dept = 0;
        for (int i = 0; i < boardW; i++)
            for (int j = 0; j < boardH; j++)
                if (board[i][j] == Item.NEAR_NODE || board[i][j] == Item.ROOT_NODE)
                    board[i][j] = Item.Empty;
        node = new Node(snakes.get(0).coords.get(HEAD_SNAKE_INDEX), snakes.get(0).denyDir);
    }

    private void clearNearNode() {
        for (int i = 0; i < boardW; i++)
            for (int j = 0; j < boardH; j++)
                if (board[i][j] != Item.Snake && board[i][j] != Item.SnakeHead && board[i][j] != Item.Food)
                    board[i][j] = Item.Empty;
    }

    private void initData() {
        snakes.add(new Snake(new Point(15, 15), Direction.Down, Color.orange));
        node = new Node(snakes.get(0).coords.get(HEAD_SNAKE_INDEX), snakes.get(0).denyDir);
//        snakes.add(new Snake(new Point(3, 2), Direction.Up, Color.green));
//        foods.add(new Point(20, 15));
        foods.add(new Point(3, 15));
        foods.add(new Point(3, 25));
        foods.add(new Point(1, 5));
        foods.add(new Point(4, 15));
        foods.add(new Point(5, 25));
        foods.add(new Point(6, 35));
        foods.add(new Point(7, 45));
        foods.add(new Point(7, 45));
        foods.add(new Point(7, 45));
        foods.add(new Point(7, 45));
        foods.add(new Point(7, 45));
        foods.add(new Point(7, 45));
        foods.add(new Point(7, 45));
        foods.add(new Point(7, 45));
        foods.add(new Point(7, 45));
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            int x = rand.nextInt(50);
            int y = rand.nextInt(50);
            Point point = new Point(x, y);
            if(!foods.contains(point))  foods.add(point);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOn) {
            updateBoard();
            drawBoard(g);
        } else
            drawBoard(g);
    }

    private int indexHeadCell(Point point) {
        for (int i = 0; i < snakes.size(); i++)
            if (point.equals(snakes.get(i).coords.get(HEAD_SNAKE_INDEX)))
                return i;
        return EMPTY_HEAD_SNAKE;
    }

    private int indexBodyCell(Point point) {
        for (int i = 0; i < snakes.size(); i++)
            if (snakes.get(i).coords.contains(point)) {
                return i;
            }
        return EMPTY_BODY_SNAKE;
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, getSize().width, getSize().height);
        for (int i = 0; i < boardW; i++) {
            for (int j = 0; j < boardH; j++) {
                Item cell = board[i][j];
                if (cell == Item.ROOT_NODE)
                    g.setColor(Color.white);
                else if (cell == Item.NEAR_NODE)
                    g.setColor(Color.white);
                else if (cell == Item.Empty) {
                    g.setColor(Color.white);
                } else if (cell == Item.Food) {
                    g.setColor(Color.red);
                } else {
                    int index = indexHeadCell(new Point(i, j));
                    if (index != EMPTY_HEAD_SNAKE) {
                        g.setColor(snakes.get(index).color);
                    } else {
                        index = indexBodyCell(new Point(i, j));
                        if (index != EMPTY_BODY_SNAKE) {
                            System.out.println("body " + index);
                            g.setColor(snakes.get(index).color);
                        } else
                            g.setColor(Color.white);
                    }
                }
                g.fillRect((i * blockSize) + 1, (j * blockSize) + 1, blockSize - 1, blockSize - 1);
            }
        }

    }

    private void updateBoard() {
        for (Snake snake : snakes)
            for (int i = 0; i < snake.coords.size(); i++)
                board[snake.coords.get(i).x][snake.coords.get(i).y] = i == 0 ? Item.SnakeHead : Item.Snake;
        for (Point food : foods)
            board[food.x][food.y] = Item.Food;
    }

    boolean isFound = false;
    Node foodFound = null;
    Iterator<Direction> moves;

    void findFood(Node root) {
//        if (isFound) return;
        ArrayList<Node> list = root.getChildren();
        if (list.size() == 0) return;
        for (Node n : list) {
            if (isFound) break;
            if (board[n.position.x][n.position.y] == Item.Food) {
                System.out.println("found food" + n.position.toString());
                foodFound = n;
                ArrayList<Direction> movesToFood = new ArrayList<>();
                isFound = true;
                while (foodFound.root != null) {
                    movesToFood.add(0, foodFound.denyDir);
                    board[foodFound.root.position.x][foodFound.root.position.y] = Item.ROOT_NODE;
                    foodFound = foodFound.root;
                }
                moves = movesToFood.iterator();
//                this.repaint();
//                return;
            } else {
                board[n.position.x][n.position.y] = Item.NEAR_NODE;
                findFood(n);
            }
        }
    }


    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            timeEnd = System.currentTimeMillis();
            tickRepaint = timeEnd - timeStart;
            if (tickRepaint >= updateTime) {
                if (foods.isEmpty()) System.exit(9);
                if (node != null && node.dept < 1) findFood(node);
                else if (!snakes.isEmpty() && moves != null) {
                    if (moves.hasNext()) {
                        Direction next = moves.next();
                        if (snakes.get(0).isEat(Utils.getDirection(next))) {
                            renewSnake();
                            snakes.get(0).eat(Utils.getDirection(next));
                            System.out.println("eat food ");
                            this.repaint();
                        } else {
                            snakes.get(0).move(Utils.getDirection(next));
                        }
                    } else {
                        System.out.println(moves.hasNext());
//                        snakes.get(0).moveForward();
                        renewSnake();
                    }

                } else {
                    System.out.println("stuck");
                    snakes.get(0).moveForward();
                    renewSnake();
                }
                this.repaint();
                timeStart = timeEnd;
            }
        }
    }
}
