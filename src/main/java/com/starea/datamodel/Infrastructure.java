package com.starea.datamodel;

import javafx.scene.paint.Color;

/**
 * Define essential information about drawing and connection of the application
 */
public class Infrastructure {
    //region Private Attributes
    private static Infrastructure instance = new Infrastructure();
    private String color;
    private int thickness;
    private DrawingMode mode;
    private int penSize;
    private int eraserSize;
    private String code;
    private String protocol;
    private String data;
    private String joinCode;
    private String name;
    private String result;
    private String notification;
    //endregion

    //region Public Methods

    /**
     * @return a instance of Infrastructure class
     */
    public static Infrastructure getInstance() {
        return instance;
    }

    public String getColor() {
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

    public void setColor(String color) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    //endregion
}
