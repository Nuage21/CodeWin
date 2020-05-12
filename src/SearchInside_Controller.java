import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class SearchInside_Controller {
    @FXML
    private Pane holder;

    @FXML private Label title;

    @FXML public void initialize()
    {

    }

    public void setTitle(String _title)
    {
        title.setText(_title);
    }

    public Pane getHolder() {
        return holder;
    }
}
