import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Settings {

    private Stage appStage;

    public static void newNotification(Parent root)
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root) );
        dialog.show();
    }

    public void setAppStage(Stage appStage) {
        this.appStage = appStage;
    }
}
