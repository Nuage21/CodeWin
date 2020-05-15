import DialogBoxes.ErrorBox_Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionPane_Controller implements Controller {

    @FXML
    private VBox daddy;

    @FXML
    private VBox holderVBox;

    @FXML
    private VBox propositionsHolderVBox;

    private ArrayList<PropositionPane_Controller> propositionsControllers = new ArrayList<>();
    private Question question;

    private NotePane_Controller notePane_controller;
    private AnswerPane_Controller answerPane_controller;

    @FXML
    public void initialize() {

    }

    public void showQuestion(Question question) throws IOException {
        this.question = question;
        String core = question.core;
        int clng = core.length();
        int i = 0; // iterator

        String gathered = "";
        ArrayList<Parent> panes = new ArrayList<>();
        while (i < clng) {
            char c = core.charAt(i);
            if (core.charAt(i) == '<' && i + 1 < clng) // image-?list spotted !
            {
                if (core.charAt(i + 1) == '!') // image-?list spotted !
                {
                    if (gathered.length() >= 5) {
//                        Parent p = this.appendText(gathered.strip());
                        Parent p = this.appendText(gathered.trim());
                        panes.add(p);
                        gathered = ""; // empty
                    }

                    int endex = getEndex(core, i + 2, '!');
                    String list = getHeadline(core, i + 2, endex);
                    String images[] = list.split("~");
                    Debug.debugMsg("" + images.length);
                    HBox box = new HBox();
                    double avg_ratio = 0;
                    int nImages = 0;
                    for (String file : images) {
                        if (!Checker.isImage(file))
                            continue;
                        Debug.debugMsg("" + getCorrectImageFullpath(question.folder, file));
                        Image crop = new Image(new FileInputStream(getCorrectImageFullpath(question.folder, file)));
                        avg_ratio += crop.getWidth() / crop.getHeight();
                        nImages++; // may be different from images.length (there may be img-sources included)

                    }
                    avg_ratio /= nImages;
                    for (String file : images) {
                        if (!Checker.isImage(file))
                            continue;
                        StackPane holder = new StackPane();
                        ImageView img = new ImageView();
                        Image crop = new Image(new FileInputStream(getCorrectImageFullpath(question.folder, file)));
                        img.setImage(crop);
                        img.setFitHeight(200);
                        img.setFitWidth(200 * avg_ratio);
                        holder.getChildren().add(img);
                        holder.setStyle("-fx-padding: 10; -fx-background-radius: 20; -fx-border-radius: 20; -fx-background-color:  #2378aa; -fx-border-color: #eeeeee; -fx-border-width: 1;");
                        Pane sup_holder = new Pane();
                        sup_holder.getChildren().add(holder);
                        sup_holder.setStyle("-fx-padding: 0 20 0 0;");
                        box.getChildren().add(sup_holder);
                    }
                    ScrollPane scp = new ScrollPane();
                    scp.setContent(box);
                    scp.setStyle("-fx-padding: 20; -fx-background-color: white;");
                    scp.setPrefHeight(270);
                    scp.setMinHeight(270);
                    panes.add((Parent) scp);
                    i = endex + 1;
                }
            } else
                gathered += c;
            i++;
        }

        if (gathered.length() >= 5) {
//            Parent p = this.appendText(gathered.strip());
            Parent p = this.appendText(gathered.trim());
            panes.add(p);
        }
        holderVBox.getChildren().addAll(panes);
        // show propositions
        ArrayList<String> props = question.propositions;

        for (String p : props) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PropositionPane.fxml"));
            Parent root = loader.load();
            PropositionPane_Controller ctr = loader.getController();
            ctr.setProposition(p);
            Pane tmp = new Pane();
            tmp.getChildren().add(root);
            tmp.setStyle("-fx-padding: 4 0 0 0;");
            this.propositionsHolderVBox.getChildren().add(tmp);
            this.propositionsControllers.add(ctr); // add controller
            ctr.resetWidth();
        }

        {
            // add answerPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AnswerPane.fxml"));
            Parent root = loader.load();
            AnswerPane_Controller ctr = loader.getController();
            ctr.setVisible(false);
            this.propositionsHolderVBox.getChildren().add(root);
            this.answerPane_controller = ctr;
        }

        // display note
        if (!question.note.isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NotePane.fxml"));
            Parent root = loader.load();
            notePane_controller = loader.getController();
            notePane_controller.setNote(question.note);
            root.setStyle("-fx-padding: 50 0 0 0 !important;");
            this.propositionsHolderVBox.getChildren().add(root);
            notePane_controller.resetWidth();
        }

    }

    public Parent appendText(String _txt) throws IOException {
        TextArea txta = new TextArea(_txt);
        txta.setFont(new Font("Helvetica", 18));
        txta.setWrapText(true);
        txta.setStyle("-fx-padding: 5;");
        int lng = _txt.length();
        int rows = 2 + (int) (lng / 167.0);
        int height = rows * 30;
        txta.setMinHeight(height);
        txta.setPrefHeight(height);
        txta.setMaxHeight(height);
        txta.setEditable(false); // block edition
        return txta;
    }

    public static int getEndex(String core, int begIndex, char sep) {
        int length = core.length();
        for (; begIndex < length && core.charAt(begIndex) != sep; ++begIndex) ;
        return begIndex;
    }

    public static String getHeadline(String core, int beg, int end) {
//        return core.substring(beg, end).strip();
        return core.substring(beg, end).trim();
    }


    // correct mistyped extensions
    public String getCorrectImageFullpath(String folder, String file) {
        String suspect = folder + file;
        if ((new File(suspect)).isFile())
            return suspect;
        String withoutExtension = folder + file.split("[.]")[0];
        String extensions[] = {".jpeg", ".jpg", ".png"};
        String fullPath = null;
        for (String x : extensions) {
            fullPath = withoutExtension + x;
            if ((new File(fullPath)).isFile())
                return fullPath;
        }
        Logger.log(Settings.logfile, suspect + "\n");
        return fullPath;
    }

    public VBox getHolderVBox() {
        return holderVBox;
    }

    public void adjustWidth() {
        Platform.runLater(() -> {
            double delta = Settings.SIDEBAR_DELTA;
            if (Settings.SIDEBAR_STATE == Settings.SIDEBAR_SHRINKED) {
                resetWidth();
            } else
                resetWidth();
        });
    }

    private void resetWidth() {

        for (PropositionPane_Controller ctr : this.propositionsControllers)
            ctr.resetWidth();

        if (notePane_controller != null)
            notePane_controller.resetWidth();
    }

    public boolean evaluateAnswer() {
        int nProp = question.answers.size();
        for (int i = 0; i < nProp; ++i) {
            boolean isChecked = this.propositionsControllers.get(i).isChosen();
            boolean isCorrect = this.question.answers.get(i);
            if (isChecked != isCorrect)
                return false;
        }
        return true;
    }

    public AnswerPane_Controller getAnswerPane_controller() {
        return answerPane_controller;
    }

    public NotePane_Controller getNotePane_controller() {
        return notePane_controller;
    }

    public Question getQuestion()
    {
        return question;
    }

    public ArrayList<PropositionPane_Controller> getPropositionsControllers()
    {
        return propositionsControllers;
    }

}

