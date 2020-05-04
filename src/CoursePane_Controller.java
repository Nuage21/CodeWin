import DialogBoxes.ErrorBox_Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.json.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CoursePane_Controller implements Controller {
    @FXML
    private VBox holderVBox;

    private String courseImagesFolder;


    @FXML
    public void initialize() throws IOException {
    }

    public void displayFromJson(String filename) throws IOException {

        Platform.runLater( () -> {
            if(Settings.SIDEBAR_STATE == Settings.SIDEBAR_SHRINKED)
                Design.setWidth(holderVBox, holderVBox.getWidth() + Settings.SIDEBAR_DELTA);
        });
        String jsonCourse = Files.readString(Paths.get(filename), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(jsonCourse);

        String core = obj.getString("core");

        // some preprocessing
        core = core.replace('\n', ' ');
        core = core.replace("  ", " ");
        core = core.strip();

        ArrayList<Parent> panes = new ArrayList<>();
        String gathered = "";

        int clng = core.length();
        int i = 0;
        while (i < clng) {
            char c = core.charAt(i);
            if (c == '#') {
                // append gathered text first
                if (gathered.length() >= 5) {
                    Parent p = this.appendText(gathered.strip());
                    panes.add(p);
                    gathered = ""; // empty
                }
                int depth = getHeadlineDepth(core, i);
                int endex = getEndex(core, i + depth, '#');
                String headline = getHeadline(core, i + depth, endex);
                // append new Headline
                FXMLLoader loader = new FXMLLoader(getClass().getResource("H" + depth + ".fxml"));
                Pane root = loader.load();
                H_Controller ctr = loader.getController();
                root.setMinHeight(root.getPrefHeight());
                ctr.setTitle(headline);
                panes.add((Parent) root);
                i = endex;
            } else if (c == '-' && i + 1 < clng) {
                if (core.charAt(i + 1) == '>') // element list spotted !
                {
                    if (gathered.length() >= 5) {
                        Parent p = this.appendText(gathered.strip());
                        panes.add(p);
                        gathered = ""; // empty
                    }

                    int endex = getEndex(core, i + 2, '#');
                    String list_el = getHeadline(core, i + 2, endex);
                    // append new Headline
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ListElement.fxml"));
                    Pane root = loader.load();
                    H_Controller ctr = loader.getController();
                    root.setMinHeight(root.getPrefHeight());
                    ctr.setTitle(list_el);
                    panes.add((Parent) root);
                    i = endex;
                }
            } else if (c == '<' && i + 1 < clng) {
                if (core.charAt(i + 1) == '!') // image-?list spotted !
                {
                    if (gathered.length() >= 5) {
                        Parent p = this.appendText(gathered.strip());
                        panes.add(p);
                        gathered = ""; // empty
                    }

                    int endex = getEndex(core, i + 2, '!');
                    String list = getHeadline(core, i + 2, endex);
                    String images[] = list.split("[~]");
                    HBox box = new HBox();
                    double avg_ratio = 0;

                    int nImages = 0;
                    for (String file : images) {
                        if (!Checker.isImage(file))
                            continue;
                        Image crop = new Image(new FileInputStream(getCorrectImageFullpath(file)));
                        avg_ratio += crop.getWidth() / crop.getHeight();
                        nImages++; // different from images.length (there may be img-sources included)

                    }
                    avg_ratio /= nImages;
                    for (String file : images) {
                        if (!Checker.isImage(file))
                            continue;
                        StackPane holder = new StackPane();
                        ImageView img = new ImageView();
                        Image crop = new Image(new FileInputStream(getCorrectImageFullpath(file)));
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
            Parent p = this.appendText(gathered.strip());
            panes.add(p);
        }
        holderVBox.getChildren().addAll(panes);
        Platform.runLater( ()->{
            Design.setWidth(holderVBox, Design.CENTRAL_PANE_WIDTH * 0.955);
        });
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

    public static int getHeadlineDepth(String core, int begIndex) {
        int counter = 0;
        int length = core.length();
        for (; begIndex < length && core.charAt(begIndex) == '#'; ++begIndex, ++counter) ;
        return counter;
    }

    public static int getEndex(String core, int begIndex, char sep) {
        int length = core.length();
        for (; begIndex < length && core.charAt(begIndex) != sep; ++begIndex) ;
        return begIndex;
    }

    public static String getHeadline(String core, int beg, int end) {
        return core.substring(beg, end).strip();
    }

    public void setCourseImagesFolder(String chapterFolder) {
        this.courseImagesFolder = chapterFolder;
    }

    // correct mistyped extensions
    public String getCorrectImageFullpath(String file) {
        String suspect = courseImagesFolder + file;
        if ((new File(suspect)).isFile())
            return suspect;
        String withoutExtension = courseImagesFolder + file.split("[.]")[0];
        String extensions[] = {".jpeg", ".jpg", ".png"};
        for (String x : extensions) {
            String fullPath = withoutExtension + x;
            if ((new File(fullPath)).isFile())
                return fullPath;
        }
        Logger.log(Settings.logfile, suspect + "\n");
        return null;
    }

    public VBox getHolderVBox() {
        return holderVBox;
    }


}
