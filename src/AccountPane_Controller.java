import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountPane_Controller {

    @FXML
    Pane paramsPane;

    @FXML
    Pane signoutPane;

    @FXML
    Pane quitPane;

    @FXML Pane minimizePane;

    @FXML
    VBox daddyVBox;

    private boolean isMouseEntered = false;

    private Stage stage;

    @FXML void initialize()
    {
        quitPane.setOnMouseClicked( event -> {
            stage.close();
            Settings.appStage.close();
            Platform.exit();
            System.exit(0);
        });

        minimizePane.setOnMouseClicked(mouseEvent -> {
            stage.close();
            Settings.appStage.setIconified(true);
        });
        daddyVBox.setOnMouseEntered(event -> {
            isMouseEntered = true;
        });

        daddyVBox.setOnMouseExited( event -> {
            if (isMouseEntered);
            {
                stage.setWidth(0);
                stage.setHeight(0);
                stage.hide();
                stage.close();
            }
        });

        signoutPane.setOnMouseClicked(e->{
            if(isMouseEntered){
                stage.close();
                SceneLoader.loadAuthenticator();
            }
        });

    }

    public Pane getParamsPane()
    {
        return paramsPane;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
