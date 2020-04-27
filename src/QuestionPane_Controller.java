import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionPane_Controller {

    @FXML
    private VBox holderVBox;

    @FXML
    private VBox propositionsHolderVBox;

    private ArrayList<PropositionPane_Controller> propositionsControllers = new ArrayList<>();
    private Question question;

    @FXML
    void initialize() {

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
                    if (!gathered.isEmpty()) {
                        Pane p = this.appendText(gathered.strip());
                        panes.add(p);
                        gathered = ""; // empty
                    }

                    int endex = getEndex(core, i + 2, '!');
                    String list = getHeadline(core, i + 2, endex);
                    String images[] = list.split("~");
                    HBox box = new HBox();
                    double avg_ratio = 0;
                    for (String file : images) {
                        System.out.println(file);
                        Image crop = new Image(new FileInputStream(getCorrectImageFullpath(question.folder, file)));
                        avg_ratio += crop.getWidth() / crop.getHeight();
                    }
                    avg_ratio /= images.length;

                    for (String file : images) {
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

        gathered = gathered.strip();
        if (!gathered.isEmpty())
            panes.add(appendText(gathered));
        holderVBox.getChildren().addAll(panes);
        // show propositions
        ArrayList<String> props = question.propositions;

        for(String p : props)
        {
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("PropositionPane.fxml"));
            Parent root = loader.load();
            PropositionPane_Controller ctr = loader.getController();
            ctr.setProposition(p);
            Pane tmp = new Pane();
            tmp.getChildren().add(root);
            tmp.setStyle("-fx-padding: 4 0 0 0;");
            this.propositionsHolderVBox.getChildren().add(tmp);
            this.propositionsControllers.add(ctr); // add controller
        }

        // display note
        if(!question.note.isEmpty())
        {
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("NotePane.fxml"));
            Parent root = loader.load();
            NotePane_Controller ctr = loader.getController();
            ctr.setNote(question.note);
            root.setStyle("-fx-padding: 50 0 0 0 !important;");
            this.propositionsHolderVBox.getChildren().add(root);
        }
    }

    public Pane appendText(String _txt) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Text.fxml"));
        Pane root = loader.load();
        Text_Controller ctr = loader.getController();
        ctr.setText(_txt);
        return root;
    }

    public static int getEndex(String core, int begIndex, char sep) {
        int length = core.length();
        for (; begIndex < length && core.charAt(begIndex) != sep; ++begIndex) ;
        return begIndex;
    }

    public static String getHeadline(String core, int beg, int end) {
        return core.substring(beg, end).strip();
    }


    // correct mistyped extensions
    public String getCorrectImageFullpath(String folder, String file) {
        String suspect = folder + file;
        if ((new File(suspect)).isFile())
            return suspect;
        String withoutExtension = folder + file.split("[.]")[0];
        String extensions[] = {".jpeg", ".jpg", ".png"};
        String fullPath = "None";
        for (String x : extensions) {
            fullPath = withoutExtension + x;
            if ((new File(fullPath)).isFile())
                return fullPath;
        }
        return null;
    }

    public VBox getHolderVBox() {
        return holderVBox;
    }

}

