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
            if(isChosen())
                holder.setStyle(holder.getStyle() + "-fx-effect: dropshadow(three-pass-box, #2389bb, 10, 0, 0, 0);");
            else
                holder.setStyle(holder.getStyle() + "-fx-effect: dropshadow(three-pass-box, transparent, 10, 0, 0, 0);");

        });
    }

    public void setProposition(String _prop)
    {
        propositionLabel.setText(_prop);
    }

    public Boolean isChosen(){ return propositionCheckBox.isSelected(); }
}
