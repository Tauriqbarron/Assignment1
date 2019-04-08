package com.example.paintview3;

public class ball {
    float x,y;
    int color;
    int radius;

    public void setColor(int color) {
        this.color = color;
    }

    public ball(float x, float y, int color, int radius) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = radius;
    }

    public ball(float x, float y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
