package com.snake.ai.modal;

import com.snake.ai.utils.Utils;

import java.awt.*;

import static com.snake.ai.modal.GameState.*;

public class Player extends Snake {

    public Player(Point startPoint, Direction tailDirection, Color color) {
        super(startPoint, tailDirection, color);
        this.isBot = false;
    }

    @Override
    public void run() {
        while (isSnakeAlive()) {
            timeEnd = System.currentTimeMillis();
            if (timeEnd - timeStart >= updateTime) {
                    Direction move = Utils.getDirectionPlayer();
                    if (isEat(move)) {
                        eat(move);
                    } else
                        move(move);
                timeStart = timeEnd;
            }
        }
    }
}
