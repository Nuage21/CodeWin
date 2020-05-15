import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class LogErrorController implements Initializable {
    @FXML
    Pane main;
    @FXML
    VBox msgbox;

    public static ArrayList<Auth_Controller.Msg> list = new ArrayList<>();

    public void updMsg() {
        msgbox.getChildren().clear();
        for (Auth_Controller.Msg m : list) {
            String valid = m.b ? "checked" : "unchecked";
            Label t = new Label("\t" + m.msg);
            t.setStyle("-fx-text-fill: white");
            Image image = null;
            try {
                image = new Image(new FileInputStream(Settings.projectPath + "img\\" + valid + ".png"));
            } catch (FileNotFoundException e) {
                Debug.debugException(e);
            }
            ImageView img = new ImageView(image);
            img.setFitHeight(25);
            img.setFitWidth(25);
            t.setGraphic(img);

            msgbox.getChildren().add(t);


            Settings.appStage.requestFocus();

        }

    }

    public void errorMsg() {
        main.setStyle("-fx-background-size: 1200 900;" +
                "-fx-background-radius: 18 18 18 18;" +
                "-fx-border-radius: 18 18 18 18;" +
                "-fx-background-color: #ff1a1a;");

        Label er = new Label("Veuillez remplir ce champ correctement");
        er.setStyle("-fx-text-fill: white");
        Image image = null;
        try {
            image = new Image(new FileInputStream("img/unchecked.jpg"));
        } catch (FileNotFoundException e) {
            Debug.debugException(e);
        }
        ImageView img = new ImageView(image);
        img.setFitHeight(25);
        img.setFitWidth(25);
        er.setGraphic(img);
        msgbox.getChildren().clear();
        msgbox.getChildren().add(er);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updMsg();

        main.setStyle("-fx-background-size: 1200 900;" +
                "-fx-background-radius: 18 18 18 18;" +
                "-fx-border-radius: 18 18 18 18;" +
                "-fx-background-color: #2378ab;");


    }

}
