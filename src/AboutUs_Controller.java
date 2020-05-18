import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;


public class AboutUs_Controller {

    @FXML private Button quitButton;
    @FXML private Hyperlink githubHLink;

    Stage stage;

    public void setStage(Stage stg)
    {
        this.stage = stg;
    }

    @FXML public void initialize()
    {
        githubHLink.setOnMouseClicked(mouseEvent -> {
            try {
                Desktop.getDesktop().browse(new URL(Settings.githubLink).toURI());
            } catch (Exception e) {
                Debug.debugException(e);
            }
        });

        quitButton.setOnMouseClicked(mouseEvent -> {
            stage.close();
        });
    }
}
