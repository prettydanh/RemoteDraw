package com.starea;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleNode;
import com.starea.datamodel.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.lang.Object;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;

public class FXMLController {
    //region Private Attributes
    @FXML
    private BorderPane leftMenuContainer;
    @FXML
    private Canvas canvas;
    @FXML
    private StackPane mainWindow;
    @FXML
    private BorderPane artBoard;
    @FXML
    private SplitMenuButton dropDownMenu;

    private GridPane colorMenu;
    private GridPane pencilMenu;
    private GridPane eraserMenu;
    private GridPane shapeMenu;
    private ColorPicker picker;
    private List<JFXButton> colorListBtn;
    private List<JFXButton> shapeListBtn;
    private JFXSlider penSizeSlider;
    private JFXSlider eraserSizeSlider;
    private JFXSlider thicknessSlider;
    private Drawing drawing;
    private boolean isChanged = false;
    //endregion

    //region Public Method

    /**
     * This function run before the application show up
     */
    @FXML
    public void initialize() {
        prepare();
    }

    /**
     * Handle button clicked event
     */
    @FXML
    public void onButtonClicked(ActionEvent e) {
        if (((JFXButton) e.getSource()).getId().equals("colorBtn")) {
            leftMenuContainer.setMargin(colorMenu, new Insets(110, 0, 0, 10));
            leftMenuContainer.setRight(colorMenu);
        } else {
            leftMenuContainer.getChildren().remove(colorMenu);
        }

        if (((JFXButton) e.getSource()).getId().equals("pencilBtn")) {
            leftMenuContainer.setMargin(pencilMenu, new Insets(155, 0, 0, 10));
            leftMenuContainer.setRight(pencilMenu);
            Infrastructure.getInstance().setMode(DrawingMode.PENCIL);
        } else {
            leftMenuContainer.getChildren().remove(pencilMenu);
        }

        if (((JFXButton) e.getSource()).getId().equals("eraserBtn")) {
            leftMenuContainer.setMargin(eraserMenu, new Insets(200, 0, 0, 10));
            leftMenuContainer.setRight(eraserMenu);
            Infrastructure.getInstance().setMode(DrawingMode.ERASER);
        } else {
            leftMenuContainer.getChildren().remove(eraserMenu);
        }

        if (((JFXButton) e.getSource()).getId().equals("shapeBtn")) {
            leftMenuContainer.setMargin(shapeMenu, new Insets(245, 0, 0, 10));
            leftMenuContainer.setRight(shapeMenu);
        } else {
            leftMenuContainer.getChildren().remove(shapeMenu);
        }

        if (((JFXButton) e.getSource()).getId().equals("selectBtn")) {
            Infrastructure.getInstance().setMode(DrawingMode.SELECT);
        }
        if (((JFXButton) e.getSource()).getId().equals("eraserBtn")) {
            Infrastructure.getInstance().setMode(DrawingMode.ERASER);
        }

        if (((JFXButton) e.getSource()).getId().equals("undoBtn")) {
            drawing.Undo();
        }

        if (((JFXButton) e.getSource()).getId().equals("exportBtn")) {
            export();
        }
    }

