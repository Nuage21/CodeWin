import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.*;

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
    void initialize() throws IOException {

        side_bar_activated_pane = Side_GO_Pane;

        Side_Params_Pane.setOnMouseClicked((event) -> {
            Central_Container_SPane.getChildren().clear();
            try {
                Central_Up_Pane.setPrefHeight(305);
                Pane newLoadedPane = FXMLLoader.load(getClass().getResource("Params.fxml"));
                Central_Container_SPane.getChildren().add(newLoadedPane);
                Central_Container_SPane.setPrefHeight(1000);

                this.sidebarFocusNewPane(Side_Params_Pane);

                Central_Up_Title_Label.setText("Parametres du Compte");


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Side_GO_Pane.setOnMouseClicked((event) ->{
            Central_Container_SPane.getChildren().clear();
            try {
                this.loadCourseGeneralOverview();
                this.sidebarFocusNewPane(Side_GO_Pane);
                Central_Up_Title_Label.setText("Vue Générale");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.loadCourseGeneralOverview(); // Course's General Overview shown by default
    }

    @FXML
    private void quitApp() {
        Platform.exit();
        System.exit(0);
    }

    public void sidebarFocusNewPane(Pane p)
    {
        sidebarPaneFocus(p, false);
        sidebarPaneFocus(side_bar_activated_pane, true);
        side_bar_activated_pane = p;
    }

    public void sidebarPaneFocus(Pane p, boolean unfocus) {
        if (unfocus)
            p.setStyle("-fx-background-color: transparent; -fx-border-color:  transparent;");
        else
            p.setStyle("-fx-background-color: white !important; -fx-border-color:  #1354a1");

    }

    public void loadCourseGeneralOverview() throws IOException {

        CourseOverview CO = new CourseOverview("C:\\Users\\hbais\\Desktop\\test_loggedin\\src\\sample\\Overview.json");
        ArrayList<CourseOverview.ChapterOverview> chapters = CO.getChapters();

        VBox vb = new VBox(); // CoursePanes' container (he's inside a ScrollPane)

        for(int i = 0; i < chapters.size(); ++i)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GO_Course_Pane.fxml"));
            Pane course_pane = loader.load();
            GO_Course_Pane_Controller controller = loader.getController();

            CourseOverview.ChapterOverview chap = chapters.get(i);

            String title = CO.getChapterNomination() + " " + chap.getID() + ": " + chap.getChapterTitle();
            int nCourses = chap.getnCourse();
            int nQuestions = chap.getnQuestions();
            int nQuizes = chap.getnQuizes();

            controller.setAll(title, nCourses, nQuestions, nQuizes);

            vb.getChildren().add(course_pane);
        }
        ScrollPane scp = new ScrollPane();
        scp.setContent(vb);
        Central_Container_SPane.getChildren().add(scp);
    }
}
