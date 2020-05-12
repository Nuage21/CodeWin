import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class Design {

    public static double CENTRAL_PANE_WIDTH;

    public static void setWidth(Pane p, double width)
    {
        p.setMinWidth(width);
        p.setPrefWidth(width);
        p.setMaxWidth(width);
    }

    public static void setHeight(Pane p, double height)
    {
        p.setMinHeight(height);
        p.setPrefHeight(height);
        p.setMaxHeight(height);
    }

    public static void setWidth(ScrollPane p, double width)
    {
        p.setMinWidth(width);
        p.setPrefWidth(width);
        p.setMaxWidth(width);
    }

    public static void setVisible(Pane p, boolean v)
    {
        p.setVisible(v);
        p.setManaged(v);
    }
}
