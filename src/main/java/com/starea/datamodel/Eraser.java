package com.starea.datamodel;

import javafx.scene.paint.Color;

import java.util.List;

/**
 * Define any erasing path on the canvas
 */
public class Eraser extends DrawingObject {

    //region Private Attributes
    private List<Double> path;
    private double eraserSize;
    //endregion

    //region Constructor
    public Eraser(List<Double> path, double eraserSize) {
        this.path = path;
        this.eraserSize = eraserSize;
    }
    //endregion

    //region Public Methods
    public List<Double> getPath() {
        return path;
    }

    public void setPath(List<Double> path) {
        this.path = path;
    }

    public double getEraserSize() {
        return eraserSize;
    }

    public void setEraserSize(double eraserSize) {
        this.eraserSize = eraserSize;
    }
}
//endregion
