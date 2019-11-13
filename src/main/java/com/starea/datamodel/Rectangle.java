package com.starea.datamodel;

import javafx.scene.paint.Color;

/**
 * Define any rectangle that is drew on the canvas
 * A child of Shape class
 */
public class Rectangle extends Shape {

    //region Private Attributes
    private String type;
    private double width;
    private double height;
    //endregion

    //region Constructor
    public Rectangle(double x1, double y1, double x2, double y2, boolean fill, Color color, int thickness, String type) {
        super(x1, y1, x2, y2, fill, color, thickness);
        this.type = type;
    }
    //endregion

    //region Public Methods
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getWidth() {
        width = getX2() - getX1();
        return width;
    }

    public double getHeight() {
        height = getY2() - getY1();
        return height;
    }

    public boolean isInsideRectangle(double x, double y) {
        if(x > getX1() && x < getX2() && y > getY1() && y < getY2()) {
            return true;
        }
        return false;
    }

    public boolean isOnRectangle(double x, double y) {
        if(y == getY1() || y == getY2()) {
            if(x >= getX1() && x <= getX2()) {
                return true;
            }
        }
        if(x == getX1() || x == getX2()) {
            if(y >= getY1() && y <= getY2()) {
                return true;
            }
        }
        return false;
    }
    //endregion
}
