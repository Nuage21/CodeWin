import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ListElement_Controller implements H_Controller {

    @FXML
    private Label title;

    @FXML void initialize()
    {

    }

    @Override
    public void setTitle(String _txt) {
        title.setText(_txt);
    }
}
