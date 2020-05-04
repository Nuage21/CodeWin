import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public class NotePane_Controller {

    @FXML
    private Label noteLabel;

    @FXML
    private Pane holder;

    @FXML
    void initialize() {
        this.setVisible(false);
    }

    public void setNote(String _note) {
        this.noteLabel.setText(_note);
    }

    public void resetWidth() {

        Design.setWidth(holder, Design.CENTRAL_PANE_WIDTH * 0.95);
        noteLabel.setPrefWidth(Design.CENTRAL_PANE_WIDTH * 0.94);
    }

    public void setVisible(boolean _visible) {
        holder.setManaged(_visible);
        holder.setVisible(_visible);
    }
}
