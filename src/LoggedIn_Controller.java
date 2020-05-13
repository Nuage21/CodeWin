import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class LoggedIn_Controller implements Controller {

    @FXML
    Hyperlink githubHyperLink;
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
    private Pane Side_Toggle_Pane;

    @FXML
    private Label Side_Params_Label;
    @FXML
    private Label Side_Stats_Label;
    @FXML
    private Label Side_Toggle_Label;
    @FXML
    private Label Side_Help_Label;
    @FXML
    private Label Side_GO_Label;

    @FXML
    private VBox sidebarChaptersHolder;

    @FXML
    private Pane Central_Up_Pane;

    @FXML
    private Pane pointsHolderPane;

    @FXML
    private ScrollPane searchSPane;

    @FXML private VBox searchVBox;

    @FXML
    private Pane Side_CourseName_Pane;

    @FXML
    private Pane Side_GO_Pane; // sidebar's general overview Pane

    @FXML
    private VBox Central_Container_SPane;

    @FXML
    private VBox Sidebar_VBox;

    @FXML
    private Label nextLabel;

    @FXML
    private Label previousLabel;

    @FXML
    private Label pointsLabel;

    @FXML
    private Label Central_Up_Title_Label;

    @FXML
    private TextField searchField;
    private Pane side_bar_activated_pane;

    @FXML
    private BorderPane daddy;

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

    private Controller occupiedController; // Central Pane loaded fxml's controller

    private static boolean waitSkipped = false; // is true when question evaluated and clicked next (so waiter thread skipped)
    private boolean questionEvalJustFired = false; // is true when question evaluated and clicked next (so waiter thread skipped)
    private boolean questionAnswerFound = false; // found the answer ?

    @FXML
    public void initialize() throws IOException {

        Platform.runLater(() -> {
            loadUserParams();
        });
        pointsHolderPane.setVisible(false);
        CourseOverview CO = new CourseOverview(Settings.dataPath + "Overview.json");
        this.courseCO = CO;
        CourseCoord.CO = CO;

        Font fnt = Font.loadFont(getClass().getResource("fonts/ErbosDraco1StNbpRegular-99V5.ttf").toExternalForm(), 75);
        pointsLabel.setFont(fnt);
        setLText(pointsLabel, "0000");

        SwitchButton modeSwitcher = new SwitchButton();
        darkmodeVBox.getChildren().add(modeSwitcher);

        side_bar_activated_pane = Side_GO_Pane;

        githubHyperLink.setOnMouseClicked(mouseEvent -> {
            Settings.application.getHostServices().showDocument(Settings.githubLink);
        });
        Side_Stats_Pane.setOnMouseClicked(event -> {
            Central_Container_SPane.getChildren().clear();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Stats.fxml"));
                Pane newLoadedPane = loader.load();
                Stats_Controller ctr = loader.getController();
                this.occupiedController = ctr;
                ScrollPane scp = new ScrollPane();
                scp.setFitToWidth(true);
                scp.setContent(newLoadedPane);
                scp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                Central_Container_SPane.getChildren().add(scp);
                Central_Container_SPane.setPrefHeight(1000);
                this.sidebarFocusNewPane(Side_Stats_Pane);
                this.sidebarOriginalColor = "transparent";
                setLText(Central_Up_Title_Label, "Statistiques");
                this.setDiplayingWhat(Settings.DISPLAYING_STATISTICS);


            } catch (IOException e) {
                Debug.debugException(e);
            }
        });

        Side_Params_Pane.setOnMouseClicked((event) -> {
            displayParams();
        });

        Side_GO_Pane.setOnMouseClicked((event) -> {
            Central_Container_SPane.getChildren().clear();
            try {
                this.loadCourseGeneralOverview();
                if (side_bar_activated_pane != Side_GO_Pane)
                    this.sidebarFocusNewPane(Side_GO_Pane);
                this.sidebarOriginalColor = "transparent";
                setLText(Central_Up_Title_Label, "Vue Générale");

            } catch (IOException e) {
                Debug.debugException(e);
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
                ctr.getParamsPane().setOnMouseClicked(mouseEvent -> {
                    displayParams();
                });
                ctr.setStage(accStage);
                accStage.show();
            } catch (IOException e) {
                Debug.debugException(e);
            }
        });

        nextLabel.setOnMouseClicked(mouseEvent -> {
            if (this.diplayingWhat == Settings.DISPLAYING_COURSE) {
//                if (this.courseCoord.isLastCourse()) {
//                    // COURSE FINISHED
//                }
//                // here... we still have some courses to go through
//                else {
//                    if (this.courseCoord.isLastCourseWithinChapter()) {
//
//                    }
//                    // Still in the same Chapter
                if (this.courseCoord.hasQuestions()) {
                    this.courseQuestions = this.courseCoord.getQuestions();
                    this.displayNeighborQuestion(true);
                } else
                    this.displayNeighborCourse(true);
//                }
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
                        Debug.debugException(e);
                    }
                    this.sidebarFocusNewPane(nextChapPane);
                    this.sidebarOriginalColor = "transparent";
                }
            } else if (this.diplayingWhat == Settings.DISPLAYING_QUESTION) {
                User u = LoggedIn_Controller.getUser();
                boolean alreadyAnswered = u.isQuesationAnswered(this.courseQuestions.get(questionsOffset));
                if (alreadyAnswered)
                    displayNeighborQuestion(true);
                else {
                    if (!questionEvalJustFired) {
                        waitSkipped = false;
                        this.questionEvalJustFired = true;
                        QuestionPane_Controller ctr = (QuestionPane_Controller) occupiedController;
                        boolean eval = ctr.evaluateAnswer();
                        this.questionAnswerFound = eval;
                        if (eval) {
                            MediaPlayer.playAnswerReaction(MediaPlayer.CORRECT_SOUND);
                            ctr.getAnswerPane_controller().setVisible(true);
                            ctr.getNotePane_controller().setVisible(true);
                            this.updateUserPoints(LoggedIn_Controller.getUser().getPoints() + ctr.getQuestion().getPoints());
                        } else {
                            MediaPlayer.playAnswerReaction(MediaPlayer.WRONG_SOUND);
                            ctr.getAnswerPane_controller().setToFalseAnswer();
                            ctr.getAnswerPane_controller().setVisible(true);
                            ctr.getNotePane_controller().setVisible(true);
                        }

                        Task<Void> sleeper = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                try {
                                    Thread.sleep(4000);
                                } catch (InterruptedException e) {
                                }
                                return null;
                            }
                        };
                        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent event) {
                                if (!waitSkipped) {
                                    if (eval) {
                                        if (questionsOffset + 1 < courseQuestions.size()) // not last question within chapter
                                        {
                                            displayNeighborQuestion(true);
                                            updateLastQuestion(false);
                                        } else {
                                            displayNeighborCourse(true);
                                            updateLastQuestion(true);
                                        }
                                    } else {
                                        try {
                                            displayCurrentCourse();
                                        } catch (IOException e) {
                                            Debug.debugException(e);
                                        }
                                    }
                                }
                            }
                        });
                        new Thread(sleeper).start();
                    } else {
                        waitSkipped = true;
                        if (this.questionAnswerFound)
                            displayNeighborQuestion(true);
                        else {
                            try {
                                displayCurrentCourse(); // get them back to the course... need more reading
                            } catch (IOException e) {
                                Debug.debugException(e);
                            }
                        }
                    }
                }
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
                        Debug.debugException(e);
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
                        this.questionsOffset = -1;
                    } catch (IOException e) {
                        Debug.debugException(e);
                    }
                }
            }
        });

        this.loadCourseGeneralOverview(); // Course's General Overview shown by default
        this.viewSidebarChapters();

        Side_Toggle_Pane.setOnMouseClicked(mouseEvent -> {
            this.toggleSidebar();
        });

        Platform.runLater(() -> {
            Settings.SIDEBAR_WIDTH = this.Sidebar_VBox.getWidth();
            Settings.SIDEBAR_DELTA = Settings.SIDEBAR_WIDTH * (1 - (1 / Settings.SIDEBAR_EXTEND_COEFF));
            Design.setWidth(daddy, Settings.appStage.getWidth());
            Design.setHeight(daddy, Settings.appStage.getHeight());
            Design.CENTRAL_PANE_WIDTH = Central_Container_SPane.getWidth();
        });

        Platform.runLater(() -> {
            User u = LoggedIn_Controller.getUser();
            if (!u.neverReadACourse()) {
                int chapterID = u.lastCourseChapter();
                int courseID = u.lastCourseID();
                if (u.wasReadingCourse()) {
                    try {
                        displayCourse(chapterID, courseID);
                    } catch (IOException e) {
                        Debug.debugException(e);
                    }
                } else {
                    int questionID = u.lastQuestionID();
                    try {
                        try {
                            this.courseCoord = new CourseCoord(chapterID, courseID);
                        } catch (IOException e) {
                            Debug.debugException(e);
                        }
                        this.questionsOffset = courseCoord.getQuestions().indexOf(questionID);
                        this.courseQuestions = courseCoord.getQuestions();
                        try {
                            displayQuestion(questionID);
                        } catch (IOException e) {
                            Debug.debugException(e);
                        }
                    } catch (ParseException e) {
                        Debug.debugException(e);
                    }
                }
            }
        });

        searchField.setOnKeyReleased(keyEvent -> {
            String query = searchField.getText();
            ArrayList<CourseCoord> courses = getCoursesThatContains(query);
            showFrom(courses);
        });

        searchSPane.setOnMouseExited(mouseEvent -> {
            searchSPane.setVisible(false);
        });
    }

    public ArrayList<CourseCoord> getCoursesThatContains(String fetchQuery) {
        ArrayList<CourseCoord> ret = new ArrayList<>();
        ArrayList<CourseOverview.ChapterOverview> chapters = courseCO.getChapters();
        for (int i = 0; i < chapters.size(); i++) {
            CourseOverview.ChapterOverview chapter = chapters.get(i);
            ArrayList<String> courses = chapter.getCourses();
            for (int j = 0; j < courses.size(); ++j) {
                String course = courses.get(j);
                if (course.toLowerCase().contains(fetchQuery.toLowerCase())) {
                    try {
                        System.out.println("Chapter = " + i + " -- " + j + " -- " + chapter.getFolder() + "size= " + chapter.getnCourse());
                        CourseCoord cc = new CourseCoord(i, j);
                        ret.add(cc);
                    } catch (IOException e) {
                        Debug.debugException(e);
                    }
                }
            }
        }
        return ret;
    }

    public void toggleSidebar() {
        boolean visibility = true;
        double coeff = Settings.SIDEBAR_EXTEND_COEFF;
        if (Settings.SIDEBAR_STATE == Settings.SIDEBAR_EXPANDED) {
            coeff = 1 / coeff;
            visibility = false;
        }

        this.setNodeVisibility(this.Side_Params_Label, visibility);
        this.setNodeVisibility(this.Side_Toggle_Label, visibility);
        this.setNodeVisibility(this.Side_Help_Label, visibility);
        this.setNodeVisibility(this.Side_Stats_Label, visibility);
        this.setNodeVisibility(this.Side_GO_Label, visibility);
        this.setNodeVisibility(this.sidebarChaptersHolder, visibility);
        this.setNodeVisibility(this.Side_CourseName_Pane, visibility);


        double width = Sidebar_VBox.getWidth();
        double newWidth = width * coeff;
        Sidebar_VBox.setMaxWidth(newWidth);
        Sidebar_VBox.setMinWidth(newWidth);
        Sidebar_VBox.setPrefWidth(newWidth);

        double toadd = width - newWidth;

        Design.CENTRAL_PANE_WIDTH += toadd;

        Settings.SIDEBAR_STATE = 1 - Settings.SIDEBAR_STATE;

        if (this.diplayingWhat == Settings.DISPLAYING_STATISTICS) {
            Stats_Controller ctr = (Stats_Controller) this.occupiedController;
            ctr.resetAreaChartsWidth(toadd);
        } else if (this.diplayingWhat == Settings.DISPLAYING_COURSE) {
            CoursePane_Controller ctr = (CoursePane_Controller) occupiedController;
            Design.setWidth(ctr.getHolderVBox(), ctr.getHolderVBox().getWidth() + toadd);
        } else if (this.diplayingWhat == Settings.DISPLAYING_QUESTION) {
            QuestionPane_Controller ctr = (QuestionPane_Controller) occupiedController;
            ctr.adjustWidth();
        }

        searchSPane.setLayoutX(searchSPane.getLayoutX() + toadd);
    }

    public void setNodeVisibility(Parent p, boolean visible) {
        p.setVisible(visible);
        p.setManaged(visible);
    }

    public void loadUserParams() {
        String username = get12CharUsername(LoggedIn_Controller.user.getUsername());
        setLText(accountUsernameLabel, username);
        updateUserPoints(LoggedIn_Controller.getUser().getPoints());
    }

    private static String get12CharUsername(String s) {
        int l = s.length();
        if (l >= 12)
            return s.substring(0, 12);
        for (int i = 0; i < (12 - l); ++i)
            s += "_";
        return s;
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
            p.setStyle("-fx-background-color: " + sidebarOriginalColor + ";");
        } else
            p.setStyle("-fx-background-color: white !important; -fx-border-color:  #1354a1;");

    }

    public void loadCourseGeneralOverview() throws IOException {

        ArrayList<CourseOverview.ChapterOverview> chapters = courseCO.getChapters();

        VBox vb = new VBox(); // CoursePanes' container (he's inside a ScrollPane)

        for (int i = 0; i < chapters.size(); ++i) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GO_Course_Pane.fxml"));
            Parent course_pane = loader.load();
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
                    Debug.debugException(e);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SidebarChapterPane.fxml"));
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
                        Debug.debugException(e);
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
                            Debug.debugException(e);
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
                Debug.debugException(e);
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

//        String jsonOverviewString = Files.readString(Paths.get(json_file), StandardCharsets.UTF_8);

        Scanner scanner = new Scanner( new File(json_file), "UTF-8" );
        String jsonOverviewString = scanner.useDelimiter("\\A").next();
        scanner.close(); // Put this call in a finally block

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
                    Debug.debugException(e);
                }
            });
        }
        scp.setContent(holder);
        scp.setFitToWidth(true);
        Central_Container_SPane.getChildren().add(scp);
    }

    public void displayCourse(int _chapID, int _courseID) throws IOException {

        // update Displaying_What
        User u = LoggedIn_Controller.getUser();
        if (u.neverReadACourse())
            User.updateLastQuestion(u, "0/0/0");

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
            Debug.debugException(e);
        }
        CoursePane_Controller coursePaneController = coursePaneLoader.getController();
        this.occupiedController = coursePaneController;
        try {
            coursePaneController.displayFromJson(this.courseCoord.getFilePath());
        } catch (IOException e) {
            Debug.debugException(e);
        }
        Central_Container_SPane.getChildren().clear();
        Central_Container_SPane.getChildren().add(readCourseSPane);
        Central_Up_Title_Label.setText(courseCoord.getCourseTitle());
        this.questionsOffset = -1;
    }

    public void displayQuestion(int id) throws IOException, ParseException {
        Question question = new Question().loadFromJson(Settings.dataPath + "Questions.json", id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionPane.fxml"));
        Parent root = loader.load();
        QuestionPane_Controller ctr = loader.getController();
        ctr.showQuestion(question);
        ScrollPane scp = new ScrollPane();
        scp.setFitToWidth(true);
        scp.setContent(root);
        this.Central_Container_SPane.getChildren().clear();
        this.Central_Container_SPane.getChildren().add(scp);
        this.setDiplayingWhat(Settings.DISPLAYING_QUESTION);
        this.occupiedController = ctr;

        User u = LoggedIn_Controller.getUser();
        if (u.isQuesationAnswered(id)) {
            int i = 0;
            for (PropositionPane_Controller pctr : ctr.getPropositionsControllers())
                pctr.validate(question.answers.get(i++));
        }
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
            Debug.debugException(e);
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
            Debug.debugException(e);
        } catch (ParseException e) {
            Debug.debugException(e);
        }
        this.Central_Up_Title_Label.setText(this.courseCO.getCourseTitle() + "| Question " + (this.questionsOffset + 1)); // + 1 for the user (question 0 lol)

    }

    public void setDiplayingWhat(int _mode) {
        this.pointsHolderPane.setVisible(_mode == Settings.DISPLAYING_COURSE || _mode == Settings.DISPLAYING_QUESTION);
        this.resetQuestionGlobalVars();
        this.diplayingWhat = _mode;
    }

    public String __(String s) {
        return new String(s.getBytes(StandardCharsets.ISO_8859_1));
    }

    public void setLText(Label l, String s) {
        l.setText(__(s));
    }

    public void resetQuestionGlobalVars() {
        this.questionEvalJustFired = false;
    }

    public void updateUserPoints(int points) {
        User u = LoggedIn_Controller.getUser();
        u.setPoints(points);
        pointsLabel.setText(String.format("%04d", points));

        if (Settings.ACTIVE_DB_MODE) {
            // save to DataBase
            User.updatePoints(u, points);
        }
    }

    public void updateLastQuestion(boolean isCourse) {
        User u = LoggedIn_Controller.getUser();
        String newLQst = courseCoord.chapterID + "/" + courseCoord.courseID + "/" + (isCourse ? "-" : "") + courseQuestions.get(questionsOffset);
        u.setLastAnsweredQuestion(newLQst);
        if (Settings.ACTIVE_DB_MODE) {
            User.updateLastQuestion(u, newLQst);
        }
    }

    public void displayParams() {
        Central_Container_SPane.getChildren().clear();
        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("Params.fxml"));
            ScrollPane scp = new ScrollPane(newLoadedPane);
            scp.setFitToWidth(true);
            Central_Container_SPane.getChildren().add(scp);
            this.sidebarFocusNewPane(Side_Params_Pane);
            this.sidebarOriginalColor = "transparent";
            setLText(Central_Up_Title_Label, "Paramètres du Compte");
            this.setDiplayingWhat(Settings.DISPLAYING_PARAMETERS);

        } catch (IOException e) {
            Debug.debugException(e);
        }
    }

    public void showFrom(ArrayList<CourseCoord> list)
    {
        searchVBox.getChildren().clear();
        searchSPane.setVisible(true);
        for(CourseCoord course : list)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchInsidePane.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                Debug.debugException(e);
            }
            SearchInside_Controller ctr = loader.getController();
            ctr.setTitle(course.getCourseTitle());
            ctr.getHolder().setOnMouseClicked(mouseEvent -> {
                try {
                    displayCourse(course.chapterID, course.courseID);
                } catch (IOException e) {
                    Debug.debugException(e);
                }
            });
            searchVBox.getChildren().add(root);
        }
    }
}
