package app;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static javafx.scene.paint.Color.BLACK;

public final class AppController
{
    @FXML private Spinner<Integer> stepSize;
    @FXML private Canvas canvas;
    @FXML private VBox vBox;
    @FXML private HBox hBox;

    private final ToggleButtonList xButtons, yButtons;
    private GraphicsContext gc;

    public AppController()
    {
        xButtons = new ToggleButtonList();
        yButtons = new ToggleButtonList();
    }

    @FXML
    void initialize()
    {
        // Step-size spinner
        //
        var valueFactory = new IntegerSpinnerValueFactory(10,100,30, 1);
        stepSize.setValueFactory(valueFactory);

        // Buttons
        //
        xButtons.parentProperty().set(hBox);
        yButtons.parentProperty().set(vBox);
        xButtons.stepSizeProperty().bind(stepSize.valueProperty());
        yButtons.stepSizeProperty().bind(stepSize.valueProperty());

        // Canvas
        //
        gc = canvas.getGraphicsContext2D();
        draw();
        xButtons.buttons().subscribe(this::draw);
        yButtons.buttons().subscribe(this::draw);
    }

    private void draw()
    {
        // Clear entire canvas to background color
        //
        gc.setFill(BLACK);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        // Draw the pattern
        //
    }
}
