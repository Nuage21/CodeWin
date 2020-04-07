import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class Auth_Controller {

    @FXML
    private Tab signinTab;
    @FXML
    private Tab signupTab;
    @FXML
    private Button signInCancealBtn;
    @FXML
    private Button signUpCancealBtn;

    @FXML
    private Button signUpBtn;
    @FXML
    private Pane UsernamePane;
    @FXML
    private TextField UsernameField;

    @FXML
    private Pane UsernameSignInPane;
    @FXML
    private TextField UsernameSignInField;

    @FXML
    private Pane FirstnamePane;
    @FXML
    private TextField FirstnameField;

    @FXML
    private Pane LastnamePane;
    @FXML
    private TextField LastnameField;

    @FXML
    private Pane PhonePane;
    @FXML
    private TextField PhoneField;

    @FXML
    private Pane EmailPane;
    @FXML
    private TextField EmailField;

    @FXML
    private Pane AddressPane;
    @FXML
    private TextField AddressField;

    @FXML
    private Pane DatePickerPane;
    @FXML
    private DatePicker DatePickerField;

    @FXML
    private PasswordField PwdField;
    @FXML
    private Pane PasswordPane;

    @FXML
    private PasswordField PwdSignInField;
    @FXML
    private Pane PasswordSignInPane;


    enum SignCase {signIn, signUp}

    private SignCase signCase = SignCase.signUp;

    private Control Focused = null;

    @FXML
    private void initialize() {

        applyTabStyle(signupTab, true);

        signInCancealBtn.setOnAction((event) -> {
            Platform.exit();
        });

        signUpCancealBtn.setOnAction((event) -> {
            Platform.exit();
        });

        signupTab.setOnSelectionChanged((event) -> {

            signCase = (signCase == SignCase.signUp) ? SignCase.signIn : SignCase.signUp; // toggle sign case
            if (signCase == SignCase.signUp) {
                applyTabStyle(signupTab, true); // select
                applyTabStyle(signinTab, false); // unselect
            } else {
                applyTabStyle(signupTab, false); // select
                applyTabStyle(signinTab, true); // unselect
            }

        });

        ArrayList<Control> focusable = new ArrayList<>();

        focusable.add(UsernameField);
        focusable.add(UsernameSignInField);
        focusable.add(FirstnameField);
        focusable.add(LastnameField);
        focusable.add(EmailField);
        focusable.add(PhoneField);
        focusable.add(AddressField);
        focusable.add(DatePickerField);
        focusable.add(PwdSignInField);
        focusable.add(PwdField);

        for (Control c : focusable)
            c.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> applyFocusStyle((Pane) c.getParent(), newPropertyValue));

        signUpBtn.setOnAction((event) -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("LoggedIn.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stg = Main.appSettings.getAppStage();
            stg.setScene(new Scene(root));
            stg.setFullScreen(true);
            stg.show();
        });
    }

    private void applyTabStyle(Tab tab, boolean isSelected) {
        if (isSelected)
            tab.setStyle("-fx-background-color: #1da1cc !important;");
        else
            tab.setStyle("-fx-background-color: #1d1d1d !important;");
    }

    private void applyFocusStyle(Pane p, boolean app) {
        if (app) {
            p.setStyle("-fx-effect: dropshadow(three-pass-box, #1299bb, 10, 0, 0, 0); ");
        } else {
            p.setStyle("-fx-effect: dropshadow(three-pass-box, #1299bb, 0, 0, 0, 0);");
        }
    }
}