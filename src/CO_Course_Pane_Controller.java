import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public class CO_Course_Pane_Controller {

    @FXML
    private Label titleLabel;
    @FXML
    private Label readTimeLabel;

    @FXML private Pane holderPane;

    @FXML
    public void initialize() {
    }

    public void setAll(String title, int _rtime) {
        titleLabel.setText(title);
        readTimeLabel.setText("" + _rtime);
    }

    public Pane getHolderPane() {
        return holderPane;
    }
}
