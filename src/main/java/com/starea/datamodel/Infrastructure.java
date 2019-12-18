package com.starea.datamodel;

import javafx.scene.paint.Color;

/**
 * Define essential information about drawing and connection of the application
 */
public class Infrastructure {
    //region Private Attributes
    private static volatile Infrastructure instance;
    private static String color;
    private static int thickness;
    private static DrawingMode mode;
    private static int penSize;
    private static int eraserSize;
    private static volatile String code;
    private static volatile String protocol;
    private static volatile String data;
    private static volatile String name;
    private static volatile String connectionState;
    private static volatile String notification;
    private static volatile String incomingMessage;
    private static volatile String outgoingMessage;
    //endregion

    //region Public Methods

    /**
     * @return a instance of Infrastructure class
     */
    public static Infrastructure getInstance() {
        if (instance == null) {
            synchronized (Infrastructure.class) {
                if (instance == null) {
                    instance = new Infrastructure();
                }
            }
        }
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
        Infrastructure.color = color;
    }

    public void setThickness(int thickness) {
        Infrastructure.thickness = thickness;
    }

    public void setMode(DrawingMode mode) {
        Infrastructure.mode = mode;
    }

    public void setPenSize(int penSize) {
        Infrastructure.penSize = penSize;
    }

    public void setEraserSize(int eraserSize) {
        Infrastructure.eraserSize = eraserSize;
    }

    public synchronized String getCode() {
        return code;
    }

    public synchronized void setCode(String code) {
        Infrastructure.code = code;
    }

    public synchronized String getProtocol() {
        return protocol;
    }

    public synchronized void setProtocol(String protocol) {
        Infrastructure.protocol = protocol;
    }

    public synchronized String getData() {
        return data;
    }

    public synchronized void setData(String data) {
        Infrastructure.data = data;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        Infrastructure.name = name;
    }

    public synchronized String getNotification() {
        return notification;
    }

    public synchronized void setNotification(String notification) {
        Infrastructure.notification = notification;
    }

    public synchronized String getIncomingMessage() {
        return incomingMessage;
    }

    public synchronized void setIncomingMessage(String incomingMessage) {
        Infrastructure.incomingMessage = incomingMessage;
    }

    public synchronized String getOutgoingMessage() {
        return outgoingMessage;
    }

    public synchronized void setOutgoingMessage(String outgoingMessage) {
        Infrastructure.outgoingMessage = outgoingMessage;
    }

    public synchronized String getConnectionState() {
        return connectionState;
    }

    public synchronized void setConnectionState(String connectionState) {
        Infrastructure.connectionState = connectionState;
    }

    //endregion
}
