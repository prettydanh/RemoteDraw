package com.starea.datamodel;

import javafx.scene.paint.Color;

/**
 * Define essential information about drawing and connection of the application
 */
public class Infrastructure {
    //region Private Attributes
    private static Infrastructure instance = new Infrastructure();
    private Color color;
    private int thickness;
    private DrawingMode mode;
    private int penSize;
    private int eraserSize;
    //endregion

    //region Public Methods

    /**
     * @return a instance of Infrastructure class
     */
    public static Infrastructure getInstance() {
        return instance;
    }

    public Color getColor() {
        return color;
    }

    public int getThickness() {
        return thickness;
    }

    public DrawingMode getMode() {
        return mode;
    }

    public int getPenSize() {
        return penSize;
    }

    public int getEraserSize() {
        return eraserSize;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public void setMode(DrawingMode mode) {
        this.mode = mode;
    }

    public void setPenSize(int penSize) {
        this.penSize = penSize;
    }

    public void setEraserSize(int eraserSize) {
        this.eraserSize = eraserSize;
    }
    //endregion
}
