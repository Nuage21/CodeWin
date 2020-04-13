import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class SidebarExpandedChapterPane_Controller {

    @FXML private Label titleLabel;

    @FXML private Pane holderPane;

    @FXML
    void initialize()
    {
        holderPane.setOnMouseEntered( event -> {
            this.setTitle(" " + titleLabel.getText());
        });

        holderPane.setOnMouseExited( event -> {
            this.setTitle(titleLabel.getText().substring(1));
        });
    }

    public void setTitle(String _title)
    {
        titleLabel.setText(_title);
    }

}
