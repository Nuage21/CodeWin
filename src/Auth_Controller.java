import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;

public class Auth_Controller {

    @FXML
    private Tab signinTab;
    @FXML
    private Tab signupTab;
    @FXML
    private Button signInCancealBtn;
    @FXML
    private Button signUpCancealBtn;

    enum SignCase {signIn, signUp};
    private SignCase signCase = SignCase.signUp;

    @FXML
    private void initialize() {

        applyTabStyle(signupTab, true);

        signInCancealBtn.setOnAction( (event) -> {
            Platform.exit();
        });

        signUpCancealBtn.setOnAction( (event) -> {
            Platform.exit();
        });

        signupTab.setOnSelectionChanged( (event) -> {

           signCase = (signCase == SignCase.signUp)?SignCase.signIn:SignCase.signUp; // toggle sign case
           if(signCase == SignCase.signUp)
           {
               applyTabStyle(signupTab, true); // select
               applyTabStyle(signinTab, false); // unselect
           }
           else
           {
               applyTabStyle(signupTab, false); // select
               applyTabStyle(signinTab, true); // unselect
           }

        });

    }
    private void applyTabStyle(Tab tab, boolean isSelected)
    {
        if(isSelected)
            tab.setStyle("-fx-background-color: #1da1cc !important;");
        else
            tab.setStyle("-fx-background-color: #1d1d1d !important;");
    }
}