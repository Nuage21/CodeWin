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
import java.time.LocalDate;
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
            //l'ajout de user ici
            this.addUser();
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



/*la fonction user qui ajoute l'user a la base de données s'il n'existe pas déja et l'affecter a la fenetre
 loggedln_controller comme une variable statique
*/
    @FXML
    private void addUser()
    {   //les variables de la fonction
    	boolean userExiste = false;
    	User user = new User() ;

    /* la recuperation de la date et La transformer vers une date sql donc vous devez remplir le champ date sinon
    il y aura une nullpointer error */
    LocalDate localdate = DatePickerField.getValue();
    java.sql.Date dob = java.sql.Date.valueOf(localdate);


//l'ajout de user a la base de données
    userExiste = User.addUser(FirstnameField.getText(), LastnameField.getText(), UsernameField.getText(), dob , EmailField.getText(),PwdField.getText(),PhoneField.getText() , "",  AddressField.getText(), 0 , false);

//verification du succes du poccessus d'ajout de user vers la base de données
    if(userExiste) //s'il existe déja
    	{
    		//le traitement lorsque le usrname ou son email existe déja
    	//sinon on va affecter le user a Loggedln_Controller
    	}
    	else{  //sinon on affecte le user vers la fenetre loggedln comme variable statique
    		//d'abord on remplit les attributs de user
user.setUsername(UsernameField.getText());
user.setName(FirstnameField.getText());
user.setPrenom(LastnameField.getText());
user.setEmail(EmailField.getText());
user.setPassword(PwdField.getText());
user.setTelephone(PhoneField.getText());
user.setAddress(AddressField.getText());
user.setDob(dob);
user.setLang(0);
user.setDarkmode(false);
user.setPhoto("");
//l'affectation de user
LoggedIn_Controller.setUser(user);
    	}

    }


}