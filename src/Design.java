import javafx.scene.layout.Pane;

public class Design {

    public static void setWidth(Pane p, double width)
    {
        p.setMaxWidth(width);
        p.setMinWidth(width);
        p.setPrefWidth(width);
    }
}
