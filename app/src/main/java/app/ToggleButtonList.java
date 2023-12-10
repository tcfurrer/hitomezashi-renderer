package app;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Subscription;
import javafx.util.Callback;
import java.util.*;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

final class ToggleButtonList
{
    private final ObjectProperty<Pane> parent;
    private final IntegerProperty stepSize;
    private final ReadOnlyListWrapper<ToggleButton> buttons;

    private Subscription parentSizeSubscription = Subscription.EMPTY;

    ToggleButtonList()
    {
        parent = new SimpleObjectProperty<>();
        stepSize = new SimpleIntegerProperty(10);
        Callback<ToggleButton, Observable[]> extractor =
                button -> List.of(button.selectedProperty()).toArray(new Observable[]{});
        buttons = new ReadOnlyListWrapper<>(FXCollections.observableArrayList(extractor));

        parent.subscribe((oldParent,newParent) -> {
            if (oldParent != null)
            {
                removeButtons(oldParent);
            }
            if (newParent != null)
            {
                addButtons(newParent);
            }
        });
    }

    public ObjectProperty<Pane> parentProperty() { return parent; }
    public IntegerProperty stepSizeProperty() { return stepSize; }
    public ReadOnlyListProperty<ToggleButton> buttons() { return buttons.getReadOnlyProperty(); }

    private void updateButtons()
    {
        var pane = parent.get();
        removeButtons(pane);
        addButtons(pane);
    }
    private void addButtons(Pane pane)
    {
        //
        // Compute the number of buttons needed to fill the given pane
        //
        int paneSize = switch (pane)
        {
            case VBox vBox -> (int)Math.floor(vBox.getHeight());
            case HBox hBox -> (int)Math.floor(hBox.getWidth());
            default -> throw new UnsupportedOperationException();
        };
        int numButtons = paneSize / stepSize.get();

        //
        // Create the buttons and add them to the pane
        //
        buttons.clear();
        for (int i=0; i<numButtons; i++)
        {
            var button = new ToggleButton();
            button.setMinSize(USE_PREF_SIZE,USE_PREF_SIZE);
            button.setMaxSize(USE_PREF_SIZE,USE_PREF_SIZE);
            switch (pane)
            {
                case VBox __ignored ->
                {
                    button.prefHeightProperty().bind(stepSize);
                    button.setPrefWidth(30);
                }
                case HBox __ignored ->
                {
                    button.prefWidthProperty().bind(stepSize);
                    button.setPrefHeight(30);
                }
                default -> throw new UnsupportedOperationException();
            }
            buttons.add(button);
        }
        pane.getChildren().addAll(buttons);

        //
        // Ensure that buttons always fill up the parent pane
        //
        parentSizeSubscription = switch (pane)
        {
            case VBox vBox -> vBox.heightProperty().subscribe(this::updateButtons);
            case HBox hBox -> hBox.widthProperty().subscribe(this::updateButtons);
            default -> throw new UnsupportedOperationException();
        };
    }

    private void removeButtons(Pane pane)
    {
        parentSizeSubscription.unsubscribe();
        parentSizeSubscription = Subscription.EMPTY;
        pane.getChildren().removeAll(buttons);
        buttons.clear();
    }
}
