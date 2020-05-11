import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;


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
            Settings.application.getHostServices().showDocument(Settings.githubLink);
        });

        quitButton.setOnMouseClicked(mouseEvent -> {
            stage.close();
        });
    }
}
