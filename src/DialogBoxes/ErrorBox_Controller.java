package DialogBoxes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ErrorBox_Controller {

    @FXML private Label titleLabel;
    @FXML private Label coreLabel;
    @FXML private Button okButton;

    private Stage stage;

    @FXML public void initialize()
    {
        okButton.setOnMouseClicked(mouseEvent -> { stage.close(); });
    }

    public static ErrorBox_Controller showErrorBox(Stage _owner, String _title, String _core) {
        FXMLLoader loader = new FXMLLoader(ErrorBox_Controller.class.getResource("ErrorBox.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ErrorBox_Controller ctr = loader.getController();
        ctr.stage = new Stage();
        Scene scene = new Scene(root);
        ctr.stage.setScene(scene);
        ctr.stage.initStyle(StageStyle.UNDECORATED);

        ctr.titleLabel.setText(_title);
        ctr.coreLabel.setText(_core);

        ctr.stage.initStyle(StageStyle.UNDECORATED);
        ctr.stage.initOwner(_owner);

        ctr.stage.show();

        return ctr;
    }
}
