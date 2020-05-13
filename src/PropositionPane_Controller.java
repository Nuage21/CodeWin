import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PropositionPane_Controller {

    @FXML
    private Label propositionLabel;
    @FXML
    private CheckBox propositionCheckBox;
    @FXML
    private Pane holder;

    @FXML
    private ImageView checkImageView;

    private boolean isAnswered = false;

    @FXML
    public void initialize() {
        holder.setOnMouseClicked(mouseEvent -> {
            if (!isAnswered) {
                propositionCheckBox.setSelected(!isChosen()); // toggle selection
                if (isChosen())
                    holder.setStyle(holder.getStyle() + "-fx-effect: dropshadow(three-pass-box, #2389bb, 10, 0, 0, 0);");
                else
                    holder.setStyle(holder.getStyle() + "-fx-effect: dropshadow(three-pass-box, transparent, 10, 0, 0, 0);");
            }
        });
    }

    public void validate(boolean isRight) {
        this.isAnswered = true;
        propositionCheckBox.setVisible(false);
        if (!isRight) {
            Image imgFalse = null;
            imgFalse = new Image(getClass().getResourceAsStream("img\\unchecked.png"));
            checkImageView.setImage(imgFalse);
        }
        checkImageView.setVisible(true);
        propositionLabel.setMinWidth(Region.USE_PREF_SIZE);
        if (isRight)
            holder.setStyle(holder.getStyle() + "-fx-effect: dropshadow(three-pass-box, #23bb89, 10, 0, 0, 0);");
        else
            holder.setStyle(holder.getStyle() + "-fx-effect: dropshadow(three-pass-box, darkred, 10, 0, 0, 0);");
    }

    public void setProposition(String _prop) {
        propositionLabel.setText(_prop);
    }

    public Boolean isChosen() {
        return propositionCheckBox.isSelected();
    }

    public void resetWidth() {
        Design.setWidth(holder, Design.CENTRAL_PANE_WIDTH * 0.955);
        propositionLabel.setPrefWidth(Design.CENTRAL_PANE_WIDTH * 0.94);
    }
}