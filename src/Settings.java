import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Settings {

    private Stage appStage;

    public void setAppStage(Stage appStage) {
        this.appStage = appStage;
    }

    public Stage getAppStage() {
        return this.appStage;
    }

}
