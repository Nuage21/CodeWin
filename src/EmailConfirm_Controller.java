import DialogBoxes.ErrorBox_Controller;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class EmailConfirm_Controller implements Controller{

    @FXML
    private TextField c1;
    @FXML
    private TextField c2;
    @FXML
    private TextField c3;
    @FXML
    private TextField c4;
    @FXML
    private TextField c5;
    @FXML
    private TextField c6;

    @FXML
    private Label changeEmailLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancealButton;

    @FXML
    private Pane errorPane;

    private String correctCode;

    private ArrayList<TextField> digits = new ArrayList<>();

    public ArrayList<Node> childrens = new ArrayList<>();
    public VBox signupvbox;

    @FXML public
    void initialize() {

        errorPane.setVisible(false); // hide no error (left visible for design purposes)
        cancealButton.setOnMouseClicked(mouseEvent -> {
            Platform.exit();
        });

        digits.add(c1);
        digits.add(c2);
        digits.add(c3);
        digits.add(c4);
        digits.add(c5);
        digits.add(c6);

        for (int i = 0 ; i < 6; ++i) {
            TextField t = digits.get(i);
            int finalI = i;
            t.setOnKeyPressed(keyEvent -> {
                errorPane.setVisible(false);
                t.setText("");
            });
            t.setOnKeyReleased(keyEvent -> {
                if(finalI <= 4)
                    digits.get(finalI + 1).requestFocus();
            });
            // add clipboard paste feature
        }

        submitButton.setOnMouseClicked(mouseEvent -> {
            boolean isValid = enteredCode().equals(this.correctCode);
            if(isValid)
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoggedIn.fxml"));
                LoggedIn_Controller ctr = loader.getController();
                Parent root = null;
                try {
                    root = loader.load();
                    LanguageManager.resyncLanguage(loader, "loggedin");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                boolean isInserted = true;
                if(Settings.ACTIVE_DB_MODE)
                {
                    isInserted = User.addUser(LoggedIn_Controller.getUser());
                    if(!isInserted)
                        Checker.showConnexionError();
                }
                if(isInserted)
                {
                    Stage stg = Settings.appStage;
                    Scene sc = new Scene(root);
                    stg.setScene(sc);
                    stg.setFullScreen(Settings.FULLSCREEN_MODE);
                    ctr.loadUserParams();
                }
            }
            else
            {
                // show error message
                errorPane.setVisible(true);
            }
        });

        changeEmailLabel.setOnMouseClicked(mouseEvent -> {
            signupvbox.getChildren().clear();
            signupvbox.getChildren().addAll(childrens);
        });
    }

    private String enteredCode()
    {
        String acc = "";
        for(TextField t : digits)
        {
            acc += t.getText();
        }
        return acc;
    }

    public void setEmail(String _email)
    {
        emailLabel.setText(_email);
    }
    public static String generate6Digits()
    {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public boolean sendConfEmail(String recipient)
    {
        correctCode = generate6Digits();
        MailSender ms = new MailSender(Settings.CodeWinEmail, Settings.CodeWinEmailPwd);
        return ms.sendConfirmationMail(emailLabel.getText(), correctCode);
    }

    private String getClipboardString(){
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = c.getContents(this);
        String txt = null;
        try {
            txt = (String) t.getTransferData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txt;
    }
}
