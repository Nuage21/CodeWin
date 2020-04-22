import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoggedIn_Controller {

    @FXML
    private Pane UsernameSignInPane;

    @FXML
    private Pane accountHolderPane;

    @FXML
    private Label accountUsernameLabel;

    @FXML
    private Pane Side_Stats_Pane;

    @FXML
    private VBox darkmodeVBox;

    @FXML
    private Pane Side_Params_Pane;

    @FXML
    private VBox sidebarChaptersHolder;

    @FXML
    private Pane Central_Up_Pane;

    @FXML
    private Pane Side_GO_Pane; // sidebar's general overview Pane

    @FXML
    private VBox Central_Container_SPane;

    @FXML
    private Label Side_Stats_Label;

    @FXML
    private Label nextLabel;

    @FXML
    private Label previousLabel;

    @FXML
    private Label Central_Up_Title_Label;

    @FXML
    private Pane side_bar_activated_pane;

    private int diplayingWhat = Settings.DISPLAYING_COURSE_OVERVIEW; // mainly to decide action to take when previous<->next labels are hit
    private CourseCoord courseCoord;

    private static User user;   // Signed In user
    public static User getUser() {
        return user;
    }
    public static void setUser(User user) {
        LoggedIn_Controller.user = user;
    }


    private CourseOverview courseCO;

    @FXML
    void initialize() throws IOException {

        SwitchButton modeSwitcher = new SwitchButton();
        darkmodeVBox.getChildren().add(modeSwitcher);

        side_bar_activated_pane = Side_GO_Pane;

        Side_Stats_Pane.setOnMouseClicked( event -> {
            Central_Container_SPane.getChildren().clear();
            try {
                Central_Up_Pane.setPrefHeight(295);
                Pane newLoadedPane = FXMLLoader.load(getClass().getResource("Stats.fxml"));
                ScrollPane scp = new ScrollPane();
                scp.setFitToWidth(true);
                scp.setContent(newLoadedPane);
                scp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                Central_Container_SPane.getChildren().add(scp);
                Central_Container_SPane.setPrefHeight(1000);
                this.sidebarFocusNewPane(Side_Stats_Pane);
                Central_Up_Title_Label.setText("Statistiques");


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Side_Params_Pane.setOnMouseClicked((event) -> {
            Central_Container_SPane.getChildren().clear();
            try {
                Central_Up_Pane.setPrefHeight(295);
                Pane newLoadedPane = FXMLLoader.load(getClass().getResource("Params.fxml"));
                Central_Container_SPane.getChildren().add(newLoadedPane);
                Central_Container_SPane.setPrefHeight(1000);

                this.sidebarFocusNewPane(Side_Params_Pane);

                Central_Up_Title_Label.setText("Paramètres du Compte");


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Side_GO_Pane.setOnMouseClicked((event) ->{
            Central_Container_SPane.getChildren().clear();
            try {
                this.loadCourseGeneralOverview();
                if(side_bar_activated_pane != Side_GO_Pane)
                    this.sidebarFocusNewPane(Side_GO_Pane);
                Central_Up_Title_Label.setText("Vue Générale");

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        accountUsernameLabel.setOnMouseEntered( (event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPane.fxml"));
                Parent root = loader.load();
                Stage accStage = new Stage();
                accStage.initStyle(StageStyle.UNDECORATED);
                accStage.setX(accountHolderPane.getLayoutX());
                accStage.setY(accountHolderPane.getHeight() - 3);
                accStage.initOwner(Main.appSettings.appStage);
                accStage.setScene(new Scene(root));
                AccountPane_Controller ctr = loader.getController();
                ctr.setStage(accStage);
                accStage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        nextLabel.setOnMouseClicked(mouseEvent -> {
            if(this.diplayingWhat == Settings.DISPLAYING_COURSE)
            {
                if(this.courseCoord.isLastCourse())
                {
                    // COURSE FINISHED
                }
                // here... we still have some courses to go through
                else
                {
                    if(this.courseCoord.isLastCourseWithinChapter())
                    {

                    }
                    // Still in the same Chapter
                    try {
                        CourseCoord nextCourse = this.courseCoord.getNextCourse();
                        this.displayCourse(nextCourse.chapterID, nextCourse.courseID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            previousLabel.setOnMouseClicked(mouseEvent1 -> {
                try {
                    CourseCoord previousCourse = this.courseCoord.getPreviousCourse();
                    this.displayCourse(previousCourse.chapterID, previousCourse.courseID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        this.loadCourseGeneralOverview(); // Course's General Overview shown by default
        this.viewSidebarChapters();

    }
    public void loadUserParams()
    {
        accountUsernameLabel.setText(LoggedIn_Controller.user.getUsername());

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

        CourseOverview CO = new CourseOverview(Main.appSettings.dataPath + "Overview.json");
        this.courseCO = CO;
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
            String folder = chap.getFolder();

            controller.setAll(title, nCourses, nQuestions, nQuizes);

            Pane holder = controller.getHolderPane();
            int finalI = i;
            holder.setOnMouseClicked(event -> {
                try {
                    this.loadChapterOverview(folder, finalI);
                    this.diplayingWhat = Settings.DISPLAYING_CHAPTER_OVERVIEW;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            vb.getChildren().add(course_pane);
        }
        ScrollPane scp = new ScrollPane();
        scp.setFitToWidth(true);
        scp.setContent(vb);
        Central_Container_SPane.getChildren().add(scp);
    }


    @FXML
    public void viewSidebarChapters()
    {
        VBox vb = new VBox();
        for(int i = 0; i < courseCO.getChapters().size(); ++i)
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SideBarChapterPane.fxml"));
                Pane root = loader.load();
                CourseOverview.ChapterOverview chapter = courseCO.getChapters().get(i);
                SidebarChapterPane_Controller ctr = loader.getController();
                ctr.setTitle(chapter.getChapterTitle());
                vb.getChildren().add(root);

                VBox expandedHolder = new VBox();

                for(int j =0; j < chapter.getCourses().size(); ++j)
                {
                    String courseTitle = chapter.getCourses().get(j);
                    FXMLLoader expLoader = new FXMLLoader(getClass().getResource("SidebarExpandedChapterPane.fxml"));
                    Pane expCoursePane = expLoader.load();
                    SidebarExpandedChapterPane_Controller expController = expLoader.getController();
                    expController.setTitle("> " + courseTitle);
                    expandedHolder.getChildren().add(expCoursePane);
                }

                ctr.setExpandablePane(expandedHolder);
                expandedHolder.setManaged(false);
                expandedHolder.setVisible(false);
                vb.getChildren().add(expandedHolder);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        ScrollPane scp = new ScrollPane();
        scp.setContent(vb);
        scp.setFitToWidth(true);
        sidebarChaptersHolder.getChildren().add(scp);
    }

    public void loadChapterOverview(String folder, int chapterID) throws IOException {
        String json_file = Main.appSettings.dataPath + folder + "Overview.json";
        String jsonOverviewString = Files.readString(Paths.get(json_file), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(jsonOverviewString);
        JSONArray courses = obj.getJSONArray("courses");
        JSONArray files = obj.getJSONArray("files");
        JSONArray readTimes = obj.getJSONArray("readTimes");
        int nCourses = courses.length();

        // empty Central Pane
        Central_Container_SPane.getChildren().clear();

        VBox holder = new VBox();
        ScrollPane scp = new ScrollPane();

        for(int i = 0; i < nCourses; ++i)
        {
            String courseTitle = courses.getString(i);
            int readTime = readTimes.getInt(i);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CO_Course_Pane.fxml"));
            Pane coursePane = loader.load();
            CO_Course_Pane_Controller controller = loader.getController();
            controller.setAll(courseTitle, readTime);
            holder.getChildren().add(coursePane);

            // add mouse click event
            int finalI = i;
            coursePane.setOnMouseClicked(mouseEvent -> {
                try {
                    this.displayCourse(chapterID, finalI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        scp.setContent(holder);
        scp.setFitToWidth(true);
        Central_Container_SPane.getChildren().add(scp);
    }

    public void displayCourse(int _chapID, int _courseID) throws IOException {
        // update Displaying_What
        this.diplayingWhat = Settings.DISPLAYING_COURSE;
        CourseCoord.CO = this.courseCO; // static
        this.courseCoord = new CourseCoord(_chapID, _courseID);

        FXMLLoader coursePaneLoader = new FXMLLoader(getClass().getResource("CoursePane.fxml"));
        Parent readCourseSPane = null;
        try {
            readCourseSPane = coursePaneLoader.load();
            CoursePane_Controller cPaneController = coursePaneLoader.getController();
            cPaneController.setCourseImagesFolder(this.courseCoord.imagesPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CoursePane_Controller coursePaneController = coursePaneLoader.getController();
        try {
            coursePaneController.displayFromJson(this.courseCoord.filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Central_Container_SPane.getChildren().clear();
        Central_Container_SPane.getChildren().add(readCourseSPane);
        Central_Up_Title_Label.setText(this.courseCoord.getCourseTitle());
    }

}
