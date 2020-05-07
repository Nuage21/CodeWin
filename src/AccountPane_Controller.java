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
            if (isMouseEntered);
            {
                stage.hide();
                stage.close();
            }
        });

        signoutPane.setOnMouseClicked(e->{
            if(isMouseEntered){
                stage.close();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Authenticator.fxml"));
                    Parent root = null ;
                    root = loader.load();
                    Stage stg = Settings.appStage;
                    Scene sc = new Scene(root);
                    LoggedIn_Controller.setUser(null);
                    stg.setScene(sc);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
