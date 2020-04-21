import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class H1_Controller implements H_Controller{

    @FXML private Label titleLabel;

    @FXML
    public void initialize()
    {

    }

    public void setTitle(String _title)
    {
        titleLabel.setText(_title);
    }
}
