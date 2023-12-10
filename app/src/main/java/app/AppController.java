package app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.*;
import static javafx.scene.input.Clipboard.getSystemClipboard;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.paint.Color.*;

public final class AppController
{
    private static final int MIN_STEP_SIZE = 10;
    private static final int DEFAULT_STEP_SIZE = 10;
    private static final int MAX_STEP_SIZE = 100;
    private static final int LINE_WIDTH = 2;
    private static final Color LINE_COLOR = GREEN;

    @FXML private Pane topPane;
    @FXML private Spinner<Integer> stepSize;
    @FXML private CanvasPane canvasPane;
    @FXML private VBox buttonsVBox;
    @FXML private HBox buttonsHBox;

    private final ToggleButtonList xButtons, yButtons;
    private final Random random;
    private Canvas canvas;
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
        canvas = canvasPane.getCanvas();

        // Step-size spinner
        //
        var valueFactory = new IntegerSpinnerValueFactory(MIN_STEP_SIZE,MAX_STEP_SIZE,DEFAULT_STEP_SIZE,1);
        stepSize.setValueFactory(valueFactory);

        // Buttons
        //
        xButtons.parentProperty().set(buttonsHBox);
        yButtons.parentProperty().set(buttonsVBox);
        xButtons.stepSizeProperty().bind(stepSize.valueProperty());
        yButtons.stepSizeProperty().bind(stepSize.valueProperty());

        // Canvas
        //
        gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().subscribe(() -> Platform.runLater(this::draw));
        canvas.heightProperty().subscribe(() -> Platform.runLater(this::draw));
        canvas.setManaged(false);
        Platform.runLater(this::draw);
        xButtons.buttons().subscribe(this::draw);
        yButtons.buttons().subscribe(this::draw);

        // HotKeys
        //
        topPane.addEventHandler(KEY_PRESSED, e -> {
            switch (e.getCode())
            {
                case KeyCode.R -> randomize();
                case KeyCode.C -> copy();
            }
        });
    }

    private void draw()
    {
        if (canvas.getWidth() == 0 || canvas.getHeight() == 0) return;

        // Clear entire canvas
        //
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());

        // Set canvas parameters for drawing
        //
        gc.setStroke(LINE_COLOR);
        gc.setLineWidth(LINE_WIDTH);

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

    private void copy()
    {
        var parameters = new SnapshotParameters();
        parameters.setFill(TRANSPARENT);
        var image = canvas.snapshot(parameters, null);
        var clipboard = getSystemClipboard();
        var content = new ClipboardContent();
        content.putImage(image);
        clipboard.setContent(content);
    }
}
