import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SidebarChapterPane_Controller {

    @FXML
    private Label titleLabel;

    @FXML
    private StackPane expandStackPane;

    @FXML
    ImageView expanderImageView;

    private Pane expandablePane;

    private boolean isExpanded = false;

    public static String[] expImages = {"\\img\\icons\\unexpand.png", "\\img\\icons\\expand.png"};

    @FXML
    void initialize() {
        expandStackPane.setOnMouseClicked(event -> {
            this.isExpanded = !isExpanded;
            expandablePane.setVisible(isExpanded);
            expandablePane.setManaged(isExpanded);
            Image image = null;
            try {
                image = new Image(new FileInputStream(Settings.projectPath + expImages[isExpanded ? 0 : 1]));
            } catch (FileNotFoundException e) {
                Debug.debugException(e);
            }
            expanderImageView.setImage(image);
            event.consume();
        });
    }

    public void expand() {
        if (this.isExpanded)
            return;
        this.isExpanded = true;
        expandablePane.setVisible(true);
        expandablePane.setManaged(true);
        Image image = null;
        try {
            image = new Image(new FileInputStream(Settings.projectPath + expImages[0]));
            expanderImageView.setImage(image);
        } catch (FileNotFoundException e) {
            Debug.debugException(e);
        }
    }

    public void unexpand() {
        if (!this.isExpanded)
            return;
        this.isExpanded = false;
        expandablePane.setVisible(false);
        expandablePane.setManaged(false);
        Image image = null;
        try {
            image = new Image(new FileInputStream(Settings.projectPath + expImages[1]));
            expanderImageView.setImage(image);
        } catch (FileNotFoundException e) {
            Debug.debugException(e);
        }
    }

    public void setTitle(String _title) {
        titleLabel.setText(_title);
    }

    public void setExpandablePane(Pane expandablePane) {
        this.expandablePane = expandablePane;
    }

}
