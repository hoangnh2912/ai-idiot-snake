package com.snake.ai;

import com.snake.ai.modal.Board;
import com.snake.ai.modal.Direction;
import com.snake.ai.modal.Node;
import com.snake.ai.modal.Snake;

import javax.swing.*;

import java.awt.*;

import static com.snake.ai.modal.GameState.*;
import static com.snake.ai.modal.Snake.HEAD_SNAKE_INDEX;

public class Main extends JFrame {
    public Main() {
        initFrame();
    }

    private void initFrame() {
        Board board = new Board();
        add(board);
        board.start();
        setResizable(false);
        setTitle("Con rắn dễ thương");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

}
