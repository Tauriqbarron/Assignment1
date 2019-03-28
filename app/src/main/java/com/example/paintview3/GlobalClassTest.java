package com.example.paintview3;

import android.app.Application;

public class GlobalClassTest extends Application {
    static int radius = 0;
    static int undoStackCount = 0;
    static int redoStackCount = 0;



    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
