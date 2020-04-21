import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

public class Text_Controller {

    @FXML private TextArea Text;

    @FXML
    void initialize()
    {
    }

    public void setText(String _title)
    {
        Text.setText(_title);
        int lng = _title.length();
        int rows = 1 + (int) (lng / 167.0);
        int height = rows * 30;
        Text.setPrefHeight(height);
        Pane daddy = (Pane) Text.getParent();
        daddy.setMaxHeight(height);
        daddy.setPrefHeight(height);
        daddy.setMinHeight(height);
    }
}
