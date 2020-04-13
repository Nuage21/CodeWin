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

    public static String[] expImages = {"C:\\Users\\hbais\\Desktop\\test_loggedin\\src\\sample\\img\\icons\\unexpand.png", "C:\\Users\\hbais\\Desktop\\test_loggedin\\src\\sample\\img\\icons\\expand.png"};
    @FXML
    void initialize() {
        expandStackPane.setOnMouseClicked(event -> {
            isExpanded = !isExpanded;
            expandablePane.setVisible(isExpanded);
            expandablePane.setManaged(isExpanded);
            try {
                expanderImageView.setImage(new Image(new FileInputStream(expImages[isExpanded?0:1]) ));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void setTitle(String _title) {
        titleLabel.setText(_title);
    }

    public void setExpandablePane(Pane expandablePane) {
        this.expandablePane = expandablePane;
    }

}
