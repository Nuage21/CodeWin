import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LoggedIn_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Pane UsernameSignInPane;

    @FXML
    private Pane Side_Stats_Pane;

    @FXML
    private Pane Side_Params_Pane;

    @FXML
    private Pane Central_Up_Pane;

    @FXML
    private Pane Side_GO_Pane; // sidebar's general overview Pane

    @FXML
    private VBox Central_Container_SPane;

    @FXML
    private Label Side_Stats_Label;

    @FXML
    private Label Central_Up_Title_Label;

    private Pane side_bar_activated_pane;


    private static User user;   // Signed In user
    public static User getUser() {
        return user;
    }
    public static void setUser(User user) {
        LoggedIn_Controller.user = user;
    }


    @FXML
    void initialize() {

        side_bar_activated_pane = Side_GO_Pane;

        Side_Params_Pane.setOnMouseClicked((event) -> {
            Central_Container_SPane.getChildren().clear();
            try {
                Central_Up_Pane.setPrefHeight(305);
                Pane newLoadedPane = FXMLLoader.load(getClass().getResource("Params.fxml"));
                Central_Container_SPane.getChildren().add(newLoadedPane);
                Central_Container_SPane.setPrefHeight(1000);

                sidebarPaneFocus(Side_Params_Pane, false);
                sidebarPaneFocus(side_bar_activated_pane, true);
                side_bar_activated_pane = Side_Params_Pane;

                Central_Up_Title_Label.setText("Paramètres du Compte");


            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void quitApp() {
        Platform.exit();
        System.exit(0);
    }

    public void sidebarPaneFocus(Pane p, boolean unfocus) {
        if (unfocus)
            p.setStyle("-fx-background-color: transparent; -fx-border-color:  transparent;");
        else
            p.setStyle("-fx-background-color: white !important; -fx-border-color:  #1354a1");

    }
}
