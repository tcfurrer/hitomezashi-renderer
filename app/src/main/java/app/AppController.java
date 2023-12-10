package app;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.util.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.GREEN;

public final class AppController
{
    @FXML private Pane topPane;
    @FXML private Spinner<Integer> stepSize;
    @FXML private Canvas canvas;
    @FXML private VBox vBox;
    @FXML private HBox hBox;

    private final ToggleButtonList xButtons, yButtons;
    private final Random random;
    private GraphicsContext gc;

    public AppController()
    {
        xButtons = new ToggleButtonList();
        yButtons = new ToggleButtonList();
        random = new Random(23948);
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

        // HotKeys
        //
        topPane.addEventHandler(KEY_PRESSED, e -> {
            switch (e.getCode())
            {
                case KeyCode.R -> randomize();
            }
        });
    }

    private void draw()
    {
        // Clear entire canvas to background color
        //
        gc.setFill(BLACK);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        // Set canvas parameters for drawing
        //
        gc.setStroke(GREEN);
        gc.setLineWidth(2);

        // Pre-compute some common parameters
        //
        var numRows = yButtons.buttons().size();
        var numColumns = xButtons.buttons().size();
        var s = stepSize.getValue();

        // Draw the vertical lines
        //
        {
            for (int i = 0; i < numColumns; i++)
            {
                var x = i * s;
                var jBegin = xButtons.buttons().get(i).isSelected() ? 0 : 1;
                for (int j=jBegin; j < numRows; j+=2)
                {
                    var y1 = j*s;
                    var y2 = y1+s;
                    gc.strokeLine(x, y1, x, y2);
                }
            }
        }

        // Draw the horizontal lines
        //
        {
            for (int i = 0; i < numRows; i++)
            {
                var y = i * s;
                var jBegin = yButtons.buttons().get(i).isSelected() ? 0 : 1;
                for (int j=jBegin; j < numColumns; j+=2)
                {
                    var x1 = j*s;
                    var x2 = x1+s;
                    gc.strokeLine(x1, y, x2, y);
                }
            }
        }
    }

    private void randomize()
    {
        randomize(xButtons.buttons());
        randomize(yButtons.buttons());
    }

    private void randomize(List<ToggleButton> buttons)
    {
        buttons.forEach(button -> button.setSelected(random.nextBoolean()));
    }
}
