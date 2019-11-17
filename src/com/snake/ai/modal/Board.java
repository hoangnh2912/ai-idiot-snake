package com.snake.ai.modal;

import com.snake.ai.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static com.snake.ai.modal.GameState.*;
import static com.snake.ai.modal.Snake.HEAD_SNAKE_INDEX;

public class Board extends JPanel implements Runnable {
    private static final int EMPTY_HEAD_SNAKE = -2;
    private static final int EMPTY_BODY_SNAKE = -3;

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



    private void clearNearNode() {
        for (int i = 0; i < boardW; i++)
            for (int j = 0; j < boardH; j++)
                if (board[i][j] == Item.NEAR_NODE)
                    board[i][j] = Item.Empty;
    }

    private void initData() {
        snakes.add(new Snake(new Point(2, 2), Direction.Up, Color.green));
//        snakes.add(new Snake(new Point(3, 3), Direction.Down, Color.orange));
//        snakes.add(new Snake(new Point(4, 4), Direction.Down, Color.yellow));
//        snakes.add(new Snake(new Point(5, 5), Direction.Down, Color.blue));
        snakes.add(new Player(new Point(6, 6), Direction.Up, Color.orange));
        Random rand = new Random();
        for (int i = 0; i < num_food; i++) {
            int x = rand.nextInt(boardW);
            int y = rand.nextInt(boardH);
            Point point = new Point(x, y);
            if (!foods.contains(point) && x > 2 && x < boardW - 2 && y > 2 && y < boardH - 2) foods.add(point);
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
                if (cell == Item.NEAR_NODE)
                    g.setColor(Color.blue);
                else if (cell == Item.Empty) {
                    g.setColor(Color.white);
                } else if (cell == Item.Food) {
                    g.setColor(Color.red);
                } else {
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

    private void updateBoard() {
        for (Snake snake : snakes)
            for (int i = 0; i < snake.coords.size(); i++)
                board[snake.coords.get(i).x][snake.coords.get(i).y] = i == 0 ? Item.SnakeHead : Item.Snake;
        for (Point food : foods)
            board[food.x][food.y] = Item.Food;
    }


    public void start() {
        thread.start();
//        snakes.get(0).start();
        snakes.get(1).start();
//        snakes.get(2).start();
//        snakes.get(3).start();
    }

    @Override
    public void run() {
        while (true) {
            timeEnd = System.currentTimeMillis();
            tickRepaint = timeEnd - timeStart;
            if (tickRepaint >= updateTime) {
                if (foods.isEmpty()) System.exit(9);
                this.repaint();
                timeStart = timeEnd;
            }
        }
    }
}
