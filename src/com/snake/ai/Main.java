package com.snake.ai;

import com.snake.ai.modal.Board;
import com.snake.ai.modal.Direction;
import com.snake.ai.modal.Snake;

import javax.swing.*;

import java.awt.*;

import static com.snake.ai.modal.GameState.foods;
import static com.snake.ai.modal.GameState.snakes;

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
        initData();
    }

    private void initData() {
        snakes.add(new Snake(new Point(1, 2), Direction.Up, Color.orange));
        snakes.add(new Snake(new Point(3, 2), Direction.Up, Color.green));
        foods.add(new Point(3, 5));
        foods.add(new Point(3, 15));
        foods.add(new Point(3, 25));
        foods.add(new Point(3, 35));
        foods.add(new Point(3, 45));
        foods.add(new Point(1, 5));
        foods.add(new Point(1, 15));
        foods.add(new Point(1, 25));
        foods.add(new Point(1, 35));
        foods.add(new Point(1, 45));
        snakes.get(0).start();
        snakes.get(1).start();


    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }

}
