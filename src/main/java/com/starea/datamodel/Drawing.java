package com.starea.datamodel;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;


/**
 * Store and draw graphic elements like shape and stroke to the canvas
 */
public class Drawing {

    //region Private Attributes
    private List<DrawingObject> graphicElements;
    private List<DrawingObject> selectionGraphicElements;
    private GraphicsContext graphicsContext;
    private Canvas canvas;
    private double initX;
    private double initY;
    private double xDiff;
    private double yDiff;
    private boolean isFinishedErasing = true;
    //endregion

    //region Constructor
    public Drawing(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = this.canvas.getGraphicsContext2D();
        this.graphicElements = new ArrayList<>();
        this.selectionGraphicElements = new ArrayList<>();
    }
    //endregion

    //region Public methods

    /**
     * Draw every graphic elements which are being stored
     */
    public void Render() {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for(Object object: graphicElements) {
            if(object instanceof Rectangle) {
                Rectangle rectangle = ((Rectangle) object);
                if(((Rectangle) object).isFill()) {
                    graphicsContext.setFill(Color.valueOf(rectangle.getColor()));
                    graphicsContext.fillRect(rectangle.getX1(), rectangle.getY1(), rectangle.getWidth(), rectangle.getHeight());
                } else {
                    graphicsContext.setStroke(Color.valueOf(rectangle.getColor()));
                    graphicsContext.setLineWidth(rectangle.getThickness());
                    graphicsContext.strokeRect(rectangle.getX1(), rectangle.getY1(), rectangle.getWidth(), rectangle.getHeight());
                }
            }

            if(object instanceof Stroke) {
                Stroke stroke = ((Stroke) object);
                graphicsContext.setStroke(Color.valueOf(stroke.getColor()));
                graphicsContext.setLineWidth(stroke.getPenSize());
                for(int i=0; i<stroke.getPath().size()-3; i+=2) {
                    graphicsContext.strokeLine(stroke.getPath().get(i), stroke.getPath().get(i+1), stroke.getPath().get(i+2), stroke.getPath().get(i+3));
                }
            }

            if(object instanceof Ellipse) {
                Ellipse ellipse = ((Ellipse) object);
                if(ellipse.isFill()) {
                    graphicsContext.setFill(Color.valueOf(ellipse.getColor()));
                    graphicsContext.fillOval(ellipse.getX1(), ellipse.getY1(), ellipse.getWidth(), ellipse.getHeight());
                } else {
                    graphicsContext.setStroke(Color.valueOf(ellipse.getColor()));
                    graphicsContext.setLineWidth(ellipse.getThickness());
                    graphicsContext.strokeOval(ellipse.getX1(), ellipse.getY1(), ellipse.getWidth(), ellipse.getHeight());
                }
            }

            if(object instanceof Eraser) {
                Eraser eraser = ((Eraser) object);
                for(int i=0; i<eraser.getPath().size()-2; i+=2) {
                    graphicsContext.clearRect(eraser.getPath().get(i), eraser.getPath().get(i+1), eraser.getEraserSize(), eraser.getEraserSize());
                }
                if(!isFinishedErasing) {
                    if(graphicElements.get(graphicElements.size() - 1) instanceof Eraser) {
                        Eraser latestEraser = ((Eraser) graphicElements.get(graphicElements.size() - 1));
                        int size = latestEraser.getPath().size();
                        graphicsContext.setLineWidth(3);
                        graphicsContext.setStroke(Color.RED);
                        graphicsContext.strokeRect(latestEraser.getPath().get(size-2), latestEraser.getPath().get(size-1), latestEraser.getEraserSize(), latestEraser.getEraserSize());
                    }
                }
            }
        }
    }

    /**
     * Remove the last element in graphicElements list
     */
    public void Undo() {
        if(!graphicElements.isEmpty()) {
            graphicElements.remove(graphicElements.size()-1);
            Render();
        }
    }

    /**
     * Clear canvas
     */
    public void Clear() {
        graphicElements.clear();
        selectionGraphicElements.clear();
        Render();
    }

    public List<DrawingObject> getSelectionGraphicElements() {
        return selectionGraphicElements;
    }

    public void setSelectionGraphicElements(List<DrawingObject> selectionGraphicElements) {
        this.selectionGraphicElements = selectionGraphicElements;
    }

    public List<DrawingObject> getGraphicElements() {
        return graphicElements;
    }

    public void setGraphicElements(List<DrawingObject> graphicElements) {
        this.graphicElements = graphicElements;
    }

    /**
     * Move selection object corresponding to the position of the mouse when is is being dragged
     */
    public void moveSelection(double x, double y) {
        for(Object object: selectionGraphicElements) {
            if(object instanceof Rectangle || object instanceof Ellipse) {
                ((Shape) object).setX1(((Shape) object).getX1() + x);
                ((Shape) object).setY1(((Shape) object).getY1() + y);
                ((Shape) object).setX2(((Shape) object).getX2() + x);
                ((Shape) object).setY2(((Shape) object).getY2() + y);
            }

            if(object instanceof Stroke) {
                for(int i=0; i<((Stroke) object).getPath().size()-1; i+=2) {
                    ((Stroke) object).getPath().set(i, ((Stroke) object).getPath().get(i) + x);
                    ((Stroke) object).getPath().set(i+1, ((Stroke) object).getPath().get(i+1) + y);
                }
            }
        }
        Render();
    }

