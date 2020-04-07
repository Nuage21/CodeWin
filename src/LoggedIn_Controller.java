import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class LoggedIn_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane UsernameSignInPane;

    @FXML
    void initialize() {
        assert UsernameSignInPane != null : "fx:id=\"UsernameSignInPane\" was not injected: check your FXML file 'LoggedIn.fxml'.";

    }
}
