package app;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import static javafx.scene.paint.Color.BLACK;

public final class AppController
{

    @FXML private Label xLabel, yLabel;
    @FXML private Canvas canvas;

    private GraphicsContext gc;

    @FXML
    void initialize()
    {
        gc = canvas.getGraphicsContext2D();
        resetCanvas();
    }

    private void resetCanvas()
    {
        gc.setFill(BLACK);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
    }
}
