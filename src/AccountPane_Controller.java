import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AccountPane_Controller {

    @FXML
    Pane paramsPane;

    @FXML
    Pane signoutPane;

    @FXML
    Pane quitPane;

    @FXML
    VBox daddyVBox;

    private boolean isMouseEntered = false;

    private Stage stage;

    @FXML void initialize()
    {
        quitPane.setOnMouseClicked( event -> {
            Platform.exit();
            System.exit(0);
        });

        daddyVBox.setOnMouseEntered(event -> {
            isMouseEntered = true;
        });

        daddyVBox.setOnMouseExited( event -> {
            if (isMouseEntered)
                stage.close();
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
