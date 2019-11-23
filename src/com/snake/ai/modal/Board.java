package com.snake.ai.modal;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static com.snake.ai.modal.GameState.*;
import static com.snake.ai.utils.Utils.getDenyDir;

public class Board extends JPanel implements Runnable {
    private final int HEAD_SNAKE_INDEX = 0;
    private final int EMPTY_HEAD_SNAKE = -2;
    private final int EMPTY_BODY_SNAKE = -3;
    private final int EMPTY_FOOD = 9;
    private final int blockSize = 10; // size of each cell in the GUI
    private final int num_food = 100;
    private final long updateTime = 30; // (lower = faster, higher = slower)
    private long timeStart;
    private ArrayList<Snake> snakes = new ArrayList<>();
    private ArrayList<Point> foods = new ArrayList<>();
    private Item[][] board;


    public Board() {
        setPreferredSize(new Dimension(boardW * blockSize, boardH * blockSize));
        setFocusable(true);
        board = new Item[boardW][boardH];
        initBoard();
        timeStart = System.currentTimeMillis();
        Thread thread = new Thread(this);
        thread.start();
    }

    private void initBoard() {
        for (int i = 0; i < boardW; i++)
            for (int j = 0; j < boardH; j++)
                board[i][j] = Item.Empty;
        initData();
    }

    private void initData() {
        snakes.add(new Snake(new Point(2, 2), Direction.Up, Color.green, foods, board));
        snakes.add(new Snake(new Point(56, 6), Direction.Up, Color.orange, foods, board));
        snakes.add(new Snake(new Point(27, 8), Direction.Up, Color.yellow, foods, board));
        snakes.add(new Snake(new Point(30, 22), Direction.Up, Color.cyan, foods, board));
//        snakes.add(new Player(new Point(30, 9), Direction.Up, Color.pink));
        Random rand = new Random();
        for (int i = 0; i < num_food; i++) {
            int x = rand.nextInt(boardW);
            int y = rand.nextInt(boardH);
            Point point = new Point(x, y);
            if (!foods.contains(point) && x > 2 && x < boardW - 2 && y > 2 && y < boardH - 2) foods.add(point);
        }
//        for (Snake snake : snakes)
//            snake.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateBoard();
        drawBoard(g);
    }

    private synchronized int indexHeadCell(Point point) {
        for (int i = 0; i < snakes.size(); i++)
            if (point.equals(snakes.get(i).coords.get(HEAD_SNAKE_INDEX)))
                return i;
        return EMPTY_HEAD_SNAKE;
    }

    private synchronized int indexBodyCell(Point point) {
        for (int i = 0; i < snakes.size(); i++)
            if (snakes.get(i).coords.contains(point)) {
                return i;
            }
        return EMPTY_BODY_SNAKE;
    }

    private synchronized void drawBoard(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, getSize().width, getSize().height);
        for (int i = 0; i < boardW; i++) {
            for (int j = 0; j < boardH; j++) {
                Item cell = board[i][j];
                if (cell == Item.Empty) {
                    g.setColor(Color.white);
                } else if (cell == Item.Food) {
                    g.setColor(Color.blue);
                } else if (cell == Item.NearestFood)
                    g.setColor(Color.red);
                else if (cell == Item.NearMove)
                    g.setColor(Color.pink);
                else {
                    int index = indexHeadCell(new Point(i, j));
                    if (index != EMPTY_HEAD_SNAKE) {
                        g.fillRect((i * blockSize) + 1, (j * blockSize) + 1, blockSize - 1, blockSize - 1);
                        g.setColor(snakes.get(index).color.darker().darker());
                    } else {
                        index = indexBodyCell(new Point(i, j));
                        if (index != EMPTY_BODY_SNAKE) {
                            g.setColor(snakes.get(index).color);
                        } else
                            g.setColor(Color.white);
                    }
                }
                g.fillRect((i * blockSize) + 1, (j * blockSize) + 1, blockSize - 1, blockSize - 1);
            }
        }

    }

    private synchronized void snakeMove() {
        try {
            for (Snake snake : snakes) {
                if (snake.isAlive) {
                    if (isEat(snake))
                        eat(snake);
                    else move(snake);
                } else snakes.remove(snake);
                clearNearMove();
            }
        } catch (Exception ignored) {
        }
    }


    private synchronized void eat(Snake snake) {
        Direction direction = snake.nextMove;
        Point point = snake.coords.get(HEAD_SNAKE_INDEX);
        Point nextPoint = snake.newPointAfterMove(point, direction);
        snake.score++;
        snake.coords.add(HEAD_SNAKE_INDEX, nextPoint);
        foods.remove(nextPoint);
    }

    private synchronized void move(Snake snake) {
        Direction direction = snake.nextMove;
//        if (direction == snake.denyDir) System.out.println("wrong");
//        if (direction != snake.denyDir) {
        snake.denyDir = getDenyDir(direction);
        Point headPoint = snake.coords.get(HEAD_SNAKE_INDEX);
        Point newPoint = snake.newPointAfterMove(headPoint, direction);
        try {
            if (board[newPoint.x][newPoint.y] == Item.Snake || board[newPoint.x][newPoint.y] == Item.SnakeHead) {
                System.out.println("dead " + board[newPoint.x][newPoint.y]);
                for (Point p : snake.coords)
                    board[p.x][p.y] = Item.Empty;
                snakes.remove(snake);
            } else {
            snake.coords.add(HEAD_SNAKE_INDEX, newPoint);
            Point tailPoint = snake.coords.get(snake.coords.size() - 1);
            board[tailPoint.x][tailPoint.y] = Item.Empty;
            snake.coords.remove(tailPoint);
            }
        } catch (Exception e) {
            System.out.println("dead out of move " + e.getMessage());
        }

//        }
    }

    private synchronized boolean isEat(Snake snake) {
        Direction direction = snake.nextMove;
        Point point = snake.coords.get(HEAD_SNAKE_INDEX);
        Point nextPoint = snake.newPointAfterMove(point, direction);
        Item check;
        try {
            check = board[nextPoint.x][nextPoint.y];
        } catch (Exception e) {
            check = null;
        }
        return (check == Item.Food || check == Item.NearestFood);
    }

    private synchronized void updateBoard() {
        for (Snake snake : snakes)
            for (int i = 0; i < snake.coords.size(); i++)
                board[snake.coords.get(i).x][snake.coords.get(i).y] = i == 0 ? Item.SnakeHead : Item.Snake;
        for (Point food : foods)
            board[food.x][food.y] = board[food.x][food.y] == Item.NearestFood ? Item.NearestFood : Item.Food;
    }


    private synchronized void clearNearMove() {
        for (int i = 0; i < boardW; i++)
            for (int j = 0; j < boardH; j++)
                if (board[i][j] == Item.NearMove) board[i][j] = Item.Empty;
    }

    @Override
    public void run() {
        boolean isDone = false, isRun = false;
        while (true) {
            long timeEnd = System.currentTimeMillis();
            if (!isRun) {
                isRun = true;
                for (Snake snake : snakes)
                    snake.run();
            } else {
                isDone = true;
                for (Snake snake : snakes) {
                    if (!snake.isFindDone) {
                        isDone = false;
                        break;
                    }
                }
            }
            if (timeEnd - timeStart >= updateTime) {
                if (isDone) {
                    isRun = false;
                    snakeMove();
                    if (foods.isEmpty()) System.exit(EMPTY_FOOD);
                    this.repaint();
                    timeStart = timeEnd;
                }
            }
        }
    }


}
