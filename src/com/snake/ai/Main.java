package com.snake.ai;

import com.snake.ai.modal.Board;

import javax.swing.*;

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
