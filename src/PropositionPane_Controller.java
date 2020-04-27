import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class PropositionPane_Controller {

    @FXML private Label propositionLabel;
    @FXML private CheckBox propositionCheckBox;
    @FXML private Pane holder;

    @FXML
    public void initialize()
    {
        holder.setOnMouseClicked( mouseEvent -> {
            propositionCheckBox.setSelected(!isChosen()); // toggle selection
        });
    }

    public void setProposition(String _prop)
    {
        propositionLabel.setText(_prop);
    }

    public Boolean isChosen(){ return propositionCheckBox.isSelected(); }
}
