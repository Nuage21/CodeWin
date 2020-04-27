import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class NotePane_Controller {

    @FXML private Label noteLabel;

    @FXML void initialize()
    {

    }

    public void setNote(String _note)
    {
        this.noteLabel.setText(_note);
    }
}
