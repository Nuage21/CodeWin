import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Auth_Controller {

    //region fxml

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
    public TextField UsernameSignInField;

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

    //endregion


    enum SignCase {signIn, signUp}

    private SignCase signCase = SignCase.signUp;

    private Control Focused = null;

    private IntegerProperty ffs = new SimpleIntegerProperty(0);
    public Control lastCtrl = UsernameField;
    public Control ctrl = UsernameField;

    @FXML
    private void initialize() {

        //region oh god

        applyTabStyle(signupTab, true);

        signInCancealBtn.setOnAction((event) -> {
            Platform.exit();
        });

        signUpCancealBtn.setOnAction((event) -> {
            Platform.exit();
        });

        signupTab.setOnSelectionChanged((event) -> { //swap tabs

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
            if (dateVer(!dateVer(false)) && (mailVer(!mailVer(false))) && (psswrdVer(!psswrdVer(false))) && (userNameVer(!userNameVer(false))) && (notEmpty(FirstnameField, !notEmpty(FirstnameField, false))) && (notEmpty(LastnameField, !notEmpty(LastnameField, false))) && (notEmpty(PhoneField, !notEmpty(PhoneField, false))) && (notEmpty(AddressField, !notEmpty(AddressField, false)))) {
                this.addUser();
                Stage stg = Main.appSettings.getAppStage();
                stg.setScene(new Scene(root));
                stg.setFullScreen(true);
                stg.show();
            }
        });
        //endregion
        PhoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    PhoneField.setText(oldValue);
                }
            }
        });


        UsernameField.setOnMouseClicked(e -> {
            {

                ffs.setValue(ffs.getValue() + 1);
                userNameVer(true);

            }
        });
        PwdField.setOnMouseClicked(e -> {
            {

                ffs.setValue(ffs.getValue() + 1);
                psswrdVer(true);

            }
        });
        FirstnameField.setOnMouseClicked(e -> {
            ctrl = FirstnameField;
            ffs.setValue(ffs.getValue() + 1);
            notEmpty(FirstnameField, true);
        });
        LastnameField.setOnMouseClicked(e -> {
            ctrl = LastnameField;
            ffs.setValue(ffs.getValue() + 1);
            notEmpty(LastnameField, true);
        });
        AddressField.setOnMouseClicked(e -> {
            ctrl = AddressField;
            ffs.setValue(ffs.getValue() + 1);
            notEmpty(AddressField, true);
        });

        EmailField.setOnMouseClicked(e -> {
            ctrl = EmailField;
            ffs.setValue(ffs.getValue() + 1);
            mailVer(true);
        });

        DatePickerField.setEditable(false);
        PhoneField.setOnMouseClicked(e -> {
            ctrl = PhoneField;
            ffs.setValue(ffs.getValue() + 1);
            notEmpty(PhoneField, true);
        });


        UsernameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                ffs.setValue(ffs.getValue() + 1);
                userNameVer(true);
            }
        });
        PwdField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ctrl = PwdField;
                ffs.setValue(ffs.getValue() + 1);
                psswrdVer(true);
            }
        });
        FirstnameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                ffs.setValue(ffs.getValue() + 1);
                notEmpty(FirstnameField, true);
            }
        });
        LastnameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                ffs.setValue(ffs.getValue() + 1);
                notEmpty(LastnameField, true);
            }
        });
        PhoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                ffs.setValue(ffs.getValue() + 1);
                notEmpty(PhoneField, true);
            }
        });
        EmailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                ffs.setValue(ffs.getValue() + 1);
                mailVer(true);
            }
        });
        AddressField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                ffs.setValue(ffs.getValue() + 1);
                notEmpty(AddressField, true);
            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                UsernameField.requestFocus();
                ctrl = UsernameField;
                lastCtrl = null;
            }
        });
    }


    public boolean userNameVer(boolean show) {

        String usr = UsernameField.getText();
        ctrl = UsernameField;
        boolean is_not_taken = true;  // verification au pres de la bdd
        boolean lenght = usr.length() >= 6;

        LogErrorController.list.clear();
        LogErrorController.list.add(new Msg(Integer.toString(usr.length()), is_not_taken));
        LogErrorController.list.add(new Msg("plus de 6 characteres", lenght));
        if (show) loger(UsernameField);
        return lenght && is_not_taken;
    }

    public boolean psswrdVer(boolean show) {
        String usr = PwdField.getText();
        ctrl = PwdField;
        boolean lengh = usr.length() >= 8;
        boolean num = usr.matches("^.*[0-9].*.[0-9].*");
        boolean spec = usr.matches("^.*(?=.*[*@_;,&çà]).*$");

        LogErrorController.list.clear();
        LogErrorController.list.add(new Msg("au moin 8 caractéres", lengh));
        LogErrorController.list.add(new Msg("au moin 2 chiffres", num));
        LogErrorController.list.add(new Msg("au moin un cara special", spec));
        if (show) loger(PwdField);
        return lengh && num && spec;
    }

    public boolean notEmpty(TextField t, boolean show) {
        boolean emp = t.getText().length() != 0;
        ctrl = t;
        LogErrorController.list.clear();
        LogErrorController.list.add(new Msg("remplir ce champs", emp));
        if (show) loger(t);
        return emp;
    }

    public boolean mailVer(boolean show) {
        String email = EmailField.getText();
        String mail_format = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(mail_format);
        Matcher match = pattern.matcher(email);

        boolean valide_mail = match.matches();
        boolean is_not_used = true; // BDD request here
        LogErrorController.list.clear();
        LogErrorController.list.add(new Msg("format de mail valide", valide_mail));
        LogErrorController.list.add(new Msg("mail non utilisé", is_not_used));
        if (show) loger(EmailField);
        return valide_mail && is_not_used;
    }

    public boolean dateVer(boolean show) {
        boolean empty = DatePickerField.getValue() != null;
        LogErrorController.list.clear();
        LogErrorController.list.add(new Msg("selectionnez une date", empty));
        if (show) loger(DatePickerField);
        return empty;
    }

    public boolean same() {
        return ctrl.equals(lastCtrl);
    }

    public void loger(Control c) {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogError.fxml"));

            Stage stg = new Stage();
            Point2D p = c.localToScreen(0.0, 0.0);
            stg.setX(p.getX());
            stg.setY(p.getY() + 30);

            stg.setAlwaysOnTop(true);
            stg.initOwner(Main.appSettings.getAppStage());
            stg.initStyle(StageStyle.TRANSPARENT);
            Scene sc = new Scene(loader.load());
            sc.setFill(Color.TRANSPARENT);
            stg.setScene(sc);
            LogErrorController controller = (LogErrorController) loader.getController();
            controller.main.setOnMouseClicked(e -> stg.close());
            ffs.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                    stg.close();
                }
            });


            PauseTransition dela = new PauseTransition(Duration.seconds(1));
            dela.setOnFinished(event -> {
                Timeline timeline = new Timeline();
                KeyFrame key = new KeyFrame(Duration.millis(2000),
                        new KeyValue(stg.getScene().getRoot().opacityProperty(), 0));
                timeline.getKeyFrames().add(key);
                timeline.setOnFinished((ae) -> stg.close());
                timeline.play();
            });
            dela.play();
            PauseTransition delay = new PauseTransition(Duration.millis(1));
            delay.setOnFinished(event -> {
                        Main.appSettings.getAppStage().requestFocus();
                        ctrl.requestFocus();
                        ((TextInputControl) ctrl).positionCaret(((TextInputControl) ctrl).getText().length());
                    } // RECUP LE FOCUS ?GETME OUT JAVAFX
            );
            delay.play();
            stg.show();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
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
    private void addUser() {   //les variables de la fonction
        boolean userExiste = false;
        User user = new User();

    /* la recuperation de la date et La transformer vers une date sql donc vous devez remplir le champ date sinon
    il y aura une nullpointer error  */
        LocalDate localdate = DatePickerField.getValue();
        java.sql.Date dob = java.sql.Date.valueOf(localdate);


//l'ajout de user a la base de données
        userExiste = User.addUser(FirstnameField.getText(), LastnameField.getText(), UsernameField.getText(), dob, EmailField.getText(), PwdField.getText(), PhoneField.getText(), "", AddressField.getText(), 0, false);

//verification du succes du poccessus d'ajout de user vers la base de données
        if (userExiste) //s'il existe déja
        {
            //le traitement lorsque le usrname ou son email existe déja
            //sinon on va affecter le user a Loggedln_Controller
        } else {  //sinon on affecte le user vers la fenetre loggedln comme variable statique
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


    static class Msg {
        String msg;
        boolean b;

        public Msg(String msg, boolean b) {
            this.b = b;
            this.msg = msg;
        }
    }
}