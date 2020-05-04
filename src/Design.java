import javafx.scene.layout.Pane;

public class Design {

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
}
