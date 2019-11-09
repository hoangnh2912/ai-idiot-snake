package com.snake.ai.modal;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import static com.snake.ai.modal.GameState.*;
import static com.snake.ai.modal.Snake.HEAD_SNAKE_INDEX;

public class Board extends JPanel implements Runnable {
    private static final int EMPTY_HEAD_SNAKE = -2;
    private static final int EMPTY_BODY_SNAKE = -1;
    private final int num_food = 15; // how many pieces of food there should be
    private final int boardW = 50; // how many cells wide the board will be
    private final int boardH = 50; // how many cells tall the board will be
    private final int blockSize = 10; // size of each cell in the GUI
    // must give enough time for each snake's AI to make a decision
    private Item[] headTiles = {}; // SnakeHead
    private Item[] bodyTiles = {}; // Snake
    Thread thread;

    enum Item {Empty, Food, Snake, SnakeHead}

    // don't modify these
    private boolean gameOn = true, paused, playable = true;
    private LinkedList<GameState> previous_turns;

    public Board() {
        setPreferredSize(new Dimension(boardW * blockSize, boardH * blockSize));
        setFocusable(true);
        board = new Item[boardW][boardH];
        previous_turns = new LinkedList<>();
        initBoard();
        timeStart = System.currentTimeMillis();
        thread = new Thread(this);

    }

    private void initBoard() {
        for (int i = 0; i < boardW; i++)
            for (int j = 0; j < boardH; j++)
                board[i][j] = Item.Empty;
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
            if (snakes.get(i).coords.contains(point))
                return i;
        return EMPTY_BODY_SNAKE;
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, getSize().width, getSize().height);
        for (int i = 0; i < boardW; i++) {
            for (int j = 0; j < boardH; j++) {
                Item cell = board[i][j];
                if (cell == Item.Empty)
                    g.setColor(Color.white);
                else if (cell == Item.Food) {
                    g.setColor(Color.red);
                } else {
                    int index = indexHeadCell(new Point(i, j));
                    if (index != EMPTY_HEAD_SNAKE) {
                        // give head cells a black outline instead of grey
                        g.setColor(Color.black);
                        g.fillRect((i * blockSize), (j * blockSize), blockSize + 2, blockSize + 2);
                        g.setColor(snakes.get(index).color);
                    } else {
                        index = indexBodyCell(new Point(i, j));
                        if (index != EMPTY_BODY_SNAKE)
//                            System.exit(1);
                            g.setColor(snakes.get(index).color);
                    }
                }
                g.fillRect((i * blockSize) + 1, (j * blockSize) + 1, blockSize - 1, blockSize - 1);
            }
        }
        g.setColor(Color.black);
    }

    private void updateBoard() {
        for (Snake snake : snakes)
            for (int i = 0; i < snake.coords.size(); i++)
                board[snake.coords.get(i).x][snake.coords.get(i).y] = i == 0 ? Item.SnakeHead : Item.Snake;
        if (safeThread == 0) {
            safeThread = 1;
            for (Point food : foods)
                board[food.x][food.y] = Item.Food;
            safeThread = 0;
        }
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            timeEnd = System.currentTimeMillis();
            if (timeEnd - timeStart >= updateTime) {
                this.repaint();
                timeStart = timeEnd;
            }
        }

    }
}