    @FXML
    public void OnMenuItemSelected(ActionEvent e) {
        if (isChanged) {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Remote Draw");
            alert.setContentText("Export this art board before refreshing?");
            ButtonType exportBtn = new ButtonType("Export");
            ButtonType newBtn = new ButtonType("New");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(exportBtn, newBtn, cancelBtn);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == exportBtn) {
                export();
            } else if (result.get() == newBtn) {
                drawing.Clear();
            } else {
                alert.close();
            }
        } else {
            drawing.Clear();
        }
    }
    //endregion

    //region Private Methods

    /**
     * Initialize essential information for the application
     */
    private void prepare() {

        //region Initialize color, thickness, pen size, zoom percent, eraser size and the current function
        Infrastructure.getInstance().setColor(Color.web("#000000"));
        Infrastructure.getInstance().setThickness(1);
        Infrastructure.getInstance().setPenSize(1);
        Infrastructure.getInstance().setEraserSize(1);
        Infrastructure.getInstance().setMode(DrawingMode.SELECT);
        //endregion

        //region Create the width and height of the canvas
        canvas.widthProperty().bind(mainWindow.widthProperty());
        canvas.heightProperty().bind(mainWindow.heightProperty());
        //endregion

        //region Create list of color buttons
        colorListBtn = new ArrayList<>();
        colorListBtn.add(new JFXButton("", new Circle(10, Color.BLACK)));
        colorListBtn.add(new JFXButton("", new Circle(10, Color.RED)));
        colorListBtn.add(new JFXButton("", new Circle(10, Color.GREEN)));
        colorListBtn.add(new JFXButton("", new Circle(10, Color.BLUE)));
        colorListBtn.add(new JFXButton("", new Circle(10, Color.YELLOW)));
        colorListBtn.add(new JFXButton("", new Circle(10, Color.PURPLE)));
        colorListBtn.add(new JFXButton("", new Circle(10, Color.BROWN)));

        for (JFXButton btn : colorListBtn) {
            btn.getStyleClass().addAll("btn", "btn-default", "menu-button");
        }
        //endregion

        //region Create color picker
        picker = new ColorPicker();
        picker.setValue(Color.BLACK);
        picker.getStyleClass().addAll("btn", "btn-default", "menu-button");
        //endregion

        //region Create pen size slider
        penSizeSlider = new JFXSlider(1, 10, 1);
        penSizeSlider.getStyleClass().add("jfx-slider");
        penSizeSlider.setShowTickLabels(true);
        penSizeSlider.setBlockIncrement(1);
        penSizeSlider.setMinWidth(200);
        //endregion

        //region Create eraser size slider
        eraserSizeSlider = new JFXSlider(1, 10, 1);
        eraserSizeSlider.getStyleClass().add("jfx-slider");
        eraserSizeSlider.setShowTickLabels(true);
        eraserSizeSlider.setBlockIncrement(1);
        eraserSizeSlider.setMinWidth(200);
        //endregion

        //region Create list of shape buttons
        shapeListBtn = new ArrayList<>();

        //Not filled rectangle button
        Rectangle rect1 = new Rectangle(40, 20);
        rect1.setFill(Color.TRANSPARENT);
        rect1.setStroke(Color.BLACK);
        JFXButton notFilledRectButton = new JFXButton("", rect1);
        notFilledRectButton.setId("notFilledRectButton");
        shapeListBtn.add(notFilledRectButton);

        //Filled rectangle button
        Rectangle rect2 = new Rectangle(40, 20);
        rect2.setFill(Color.BLACK);
        JFXButton filledRectButton = new JFXButton("", rect2);
        filledRectButton.setId("filledRectButton");
        shapeListBtn.add(filledRectButton);

        //Not filled circle button
        Circle cir1 = new Circle(10);
        cir1.setFill(Color.TRANSPARENT);
        cir1.setStroke(Color.BLACK);
        JFXButton notFilledEllipseButton = new JFXButton("", cir1);
        notFilledEllipseButton.setId("notFilledEllipseButton");
        shapeListBtn.add(notFilledEllipseButton);

        //Filled circle button
        Circle cir2 = new Circle(10);
        cir2.setFill(Color.BLACK);
        JFXButton filledEllipseButton = new JFXButton("", cir2);
        filledEllipseButton.setId("filledEllipseButton");
        shapeListBtn.add(filledEllipseButton);

        for (JFXButton btn : shapeListBtn) {
            btn.getStyleClass().addAll("btn", "btn-default", "menu-button");
        }
        //endregion

        //region Create thickness slider
        thicknessSlider = new JFXSlider(1, 10, 1);
        thicknessSlider.getStyleClass().add("jfx-slider");
        thicknessSlider.setShowTickLabels(true);
        thicknessSlider.setBlockIncrement(1);
        thicknessSlider.setMinWidth(200);
        //endregion

        //region Create popup menu for color, pencil, eraser and shape buttons
        createColorMenu();
        createPencilMenu();
        createEraserMenu();
        createShapeMenu();
        //endregion

        //region Handle events
        //Handle color changed event
        onColorChanged();

        //Handle pen size changed event
        onPenSizeChanged();

        //Handle eraser size changed event
        onEraserSizeChanged();

        //Handle thickness size changed event
        onThicknessChanged();

        //Handle shape changed event
        onShapeChanged();

        //Handle mouse action event
        onMouseActionEvent();


        //endregion
        drawing = new Drawing(canvas);
    }

    /**
     * Close any popup menu
     */
    private void clearMenu() {
        leftMenuContainer.getChildren().remove(colorMenu);
        leftMenuContainer.getChildren().remove(pencilMenu);
        leftMenuContainer.getChildren().remove(eraserMenu);
        leftMenuContainer.getChildren().remove(shapeMenu);
    }

    /**
     * Create popup menu to choose color
     */
    private void createColorMenu() {
        colorMenu = new GridPane();
        colorMenu.getStyleClass().add("menu");
        colorMenu.setMaxHeight(120);

        HBox hb = new HBox();
        hb.getChildren().addAll(colorListBtn);

        Line bl = new Line();
        bl.setOpacity(0.2);
        bl.startXProperty().bind(colorMenu.scaleXProperty());
        bl.startYProperty().bind(colorMenu.scaleYProperty());
        bl.setEndX(bl.getStartX() + 300);
        bl.setEndY(bl.getStartY());

        colorMenu.setVgap(10);
        colorMenu.add(hb, 0, 0);
        colorMenu.add(bl, 0, 1);
        colorMenu.add(picker, 0, 2);
    }

    /**
     * Create popup menu to choose pen size
     */
    private void createPencilMenu() {
        pencilMenu = new GridPane();
        pencilMenu.getStyleClass().add("menu");
        pencilMenu.setMaxHeight(80);

        Label lb = new Label();
        lb.setText("Pen size");
        lb.setTextFill(Color.web("#c9c9c9"));
        lb.setStyle("-fx-font-family: 'Open Sans ExtraBold'; -fx-font-size: 15px;");

        pencilMenu.setVgap(10);
        pencilMenu.add(lb, 0, 0);
        pencilMenu.add(penSizeSlider, 0, 1);
    }

    /**
     * Create popup menu to choose eraser size
     */
    private void createEraserMenu() {
        eraserMenu = new GridPane();
        eraserMenu.getStyleClass().add("menu");
        eraserMenu.setMaxHeight(80);

        Label lb = new Label();
        lb.setText("Eraser size");
        lb.setTextFill(Color.web("#c9c9c9"));
        lb.setStyle("-fx-font-family: 'Open Sans ExtraBold'; -fx-font-size: 15px;");

        eraserMenu.setVgap(10);
        eraserMenu.add(lb, 0, 0);
        eraserMenu.add(eraserSizeSlider, 0, 1);
    }

    /**
     * Create popup menu to choose shape for drawing
     */
    private void createShapeMenu() {
        shapeMenu = new GridPane();
        shapeMenu.getStyleClass().add("menu");
        shapeMenu.setMaxHeight(120);

        HBox hb = new HBox();
        hb.getChildren().addAll(shapeListBtn);

        Line bl = new Line();
        bl.setOpacity(0.2);
        bl.startXProperty().bind(colorMenu.scaleXProperty());
        bl.startYProperty().bind(colorMenu.scaleYProperty());
        bl.setEndX(bl.getStartX() + 300);
        bl.setEndY(bl.getStartY());

        Label lb = new Label();
        lb.setText("Border size");
        lb.setTextFill(Color.web("#c9c9c9"));
        lb.setStyle("-fx-font-family: 'Open Sans ExtraBold'; -fx-font-size: 15px;");

        shapeMenu.setVgap(7);
        shapeMenu.add(hb, 0, 0);
        shapeMenu.add(lb, 0, 1);
        shapeMenu.add(thicknessSlider, 0, 2);
    }

    /**
     * Listen to the changes from color picker and handle color buttons clicked event
     */
    private void onColorChanged() {
        for (JFXButton btn : colorListBtn) {
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    clearMenu();
                    Color t = ((Color) ((Circle) btn.getGraphic()).getFill());
                    picker.setValue(t);
                    Infrastructure.getInstance().setColor(t);
                }
            });
        }

        picker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                Infrastructure.getInstance().setColor(newValue);
            }
        });
    }

    /**
     * Listen to the changes of pen size slider
     */
    private void onPenSizeChanged() {
        penSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Infrastructure.getInstance().setPenSize(newValue.intValue());
            }
        });
    }

    /**
     * Listen to the changes of eraser size slider
     */
    private void onEraserSizeChanged() {
        eraserSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Infrastructure.getInstance().setEraserSize(newValue.intValue());
            }
        });
    }

    /**
     * Listen to the changes of thickness slider
     */
    private void onThicknessChanged() {
        thicknessSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Infrastructure.getInstance().setThickness(newValue.intValue());
            }
        });
    }

    /**
     * Handle shape button clicked
     */
    private void onShapeChanged() {
        for (JFXButton btn : shapeListBtn) {
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    clearMenu();
                    switch (((JFXButton) event.getSource()).getId()) {
                        case "notFilledRectButton":
                            Infrastructure.getInstance().setMode(DrawingMode.RECT);
                            break;
                        case "filledRectButton":
                            Infrastructure.getInstance().setMode(DrawingMode.FILLRECT);
                            break;
                        case "notFilledEllipseButton":
                            Infrastructure.getInstance().setMode(DrawingMode.ELLIPSE);
                            break;
                        case "filledEllipseButton":
                            Infrastructure.getInstance().setMode(DrawingMode.FILLELLIPSE);
                            break;
                    }
                }
            });
        }
    }

    /**
     * Export canvas
     */
    private void export() {
        isChanged = false;
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users\\stare\\Desktop"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fc.setTitle("Save Map");
        File file = fc.showSaveDialog(mainWindow.getScene().getWindow());
        if (file != null) {
            WritableImage wi = new WritableImage((int)mainWindow.getWidth(), (int)mainWindow.getHeight());
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, wi), null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handle mouse action event
     */
    private void onMouseActionEvent() {
        artBoard.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                drawing.Render();
            }
        });

        artBoard.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                clearMenu();
                switch (Infrastructure.getInstance().getMode()) {
                    case RECT:
                        isChanged = true;
                        drawing.initDrawRect(e.getX(),
                                e.getY(),
                                false,
                                Infrastructure.getInstance().getColor(),
                                Infrastructure.getInstance().getThickness());
                        break;
                    case FILLRECT:
                        isChanged = true;
                        drawing.initDrawRect(e.getX(),
                                e.getY(),
                                true,
                                Infrastructure.getInstance().getColor(),
                                Infrastructure.getInstance().getThickness());
                        break;
                    case PENCIL:
                        isChanged = true;
                        drawing.initDrawStroke(e.getX(),
                                e.getY(),
                                Infrastructure.getInstance().getColor(),
                                Infrastructure.getInstance().getPenSize());
                        break;
                    case ELLIPSE:
                        isChanged = true;
                        drawing.initDrawEllipse(e.getX(),
                                e.getY(),
                                false,
                                Infrastructure.getInstance().getColor(),
                                Infrastructure.getInstance().getThickness());
                        break;
                    case FILLELLIPSE:
                        isChanged = true;
                        drawing.initDrawEllipse(e.getX(),
                                e.getY(),
                                true,
                                Infrastructure.getInstance().getColor(),
                                Infrastructure.getInstance().getThickness());
                        break;
                    case ERASER:
                        isChanged = true;
                        drawing.initEraser(e.getX(), e.getY(), Infrastructure.getInstance().getEraserSize() * 10);
                        break;
                    case SELECT:
                        isChanged = true;
                        List<Object> obj = drawing.getGraphicElements();
                        for (int i = obj.size() - 1; i >= 0; i--) {
                            if (obj.get(i) instanceof com.starea.datamodel.Rectangle) {
                                if (((com.starea.datamodel.Rectangle) obj.get(i)).isFill()) {
                                    if (((com.starea.datamodel.Rectangle) obj.get(i)).isInsideRectangle(e.getX(), e.getY())) {
                                        drawing.getSelectionGraphicElements().add(obj.get(i));
                                        drawing.initSelection(e.getX(), e.getY());
                                        break;
                                    }
                                } else {
                                    if (((com.starea.datamodel.Rectangle) obj.get(i)).isOnRectangle(e.getX(), e.getY())) {
                                        drawing.getSelectionGraphicElements().add(obj.get(i));
                                        drawing.initSelection(e.getX(), e.getY());
                                        break;
                                    } else {
                                        drawing.getSelectionGraphicElements().clear();
                                    }
                                }
                            }

                            if (obj.get(i) instanceof com.starea.datamodel.Ellipse) {
                                if (((com.starea.datamodel.Ellipse) obj.get(i)).isFill()) {
                                    if (((com.starea.datamodel.Ellipse) obj.get(i)).isInsideEllipse(e.getX(), e.getY())) {
                                        drawing.getSelectionGraphicElements().add(obj.get(i));
                                        drawing.initSelection(e.getX(), e.getY());
                                        break;
                                    }
                                } else {
                                    if (((com.starea.datamodel.Ellipse) obj.get(i)).isOnEllipse(e.getX(), e.getY())) {
                                        drawing.getSelectionGraphicElements().add(obj.get(i));
                                        drawing.initSelection(e.getX(), e.getY());
                                        break;
                                    } else {
                                        drawing.getSelectionGraphicElements().clear();
                                    }
                                }
                            }

                            if (obj.get(i) instanceof com.starea.datamodel.Stroke) {
                                if (((Stroke) obj.get(i)).isOnStroke(e.getX(), e.getY())) {
                                    drawing.getSelectionGraphicElements().add(obj.get(i));
                                    drawing.initSelection(e.getX(), e.getY());
                                    break;
                                } else {
                                    drawing.getSelectionGraphicElements().clear();
                                }
                            }
                        }
                        break;
                }
            }
        });

        artBoard.addEventHandler(MouseDragEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                switch (Infrastructure.getInstance().getMode()) {
                    case RECT:
                        drawing.onDrawRect(e.getX(), e.getY());
                        break;
                    case FILLRECT:
                        drawing.onDrawRect(e.getX(), e.getY());
                        break;
                    case PENCIL:
                        drawing.onDrawStroke(e.getX(), e.getY());
                        break;
                    case ELLIPSE:
                        drawing.onDrawEllipse(e.getX(), e.getY());
                        break;
                    case FILLELLIPSE:
                        drawing.onDrawEllipse(e.getX(), e.getY());
                        break;
                    case ERASER:
                        drawing.onEraser(e.getX(), e.getY());
                        break;
                    case SELECT:
                        if (!drawing.getSelectionGraphicElements().isEmpty()) {
                            drawing.onSelection(e.getX(), e.getY());
                        }
                        break;
                }
            }
        });

        artBoard.addEventHandler(MouseDragEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                drawing.getSelectionGraphicElements().clear();
            }
        });
    }
    //endregion
}
