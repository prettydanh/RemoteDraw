package com.starea.datamodel;

import javafx.scene.paint.Color;

/**
 * Define any ellipse that is drew on the canvas
 * A child of Shape class
 */
public class Ellipse extends Shape {

    //region Private Attributes
    private double width;
    private double height;
    //endregion

    //region Constructor
    public Ellipse(double x1, double y1, double x2, double y2, boolean fill, Color color, int thickness) {
        super(x1, y1, x2, y2, fill, color, thickness);
    }
    //endregion

    //region Public Methods

    public double getWidth() {
        this.width = getX2() - getX1();
        return width;
    }

    public double getHeight() {
        this.height = getY2() - getY1();
        return height;
    }

    public boolean isOnEllipse(double x, double y) {
        double xCenter = getX2() - ((getX2() - getX1())/2);
        double yCenter = getY2() - ((getY2() - getY1())/2);
        double p = (Math.pow((x - xCenter), 2) / Math.pow(getWidth()/2, 2)) + (Math.pow((y - yCenter), 2) / Math.pow(getHeight()/2, 2));

        if(Math.round(p) == 1) {
            return true;
        }
        return false;
    }

    public boolean isInsideEllipse(double x, double y) {
        double xCenter = getX2() - ((getX2() - getX1())/2);
        double yCenter = getY2() - ((getY2() - getY1())/2);
        double p = (Math.pow((x - xCenter), 2) / Math.pow(getWidth()/2, 2)) + (Math.pow((y - yCenter), 2) / Math.pow(getHeight()/2, 2));

        if(p < 1) {
            return true;
        }
        return false;
    }

    //endregion
}
