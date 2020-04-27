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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

import javafx.scene.text.Font;
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
    private Pane pointsHolderPane;

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
    private Label pointsLabel;

    @FXML
    private Label Central_Up_Title_Label;

    @FXML
    private Pane side_bar_activated_pane;

    private int diplayingWhat = Settings.DISPLAYING_COURSE_OVERVIEW; // mainly to decide action to take when previous<->next labels are hit
    private CourseCoord courseCoord;
    private int chapterID;

    private ArrayList<Integer> courseQuestions; // when displaying questions - keep record of all <-prev|next-> qsts
    private int questionsOffset = -1; // keep trace of questions navigation

    private TreeMap<CourseCoord, Pane> SidebarCoursePaneMapper = new TreeMap<>();
    private TreeMap<Integer, SidebarChapterPane_Controller> SidebarChapterPanesControllersMapper = new TreeMap<>(); // Key: chapterID: ChapterPaneCtr
    private ArrayList<Pane> sidebarChaptersPanes = new ArrayList<>();
    private static User user;   // Signed In user

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        LoggedIn_Controller.user = user;
    }

    private String sidebarOriginalColor = "tranparent";

    private CourseOverview courseCO;

    @FXML
    void initialize() throws IOException {

        pointsHolderPane.setVisible(false);
        CourseOverview CO = new CourseOverview(Settings.dataPath + "Overview.json");
        this.courseCO = CO;
        CourseCoord.CO = CO;

        Font fnt = Font.loadFont(getClass().getResource("fonts/ErbosDraco1StNbpRegular-99V5.ttf").toExternalForm(), 75);
        pointsLabel.setFont(fnt);
        pointsLabel.setText(user.);

        SwitchButton modeSwitcher = new SwitchButton();
        darkmodeVBox.getChildren().add(modeSwitcher);

        side_bar_activated_pane = Side_GO_Pane;

        Side_Stats_Pane.setOnMouseClicked(event -> {
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
                this.sidebarOriginalColor = "transparent";
                Central_Up_Title_Label.setText("Statistiques");
                this.setDiplayingWhat(Settings.DISPLAYING_STATISTICS);

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
                this.sidebarOriginalColor = "transparent";
                Central_Up_Title_Label.setText("Paramètres du Compte");

                this.setDiplayingWhat(Settings.DISPLAYING_PARAMETERS);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Side_GO_Pane.setOnMouseClicked((event) -> {
            Central_Container_SPane.getChildren().clear();
            try {
                this.loadCourseGeneralOverview();
                if (side_bar_activated_pane != Side_GO_Pane)
                    this.sidebarFocusNewPane(Side_GO_Pane);
                this.sidebarOriginalColor = "transparent";
                Central_Up_Title_Label.setText("Vue Générale");

            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setDiplayingWhat(Settings.DISPLAYING_COURSE_OVERVIEW);
        });

        accountUsernameLabel.setOnMouseEntered((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountPane.fxml"));
                Parent root = loader.load();
                Stage accStage = new Stage();
                accStage.initStyle(StageStyle.UNDECORATED);
                accStage.setX(accountHolderPane.getLayoutX());
                accStage.setY(accountHolderPane.getHeight() - 3);
                accStage.initOwner(Settings.appStage);
                accStage.setScene(new Scene(root));
                AccountPane_Controller ctr = loader.getController();
                ctr.setStage(accStage);
                accStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        nextLabel.setOnMouseClicked(mouseEvent -> {
            if (this.diplayingWhat == Settings.DISPLAYING_COURSE) {
                if (this.courseCoord.isLastCourse()) {
                    // COURSE FINISHED
                }
                // here... we still have some courses to go through
                else {
                    if (this.courseCoord.isLastCourseWithinChapter()) {

                    }
                    // Still in the same Chapter
                    if (this.courseCoord.hasQuestions()) {
                        this.courseQuestions = this.courseCoord.getQuestions();
                        this.displayNeighborQuestion(true);
                    } else
                        this.displayNeighborCourse(true);
                }
            } else if (this.diplayingWhat == Settings.DISPLAYING_CHAPTER_OVERVIEW) {
                ArrayList<CourseOverview.ChapterOverview> chapters = this.courseCO.getChapters();
                if (chapterID + 1 < chapters.size()) {
                    this.SidebarChapterPanesControllersMapper.get(chapterID).unexpand();
                    this.chapterID++;
                    this.SidebarChapterPanesControllersMapper.get(chapterID).expand();
                    Pane nextChapPane = this.sidebarChaptersPanes.get(chapterID);
                    try {
                        this.loadChapterOverview(chapters.get(chapterID).getFolder(), chapterID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.sidebarFocusNewPane(nextChapPane);
                    this.sidebarOriginalColor = "transparent";
                }
            } else if (this.diplayingWhat == Settings.DISPLAYING_QUESTION) {
                if (this.questionsOffset < this.courseQuestions.size())
                    displayNeighborQuestion(true);
                else
                    this.displayNeighborCourse(true);
            }
        });

        previousLabel.setOnMouseClicked(mouseEvent -> {
            if (this.diplayingWhat == Settings.DISPLAYING_COURSE) {
                displayNeighborCourse(false);
            } else if (this.diplayingWhat == Settings.DISPLAYING_CHAPTER_OVERVIEW) {
                if (this.chapterID != 0) // no previous chapter
                {
                    ArrayList<CourseOverview.ChapterOverview> chapters = this.courseCO.getChapters();
                    this.SidebarChapterPanesControllersMapper.get(chapterID).unexpand();
                    this.chapterID--;
                    this.SidebarChapterPanesControllersMapper.get(chapterID).expand();
                    Pane prevChapPane = this.sidebarChaptersPanes.get(chapterID);
                    try {
                        this.loadChapterOverview(chapters.get(chapterID).getFolder(), chapterID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.sidebarFocusNewPane(prevChapPane);
                    this.sidebarOriginalColor = "transparent";
                }
            } else if (this.diplayingWhat == Settings.DISPLAYING_QUESTION) {
                if (this.questionsOffset >= 1)
                    displayNeighborQuestion(false);
                else {
                    try {
                        this.displayCurrentCourse();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        this.loadCourseGeneralOverview(); // Course's General Overview shown by default
        this.viewSidebarChapters();

    }

    public void loadUserParams() {
        accountUsernameLabel.setText(LoggedIn_Controller.user.getUsername());

    }

    public void sidebarFocusNewPane(Pane p) {
        if (side_bar_activated_pane == p)
            return;
        sidebarPaneFocus(p, false);
        sidebarPaneFocus(side_bar_activated_pane, true);
        side_bar_activated_pane = p;
    }

    public void sidebarPaneFocus(Pane p, boolean unfocus) {
        if (unfocus) {
            p.setStyle("-fx-border-color:  transparent;");
            p.setStyle("-fx-background-color: " + sidebarOriginalColor + " !important;");
        } else
            p.setStyle("-fx-background-color: white !important; -fx-border-color:  #1354a1 !important;");

    }

    public void loadCourseGeneralOverview() throws IOException {

        ArrayList<CourseOverview.ChapterOverview> chapters = courseCO.getChapters();

        VBox vb = new VBox(); // CoursePanes' container (he's inside a ScrollPane)

        for (int i = 0; i < chapters.size(); ++i) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GO_Course_Pane.fxml"));
            Pane course_pane = loader.load();
            GO_Course_Pane_Controller controller = loader.getController();

            CourseOverview.ChapterOverview chap = chapters.get(i);

            String title = courseCO.getChapterNomination() + " " + chap.getID() + ": " + chap.getChapterTitle();
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
                    this.sidebarFocusNewPane(this.sidebarChaptersPanes.get(finalI));
                    this.setDiplayingWhat(Settings.DISPLAYING_CHAPTER_OVERVIEW);

                    // sidebar focus and expand chapter's pane
                    this.sidebarFocusNewPane(this.sidebarChaptersPanes.get(finalI));
                    this.SidebarChapterPanesControllersMapper.get(finalI).expand();
                    this.sidebarOriginalColor = "transparent";
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
    public void viewSidebarChapters() {
        VBox vb = new VBox();
        for (int i = 0; i < courseCO.getChapters().size(); ++i) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SideBarChapterPane.fxml"));
                Pane root = loader.load();

                this.sidebarChaptersPanes.add(root);
                CourseOverview.ChapterOverview chapter = courseCO.getChapters().get(i);
                SidebarChapterPane_Controller ctr = loader.getController();
                ctr.setTitle(chapter.getChapterTitle());
                vb.getChildren().add(root);

                int finalI1 = i;
                root.setOnMouseClicked(mouseEvent -> {
                    this.sidebarFocusNewPane(root);
                    this.sidebarOriginalColor = "transparent";
                    try {
                        this.loadChapterOverview(chapter.getFolder(), finalI1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                VBox expandedHolder = new VBox();

                for (int j = 0; j < chapter.getCourses().size(); ++j) {
                    String courseTitle = chapter.getCourses().get(j);
                    FXMLLoader expLoader = new FXMLLoader(getClass().getResource("SidebarExpandedChapterPane.fxml"));
                    Pane expCoursePane = expLoader.load();
                    SidebarExpandedChapterPane_Controller expController = expLoader.getController();
                    expController.setTitle("> " + courseTitle);
                    expandedHolder.getChildren().add(expCoursePane);

                    int finalI = i;
                    int finalJ = j;
                    expCoursePane.setOnMouseClicked(mouseEvent -> {
                        try {
                            this.displayCourse(finalI, finalJ);
                            this.sidebarFocusNewPane(expCoursePane);
                            this.sidebarOriginalColor = "#abcbdb";

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    SidebarCoursePaneMapper.put(new CourseCoord(i, j), expCoursePane);
                }

                ctr.setExpandablePane(expandedHolder);
                expandedHolder.setManaged(false);
                expandedHolder.setVisible(false);
                vb.getChildren().add(expandedHolder);
                this.SidebarChapterPanesControllersMapper.put(i, ctr); // record Expanded Pane
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ScrollPane scp = new ScrollPane();
        scp.setContent(vb);
        scp.setFitToWidth(true);
        sidebarChaptersHolder.getChildren().add(scp);
    }

    public void loadChapterOverview(String folder, int chapterID) throws IOException {
        this.setDiplayingWhat(Settings.DISPLAYING_CHAPTER_OVERVIEW);
        this.chapterID = chapterID;
        String json_file = Settings.dataPath + folder + "Overview.json";
        System.out.println(json_file);
        String jsonOverviewString = Files.readString(Paths.get(json_file), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(jsonOverviewString);
        JSONArray courses = obj.getJSONArray("courses");
        JSONArray readTimes = obj.getJSONArray("readTimes");
        int nCourses = courses.length();

        // empty Central Pane
        Central_Container_SPane.getChildren().clear();

        VBox holder = new VBox();
        ScrollPane scp = new ScrollPane();

        for (int i = 0; i < nCourses; ++i) {
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
                    this.SidebarChapterPanesControllersMapper.get(chapterID).expand();
                    Pane sideExpPane = this.SidebarCoursePaneMapper.get(new CourseCoord(chapterID, finalI));
                    this.sidebarFocusNewPane(sideExpPane);
                    this.sidebarOriginalColor = "#abcbdb";
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
        this.setDiplayingWhat(Settings.DISPLAYING_COURSE);
        CourseCoord.CO = this.courseCO; // static
        this.courseCoord = new CourseCoord(_chapID, _courseID);

        FXMLLoader coursePaneLoader = new FXMLLoader(getClass().getResource("CoursePane.fxml"));
        Parent readCourseSPane = null;
        try {
            readCourseSPane = coursePaneLoader.load();
            CoursePane_Controller cPaneController = coursePaneLoader.getController();
            cPaneController.setCourseImagesFolder(this.courseCoord.getImagesPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        CoursePane_Controller coursePaneController = coursePaneLoader.getController();
        try {
            coursePaneController.displayFromJson(this.courseCoord.getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Central_Container_SPane.getChildren().clear();
        Central_Container_SPane.getChildren().add(readCourseSPane);
        Central_Up_Title_Label.setText(this.courseCoord.getCourseTitle());
    }

    public void displayQuestion(int id) throws IOException, ParseException {
        Question question = new Question().loadFromJson(Settings.dataPath + "Questions.json", id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionPane.fxml"));
        Parent root = loader.load();
        QuestionPane_Controller ctr = loader.getController();
        ctr.showQuestion(question);
        ScrollPane scp = new ScrollPane();
        scp.setContent(root);
        this.Central_Container_SPane.getChildren().clear();
        this.Central_Container_SPane.getChildren().add(scp);
        this.setDiplayingWhat(Settings.DISPLAYING_QUESTION);
    }

    public void displayNeighborCourse(boolean isNext) {
        this.courseQuestions = null;
        this.questionsOffset = -1;
        try {
            CourseCoord nextCourse = isNext ? this.courseCoord.getNextCourse() : this.courseCoord.getPreviousCourse();
            SidebarChapterPanesControllersMapper.get(courseCoord.chapterID).unexpand();
            this.displayCourse(nextCourse.chapterID, nextCourse.courseID);
            Pane p = SidebarCoursePaneMapper.get(new CourseCoord(nextCourse.chapterID, nextCourse.courseID));
            this.sidebarFocusNewPane(p);
            this.sidebarOriginalColor = "#abcbdb";
            SidebarChapterPanesControllersMapper.get(nextCourse.chapterID).expand();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayCurrentCourse() throws IOException {
        this.displayCourse(courseCoord.chapterID, courseCoord.courseID);
        Pane p = SidebarCoursePaneMapper.get(new CourseCoord(courseCoord.chapterID, courseCoord.courseID));
        this.sidebarFocusNewPane(p);
        this.sidebarOriginalColor = "#abcbdb";
        SidebarChapterPanesControllersMapper.get(courseCoord.chapterID).expand();
    }

    public void displayNeighborQuestion(boolean isNext) {
        if (isNext)
            this.questionsOffset++;
        else
            this.questionsOffset--;

        int questionID = this.courseQuestions.get(questionsOffset);
        try {
            this.displayQuestion(questionID);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDiplayingWhat(int _mode) {
        this.pointsHolderPane.setVisible(_mode == Settings.DISPLAYING_COURSE || _mode == Settings.DISPLAYING_QUESTION);
        this.diplayingWhat = _mode;
    }
}
