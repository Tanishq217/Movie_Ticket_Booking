package org.example.computer;


import java.util.*;

public class Screen {
    private int screenNumber;
    private int rows;
    private int cols;
    private Map<String, Double> basePrices;

    public Screen(int screenNumber, int rows, int cols) {
        this.screenNumber = screenNumber;
        this.rows = rows;
        this.cols = cols;
        this.basePrices = new HashMap<>();
    }

    public void setBasePrice(String category, double price) {
        basePrices.put(category, price);
    }

    public double getBasePrice(String category) {
        return basePrices.getOrDefault(category, 200.0);
    }

    public int getScreenNumber() { return screenNumber; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
}