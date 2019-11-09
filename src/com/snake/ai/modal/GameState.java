package com.snake.ai.modal;

import java.awt.*;
import java.util.ArrayList;

public class GameState {
    public static Board.Item[][] board;
    public static ArrayList<Snake> snakes = new ArrayList<>();
    public static ArrayList<Point> foods = new ArrayList<>();
    public static long timeStart = System.currentTimeMillis();
    public static long timeEnd = 0;
    public static long updateTime = 60; // (lower = faster, higher = slower)
    public static int safeThread = 0;
}