    public void initSelection(double x, double y) {
        initX = x;
        initY = y;
    }

    public void onSelection(double x, double y) {
        xDiff = x - initX;
        yDiff = y - initY;
        initX = x;
        initY = y;
        moveSelection(xDiff, yDiff);
    }

    /**
     * Add a new rectangle to object list when mouse is pressed
     * @param x,y top-left point of rectangle
     * @param fill true if want to draw a filled rectangle
     * @param thickness the width of border
     */
    public void initDrawRect(double x, double y, boolean fill, String color, int thickness) {
        graphicElements.add(new Rectangle(x, y, x, y, fill, color, thickness, "solid"));
        initX = x;
        initY = y;
    }

    /**
     * Draw the latest rectangle in the object list corresponding to the position of mouse when it is being dragged
     * @param x,y right-bottom point of rectangle
     */
    public void onDrawRect(double x, double y) {
        Rectangle rect = ((Rectangle) graphicElements.get(graphicElements.size()-1));
        if(x > initX) {
            if(y > initY) {
                rect.setX1(initX);
                rect.setY1(initY);
                rect.setX2(x);
                rect.setY2(y);
            }
            if(y < initY) {
                rect.setX1(initX);
                rect.setY1(y);
                rect.setX2(x);
                rect.setY2(initY);
            }
        }

        if(x < initX) {
            if(y > initY) {
                rect.setX1(x);
                rect.setY1(initY);
                rect.setX2(initX);
                rect.setY2(y);
            }
            if(y < initY) {
                rect.setX1(x);
                rect.setY1(y);
                rect.setX2(initX);
                rect.setY2(initY);
            }
        }
        Render();
    }

    /**
     * Add a new ellipse to object list when mouse is pressed
     * @param x,y top-left point of ellipse
     * @param fill true if want to draw a filled rectangle
     * @param thickness the width of border
     */
    public void initDrawEllipse(double x, double y, boolean fill, String color, int thickness) {
        graphicElements.add(new Ellipse(x, y, x, y, fill, color, thickness));
        initX = x;
        initY = y;
    }

    /**
     * Draw the latest ellipse in the object list corresponding to the position of mouse when it is being dragged
     * @param x,y right-bottom point of ellipse
     */
    public void onDrawEllipse(double x, double y) {
        Ellipse ellipse = ((Ellipse) graphicElements.get(graphicElements.size()-1));
        if(x > initX) {
            if(y > initY) {
                ellipse.setX1(initX);
                ellipse.setY1(initY);
                ellipse.setX2(x);
                ellipse.setY2(y);
            }
            if(y < initY) {
                ellipse.setX1(initX);
                ellipse.setY1(y);
                ellipse.setX2(x);
                ellipse.setY2(initY);
            }
        }

        if(x < initX) {
            if(y > initY) {
                ellipse.setX1(x);
                ellipse.setY1(initY);
                ellipse.setX2(initX);
                ellipse.setY2(y);
            }
            if(y < initY) {
                ellipse.setX1(x);
                ellipse.setY1(y);
                ellipse.setX2(initX);
                ellipse.setY2(initY);
            }
        }
        Render();
    }


    /**
     * Add a new stroke to object list when mouse is pressed
     * @param x,y first point added to the path of the stroke
     * @param penSize the width of the stroke
     */
    public void initDrawStroke(double x, double y, String color, int penSize) {
        List<Double> path = new ArrayList<>();
        path.add(x);
        path.add(y);
        path.add(x);
        path.add(y);
        graphicElements.add(new Stroke(path, color, penSize));
    }

    /**
     * Draw the latest stroke in the stroke list corresponding to the position of mouse when it is being dragged
     * @param x,y new point added to the path of the stroke
     */
    public void onDrawStroke(double x, double y) {
        Stroke stroke = ((Stroke) graphicElements.get(graphicElements.size()-1));
        stroke.getPath().add(x);
        stroke.getPath().add(y);
        Render();
    }

    /**
     * Add a new eraser to object list
     * @param eraserSize
     */
    public void initEraser(double x, double y, double eraserSize) {
        isFinishedErasing = false;
        List<Double> eraserPath = new ArrayList<>();
        eraserPath.add(x);
        eraserPath.add(y);
        eraserPath.add(x);
        eraserPath.add(y);
        graphicElements.add(new Eraser(eraserPath, eraserSize));
    }

    /**
     * Clear every stroke corresponding to the position of mouse when it is being dragged
     */
    public void onEraser(double x, double y) {
        Eraser eraser = ((Eraser) graphicElements.get(graphicElements.size()-1));
        eraser.getPath().add(x);
        eraser.getPath().add(y);
        Render();
    }

    public void finishErasing(boolean isFinished) {
        this.isFinishedErasing = isFinished;
        Render();
    }
    //endregion
}
