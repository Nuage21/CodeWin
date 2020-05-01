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

import java.io.IOException;
import java.util.ArrayList;

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

        correctCode = generate6Digits();

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

        for (TextField t : digits) {
            t.setOnKeyPressed(keyEvent -> {
                errorPane.setVisible(false);
                t.setText("");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stg = Settings.appStage;
                Scene sc = new Scene(root);
                stg.setScene(sc);
                stg.setFullScreen(Settings.FULLSCREEN_MODE);
                ctr.loadUserParams();
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

    private String generate6Digits()
    {
        return "123456";
    }
}
