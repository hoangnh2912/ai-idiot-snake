package com.snake.ai.modal;

import java.awt.*;
import java.util.ArrayList;

public class GameState {
    public static Item[][] board;
    public static ArrayList<Snake> snakes = new ArrayList<>();
    public static ArrayList<Point> foods = new ArrayList<>();
    public static long timeStart = System.currentTimeMillis();
    public static long timeEnd = 0;
    public static long tickRepaint = 0;
    public static long updateTime = 60; // (lower = faster, higher = slower)
    public static int safeThread = 0;
    public static final int num_food = 100; // how many pieces of food there should be
    public static final int boardW = 60; // how many cells wide the board will be
    public static final int boardH = 60; // how many cells tall the board will be
    public static final int blockSize = 10; // size of each cell in the GUI
}
