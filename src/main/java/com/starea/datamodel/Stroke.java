package com.starea.datamodel;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Define any stroke that is drew on the canvas
 */
public class Stroke {
    //region Private Attributes
    private List<Double> path;
    private double xtl;
    private double ytl;
    private double xrb;
    private double yrb;
    private Color color;
    private int penSize;
    //endregion

    //region Constructor
    public Stroke(List<Double> path, Color color, int penSize) {
        this.path = path;
        this.color = color;
        this.penSize = penSize;
    }
    //endregion

    //region Public Methods
    public List<Double> getPath() {
        return path;
    }

    public void setPath(List<Double> path) {
        this.path = path;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPenSize() {
        return penSize;
    }

    public void setPenSize(int penSize) {
        this.penSize = penSize;
    }

    public double getXtl() {
        return xtl;
    }

    public double getYtl() {
        return ytl;
    }

    public double getXrb() {
        return xrb;
    }

    public double getYrb() {
        return yrb;
    }

    public boolean isOnStroke(double x, double y) {
        for(int i=0; i<path.size()-2; i+=2) {
            if(x >= path.get(i) - 10 && x <= path.get(i) + 10 && y >= path.get(i+1) - 10 && y <= path.get(i+1) + 10) {
                return true;
            }
        }
        return false;
    }
    //endregion


}
