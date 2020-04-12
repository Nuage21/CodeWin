import com.sun.javafx.scene.control.ControlAcceleratorSupport;
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
import sun.rmi.server.MarshalInputStream;

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
    public  TextField UsernameSignInField;

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
    private  TextField PhoneField;

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

    public Stage main_stage = null;
    public LogErrorController main_controller ;
    private boolean all_is_ok = true ; // set to false if logger is called ;

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

           if(main_stage!=null ) main_stage.close();
            all_is_ok = true ;
           if(! userNameVer()) loger(UsernameField);
           if(! psswrdVer()) loger(PwdField);
            if(  !notEmpty(FirstnameField) ) loger(FirstnameField);
            if( ! notEmpty(LastnameField) ) loger(LastnameField);
            if( ! notEmpty(AddressField) )loger(AddressField);
            if( ! notEmpty(PhoneField) ) loger(PhoneField);
            if( ! mailVer() ) loger(EmailField);
            if( ! dateVer() ) loger(DatePickerField);
            

          if(all_is_ok){
              if(main_stage!=null)main_stage.close();
            this.addUser();
            Stage stg = Main.appSettings.getAppStage();
            stg.setScene(new Scene(root));
          stg.setFullScreen(true);
            stg.show();}

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
        }); // only number textfield

        //region onClikc
        UsernameField.setOnMouseClicked(e->{
            {
                userNameVer();
                onClick(UsernameField);
                main_controller.updMsg();
            }
        });
        PwdField.setOnMouseClicked(e->{
        {
            psswrdVer();
            onClick(PwdField);
            main_controller.updMsg();
            }
        });

        FirstnameField.setOnMouseClicked(e->{
            notEmpty(FirstnameField);
            onClick(FirstnameField);
            main_controller.updMsg();
        });
        LastnameField.setOnMouseClicked(e->{
            notEmpty(LastnameField);
            onClick(LastnameField);
            main_controller.updMsg();
        });
        AddressField.setOnMouseClicked(e->{
            notEmpty(AddressField);
            onClick(AddressField);
            main_controller.updMsg();
        });

        EmailField.setOnMouseClicked(e->{
            mailVer();
            onClick(EmailField);
            main_controller.updMsg();
        });
        PhoneField.setOnMouseClicked(e->{
            notEmpty(PhoneField);
            onClick(PhoneField);
            main_controller.updMsg();
        });
        DatePickerField.setEditable(false);
        //endregion
        //region on Change
        UsernameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                userNameVer();
                main_controller.updMsg();
            }
        });
        PwdField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
               psswrdVer();
               main_controller.updMsg();
            }
        });

        FirstnameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                notEmpty(FirstnameField);
                main_controller.updMsg();
            }
        });
        LastnameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                notEmpty(LastnameField);
                main_controller.updMsg();
            }
        });
        PhoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                notEmpty(PhoneField);
                main_controller.updMsg();
            }
        });
        EmailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mailVer();
                main_controller.updMsg();
            }
        });
        AddressField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                notEmpty(AddressField);
                main_controller.updMsg();
            }
        });

        //endregion
    }



     public void onClick(Control c ){
        if(main_stage != null) main_stage.close();
        init();
        main_stage.show();
        positionner(c);
    }
     public void positionner(Control c){
       Point2D p = c.localToScreen(0.0,0.0);
       main_stage.setX(p.getX());
       main_stage.setY(p.getY()+30);
   }
     public void init(){
         try {
             FXMLLoader loader =new FXMLLoader(getClass().getResource("LogError.fxml"));
             main_stage = new Stage();
             main_stage.setAlwaysOnTop(true);
            // main_stage.initOwner(Main.appSettings.getAppStage());
             Scene sc = new Scene(loader.load());
             main_controller = (LogErrorController) loader.getController();
             sc.setFill(Color.TRANSPARENT);
             main_stage.initStyle(StageStyle.TRANSPARENT);
             main_stage.setScene(sc);
           // main_controller.main.setOnMouseClicked(e->main_stage.close());

         } catch (
                 IOException e) {
             e.printStackTrace();
         }
     }



     public boolean userNameVer(){

        String usr = UsernameField.getText();
        boolean is_not_taken = true ;  // verification au pres de la bdd
        boolean lenght = usr.length()>=6  ;
              LogErrorController.list.clear();
            LogErrorController.list.add(new Msg("pseudo non utilisé ",is_not_taken));
            LogErrorController.list.add(new Msg("plus de 6 characteres",lenght));
            return lenght&&is_not_taken;
        }
     public boolean psswrdVer(){
         String usr = PwdField.getText();
         boolean lengh = usr.length()>= 8 ;
         boolean num = usr.matches("^.*[0-9].*.[0-9].*");
         boolean spec = usr.matches("^.*(?=.*[*@_;,&çà]).*$");
         LogErrorController.list.clear();
         LogErrorController.list.add(new Msg("au moin 8 caractéres",lengh));
         LogErrorController.list.add(new Msg("au moin 2 chiffres",num));
         LogErrorController.list.add(new Msg("au moin un cara special",spec));
         return lengh&&num&&spec;
     }
     public boolean notEmpty(TextField t){
        boolean emp = t.getText().length()!=0 ;
         LogErrorController.list.clear();
         LogErrorController.list.add(new Msg("remplir ce champs",emp));
         return emp ;
     }
     public boolean mailVer(){
         String email  = EmailField.getText();
         String mail_format = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
         Pattern pattern = Pattern.compile(mail_format);
         Matcher match =  pattern.matcher(email) ;

         boolean valide_mail = match.matches() ;
         boolean is_not_used = true; // BDD request here
         LogErrorController.list.clear();
         LogErrorController.list.add(new Msg("format de mail valide",valide_mail));
         LogErrorController.list.add(new Msg("mail non utilisé",is_not_used));
         return valide_mail&&is_not_used ;
     }
     public boolean dateVer(){
        boolean empty = DatePickerField.getValue() != null;
         LogErrorController.list.clear();
         LogErrorController.list.add(new Msg("selectionnez une date",empty));
         return empty ;
     }


 public void loger(Control c){
        all_is_ok = false ;

     try {
         FXMLLoader loader =new FXMLLoader(getClass().getResource("LogError.fxml"));

         Stage stg = new Stage();
         Point2D p = c.localToScreen(0.0,0.0);
        stg.setX(p.getX());
         stg.setY(p.getY()+30);

         stg.setAlwaysOnTop(true);
         stg.initOwner(Main.appSettings.getAppStage());
         stg.initStyle(StageStyle.TRANSPARENT);
         Scene sc = new Scene(loader.load());
         sc.setFill(Color.TRANSPARENT);
         stg.setScene(sc);
         LogErrorController controller = (LogErrorController) loader.getController();
          controller.main.setOnMouseClicked(e->stg.close());
          controller.errorMsg();
        PauseTransition dela = new PauseTransition(Duration.seconds(1));
         dela.setOnFinished( event -> {
             Timeline timeline = new Timeline();
             KeyFrame key = new KeyFrame(Duration.millis(1000),
                     new KeyValue(stg.getScene().getRoot().opacityProperty(), 0));
             timeline.getKeyFrames().add(key);
             timeline.setOnFinished((ae) -> stg.close());
             timeline.play();
         } );
         dela.play();
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
 loggedln_controller comme une variable statique*/

    @FXML
    private void addUser()
    {   //les variables de la fonction
    	boolean userExiste = false;
    	User user = new User() ;

    /* la recuperation de la date et La transformer vers une date sql donc vous devez remplir le champ date sinon
    il y aura une nullpointer error  */
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


     static class Msg{
        String msg ;
        boolean b ;
        public Msg(String msg,boolean b){
            this.b=b;
            this.msg=msg;
        }
     }
}