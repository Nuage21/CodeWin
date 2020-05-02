import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public class NotePane_Controller {

    @FXML private Label noteLabel;

    @FXML private Pane holder;

    @FXML void initialize()
    {

    }

    public void setNote(String _note)
    {
        this.noteLabel.setText(_note);
    }

    public void resetWidth(double width)
    {
        Design.setWidth(holder, width * 0.98);
        noteLabel.setPrefWidth(width * 0.96);
    }
}
